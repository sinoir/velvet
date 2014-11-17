package com.delectable.mobile.ui.camera.widget;

import com.delectable.mobile.api.endpointmodels.foursquare.FoursquareVenueItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class FoursquareVenuesAdapter extends BaseAdapter {

    private List<FoursquareVenueItem> mVenues = new ArrayList<FoursquareVenueItem>();

    public void setVenues(List<FoursquareVenueItem> venues) {
        mVenues = venues;
    }

    @Override
    public int getCount() {
        return mVenues.size();
    }

    @Override
    public FoursquareVenueItem getItem(int position) {
        return mVenues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FoursquareVenueRow row = (FoursquareVenueRow) convertView;
        if (row == null) {
            row = new FoursquareVenueRow(parent.getContext());
        }
        row.updateVenueData(mVenues.get(position));
        return row;
    }
}
