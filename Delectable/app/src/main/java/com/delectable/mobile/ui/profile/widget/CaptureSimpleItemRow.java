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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CaptureSimpleItemRow extends RelativeLayout {

    @InjectView(R.id.wine_image)
    protected ImageView mWineImage;

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    @InjectView(R.id.capture_comment_tagged_text)
    protected TextView mCommentText;

    @InjectView(R.id.rating_bar)
    protected RatingsBarView mRatingBarView;

    @InjectView(R.id.capture_time)
    protected TextView mCaptureTime;

    @InjectView(R.id.compose_capture)
    protected View mComposeCapture;

    @InjectView(R.id.discard_capture)
    protected View mDiscardCapture;

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
        ButterKnife.inject(this);
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
