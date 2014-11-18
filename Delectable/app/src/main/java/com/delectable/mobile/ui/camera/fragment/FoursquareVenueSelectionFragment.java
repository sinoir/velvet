package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.FoursquareController;
import com.delectable.mobile.api.endpointmodels.foursquare.FoursquareVenueItem;
import com.delectable.mobile.api.events.foursquare.SearchedFoursquareVenuesEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.activity.WineCaptureActivity;
import com.delectable.mobile.ui.camera.widget.FoursquareVenuesAdapter;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.util.HelperUtil;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class FoursquareVenueSelectionFragment extends BaseFragment {

    public static final String RESULT_FOURSQUARE_ID = "foursquare_id";

    public static final String RESULT_FOURSQUARE_NAME = "foursquare_name";

    private static final String TAG = FoursquareVenueSelectionFragment.class.getSimpleName();

    @Inject
    protected FoursquareController mFoursquareController;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.empty_state_layout)
    protected View mEmptyStateLayout;

    /**
     * In the layout, this covers the loading circle when it's set to visible, so there's no need to
     * hide the loading circle.
     */
    @InjectView(R.id.nothing_to_display_textview)
    protected FontTextView mNoListDataText;

    private FoursquareVenuesAdapter mAdapter = new FoursquareVenuesAdapter();

    public FoursquareVenueSelectionFragment() {
    }

    public static FoursquareVenueSelectionFragment newInstance(Fragment targetFragment,
            int requestCode) {
        FoursquareVenueSelectionFragment f = new FoursquareVenueSelectionFragment();
        f.setTargetFragment(targetFragment, requestCode);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enableBackButton(true);
        setActionBarTitle((String) null);
        setActionBarSubtitle(getString(R.string.capture_submit_drinking_where_text));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview_w_loading, container, false);
        ButterKnife.inject(this, view);
        mListView.setEmptyView(mEmptyStateLayout);
        mListView.setAdapter(mAdapter);
        mNoListDataText.setText(getString(R.string.failed_to_get_location));
        return view;
    }

    @OnItemClick(R.id.list_view)
    protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getTargetFragment() != null) {
            // Pass Back the Foursquare ID and Name
            FoursquareVenueItem venue = mAdapter.getItem(position);
            Intent data = new Intent();
            data.putExtra(RESULT_FOURSQUARE_ID, venue.getId());
            data.putExtra(RESULT_FOURSQUARE_NAME, venue.getName());
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        }
        getActivity().onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_CANCELED, null);
                }
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Location lastLocation = ((WineCaptureActivity) getActivity()).getLastLocation();
        if (lastLocation != null) {
            mNoListDataText.setVisibility(View.GONE);
            mFoursquareController
                    .searchFoursquareVenuesByLatLon(HelperUtil.getLatLngFromLocation(lastLocation));
        }
    }

    public void onEventMainThread(SearchedFoursquareVenuesEvent event) {
        mNoListDataText.setVisibility(View.VISIBLE);
        if (event.isSuccessful()) {
            mAdapter.setVenues(event.getVenues());
            mAdapter.notifyDataSetChanged();
        } else if (event.getErrorMessage() != null) {
            mNoListDataText.setText(R.string.trouble_retrieving_locations);
        }
    }
}
