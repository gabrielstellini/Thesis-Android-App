package Fragments;

import android.app.ListFragment;
import android.view.MenuItem;
import android.widget.SearchView;

public class AddFriendFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {


    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
