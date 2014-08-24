package com.delectable.mobile.ui.camera.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.model.api.foursquare.FoursquareVenueItem;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FoursquareVenueRow extends RelativeLayout {

    private static final String TAG = FoursquareVenueRow.class.getSimpleName();

    @InjectView(R.id.location_name)
    TextView mLocationName;

    @InjectView(R.id.location_address)
    TextView mLocationAddress;

    public FoursquareVenueRow(Context context) {
        this(context, null);
    }

    public FoursquareVenueRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoursquareVenueRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_foursquare_venue, this);
        ButterKnife.inject(this);
    }

    public void updateVenueData(FoursquareVenueItem foursquareVenueItem) {
        if (foursquareVenueItem.getName() != null) {
            mLocationName.setText(foursquareVenueItem.getName());
        } else {
            Log.wtf(TAG, "Foursquare Name shouldn't be null");
            mLocationName.setText("");
        }

        if (foursquareVenueItem.getAddress() != null) {
            mLocationAddress.setText(foursquareVenueItem.getAddress());
        } else {
            mLocationAddress.setText("");
        }
    }
}
