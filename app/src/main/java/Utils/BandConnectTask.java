package Utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.InvalidBandVersionException;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;


public class BandConnectTask extends AsyncTask<Void, Void, Void> {

    private BandClient client = null;
    private Context baseContext;
    private Activity activity;


    BandConnectTask(Context baseContext, Activity activity) {
        this.baseContext = baseContext;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        client = BandClientManager.getInstance().create(baseContext, devices[0]);
        try {
            ConnectionState state = client.connect().await();
            if(state == ConnectionState.CONNECTED) {
                //get permissions for HR on successful connection
                getPermissions(activity);
            }
        } catch (InterruptedException | BandException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void getPermissions(Activity activity){
        if(client.getSensorManager().getCurrentHeartRateConsent() != UserConsent.GRANTED) {
            client.getSensorManager().requestHeartRateConsent(activity, new HeartRateConsentListener() {
                @Override
                public void userAccepted(boolean consentGiven) {
                }
            });
        }
    }

    void addHRListener(BandHeartRateEventListener bandHeartRateEventListener) {
        try {
            client.getSensorManager().registerHeartRateEventListener(bandHeartRateEventListener);
        } catch(BandException ex) {
            ex.printStackTrace();
        }
    }

    void addRRIntervalListener(BandRRIntervalEventListener bandRRIntervalEventListener){
        try {
            client.getSensorManager().registerRRIntervalEventListener(bandRRIntervalEventListener);
        } catch (BandException | InvalidBandVersionException e) {
            e.printStackTrace();
        }
    }

    void addAccelerometerListener(BandAccelerometerEventListener bandAccelerometerEventListener){
        try {
            client.getSensorManager().registerAccelerometerEventListener(bandAccelerometerEventListener, SampleRate.MS128);
        } catch (BandIOException e) {
            e.printStackTrace();
        }
    }

    void addDistanceListener(BandDistanceEventListener bandDistanceEventListener){
        try {
            client.getSensorManager().registerDistanceEventListener(bandDistanceEventListener);
        } catch (BandIOException e) {
            e.printStackTrace();
        }
    }

   void addContactListener(BandContactEventListener bandContactEventListener){
       try {
           client.getSensorManager().registerContactEventListener(bandContactEventListener);
       } catch (BandIOException e) {
           e.printStackTrace();
       }
   }

   void addGsrListener(BandGsrEventListener bandGsrEventListener){
       try {
           client.getSensorManager().registerGsrEventListener(bandGsrEventListener);
       } catch (BandIOException | InvalidBandVersionException e) {
           e.printStackTrace();
       }
   }

   void addSkinTempListener(BandSkinTemperatureEventListener bandSkinTemperatureEventListener){
       try {
           client.getSensorManager().registerSkinTemperatureEventListener(bandSkinTemperatureEventListener);
       } catch (BandIOException e) {
           e.printStackTrace();
       }
   }

    void removeAllListeners(){
        try {
            client.getSensorManager().unregisterAllListeners();
        } catch (BandIOException e) {
            e.printStackTrace();
        }
    }
}
