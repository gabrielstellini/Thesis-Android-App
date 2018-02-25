package gabrieltechnologies.sehm;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import Fragments.Auth0Fragment;
import Fragments.EatingTimesFragment;
import Fragments.UserDetailsFragment;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import Services.EatingTimesCallback;

public class LoginActivity extends Activity implements APIServiceCallback, EatingTimesCallback{

    FragmentTransaction fragmentTransaction;
    Auth0Fragment auth0Fragment;
    UserDetailsFragment userDetailsFragment;
    EatingTimesFragment eatingTimesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        auth0Fragment = new Auth0Fragment();

        //adds listener for login status
        APIService.addSubscriber(this);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flContainer, auth0Fragment);
        fragmentTransaction.commit();

        //skip whole process and jump to page
//        loginStatus(200);
//        apiResponseListener(true, "", "user/sign-up");
//        finishedSavingEatingTimes();
    }

    private void checkIfSignedUp(){
        APIService.getInstance().callAPI("user/preferences", this, RequestType.GET, "");
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload, String apiUrl) {
        if(isSuccess){
            if(apiUrl.matches(".+sign-up")){

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(userDetailsFragment).commit();

                eatingTimesFragment = new EatingTimesFragment();
                eatingTimesFragment.setEatingTimesCallback(this);

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.flContainer, eatingTimesFragment);
                fragmentTransaction.commit();
            }

            else if(apiUrl.matches(".+preferences")){
                //skip signUp details
                if(!payload.equals("[]")){
                    finishedSavingEatingTimes();
                }else {
                    userDetailsFragment = new UserDetailsFragment();

                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.remove(auth0Fragment);
                    fragmentTransaction.add(R.id.flContainer, userDetailsFragment);
                    fragmentTransaction.commit();
                }

            }
        }
    }

    @Override
    public void loginStatus(int statusCode) {
        if(statusCode == 200){
            checkIfSignedUp();
        }
    }

    @Override
    public void finishedSavingEatingTimes() {
        APIService.removeSubscriber(this);

        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }
}
