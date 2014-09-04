package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.controllers.BaseWinesController;
import com.delectable.mobile.events.basewines.SearchWinesEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

public class SearchWinesTabFragment extends BaseSearchTabFragment {

    private static final String TAG = SearchWinesTabFragment.class.getSimpleName();

    @Inject
    BaseWinesController mBaseWinesController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        TextView tv = new TextView(getActivity());
        tv.setText(TAG);
        return tv;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG + ".onQueryTextSubmit", query);
        mBaseWinesController.searchWine(query, 0, 20);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchWinesEvent event) {
        Log.d(TAG + ".onEventMainThread", "SearchWineEvent");
        if (event.isSuccessful()) {
            Log.d(TAG + ".SearchWineEvent", event.getResult().getHits().toString());
        } else {
            showToastError(event.getErrorMessage());
        }
    }
}
