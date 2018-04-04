package gabrieltechnologies.sehm;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import Fragments.Auth0Fragment;
import Fragments.CalibrateFragment;
import Fragments.EatingTimesFragment;
import Fragments.UserDetailsFragment;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import Services.CalibrationCallback;
import Services.EatingTimesCallback;

public class LoginActivity extends AppCompatActivity implements APIServiceCallback, EatingTimesCallback, CalibrationCallback{

    FragmentTransaction fragmentTransaction;
    Auth0Fragment auth0Fragment;
    UserDetailsFragment userDetailsFragment;
    EatingTimesFragment eatingTimesFragment;
    CalibrateFragment calibrateFragment;

    boolean signUpCalled = false;
    boolean preferencesCalled = false;

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
//        finishedSignUp();
    }

    private void checkIfSignedUp(){
        APIService.getInstance().callAPI("user/preferences", this, RequestType.GET, "");
    }

    @Override
    public void apiResponseListener(boolean isSuccess,String originalPayload, String payload, String apiUrl, RequestType requestType) {
        if(isSuccess){
            if(apiUrl.matches(".+sign-up") && !signUpCalled){
                signUpCalled = true;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(userDetailsFragment).commit();

                eatingTimesFragment = new EatingTimesFragment();
                eatingTimesFragment.setEatingTimesCallback(this);

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.flContainer, eatingTimesFragment);
                fragmentTransaction.commit();
            }

            else if(apiUrl.matches(".+preferences") && !preferencesCalled){
                preferencesCalled = true;
                //skip signUp details
                if(!payload.equals("[]")){
                    finishedSignUp();
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

    public void finishedSignUp() {
        APIService.removeSubscriber(this);

        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboardIntent);
    }

    @Override
    public void finishedSavingEatingTimes() {
//        //switch to calibrationScreen
        calibrateFragment = new CalibrateFragment();
        calibrateFragment.setCallback(this);
        fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(eatingTimesFragment);
        fragmentTransaction.add(R.id.flContainer, calibrateFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void calibrationDone() {
        finishedSignUp();
    }
}
