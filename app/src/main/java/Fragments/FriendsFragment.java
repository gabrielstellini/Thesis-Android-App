package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaouan.revealator.Revealator;

import java.util.LinkedList;

import CustomView.ICardCallback;
import CustomView.UserCard;
import Model.RequestResponseTypes.Friends;
import Model.RequestResponseTypes.Score;
import Model.RequestResponseTypes.User;
import Model.RequestResponseTypes.UserScore;
import Model.RequestType;
import Services.APIService;
import Services.APIServiceCallback;
import gabrieltechnologies.sehm.R;


public class FriendsFragment extends Fragment implements APIServiceCallback, ICardCallback{
    private View mRevealView;
    private FloatingActionButton floatingActionButton;
    private Button backBtn;
    TableLayout tableLayout;
    LinearLayout linearLayout;

    private Gson gson = new Gson();
    private LinkedList<UserScore> userScores = new LinkedList<>();

    int totalToRecieve = 0;
    int totalRecieved = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,
                container, false);
        initialise(view);

        return view;
    }

    public void initialise(View view) {
        floatingActionButton = view.findViewById(R.id.add_friends_btn);
        backBtn = view.findViewById(R.id.back_btn);

        APIService.addSubscriber(this);
        getData();

        initialiseTable(view);
        initialiseAnimations(view);
        initialiseSearch(view);
    }

    public void initialiseAnimations(View view){
        //initialise add button
        mRevealView = view.findViewById(R.id.add_friend);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.reveal(mRevealView)
                        .from(floatingActionButton)
                        .withCurvedTranslation()
                        .withRevealDuration(500)
                        .start();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
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

    public void getData() {
        APIService.getInstance().callAPI("friends", getActivity(), RequestType.GET, "");
    }

    @Override
    public void apiResponseListener(boolean isSuccess,String originalPayload, String payload, String apiUrl, RequestType requestType) {
        if (isSuccess) {
            if (apiUrl.matches(".*friends$") && requestType == RequestType.GET) {
                //reset
                clearRowData();
                userScores = new LinkedList<>();

                //get friends from API
                Friends friends = gson.fromJson(payload, Friends.class);
                getScores(friends);
            }
            else if (apiUrl.matches(".*score.*") && requestType == RequestType.GET) {
                UserScore userScore = gson.fromJson(payload, UserScore.class);
                this.userScores.add(userScore);
                totalRecieved++;

                if(totalRecieved == totalToRecieve){
                    refreshTable();
                }

            }else if (apiUrl.matches(".*search.*") && requestType == RequestType.GET) {
                try{
                    User[] userResults = gson.fromJson(payload, User[].class);
                    refreshResults(userResults);
                }catch (Exception e){
//                    Null userResults
                    User[] userResults = new User[0];
                    refreshResults(userResults);
                }
            } else if(apiUrl.matches(".*search.*") && requestType == RequestType.POST) {
                getData();
            }
        }
    }


    private void getScores(Friends friends){
        totalToRecieve = friends.getFriends().length;
        totalRecieved = 0;

        for(int i = 0; i<friends.getFriends().length; i++){
            String authId = friends.getFriends()[i];
            APIService.getInstance().callAPI("score/"+authId, getActivity(), RequestType.GET, "");
        }


    }

    private void refreshTable(){
        addTableData(userScores);
    }



    public void initialiseTable(View view) {
        tableLayout = view.findViewById(R.id.table_main);
        tableLayout.setStretchAllColumns(true);
        tableLayout.bringToFront();
    }


    public void addTableData(LinkedList<UserScore> userScores) {
        for (UserScore userScore: userScores) {

            Score[] scores = userScore.getScores();
            int totalScore = 0;

            for(int i = 0; i < scores.length; i++){
                totalScore+=scores[i].getPoints();
            }

            final TableRow tr = new TableRow(getActivity());
            TextView c1 = new TextView(getActivity());
            c1.setText(userScore.getUser().getUsername());
            TextView c2 = new TextView(getActivity());
            c2.setText(String.valueOf(totalScore));

            tr.addView(c1);
            tr.addView(c2);

            //set row styling
            c1.setTextAppearance(getActivity(), R.style.TableDataField);
            c2.setTextAppearance(getActivity(), R.style.TableDataField);

            c1.setGravity(Gravity.CENTER);
            c2.setGravity(Gravity.CENTER);

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

    @Override
    public void loginStatus(int statusCode) { }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        APIService.removeSubscriber(this);
    }

//    Add_friend code

    public void initialiseSearch(View view){
        linearLayout = view.findViewById(R.id.itemList);
        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                APIService.getInstance().callAPI("user/search/" + s, getActivity(), RequestType.GET, "");
                return false;
            }
        });
    }

    public void refreshResults(User[] users){
//        remove all children
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(linearLayout.getChildCount() > 0){
                    linearLayout.removeAllViews();
                }
            }
        });

        for(User user: users){
            final UserCard userCard = new UserCard(this.getActivity());
            userCard.setUser(user, this, getActivity());

            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.addView(userCard);
                }
            });
        }
    }

    @Override
    public void addFriendPressed(String AuthId) {
        Friends friend = new Friends();
        friend.setFriends(new String[]{AuthId});
        Gson gson = new Gson();
        String payload = gson.toJson(friend);
        APIService.getInstance().callAPI("friends", getActivity(), RequestType.POST, payload);

        //refresh friend data
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1000);
    }
}