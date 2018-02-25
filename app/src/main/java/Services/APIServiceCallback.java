package Services;

public interface APIServiceCallback {
    public void apiResponseListener(boolean isSuccess, String payload, String apiUrl);
    public void loginStatus(int statusCode);
}
