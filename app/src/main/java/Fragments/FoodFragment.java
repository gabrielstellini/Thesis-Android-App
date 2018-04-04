package Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Model.RequestResponseTypes.DataPoint;
import Model.RequestResponseTypes.Food;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import Utils.BandRecorder;
import Utils.DataStore;
import gabrieltechnologies.sehm.R;
import android.app.Fragment;

import com.google.gson.Gson;
import com.jaouan.revealator.Revealator;

public class FoodFragment extends Fragment implements APIServiceCallback {
    private final int recordPeriodMillis = 20000;


    private Gson gson = new Gson();

    private View mRevealView;

    TableLayout tableLayout;
    private FloatingActionButton floatingActionButton;
    private EditText addFoodName;
    private EditText addFoodCalories;
    private EditText addFoodQuantity;
    private BandRecorder bandRecorder;

    boolean isRecording = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food,
                container, false);
        bandRecorder = new BandRecorder(getActivity());

        initialise(view);
        return view;
    }

    public void initialise(View view) {
        floatingActionButton = view.findViewById(R.id.add_food_btn);
        addFoodName = view.findViewById(R.id.add_food_name);
        addFoodCalories = view.findViewById(R.id.add_food_calories);
        addFoodQuantity = view.findViewById(R.id.add_food_quantity);

        APIService.addSubscriber(this);
        getData();

        initialiseTable(view);
        initialiseAnimations(view);
        initialiseButtons(view);
    }

    public void initialiseButtons(View view){
        Button button = view.findViewById(R.id.send_food_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
    }


    public void initialiseAnimations(View view){
        //initialise add button
        mRevealView = view.findViewById(R.id.add_food);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.reveal(mRevealView)
                        .from(floatingActionButton)
                        .withCurvedTranslation()
                        //.withCurvedTranslation(curvePoint)
                        //.withChildsAnimation()
                        //.withDelayBetweenChildAnimation(...)
                        //.withChildAnimationDuration(...)
                        // .withTranslateDuration(500)
                        //.withHideFromViewAtTranslateInterpolatedTime(...)
                        .withRevealDuration(500)
                        //.withEndAction(...)
                        .start();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(mRevealView)
                        .to(floatingActionButton)
                        .withCurvedTranslation()
                        .withUnrevealDuration(500)
                        .start();
            }
        });
    }

    public void onSubmit(){
        Food food = new Food();

        String foodName = addFoodName.getText().toString();
        int quantity = Integer.valueOf(addFoodQuantity.getText().toString());
        int calories = Integer.valueOf(addFoodCalories.getText().toString());




        Revealator.unreveal(mRevealView)
                .to(floatingActionButton)
                .withCurvedTranslation()
                .withUnrevealDuration(500)
                .start();

        food.setName(foodName);
        food.setQuantity(quantity);
        food.setCalories(calories);

        postData(food);


        //refresh food data
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1000);

        //start recording band data
        if(!isRecording){
            bandRecorder.startRecording((TextView)getActivity().findViewById(R.id.statusText));

            //stop recording after 10minutes
            final Handler bandHandler = new Handler();
            bandHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bandRecorder.stopRecording();
                    isRecording = false;
                    //push data when sample is ready
                    pushStressData();
                }
//            }, 600000);
            }, recordPeriodMillis);
        }
    }



    public void getData() {
        APIService.getInstance().callAPI("food", getActivity(), RequestType.GET, "");
    }

    @Override
    public void apiResponseListener(boolean isSuccess,String originalPayload, String payload, String apiUrl, RequestType requestType) {
        if (isSuccess) {
            if (apiUrl.matches(".*food") && requestType == RequestType.GET) {
                Food[] foods = gson.fromJson(payload, Food[].class);
                clearRowData();
                addTableData(foods);
            }
        }
    }


    public void initialiseTable(View view) {
        tableLayout = view.findViewById(R.id.table_main);
        tableLayout.setStretchAllColumns(true);
        tableLayout.bringToFront();
    }


    public void addTableData(Food[] foods) {
        for (Food food : foods) {
            final TableRow tr = new TableRow(getActivity());
            TextView c1 = new TextView(getActivity());
            c1.setText(food.getName());
            TextView c2 = new TextView(getActivity());
            c2.setText(String.valueOf(food.getCalories()));
            TextView c3 = new TextView(getActivity());
            c3.setText(String.valueOf(food.getQuantity()));

            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);

            //set row styling
            c1.setTextAppearance(getActivity(), R.style.TableDataField);
            c2.setTextAppearance(getActivity(), R.style.TableDataField);
            c3.setTextAppearance(getActivity(), R.style.TableDataField);

            c1.setGravity(Gravity.CENTER);
            c2.setGravity(Gravity.CENTER);
            c3.setGravity(Gravity.CENTER);

            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tableLayout.addView(tr);
                }
            });
        }
    }

    public void clearRowData() {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = tableLayout.getChildCount();
                for (int i = 1; i < count; i++) {
                    View child = tableLayout.getChildAt(i);
                    if (child instanceof TableRow) {
                        ((ViewGroup) child).removeAllViews();
                    }
                }
            }
        });
    }

    public void postData(Food food) {
        Gson gson = new Gson();
        String payload = gson.toJson(food);
        APIService.getInstance().callAPI("food", getActivity(), RequestType.POST, payload);
    }

    @Override
    public void loginStatus(int statusCode) { }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bandRecorder.onDestroy();
        APIService.removeSubscriber(this);
    }

    public void pushStressData(){
        Gson gson = new Gson();
        String payload = gson.toJson(DataStore.dataPoints.toArray());
        APIService.getInstance().callAPI("datapoint", getActivity(), RequestType.POST, payload);
        DataStore.dataPoints.clear();
    }


    public void pushDummyCalmStressData(){
        Gson gson = new Gson();
        DataPoint[] dataPoints = new DataPoint[3];
        dataPoints[0] = new DataPoint(1522711001, 80,0.79123,2000.1, "LOCKED", "WORN");
        dataPoints[1] = new DataPoint(1522711301, 80, 0.95123,3000.1, "LOCKED", "WORN");
        dataPoints[2] = new DataPoint(1522711401, 82, 0.90123,3100.1, "LOCKED", "WORN");
        String payload = gson.toJson(dataPoints);
        APIService.getInstance().callAPI("datapoint", getActivity(), RequestType.POST, payload);
    }
}