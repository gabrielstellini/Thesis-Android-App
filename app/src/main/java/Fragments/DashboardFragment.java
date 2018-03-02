package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;

import com.google.gson.Gson;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import Model.RequestResponseTypes.Food;
import Model.RequestResponseTypes.Score;
import Model.RequestResponseTypes.UserPreference;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import gabrieltechnologies.sehm.R;

public class DashboardFragment extends Fragment implements APIServiceCallback{

    private TextView scoreToday;
    private TextView scoreThisWeek;
    private TextView caloriesToday;
    private TextView caloriesThisWeek;

    private TextView currStatus;

    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,
                container, false);

//        TODO: Comment me when I want to skip login
        initialise(view);

        return view;
    }

    public void initialise(View view){
        scoreToday = view.findViewById(R.id.stat1Value);
        scoreThisWeek = view.findViewById(R.id.stat2Value);
        caloriesToday = view.findViewById(R.id.stat3Value);
        caloriesThisWeek = view.findViewById(R.id.stat4Value);
        currStatus = view.findViewById(R.id.currStatus);

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
        APIService.getInstance().callAPI("user/preferences", getActivity(), RequestType.GET, "");
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload, String apiUrl, RequestType requestType) {
        if(isSuccess){
            if(apiUrl.matches(".*food")){
                Food[] foods = gson.fromJson(payload, Food[].class);
                showCalorieStatistics(foods);
            } else if(apiUrl.matches(".*user/score")){
                Score[] scores = gson.fromJson(payload, Score[].class);
                showScoresStatistics(scores);
            } else if(apiUrl.matches(".*user/preferences")){
                UserPreference[] userPreference = gson.fromJson(payload, UserPreference[].class);
                showNextEatingTime(userPreference);
            }
        }
    }

    public void showNextEatingTime(UserPreference[] userPreferences){
        Calendar[] userDates = new Calendar[userPreferences.length*2];
        Calendar currentTime = Calendar.getInstance();

        boolean eatingAllowed = false;

        for (int i = 0; i < userPreferences.length; i++){
            String[] startTimestr = userPreferences[i].getStartTime().split(":");
            String[] endTimestr = userPreferences[i].getEndTime().split(":");

            userDates[i*2] = Calendar.getInstance();
            userDates[(i*2)+1] = Calendar.getInstance();

            userDates[i*2].set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimestr[0]));
            userDates[i*2].set(Calendar.MINUTE, Integer.parseInt(startTimestr[1]));
            userDates[i*2].set(Calendar.SECOND, 0);

            userDates[(i*2)+1].set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimestr[0]));
            userDates[(i*2)+1].set(Calendar.MINUTE, Integer.parseInt(endTimestr[1]));
            userDates[(i*2)+1].set(Calendar.SECOND, 0);

            //if times align, allow eating
            if(userDates[i*2].before(currentTime) && userDates[(i*2)+1].after(currentTime)){
                eatingAllowed = true;

                this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currStatus.setText("Buon apetit! You can eat right now");
                    }
                });
            }
        }




        if(!eatingAllowed){

            int bestIndex = 0;
            int[] hoursLeft = new int[userPreferences.length];
            int[] minutesLeft = new int[userPreferences.length];

            //calculate time difference in hours and minutes
            for(int i = 0; i<userPreferences.length; i++){
                long seconds = (userDates[i*2].getTimeInMillis() - currentTime.getTimeInMillis()) / 1000;
                hoursLeft[i] = (int) (Math.abs(seconds) / 3600);
                minutesLeft[i] = (int) (((Math.abs(seconds)) /60)% 60);

                //when time has already passed in the day, calculate 24hrs - time
                if(seconds < 0){
                    if(minutesLeft[i] != 0){
                        hoursLeft[i] = 23-hoursLeft[i];
                        minutesLeft[i] = 60-minutesLeft[i];
                    }else{
                        hoursLeft[i] = 24-hoursLeft[i];
                    }
                }
            }

            for (int i = 0; i<hoursLeft.length; i++){
                //check if your is less
                if(hoursLeft[i]<hoursLeft[bestIndex]){
                    bestIndex = i;
                }else if(hoursLeft[i] == hoursLeft[bestIndex]){
                    //when hour is the same, check if minutes are less in one use case
                    if(minutesLeft[i] < minutesLeft[bestIndex]){
                        bestIndex = i;
                    }
                }
            }

            final String resultStr = "Hold your horses, you can't eat right now. You are scheduled to eat in "
                    + hoursLeft[bestIndex] +  "hr "+ minutesLeft[bestIndex] + "minutes";


            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currStatus.setText(resultStr);
                }});
        }

    }

    public void showCalorieStatistics(final Food[] foods) {
        int caloriesTodayInt = 0;
        int caloriesThisWeekInt = 0;

        Timestamp timestamp  = new Timestamp(System.currentTimeMillis());
        Date currentDate = new Date(timestamp.getTime());


        for(Food food : foods){
            Date foodEatenDate = new Date(food.getTimestamp());
            long diff = currentDate.getTime() - foodEatenDate.getTime();
            long dayDifference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if(dayDifference <= 1){
                caloriesTodayInt += food.getCalories();
            }

            if(dayDifference <= 7){
                caloriesThisWeekInt += food.getCalories();
            }
        }

        final String caloriesTodayStr = Integer.toString(caloriesTodayInt);
        final String caloriesThisWeekStr = Integer.toString(caloriesThisWeekInt);

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                caloriesToday.setText(caloriesTodayStr);
                caloriesThisWeek.setText(caloriesThisWeekStr);
            }
        });
    }

    public void showScoresStatistics(final Score[] scores){

        int scoreTodayInt = 0;
        int scoreThisWeekInt = 0;

        Timestamp timestamp  = new Timestamp(System.currentTimeMillis());
        Date currentDate = new Date(timestamp.getTime());


        for(Score score : scores){
            Date scoreDate = new Date(score.getTimestamp());
            long diff = currentDate.getTime() - scoreDate.getTime();
            long dayDifference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if(dayDifference <= 1){
                scoreTodayInt += score.getPoints();
            }
//
            if(dayDifference <= 7){
                scoreThisWeekInt += score.getPoints();
            }
        }

        final String scoreTodayStr = Integer.toString(scoreTodayInt);
        final String scoreThisWeekStr = Integer.toString(scoreThisWeekInt);


        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                scoreToday.setText(scoreTodayStr);
                scoreThisWeek.setText(scoreThisWeekStr);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        APIService.removeSubscriber(this);
    }

    @Override
    public void loginStatus(int statusCode) {

    }
}
