package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.controllers.BaseWineController;
import com.delectable.mobile.events.basewines.SearchWinesEvent;
import com.delectable.mobile.ui.search.widget.WineSearchAdapter;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import javax.inject.Inject;

public class SearchWinesTabFragment extends BaseSearchTabFragment {

    private static final String TAG = SearchWinesTabFragment.class.getSimpleName();

    private WineSearchAdapter mAdapter = new WineSearchAdapter();

    @Inject
    protected BaseWineController mBaseWinesController;

    @Override
    protected BaseAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mBaseWinesController.searchWine(query, 0, 20); //TODO more than 20 results/pagination?
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchWinesEvent event) {
        mProgressBar.setVisibility(View.GONE);
        if (event.isSuccessful()) {
            mAdapter.setHits(event.getResult().getHits());
            mAdapter.notifyDataSetChanged();
            mEmptyStateTextView.setText("No Results"); //TODO no empty state designs yet
        } else {
            showToastError(event.getErrorMessage());
            mEmptyStateTextView.setText(event.getErrorMessage()); //TODO no empty state designs yet
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseWine baseWine = mAdapter.getItem(position);
        launchWineProfile(baseWine);
    }

    private void launchWineProfile(BaseWine baseWine) {
        Intent intent = new Intent();
        intent.putExtra(WineProfileActivity.PARAMS_BASE_WINE_ID, baseWine.getId());
        //TODO photohash gets passed in put it doesn't get used with the base_wine_id in WineProfileActivity
        intent.putExtra(WineProfileActivity.PARAMS_CAPTURE_PHOTO_HASH, (Parcelable)baseWine.getPhoto());
        intent.setClass(getActivity(), WineProfileActivity.class);
        startActivity(intent);
    }
}
