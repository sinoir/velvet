package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureState;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.RatingTextView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MinimalPendingCaptureRow extends RelativeLayout {

    private static final String TAG = MinimalPendingCaptureRow.class.getSimpleName();

    @InjectView(R.id.wine_image)
    protected ImageView mWineImage;

    @InjectView(R.id.producer_name)
    protected FontTextView mProducerName;

    @InjectView(R.id.wine_name)
    protected FontTextView mWineName;

    @InjectView(R.id.rating_textview)
    protected RatingTextView mRating;

    @InjectView(R.id.add_rating_remove_text_container)
    protected RelativeLayout mAddRatingRemoveTextContainer;

    @InjectView(R.id.add_rating_textview)
    protected TextView mAddRatingTextView;

    @InjectView(R.id.remove_textview)
    protected TextView mRemoveRatingTextView;

    private PendingCapture mPendingCapture;

    private ActionsHandler mActionsHandler;

    public MinimalPendingCaptureRow(Context context) {
        this(context, null);
    }

    public MinimalPendingCaptureRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinimalPendingCaptureRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_minimal_pending_capture, this);
        ButterKnife.inject(this);

        mRating.setVisibility(View.GONE);
    }

    public void updateData(PendingCapture capture) {
        mPendingCapture = capture;

        updateWineInfo();
    }

    private void updateWineInfo() {

        String captureTitle;
        String captureName;

        CaptureState state = CaptureState.getState(mPendingCapture);
        switch (state) {
            case IDENTIFIED:
                captureTitle = mPendingCapture.getWineProfile().getProducerName();
                captureName = mPendingCapture.getWineProfile().getName();
                break;
            case IMPOSSIBLED:
                captureTitle = "";
                captureName = mPendingCapture.getTranscriptionErrorMessage();
                break;
            case UNVERIFIED:
                captureTitle = mPendingCapture.getBaseWine().getProducerName();
                captureName = mPendingCapture.getBaseWine().getName();
            case UNIDENTIFIED:
            default:
                captureTitle = getResources().getString(R.string.user_captures_id_in_progress);
                captureName = getResources().getString(R.string.user_captures_check_back);
                break;
        }
        String captureImageUrl = mPendingCapture.getPhoto().getBestThumb();

        mProducerName.setText(captureTitle);
        mWineName.setText(captureName);
        ImageLoaderUtil.loadImageIntoView(getContext(), captureImageUrl, mWineImage);
    }

    @OnClick({R.id.wine_image, R.id.wine_name, R.id.producer_name})
    protected void onWineDetailsClick() {
        if (mActionsHandler != null) {
            mActionsHandler.launchWineProfile(mPendingCapture);
        }
    }

    @OnClick(R.id.add_rating_textview)
    protected void onAddRatingClick() {
        if (mActionsHandler != null) {
            mActionsHandler.addRatingAndComment(mPendingCapture);
        }
    }

    @OnClick(R.id.remove_textview)
    protected void onRemoveClick() {
        if (mActionsHandler != null) {
            mActionsHandler.discardCapture(mPendingCapture);
        }
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public static interface ActionsHandler {

        public void launchWineProfile(PendingCapture capture);

        public void addRatingAndComment(PendingCapture capture);

        public void discardCapture(PendingCapture capture);

    }
}
