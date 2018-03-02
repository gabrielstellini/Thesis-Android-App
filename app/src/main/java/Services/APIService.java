package Services;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import Model.RequestType;


public class APIService implements APIServiceCallback{
    private static final String BASE_API_URL = "http://10.0.0.6:3010/";
//    private static final String BASE_API_URL = "http://192.168.4.119:3010/";
//    private static final String BASE_API_URL = "http://192.168.8.102:3010/";
//    private static final String BASE_API_URL = "http://localhost:3010/";
    private static final String API_IDENTIFIER = "Android";
    private static String accessToken;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected static Gson gson = new Gson();
    private static APIService apiService = new APIService();
    private static ArrayList<APIServiceCallback> subscribers = new ArrayList<>();

    private APIService(){
    }

    public void callAPI(final String API_URL, final Activity activity, final RequestType requestType, final String payload) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                final Request.Builder reqBuilder = new Request.Builder();

                if(requestType == RequestType.GET){
                    reqBuilder
                            .get()
                            .url(BASE_API_URL + API_URL);
                }else {
                    RequestBody requestBody = RequestBody.create(JSON, payload);

                    if(requestType == RequestType.POST){
                        reqBuilder
                                .post(requestBody)
                                .url(BASE_API_URL + API_URL);
                    }else if(requestType == RequestType.PUT){
                        reqBuilder
                                .put(requestBody)
                                .url(BASE_API_URL + API_URL);
                    }else if(requestType == RequestType.DELETE){
                        reqBuilder
                                .delete(requestBody)
                                .url(BASE_API_URL + API_URL);
                    }
                }
                reqBuilder.addHeader("Authorization", "Bearer " + accessToken);


                OkHttpClient client = new OkHttpClient();
                Request request = reqBuilder.build();
                client.newCall(request)

                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        apiResponseListener(false, "An error occurred", API_URL, requestType);
                                    }
                                }).start();
                            }

                            @Override
                            public void onResponse(final Response response) throws IOException {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.isSuccessful()) {
                                            try {
                                                apiResponseListener(true, response.body().string(), API_URL, requestType);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                        } else {
//                                            Toast.makeText(activity, "API call failed :( !", Toast.LENGTH_SHORT).show();
                                            apiResponseListener(false, "API call failed", API_URL, requestType);
                                            callAPI(API_URL, activity, requestType, payload);
                                        }
                                    }
                                }).start();
                            }
                        });

            }
        }).start();
    }


    public void login(final Activity activity) {
        Auth0 auth0 = new Auth0(activity);
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .withAudience(API_IDENTIFIER)
                .withScope("openid profile email")
                .start(activity, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginStatus(401);
                                Toast.makeText(activity, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        accessToken = credentials.getAccessToken();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginStatus(200);
                                Toast.makeText(activity, "Log in: Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload, String apiUrl, RequestType requestType) {
        for(APIServiceCallback subscriber: subscribers){
            subscriber.apiResponseListener(isSuccess, payload, apiUrl, requestType);
        }
    }

    @Override
    public void loginStatus(int statusCode) {
        for(APIServiceCallback subscriber: subscribers){
            subscriber.loginStatus(statusCode);
        }
    }

    public static APIService getInstance(){
        return APIService.apiService;
    }

    public static void addSubscriber(APIServiceCallback subscriber){
        subscribers.add(subscriber);
    }

    public static void removeSubscriber(APIServiceCallback subscriber){
        subscribers.remove(subscriber);
    }
}
