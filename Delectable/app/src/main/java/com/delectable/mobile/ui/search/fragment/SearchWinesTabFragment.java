package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchWinesTabFragment extends BaseSearchTabFragment {

    private static final String TAG = SearchWinesTabFragment.class.getSimpleName();

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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG + ".onQueryTextChange", newText);
        return false;
    }
}
