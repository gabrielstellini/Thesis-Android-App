package Utils;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;

import java.util.LinkedList;

import Model.BandSensorData;
import Model.RequestResponseTypes.DataPoint;


public class DataStore implements BandAccelerometerEventListener {
    static BandSensorData bandSensorData = new BandSensorData();
    public static LinkedList<DataPoint> dataPoints = new LinkedList<>();

    @Override
    public void onBandAccelerometerChanged(BandAccelerometerEvent event) {
        addDataPoint(event);
    }

    private void addDataPoint(BandAccelerometerEvent event) {
        DataPoint dataPoint = new DataPoint(
                event.getTimestamp(),
                bandSensorData.getHeartRateData().getHeartRate(),
                bandSensorData.getRrIntervalData().getInterval(),
                bandSensorData.getGsrData().getResistance(),
                String.valueOf(bandSensorData.getHeartRateData().getQuality()),
                String.valueOf(bandSensorData.getContactData().getContactState())

        );

        dataPoints.add(dataPoint);
    }
}
