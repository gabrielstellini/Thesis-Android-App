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

import Model.RequestResponseTypes.Food;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import gabrieltechnologies.sehm.R;
import android.app.Fragment;

import com.google.gson.Gson;
import com.jaouan.revealator.Revealator;

public class FoodFragment extends Fragment implements APIServiceCallback {
    private Gson gson = new Gson();

    private View mRevealView;

    TableLayout tableLayout;
    private FloatingActionButton floatingActionButton;
    private EditText addFoodName;
    private EditText addFoodCalories;
    private EditText addFoodQuantity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food,
                container, false);

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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                getData();
            }
        }, 1000);
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
        APIService.removeSubscriber(this);
    }
}