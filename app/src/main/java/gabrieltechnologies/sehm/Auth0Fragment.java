package gabrieltechnologies.sehm;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import Services.APIService;

public class Auth0Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        // Inflate the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_auth0, parent, false);

        Button loginWithTokenButton = view.findViewById(R.id.loginButton);

        loginWithTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService.getInstance().login(getActivity());
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
