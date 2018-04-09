package Utils;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.microsoft.band.sensors.HeartRateQuality;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class BandRecorder {

    private BandConnectTask bandConnectTask;
    private DataStore dataStore = new DataStore();
    private BandListeners bandListeners;
    private Activity activity;

    //    final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    Timer timer;
    TextView statusTxt = null;

    public BandRecorder(Activity activity){
        this.activity = activity;
    }

    public void onDestroy(){
        stopRecording();
        activity = null;
    }


    public void startRecording(@Nullable final TextView statusTxt){
        this.statusTxt = statusTxt;

        //connect to band
        bandConnectTask = new BandConnectTask(activity.getBaseContext(), activity);
        bandConnectTask.execute();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //start gathering data
                bandListeners = new BandListeners(bandConnectTask, dataStore);
                bandListeners.addAllListeners();
                if(statusTxt != null){
                    updateStatus();
                }

            }
        }, 3000);
    }

    public void stopRecording(){
        try{
            timer.cancel();
            timer = null;
            bandConnectTask.removeAllListeners();
        }catch (NullPointerException e){
            System.out.println("Unable to remove listeners");
        }finally {
            if(statusTxt != null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusTxt.setText("Not recording");
                    }
                });
            }
        }
    }

    private void updateStatus(){
            timer = new Timer();
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            final int time = bandListeners.getTimeSinceLastUpdate();
                            final int hr = DataStore.bandSensorData.getHeartRateData().getHeartRate();
                            final int gsr = DataStore.bandSensorData.getGsrData().getResistance();
                            final double rr = DataStore.bandSensorData.getRrIntervalData().getInterval();
                            final HeartRateQuality quality = DataStore.bandSensorData.getHeartRateData().getQuality();


                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(timer == null){
                                            statusTxt.setText("Not recording");
                                        }else {
                                            statusTxt.setText(String.format("Heart Rate = %1$s beats per minute\n"
                                                            + "Quality = %2$s \n"
                                                            + "GSR = %3$s \n"
                                                            + "RR = %4$s \n"
                                                            + "Time since the last update is: %5$s",
                                                    hr, quality, gsr, rr, time));
                                        }
                                    }
                                });
                        }
                    },
                    0,100);
    }
}
