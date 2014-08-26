package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.controllers.FoursquareController;
import com.delectable.mobile.events.foursquare.SearchedFoursquareVenuesEvent;
import com.delectable.mobile.model.api.foursquare.FoursquareVenueItem;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.widget.FoursquareVenuesAdapter;
import com.delectable.mobile.util.HelperUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import javax.inject.Inject;

public class FoursquareVenueSelectionFragment extends BaseFragment {

    public static final String RESULT_FOURSQUARE_ID = "foursquare_id";

    public static final String RESULT_FOURSQUARE_NAME = "foursquare_name";

    private static final String TAG = FoursquareVenueSelectionFragment.class.getSimpleName();

    @Inject
    FoursquareController mFoursquareController;

    private View mView;

    private ListView mListView;

    private FoursquareVenuesAdapter mAdapter;

    private ArrayList<FoursquareVenueItem> mVenues;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_listview, container, false);

        mListView = (ListView) mView;
        mVenues = new ArrayList<FoursquareVenueItem>();
        mAdapter = new FoursquareVenuesAdapter(mVenues);
        mListView.setAdapter(mAdapter);

        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.ab_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_CANCELED, null);
                }
                getActivity().onBackPressed();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getTargetFragment() != null) {
                    // Pass Back the Fourdquare ID and Name
                    FoursquareVenueItem venue = mAdapter.getItem(position);
                    Intent data = new Intent();
                    Bundle resultArgs = new Bundle();
                    resultArgs.putString(RESULT_FOURSQUARE_ID, venue.getId());
                    resultArgs.putString(RESULT_FOURSQUARE_NAME, venue.getName());
                    data.putExtras(resultArgs);
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_OK, data);
                }
                getActivity().onBackPressed();
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Location lastLocation = ((BaseActivity) getActivity()).getLastLocation();
        if (lastLocation != null) {
            mFoursquareController
                    .searchFoursquareVenuesByLatLon(HelperUtil.getLatLngFromLocation(lastLocation));
        } else {
            // TODO: Show empty state for no location / can't find user's location
            showToastError("Failed to get Location");
        }
    }

    public void onEventMainThread(SearchedFoursquareVenuesEvent event) {
        if (event.isSuccessful()) {
            mVenues.clear();
            mVenues.addAll(event.getVenues());
            mAdapter.notifyDataSetChanged();
        } else if (event.getErrorMessage() != null) {
            showToastError(event.getErrorMessage());
        }
    }
}