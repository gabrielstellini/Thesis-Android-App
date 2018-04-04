package Utils;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.microsoft.band.sensors.HeartRateQuality;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class BandRecorder {

    private BandConnectTask bandConnectTask;
    private DataStore dataStore = new DataStore();
    private BandListeners bandListeners;
    private Activity activity;

    public BandRecorder(Activity activity){
        this.activity = activity;
    }

    public void onDestroy(){
        stopRecording();
        activity = null;
    }


    public void startRecording(@Nullable final TextView statusTxt){
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
                    updateStatus(statusTxt);
                }

            }
        }, 3000);



    }

    public void stopRecording(){
        bandConnectTask.removeAllListeners();
//        DataStore.dataPoints.removeAll();
    }


    private void updateStatus(final TextView statusTxt){

        final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {

                final int time = bandListeners.getTimeSinceLastUpdate();
                final int hr = DataStore.bandSensorData.getHeartRateData().getHeartRate();
                final int gsr = DataStore.bandSensorData.getGsrData().getResistance();
                final double rr = DataStore.bandSensorData.getRrIntervalData().getInterval();
                final HeartRateQuality quality = DataStore.bandSensorData.getHeartRateData().getQuality();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusTxt.setText(String.format("Heart Rate = %1$s beats per minute\n"
                                        + "Quality = %2$s \n"
                                        + "GSR = %3$s \n"
                                        + "RR = %4$s \n"
                                        + "Time since the last update is: %5$s",
                                hr, quality, gsr, rr, time));
                    }
                });


            }
        };
        worker.scheduleAtFixedRate(task, 100, 100, TimeUnit.MILLISECONDS);
    }
}
