package Services;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class APIService extends Activity implements APIServiceCallback{

    private static final String API_IDENTIFIER = "Android";
    protected static String accessToken;
    protected static Gson gson = new Gson();


    protected void callAPI(String API_URL) {
        final Request.Builder reqBuilder = new Request.Builder()
                .get()
                .url(API_URL);

        reqBuilder.addHeader("Authorization", "Bearer " + accessToken);


        OkHttpClient client = new OkHttpClient();
        Request request = reqBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        apiResponseListener(false, "An error occurred");
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                apiResponseListener(true, response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(LoginActivity.this, "API call success!", Toast.LENGTH_SHORT).show();

                        } else {
                            apiResponseListener(false, "API call failed");
                        }
                    }
                });
            }
        });
    }

    protected void login() {
        Auth0 auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .withAudience(API_IDENTIFIER)
                .withScope("openid profile email")
                .start(APIService.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginStatus(401);
                                Toast.makeText(APIService.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        accessToken = credentials.getAccessToken();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginStatus(200);
//                                Toast.makeText(APIService.this, "Log in: Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload) {

    }

    @Override
    public void loginStatus(int statusCode) {

    }
}
