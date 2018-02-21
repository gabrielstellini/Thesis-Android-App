package Services;

public interface APIServiceCallback {
    public void apiResponseListener(boolean isSuccess, String payload);
    public void loginStatus(int statusCode);
}
