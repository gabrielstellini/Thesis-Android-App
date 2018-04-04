package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;

import Model.RequestResponseTypes.UserPreference;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import Services.EatingTimesCallback;
import gabrieltechnologies.sehm.R;


public class EatingTimesFragment extends Fragment implements APIServiceCallback {

    ArrayList<TimePicker> timePickers = new ArrayList<>();
    EatingTimesCallback eatingTimesCallback;

    boolean successfulPost = true;
    int numberOfUserPreferences = 2;
    int numberOfPosts = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eating_times,
                container, false);

        initialise(view);

        return view;
    }

    private void initialise(View view){
        TimePicker start1 = view.findViewById(R.id.start1);
        TimePicker end1 = view.findViewById(R.id.end1);
        TimePicker start2 = view.findViewById(R.id.start2);
        TimePicker end2 = view.findViewById(R.id.end2);

        timePickers.add(start1);
        timePickers.add(end1);
        timePickers.add(start2);
        timePickers.add(end2);

        for(int i=0; i<timePickers.size(); i+=2){
            initialiseTimePicker(timePickers.get(i));
            initialiseTimePicker(timePickers.get(i+1));
        }

//      TODO: add and remove dynamically with a button

        Button submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);
            }
        });
    }

    public void initialiseTimePicker(TimePicker timePicker){
        timePicker.setIs24HourView(true);
    }


    public void onSubmit(View view){
        Time[] times = new Time[timePickers.size()];
        boolean valid = true;


        for(int i=0; i<timePickers.size(); i++){
            timePickers.get(i);

            int hour = timePickers.get(i).getCurrentHour();
            int minute = timePickers.get(i).getCurrentMinute();


            Time time = new Time(hour, minute, 0);
            times[i] = time;
        }

        for(int i=0; i<timePickers.size(); i+=2){
            if(times[i].after(times[i+1]) || times[i].getTime() == times[i+1].getTime()){
                valid = false;
            }
        }

        if(!valid){
            Toast.makeText(getActivity(), "Invalid time, please make sure the times the start and end times are after each other", Toast.LENGTH_SHORT).show();
        }
        else {
            postData(times);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void postData(Time[] times) {

        Gson gson = new Gson();

        UserPreference[] userPreferences = new UserPreference[2];

//        userPreferences[0].setStartTime(times[0]);
//        userPreferences[0].setEndTime(times[1]);
//        userPreferences[1].setStartTime(times[2]);
//        userPreferences[1].setEndTime(times[3]);

        APIService.addSubscriber(this);

        for (int i = 0; i < userPreferences.length; i++) {
            userPreferences[i] = new UserPreference();
            userPreferences[i].setStartTime(times[i * 2].getHours() + ":" + times[i * 2].getMinutes());
            userPreferences[i].setEndTime(times[(i * 2) + 1].getHours() + ":" + times[(i * 2) + 1].getMinutes());
            String payload = gson.toJson(userPreferences[i]);
            APIService.getInstance().callAPI("user/preferences", getActivity(), RequestType.POST, payload);
        }
    }

    public void setEatingTimesCallback(EatingTimesCallback eatingTimesCallback) {
        this.eatingTimesCallback = eatingTimesCallback;
    }

    @Override
    public void apiResponseListener(boolean isSuccess,String originalPayload, String payload, String apiUrl, RequestType requestType) {
        numberOfPosts++;

        //verify all posts were successful
        if (numberOfPosts == numberOfUserPreferences && successfulPost) {
            eatingTimesCallback.finishedSavingEatingTimes();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        APIService.removeSubscriber(this);
    }

    @Override
    public void loginStatus(int statusCode) {   }
}
