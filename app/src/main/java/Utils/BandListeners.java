package Utils;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class BandListeners {

    private BandConnectTask bandConnectTask;
    private DataStore dataStore;

    BandListeners(BandConnectTask bandConnectTask, DataStore dataStore){
        this.bandConnectTask = bandConnectTask;
        this.dataStore = dataStore;
    }

    void addAllListeners(){
        startTimeCalculator();

        addHRListener();
        addRRListener();
        addAccelerometerListeners();
        addDistanceListener();
        addContactListener();
        addGsrListener();
        addSkinTempListener();
    }

    private int timeSinceLastUpdate = 0;

    private void addHRListener(){
        BandHeartRateEventListener heartRateListener = new BandHeartRateEventListener() {
            @Override
            public void onBandHeartRateChanged(final BandHeartRateEvent event) {
                DataStore.bandSensorData.setHeartRateData(event);
                setTimeSinceLastUpdate(0);
            }
        };

        bandConnectTask.addHRListener(heartRateListener);
    }

    private void addRRListener() {
        BandRRIntervalEventListener bandRRIntervalEventListener = new BandRRIntervalEventListener() {
            @Override
            public void onBandRRIntervalChanged(BandRRIntervalEvent bandRRIntervalEvent) {
                DataStore.bandSensorData.setRrIntervalData(bandRRIntervalEvent);
                setTimeSinceLastUpdate(0);
            }
        };

        bandConnectTask.addRRIntervalListener(bandRRIntervalEventListener);
    }

    private void addAccelerometerListeners(){
        BandAccelerometerEventListener bandAccelerometerEventListener = new BandAccelerometerEventListener() {
            @Override
            public void onBandAccelerometerChanged(BandAccelerometerEvent bandAccelerometerEvent) {
                DataStore.bandSensorData.setAccelerometerData(bandAccelerometerEvent);
                setTimeSinceLastUpdate(0);
            }
        };

        bandConnectTask.addAccelerometerListener(bandAccelerometerEventListener);
        bandConnectTask.addAccelerometerListener(dataStore);
    }

    private void addDistanceListener(){
        BandDistanceEventListener bandDistanceEventListener = new BandDistanceEventListener() {
            @Override
            public void onBandDistanceChanged(BandDistanceEvent bandDistanceEvent) {
                DataStore.bandSensorData.setDistanceData(bandDistanceEvent);
                setTimeSinceLastUpdate(0);
            }
        };
        bandConnectTask.addDistanceListener(bandDistanceEventListener);
    }

    private void addContactListener(){
        BandContactEventListener bandContactEventListener = new BandContactEventListener() {
            @Override
            public void onBandContactChanged(BandContactEvent bandContactEvent) {
                DataStore.bandSensorData.setContactData(bandContactEvent);
                setTimeSinceLastUpdate(0);
            }
        };

        bandConnectTask.addContactListener(bandContactEventListener);
    }

    private void addGsrListener(){
        BandGsrEventListener bandGsrEventListener = new BandGsrEventListener() {
            @Override
            public void onBandGsrChanged(BandGsrEvent bandGsrEvent) {
                DataStore.bandSensorData.setGsrData(bandGsrEvent);
                setTimeSinceLastUpdate(0);
            }
        };

        bandConnectTask.addGsrListener(bandGsrEventListener);
    }

    private void addSkinTempListener(){
        BandSkinTemperatureEventListener bandSkinTemperatureEventListener = new BandSkinTemperatureEventListener() {
            @Override
            public void onBandSkinTemperatureChanged(BandSkinTemperatureEvent bandSkinTemperatureEvent) {
                DataStore.bandSensorData.setSkinTemperatureData(bandSkinTemperatureEvent);
                setTimeSinceLastUpdate(0);
            }
        };

        bandConnectTask.addSkinTempListener(bandSkinTemperatureEventListener);
    }

    private void startTimeCalculator(){
        //Change if too CPU intensive
        final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                setTimeSinceLastUpdate(getTimeSinceLastUpdate() + 100);
            }
        };
        worker.scheduleAtFixedRate(task, 100, 100, TimeUnit.MILLISECONDS);
    }

    synchronized int getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }

    private synchronized void setTimeSinceLastUpdate(int timeSinceLastUpdate) {
        this.timeSinceLastUpdate = timeSinceLastUpdate;
    }
}
