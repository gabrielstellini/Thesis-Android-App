package gabrieltechnologies.sehm;

import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

    Fragment currFragment = null;
    int selectedItem = R.id.nav_dashboard;

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

        //set default fragment to dashboard
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dashboardContainer, dashboardFragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        currFragment = dashboardFragment;
        selectedItem = 0;
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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //ignore when user clicks same item
        if(id != selectedItem){
            selectedItem = id;

            if(currFragment != null){
                FragmentTransaction removalTransaction = getFragmentManager().beginTransaction();
                removalTransaction.remove(currFragment);
                currFragment = null;
                removalTransaction.commit();
            }

            //start transaction
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // Handling navigation view item clicks here.

            if (id == R.id.nav_dashboard) {
                fragmentTransaction.replace(R.id.dashboardContainer, dashboardFragment);
                currFragment = dashboardFragment;
            } else if (id == R.id.nav_food) {
                fragmentTransaction.replace(R.id.dashboardContainer, foodFragment);
                currFragment = foodFragment;
            } else if (id == R.id.nav_friends) {
                fragmentTransaction.replace(R.id.dashboardContainer, friendsFragment);
                currFragment = friendsFragment;
            }

            fragmentTransaction.commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
