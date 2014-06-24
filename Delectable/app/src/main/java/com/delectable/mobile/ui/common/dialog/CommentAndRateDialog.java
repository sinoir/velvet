package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.common.widget.RatingSeekBar;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class CommentAndRateDialog extends DialogWithCancelButton {

    private int mCurrentRating = -1;

    private EditText mCommentEditText;

    private View mContentView;

    private CommentAndRateDialogCallback mCallback;

    private RatingSeekBar mRatingsSeekBar;

    private View mRatingHint;

    public CommentAndRateDialog(Context context, CommentAndRateDialogCallback callback) {
        super(context);
        mCallback = callback;
        mContentView = getLayoutInflater().inflate(R.layout.dialog_comment_and_rate, null);
        setView(mContentView);
        setTitle(R.string.dialog_comment_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentEditText = (EditText) mContentView.findViewById(R.id.comment_edit_text);
        mRatingsSeekBar = (RatingSeekBar) mContentView.findViewById(R.id.rate_seek_bar);
        mRatingHint = mContentView.findViewById(R.id.rating_hint_text);
        setupEditText();
        setupRatingSeekBar();
    }

    private void setupEditText() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mCommentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND && mCallback != null) {
                    finishWritingCommentWithRating();
                }
                return false;
            }
        });
    }

    private void setupRatingSeekBar() {
        mRatingsSeekBar.setMax(CaptureDetails.MAX_RATING_VALUE);

        mRatingsSeekBar.setProgress(CaptureDetails.MAX_RATING_VALUE / 2);
        mRatingsSeekBar.setOnRatingChangeListener(new RatingSeekBar.OnRatingsChangeListener() {
            @Override
            public void onRatingsChanged(int rating) {
                mCurrentRating = rating;
                if (mRatingHint.getVisibility() != View.GONE) {
                    mRatingHint.setVisibility(View.GONE);
                }
            }
        });
    }

    private void finishWritingCommentWithRating() {
        String comment = mCommentEditText.getText().toString();
        if (mCallback == null || (mCurrentRating == -1 && comment.length() == 0)) {
            return;
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mCallback.onFinishWritingCommentAndRating(comment, mCurrentRating);
        dismiss();
    }

    public static interface CommentAndRateDialogCallback {

        public void onFinishWritingCommentAndRating(String comment, int rating);
    }
}
