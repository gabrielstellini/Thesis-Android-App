package Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Model.RequestResponseTypes.Food;
import gabrieltechnologies.sehm.R;
import android.app.Fragment;

public class FoodFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food,
                container, false);

        initialise(view);

        return view;
    }

    public void initialise(View view){
        FloatingActionButton floatingActionButton = view.findViewById(R.id.add_food);
        initialiseTable(view);
    }

    public void initialiseTable(View view){
        TableLayout tableLayout = view.findViewById(R.id.table_main);
        tableLayout.setStretchAllColumns(true);
        tableLayout.bringToFront();

        Food[] foods = new Food[40];

        for (int i = 0; i < foods.length; i++){
            foods[i] = new Food();
            foods[i].setCalories(i*1000);
            foods[i].setName(String.valueOf(i));
            foods[i].setQuantity(10);
        }
//
//        TableRow th = new TableRow(getActivity());
//
//        TextView h1 = new TextView(getActivity());
//        TextView h2 = new TextView(getActivity());
//        TextView h3 = new TextView(getActivity());
//
//        h1.setText("Name");
//        h2.setText("Calories");
//        h3.setText("Quantity");
//
//        h1.setTextAppearance(getActivity(), R.style.TableHeaderField);
//        h2.setTextAppearance(getActivity(), R.style.TableHeaderField);
//        h3.setTextAppearance(getActivity(), R.style.TableHeaderField);
//
//        h1.setGravity(Gravity.CENTER);
//        h2.setGravity(Gravity.CENTER);
//        h3.setGravity(Gravity.CENTER);

//        th.addView(h1);
//        th.addView(h2);
//        th.addView(h3);
//
//        tableLayout.addView(th);


        for(int i = 0; i < foods.length; i++){
            TableRow tr =  new TableRow(getActivity());
            TextView c1 = new TextView(getActivity());
            c1.setText(foods[i].getName());
            TextView c2 = new TextView(getActivity());
            c2.setText(String.valueOf(foods[i].getCalories()));
            TextView c3 = new TextView(getActivity());
            c3.setText(String.valueOf(foods[i].getQuantity()));

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

            tableLayout.addView(tr);


        }


        //TODO: find table by id
        //TODO: add listener to button


    }

    public void onAddClicked(){

    }

    public void postData(){
//        Gson gson = new Gson();
//
//        String payload = gson.toJson(signUpUser);
//
//        APIService.getInstance().callAPI("user/sign-up", getActivity(), RequestType.POST, payload);
    }
}
