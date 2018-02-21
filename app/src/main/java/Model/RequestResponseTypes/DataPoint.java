package Model.RequestResponseTypes;

public class DataPoint {

    private int heartRate;
    private double RRInterval;
    private double GSR;
    private int timestamp;


    //Getters and setters
    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getRRInterval() {
        return RRInterval;
    }

    public void setRRInterval(double RRInterval) {
        this.RRInterval = RRInterval;
    }

    public double getGSR() {
        return GSR;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setGSR(double GSR) {
        this.GSR = GSR;
    }
}
