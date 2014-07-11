package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This view has a picture of the wine capture, as well as the producer and wine name in the bottom
 * left hand corner. Used in the Wine Profile screen, Wine Capture screen, and also in the Follower
 * Feed screen.
 */
public class WineBannerView extends RelativeLayout {

    private ImageView mWineImage;

    private TextView mProducerName;

    private TextView mWineName;

    public WineBannerView(Context context) {
        this(context, null);
    }

    public WineBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WineBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.wine_banner_view, this);

        mWineImage = (ImageView) findViewById(R.id.wine_image);
        mProducerName = (TextView) findViewById(R.id.producer_name);
        mWineName = (TextView) findViewById(R.id.wine_name);
    }

    /**
     *
     * @param captureDetails
     * @param includeVintage include the vintage year in the wine name
     */
    //Follower Feed and Wine Capture screen use this method to update their views
    public void updateCaptureDetailsData(CaptureDetails captureDetails, boolean includeVintage) {
        String wineImageUrl = captureDetails.getPhoto().getUrl();
        String producerName = captureDetails.getDisplayTitle();
        String wineName = captureDetails.getDisplayDescription();

        if(includeVintage) {
            wineName += " " + captureDetails.getWineProfile().getVintage();
        }

        ImageLoaderUtil.loadImageIntoView(getContext(), wineImageUrl, mWineImage);
        mProducerName.setText(producerName);
        mWineName.setText(wineName);
    }




}
