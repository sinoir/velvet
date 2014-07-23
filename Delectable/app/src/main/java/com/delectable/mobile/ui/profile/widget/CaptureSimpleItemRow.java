package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.common.widget.RatingsBarView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CaptureSimpleItemRow extends RelativeLayout {

    private ImageView mWineImage;

    private TextView mProducerName;

    private TextView mWineName;

    private RatingsBarView mRatingBarView;

    private CaptureDetails mCaptureData;

    public CaptureSimpleItemRow(Context context) {
        this(context, null);
    }

    public CaptureSimpleItemRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureSimpleItemRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_simple_wine_detail, this);

        mWineImage = (ImageView) findViewById(R.id.image);
        mProducerName = (TextView) findViewById(R.id.producer_name);
        mWineName = (TextView) findViewById(R.id.wine_name);
        mRatingBarView = (RatingsBarView) findViewById(R.id.rating_bar);
    }

    public void updateData(CaptureDetails capture, String userId) {
        String captureTitle = capture.getDisplayTitle();
        String captureName = capture.getDisplayDescription();
        String captureImageUrl = capture.getPhoto().getThumbUrl();

        // TODO: Toggle Privacy icon over the Image
        if (capture.getPrivate()) {
        } else {
        }

        mProducerName.setText(captureTitle);
        mWineName.setText(captureName);
        ImageLoaderUtil.loadImageIntoView(getContext(), captureImageUrl, mWineImage);

        mRatingBarView.setPercent(capture.getRatingPercentForId(userId));
    }
}
