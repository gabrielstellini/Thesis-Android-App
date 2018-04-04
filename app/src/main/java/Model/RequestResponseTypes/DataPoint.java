package Model.RequestResponseTypes;

public class DataPoint {

    private int heartRate;
    private double rrInterval;
    private double gsr;
    private long timestamp;
    private String quality;
    private String contactStatus;


    public DataPoint(long timestamp, int heartRate, double rrInterval, double gsr, String quality, String contactStatus){
        this.timestamp = timestamp;
        this.heartRate = heartRate;
        this.rrInterval = rrInterval;
        this.gsr = gsr;
        this.quality = quality;
        this.contactStatus = contactStatus;
    }

    public DataPoint(){}

    //Getters and setters
    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public double getRrInterval() {
        return rrInterval;
    }

    public void setRrInterval(double rrInterval) {
        this.rrInterval = rrInterval;
    }

    public double getGsr() {
        return gsr;
    }

    public void setGsr(double gsr) {
        this.gsr = gsr;
    }
}
