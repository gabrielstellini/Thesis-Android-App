package Services;

import Model.RequestType;

public interface APIServiceCallback {
    public void apiResponseListener(boolean isSuccess,String originalPayload, String payload, String apiUrl, RequestType requestType);
    public void loginStatus(int statusCode);
}
