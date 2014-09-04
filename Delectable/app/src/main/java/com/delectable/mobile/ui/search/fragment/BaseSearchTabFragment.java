package com.delectable.mobile.ui.search.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

/**
 * This base class handles the SearchView in the ActionBar and it's text listener.
 */
public abstract class BaseSearchTabFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Custom Back Arrow...
        inflater.inflate(R.menu.search_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        //TODO platform bug with setIconifiedByDefault, makes searchview look wierd
        //searchView.setIconifiedByDefault(false);
    }
}
