package gabrieltechnologies.sehm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Services.APIService;

public class LoginActivity extends APIService {

    private static final String API_URL = "http://192.168.4.194:3010/user/";
    private static final String API_IDENTIFIER = "Android";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button callAPIWithTokenButton = (Button) findViewById(R.id.callAPIWithTokenButton);
        Button loginWithTokenButton = (Button) findViewById(R.id.loginButton);

        loginWithTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        callAPIWithTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void loginStatus(int statusCode){
        Toast.makeText(LoginActivity.this, "Log in status: " + statusCode, Toast.LENGTH_SHORT).show();
    }
}
