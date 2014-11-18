package com.delectable.mobile.ui.camera.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.endpointmodels.foursquare.FoursquareVenueItem;

import android.view.LayoutInflater;
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
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (FoursquareVenueRow) inflater
                    .inflate(R.layout.row_foursquare_venue_impl, parent, false);
        }
        row.updateVenueData(mVenues.get(position));
        return row;
    }
}
