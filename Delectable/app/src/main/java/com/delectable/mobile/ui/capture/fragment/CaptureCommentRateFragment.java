package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.controllers.HashtagController;
import com.delectable.mobile.api.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.api.events.hashtags.SearchHashtagsEvent;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.ChipsMultiAutoCompleteTextView;
import com.delectable.mobile.ui.common.widget.NumericRatingSeekBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CaptureCommentRateFragment extends BaseFragment implements
        ChipsMultiAutoCompleteTextView.ActionsHandler {

    private static final String TAG = CaptureCommentRateFragment.class.getSimpleName();

    public static final String DATA_COMMENT = "DATA_COMMENT";

    public static final String DATA_COMMENT_ATTRIBUTES = "DATA_COMMENT_ATTRIBUTES";

    public static final String DATA_RATING = "DATA_RATING";

    public static final int HASHTAG_SEARCH_LIMIT = 15;

    public static final int MENTION_SEARCH_LIMIT = 15;

    @Inject
    public HashtagController mHashtagController;

    @Inject
    public AccountController mAccountController;

    private TextWatcher textValidationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Log.d(TAG, "Text Change Count: " + count);
            togglePostButton();
        }

        @Override
        public void afterTextChanged(Editable s) {
            togglePostButton();
        }
    };

    private static final String PARAMS_COMMENT = "PARAMS_COMMENT";

    private static final String PARAMS_COMMENT_ATTRIBUTES = "PARAMS_COMMENT_ATTRIBUTES";

    private static final String PARAMS_RATING = "PARAMS_RATING";

    private static final String PARAMS_IS_RATING = "PARAMS_IS_RATING";

    @InjectView(R.id.toolbar_title)
    protected TextView mTitle;

    @InjectView(R.id.post_button)
    protected View mPostButton;

    @InjectView(R.id.comment_edit_text)
    protected ChipsMultiAutoCompleteTextView mCommentEditText;

    @InjectView(R.id.numeric_rate_seek_bar)
    protected NumericRatingSeekBar mNumericRatingSeekBar;

    private View mView;

    private String mComment;

    private ArrayList<CaptureCommentAttributes> mCommentAttributes;

    private int mRating;

    private boolean mIsRating;

    public static CaptureCommentRateFragment newInstance(String comment,
            ArrayList<CaptureCommentAttributes> attributes, int rating,
            boolean isRating) {
        CaptureCommentRateFragment fragment = new CaptureCommentRateFragment();
        Bundle args = new Bundle();
        args.putString(PARAMS_COMMENT, comment);
        args.putParcelableArrayList(PARAMS_COMMENT_ATTRIBUTES, attributes);
        args.putInt(PARAMS_RATING, rating);
        args.putBoolean(PARAMS_IS_RATING, isRating);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        Bundle args = getArguments();
        if (args != null) {
            mComment = args.getString(PARAMS_COMMENT);
            mCommentAttributes = args.getParcelableArrayList(PARAMS_COMMENT_ATTRIBUTES);
            mRating = args.getInt(PARAMS_RATING);
            mIsRating = args.getBoolean(PARAMS_IS_RATING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_capture_rating_submit, container, false);
        ButterKnife.inject(this, mView);

        mCommentEditText.setActionsHandler(this);
        mCommentEditText.setRawInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        updateRatingUI();
        updateCommentTextUI();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enableBackButton(true);
        getActionBar().setTitle(null);
//        ViewCompat.setElevation(getActionBarToolbar(), Animate.ELEVATION);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateRatingUI() {
        // Toggle display of rating if we're rating
        if (mIsRating) {
            mNumericRatingSeekBar.setVisibility(View.VISIBLE);
            mTitle.setText(getActivity().getString(R.string.capture_submit_title));
        } else {
            mNumericRatingSeekBar.setVisibility(View.GONE);
            mTitle.setText(getActivity().getString(R.string.dialog_comment_title));
        }
        // Set Default Rating
        if (mRating == -1) {
            mNumericRatingSeekBar.getRatingSeekBar().setProgress(
                    CaptureDetails.MAX_RATING_VALUE / 2);
        } else {
            mNumericRatingSeekBar.getRatingSeekBar().setShowColors(true);
            mNumericRatingSeekBar.getRatingSeekBar().setProgress(mRating);
        }

        mNumericRatingSeekBar
                .setOnRatingChangeListener(new NumericRatingSeekBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingsChanged(int rating) {
                        if (rating > -1) {
                            mRating = rating;
                            mPostButton.setEnabled(true);
                        }
                    }
                });

    }

    private void updateCommentTextUI() {
        mCommentEditText.addTextChangedListener(textValidationWatcher);

        // Focus / Show Keyboard
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mCommentEditText.requestFocus();

        // Comment if we're editing comment
        mCommentEditText.setText(mComment);
    }

    private void togglePostButton() {
        mPostButton.setEnabled(!emptyFieldExists());
    }

    @OnClick(R.id.post_button)
    public void postData() {
        Intent data = new Intent();
        // TODO parse edit text, get chips, replace text and generate comment attributes
        mCommentAttributes = new ArrayList<>();
        ArrayList<ChipsMultiAutoCompleteTextView.ChipSpan> spans = mCommentEditText.getSpans();
        if (!spans.isEmpty()) {
            for (ChipsMultiAutoCompleteTextView.ChipSpan span : spans) {
                if (span instanceof ChipsMultiAutoCompleteTextView.HashtagChipSpan) {
                    ChipsMultiAutoCompleteTextView.HashtagChipSpan s
                            = (ChipsMultiAutoCompleteTextView.HashtagChipSpan) span;
                    mCommentAttributes.add(new CaptureCommentAttributes(
                            s.listKey,
                            CaptureCommentAttributes.TYPE_HASHTAG,
                            mCommentEditText.getText().getSpanStart(s),
                            mCommentEditText.getText().getSpanEnd(s)));
                } else if (span instanceof ChipsMultiAutoCompleteTextView.MentionChipSpan) {
                    ChipsMultiAutoCompleteTextView.MentionChipSpan s
                            = (ChipsMultiAutoCompleteTextView.MentionChipSpan) span;
                    mCommentAttributes.add(new CaptureCommentAttributes(
                            s.accountId,
                            CaptureCommentAttributes.TYPE_MENTION,
                            mCommentEditText.getText().getSpanStart(s),
                            mCommentEditText.getText().getSpanEnd(s)));
                }
            }
        }
        data.putExtra(DATA_COMMENT, mCommentEditText.getText().toString());
        data.putExtra(DATA_COMMENT_ATTRIBUTES, mCommentAttributes);
        data.putExtra(DATA_RATING, mRating);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    private boolean emptyFieldExists() {
//        Log.d(TAG, "Edit Text: " + mCommentEditText.getText());
        if (mCommentEditText.getText().toString().trim().equals("")) {
            return true;
        }
        if (mNumericRatingSeekBar.getRatingSeekBar().getProgress() == -1) {
            return true;
        }
        return false;
    }

    @Override
    public void queryHashtag(String query) {
        mHashtagController.searchHashtags(query, 0, HASHTAG_SEARCH_LIMIT);
    }

    @Override
    public void queryMention(String query) {
        // FIXME pass capture id
        mAccountController.searchAccountsContextually(query, 0, MENTION_SEARCH_LIMIT, null);
    }

    public void onEventMainThread(SearchHashtagsEvent event) {
        if (event.isSuccessful() && event.getResult() != null) {
            mCommentEditText.updateHashtagResults(event.getResult().getHits());
        }
    }

    public void onEventMainThread(SearchAccountsEvent event) {
        if (event.isSuccessful() && event.getResult() != null) {
            mCommentEditText.updateMentionResults(event.getResult().getHits());
        }
    }
}
