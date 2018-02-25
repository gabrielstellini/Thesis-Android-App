package gabrieltechnologies.sehm;

import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import Fragments.DashboardFragment;
import Fragments.FoodFragment;
import Fragments.FriendsFragment;
import Fragments.PreferencesFragment;

import android.app.FragmentTransaction;
import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FoodFragment foodFragment = new FoodFragment();
    DashboardFragment dashboardFragment = new DashboardFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    PreferencesFragment preferencesFragment = new PreferencesFragment();

    Fragment currFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        TextView textView = findViewById(R.id.nav_logout);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    Toast.makeText(DashboardActivity.this, "LOGOUT CALLED", Toast.LENGTH_SHORT).show();
                    return true;
            }
        });


        //set default fragment to dashboard
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dashboardContainer, dashboardFragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        currFragment = dashboardFragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(currFragment != null){
            FragmentTransaction removalTransaction = getFragmentManager().beginTransaction();
            removalTransaction.remove(currFragment);
            currFragment = null;
            removalTransaction.commit();
        }

        //start transaction
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            fragmentTransaction.add(R.id.dashboardContainer, dashboardFragment);
            currFragment = dashboardFragment;
        } else if (id == R.id.nav_food) {
            fragmentTransaction.add(R.id.dashboardContainer, foodFragment);
            currFragment = foodFragment;
        } else if (id == R.id.nav_friends) {
            fragmentTransaction.add(R.id.dashboardContainer, friendsFragment);
            currFragment = friendsFragment;
        }
//        else if(id == R.id.nav_preferences){
//            fragmentTransaction.add(R.id.dashboardContainer, preferencesFragment);
//            currFragment = preferencesFragment;
//        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
