package gabrieltechnologies.sehm;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import Model.RequestResponseTypes.SignUpUser;
import Services.APIService;

public class UserDetailsFragment extends Fragment {

    Switch switch1;
    Switch switch2;
    Switch switch3;
    EditText ageText;
    Spinner gender;
    Button submitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details,
                container, false);

        initialise(view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initialise(View view){
        TextView eulaTextView = view.findViewById(R.id.EULA);


        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);

        ageText = view.findViewById(R.id.age);

        gender = view.findViewById(R.id.genderSpinner);
        submitBtn = view.findViewById(R.id.submitBtn);

        //Add scrolling
        eulaTextView.setMovementMethod(new ScrollingMovementMethod());

        //Add button listener
        submitBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
    }

    public boolean verifyAge(){
        String ageStr = ageText.getText().toString();

        if(ageStr.equals("")){
            return false;
        }

        int age = Integer.parseInt(ageStr);

        return (age > 15 && age < 100);
    }

    public void onSubmit(){
        if(verifyAge()){
            postData();
        }else{
            Toast.makeText(getActivity(), "Invalid age", Toast.LENGTH_LONG).show();
        }
    }

    public void postData(){
        boolean isMale = gender.getSelectedItemPosition() == 0;

        SignUpUser signUpUser = new SignUpUser();
        signUpUser.setHasMedication(switch1.isChecked());
        signUpUser.setHasFinancialPressure(switch2.isChecked());
        signUpUser.setHasEmotionalSupport(switch2.isChecked());
        signUpUser.setMale(isMale);
        //DateOfBirth missing

        APIService.getInstance().callAPI("user/sign-up", getActivity());
    }
}
