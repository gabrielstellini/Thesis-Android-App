package gabrieltechnologies.sehm;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import Services.APIService;
import Services.APIServiceCallback;

public class LoginActivity extends Activity implements APIServiceCallback{

    FragmentTransaction fragmentTransaction;
    Auth0Fragment auth0Fragment;
    UserDetailsFragment userDetailsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

       auth0Fragment = new Auth0Fragment();

        //adds listener for login status
        APIService.addSubscriber(this);

        fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.flContainer, auth0Fragment);
        fragmentTransaction.add(R.id.flContainer, auth0Fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload) {

    }

    @Override
    public void loginStatus(int statusCode) {
        if(statusCode == 200){
            userDetailsFragment = new UserDetailsFragment();

            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(auth0Fragment);
            fragmentTransaction.add(R.id.flContainer, userDetailsFragment);
            fragmentTransaction.commit();
        }
    }
}
