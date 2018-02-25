package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gabrieltechnologies.sehm.R;


public class FriendsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,
                container, false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void postData(){
//        boolean isMale = gender.getSelectedItemPosition() == 0;
//
//        SignUpUser signUpUser = new SignUpUser();
//        signUpUser.setHasMedication(switch1.isChecked());
//        signUpUser.setHasFinancialPressure(switch2.isChecked());
//        signUpUser.setHasEmotionalSupport(switch3.isChecked());
//        signUpUser.setMale(isMale);
//        //DateOfBirth missing - need to convert age into date selector
//
//        Gson gson = new Gson();
//
//        String payload = gson.toJson(signUpUser);
//
//        APIService.getInstance().callAPI("user/sign-up", getActivity(), RequestType.POST, payload);
    }
}
