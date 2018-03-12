package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Model.RequestType;
import Services.APIServiceCallback;
import gabrieltechnologies.sehm.R;

public class PreferencesFragment extends Fragment implements APIServiceCallback{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferences,
                container, false);

//        initialise(view);

//        APIService.addSubscriber(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void initialise(View view){
//        TextView eulaTextView = view.findViewById(R.id.EULA);
//
//
//        switch1 = view.findViewById(R.id.switch1);
//        switch2 = view.findViewById(R.id.switch2);
//        switch3 = view.findViewById(R.id.switch3);
//        submitBtn = view.findViewById(R.id.submitBtn);
//
//        //Add scrolling
//        eulaTextView.setMovementMethod(new ScrollingMovementMethod());
//
//        //Add button listener
//        submitBtn.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSubmit();
//            }
//        });
//
//    }
//
//    public void onSubmit(){
//        postData();
//    }
//
//    public void postData(){
//
//        SignUpUser signUpUser = new SignUpUser();
//        signUpUser.setHasMedication(switch1.isChecked());
//        signUpUser.setHasFinancialPressure(switch2.isChecked());
//        signUpUser.setHasEmotionalSupport(switch3.isChecked());
//
//        Gson gson = new Gson();
//
//        String payload = gson.toJson(signUpUser);
//
//        APIService.getInstance().callAPI("user/sign-up", getActivity(), RequestType.POST, payload);
//    }
//
    @Override
    public void apiResponseListener(boolean isSuccess,String originalPayload, String payload, String apiUrl, RequestType requestType) {
//        if(isSuccess){
//            Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_SHORT).show();
//        } else if(!isSuccess){
//            Toast.makeText(getActivity(), "Update unsuccessful :(", Toast.LENGTH_SHORT).show();
//        }
    }
//
    @Override
    public void loginStatus(int statusCode) {}

//    @Override
//    public void onDetach(){
//        super.onDetach();
//
//        APIService.removeSubscriber(this);
//    }
}
