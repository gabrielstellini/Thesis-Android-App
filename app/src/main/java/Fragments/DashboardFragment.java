package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;

import com.google.gson.Gson;

import Model.RequestResponseTypes.Food;
import Model.RequestResponseTypes.Score;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import gabrieltechnologies.sehm.R;

public class DashboardFragment extends Fragment implements APIServiceCallback{

    TextView pointsToday;
    TextView pointsThisWeek;
    TextView caloriesToday;
    TextView caloriesThisWeek;

    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,
                container, false);

        initialise(view);

        return view;
    }

    public void initialise(View view){
        pointsToday = view.findViewById(R.id.stat1Value);
        pointsThisWeek = view.findViewById(R.id.stat2Value);
        caloriesToday = view.findViewById(R.id.stat3Value);
        caloriesThisWeek = view.findViewById(R.id.stat4Value);

        APIService.addSubscriber(this);
        getData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getData(){
        APIService.getInstance().callAPI("food", getActivity(), RequestType.GET, "");
        APIService.getInstance().callAPI("user/score", getActivity(), RequestType.GET, "");
//        APIService.getInstance().callAPI("user/preferences", getActivity(), RequestType.GET, "");
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload, String apiUrl) {
        if(isSuccess){
            if(apiUrl.matches(".*food")){
                Food[] foods = gson.fromJson(payload, Food[].class);
                System.out.println("Here");
            } else if(apiUrl.matches(".*user/score")){
                Score[] scores = gson.fromJson(payload, Score[].class);
                System.out.println("Here");
            }
        }
    }

    @Override
    public void loginStatus(int statusCode) {

    }
}
