package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.controllers.HashtagController;
import com.delectable.mobile.api.controllers.WineScanController;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.api.events.hashtags.SearchHashtagsEvent;
import com.delectable.mobile.api.events.scanwinelabel.AddedCaptureFromPendingCaptureEvent;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.fragment.FoursquareVenueSelectionFragment;
import com.delectable.mobile.ui.common.widget.ChipsMultiAutoCompleteTextView;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.NumericRatingSeekBar;
import com.delectable.mobile.ui.common.widget.RatingSeekBar;
import com.delectable.mobile.ui.tagpeople.fragment.TagPeopleFragment;
import com.delectable.mobile.util.FacebookEventUtil;
import com.delectable.mobile.util.InstagramUtil;
import com.delectable.mobile.util.TwitterUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class RateCaptureFragment extends BaseFragment
        implements ChipsMultiAutoCompleteTextView.ActionsHandler {

    private static final String TAG = RateCaptureFragment.class.getSimpleName();

    private static final String PENDING_CAPTURE = "PENDING_CAPTURE";

    private static final int REQUEST_TAG_FRIENDS = 1;

    private static final int REQUEST_LOCATION = 2;

    public static final int HASHTAG_SEARCH_LIMIT = 15;

    public static final int MENTION_SEARCH_LIMIT = 20;

    /**
     * Time how long queries are being held up before the last one in is executed. Yields at most
     * one executed query every QUERY_DELAY_MS.
     */
    public static final int QUERY_DELAY_MS = 300;

    private Callback<Tweet> TwitterCallback = new Callback<Tweet>() {
        @Override
        public void success(Result<Tweet> tweetResult) {
            Log.d(TAG, "tweet success!");
        }

        @Override
        public void failure(TwitterException e) {
            Log.d(TAG, "tweet fail");
            Log.d(TAG, "TwitterException", e);
            showToastError("Tweet failed: " + e.getMessage());
        }
    };

    @InjectView(R.id.comment_edit_text)
    protected ChipsMultiAutoCompleteTextView mCommentEditText;

    @InjectView(R.id.score_rate_seek_bar)
    protected NumericRatingSeekBar mNumericRatingSeekBar;

    @InjectView(R.id.drinking_with_who)
    protected TextView mDrinkingWithWhoButton;

    @InjectView(R.id.drinking_where)
    protected TextView mDrinkingWhereButton;

    @InjectView(R.id.share_facebook)
    protected SwitchCompat mShareFacebookButton;

    @InjectView(R.id.share_twitter)
    protected SwitchCompat mShareTwitterButton;

    @InjectView(R.id.share_instagram)
    protected SwitchCompat mShareInstagramButton;

    @InjectView(R.id.make_private)
    protected SwitchCompat mMakePrivateButton;

    @Inject
    protected WineScanController mWineScanController;

    @Inject
    public HashtagController mHashtagController;

    @Inject
    public AccountController mAccountController;

    protected View mActionView;

    protected FontTextView mRateButton;

    private Account mUserAccount;

    private String mPendingCaptureId;

    private Bitmap mCaptureImage;

    private int mCurrentRating = -1;

    private ArrayList<TaggeeContact> mTaggeeContacts;

    private String mLocationName;

    private String mFoursquareId;

    private Handler mQueryHandler = new Handler();

    private Runnable mQueryTask;

    public RateCaptureFragment() {
    }

    public static RateCaptureFragment newInstance(String pendingCaptureId) {
        RateCaptureFragment fragment = new RateCaptureFragment();
        Bundle args = new Bundle();
        args.putString(PENDING_CAPTURE, pendingCaptureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            mPendingCaptureId = args.getString(PENDING_CAPTURE);
        }

        mUserAccount = UserInfo.getAccountPrivate(getActivity());

        //event for handling incoming params
        RateCaptureInitEvent rateCaptureEvent = mEventBus
                .getStickyEvent(RateCaptureInitEvent.class);
        if (rateCaptureEvent == null) {
            return;
        }

        mEventBus.removeStickyEvent(rateCaptureEvent);

        //bitmap exists, we're good to go
        if (rateCaptureEvent.getCaptureImage() != null) {
            mCaptureImage = rateCaptureEvent.getCaptureImage();
            return;
        }

        //no bitmap,  have to use pendingCapture object to download bitmap in preparation for instagram share
        PendingCapture pendingCapture = rateCaptureEvent.getPendingCapture();
        Picasso.with(getActivity()).load(pendingCapture.getPhoto().getMediumPlus()).into(target);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionBarTitle(getString(R.string.capture_submit_title));
        setActionBarSubtitle((String) null);
        enableBackButton(true);
        getActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wine_capture_rate, container, false);
        mActionView = inflater.inflate(R.layout.action_menu_button, null, false);
        mRateButton = (FontTextView) mActionView.findViewById(R.id.action_button);

        ButterKnife.inject(this, view);

        mCommentEditText.setActionsHandler(this);
        mCommentEditText.setRawInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
//                | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setupRatingSeekBar();

        // OnCreate gets called after onActivityResult, so we should update the UI accordingly
        updateLocationUI();
        updateWithFriendsUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.capture_menu, menu);
        MenuItem postItem = menu.findItem(R.id.post);
        mRateButton.setText(getString(R.string.capture_rate));
        mRateButton.setEnabled(true);
        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommentEditText.clearFocus();
                hideKeyboard();
                rateCapture();
            }
        });
        MenuItemCompat.setActionView(postItem, mActionView);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAG_FRIENDS:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    Bundle args = data.getExtras();
                    mTaggeeContacts = args
                            .getParcelableArrayList(TagPeopleFragment.RESULT_SELECTED_CONTACTS);
                } else {
                    mTaggeeContacts = null;
                }
                break;
            case REQUEST_LOCATION:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    Bundle args = data.getExtras();
                    mLocationName = args
                            .getString(FoursquareVenueSelectionFragment.RESULT_FOURSQUARE_NAME);
                    mFoursquareId = args
                            .getString(FoursquareVenueSelectionFragment.RESULT_FOURSQUARE_ID);
                } else {
                    // User Canceled, reset info
                    mLocationName = null;
                    mFoursquareId = null;
                }
                break;
        }
    }

    private Target target = new Target() {
        final String myTag = TAG + ".PicassoBitmapDownload";

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(myTag, "onBitmapLoaded");
            mCaptureImage = bitmap;
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(myTag, "onBitmapFailed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(myTag, "onPrepareLoad");

        }
    };

    private void updateWithFriendsUI() {
        if (mTaggeeContacts != null && mTaggeeContacts.size() > 0) {
            mDrinkingWithWhoButton.setText(getResources()
                    .getQuantityString(R.plurals.with_friends, mTaggeeContacts.size(),
                            mTaggeeContacts.size()));
        } else {
            mDrinkingWithWhoButton.setText(R.string.capture_submit_drinking_with_who_text);
        }
    }

    private void updateLocationUI() {
        if (mLocationName != null) {
            mDrinkingWhereButton.setText(getString(R.string.cap_feed_at_location, mLocationName));
        } else {
            mDrinkingWhereButton.setText(R.string.capture_submit_drinking_where_text);
        }
    }

    private void setupRatingSeekBar() {
        //sets seek bar thumb to start in the middle of the
        mNumericRatingSeekBar.getRatingSeekBar().setProgress(RatingSeekBar.INCREMENTS / 2);
        mNumericRatingSeekBar
                .setOnRatingChangeListener(new NumericRatingSeekBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingsChanged(int rating) {
                        mCurrentRating = rating;
                    }

                });
    }

    /**
     * Called when user clicks the rate button in the toolbar
     */
    public void rateCapture() {
        mRateButton.setEnabled(false);
        AddCaptureFromPendingCaptureRequest captureRequest
                = new AddCaptureFromPendingCaptureRequest(mPendingCaptureId);
        updateCaptureRequestWithFormData(captureRequest);
        mWineScanController.addCaptureFromPendingCapture(captureRequest);
    }

    private void updateCaptureRequestWithFormData(AddCaptureFromPendingCaptureRequest request) {
        if (request == null) {
            return;
        }

        String comment = null;
        if (mCommentEditText.length() > 0) {
            request.setCommentAttributes(getCommentAttributesFromAutoCompleteTextView(
                    mCommentEditText));
            comment = mCommentEditText.getText().toString();
            request.setNote(comment);
        }

        if (mMakePrivateButton.isChecked()) {
            request.setPrivate(true);
        } else {
            request.setPrivate(false);
            request.setShareFb(mShareFacebookButton.isChecked());
            request.setShareTw(mShareTwitterButton.isChecked());
            if (mShareTwitterButton.isChecked()) {
                request.setUserTw(comment);
            }
        }

        request.setRating(mCurrentRating);

        if (mTaggeeContacts != null && mTaggeeContacts.size() > 0) {
            request.setTaggees(mTaggeeContacts);
        }
        if (mFoursquareId != null) {
            request.setFoursquareLocationId(mFoursquareId);
        }

        // TODO: Add Coordinates ?
    }

    /**
     * Replaces the text of the EditText (@see EditText#getText()) with the the tags and mentions
     * aquired from the spans. After calling this, autoCompleteTextView.getText() will return the
     * human readable comment text.
     *
     * @return the list of CaptureCommentAttributes parsed from the autoCompleteTextView
     */
    public static ArrayList<CaptureCommentAttributes> getCommentAttributesFromAutoCompleteTextView(
            ChipsMultiAutoCompleteTextView autoCompleteTextView) {
        ArrayList<CaptureCommentAttributes> commentAttributes = new ArrayList<>();
        Editable comment = autoCompleteTextView.getText();
        // TODO scan comment for #hashtags and account for them (non-auto-completed tags that is)
        ArrayList<ChipsMultiAutoCompleteTextView.ChipSpan> spans = autoCompleteTextView.getSpans();
        if (!spans.isEmpty()) {
            for (ChipsMultiAutoCompleteTextView.ChipSpan span : spans) {
                int spanStart = comment.getSpanStart(span);
                int spanEnd = comment.getSpanEnd(span);
                // replace single character in comment text with replacement span text
                comment.replace(spanStart, spanEnd, span.getReplacedText());
//                Log.d(TAG, "postData: spanStart=" + spanStart + ", spanEnd=" + spanEnd + ", replacedText='" + span.getReplacedText() + "', spanId=" + span.getId() + "\ncomment='" + comment.toString() + "'\n");
                // generate comment attributes
                commentAttributes.add(new CaptureCommentAttributes(
                        span.getId(),
                        span.getType(),
                        spanStart,
                        span.getReplacedText().length()));
            }
        }
        return commentAttributes.isEmpty() ? null : commentAttributes;
    }

    public void onEventMainThread(AddedCaptureFromPendingCaptureEvent event) {
        if (!event.isSuccessful()) {
            handleEventErrorMessage(event);
            return;
        }

        FacebookEventUtil.logRateEvent(getActivity(), event.getCaptureDetails());

        if (mShareTwitterButton.isChecked()) {
            String tweet = event.getCaptureDetails().getTweet();
            String shortUrl = event.getCaptureDetails().getShortShareUrl();
            TwitterUtil.tweet(tweet + " " + shortUrl, TwitterCallback);
        }

        //TODO can come to this screen from wineProfileInstant or from userProfile, if coming from WPInstant, need to finish this activity before launching userProfile
        //perhaps even better: open my wines instead
        launchUserProfile(true);

        //TODO when rating from user captures list, captureImage will be null, need to download
        if (mShareInstagramButton.isChecked() && mCaptureImage != null) {
            InstagramUtil.shareBitmapInInstagram(getActivity(), mCaptureImage,
                    mCommentEditText.getText().toString());
        }
    }

    private void handleEventErrorMessage(BaseEvent event) {
        mRateButton.setEnabled(true);
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(R.string.error_capture_wine_no_network);
        } else {
            showToastError(event.getErrorMessage());
        }
    }

    @OnClick(R.id.drinking_with_who)
    protected void selectDrinkingPartners() {
        TagPeopleFragment fragment = TagPeopleFragment
                .newInstance(this, REQUEST_TAG_FRIENDS, mTaggeeContacts);
        launchNextFragment(fragment);
    }

    @OnClick(R.id.drinking_where)
    protected void selectDrinkingLocation() {
        FoursquareVenueSelectionFragment fragment = FoursquareVenueSelectionFragment.newInstance(
                this, REQUEST_LOCATION);
        launchNextFragment(fragment);
    }

    @OnCheckedChanged(R.id.share_facebook)
    protected void shareCaptureOnFacebook(CompoundButton view, boolean isChecked) {
        // TODO: Replace with real connect to FB and stuff?
        if (mUserAccount.getFbId() == null) {
            showToastError(R.string.error_connect_facebook);
            view.setChecked(false);
            return;
        }
        if (isChecked) {
            mMakePrivateButton.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.share_twitter)
    protected void shareCaptureOnTwitter(CompoundButton view, boolean isChecked) {
        // TODO: Check if user connected Twiter:
        if (!TwitterUtil.isLoggedIn()) {
            showToastError(R.string.error_connect_twitter);
            view.setChecked(false);
            return;
        }
        if (isChecked) {
            mMakePrivateButton.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.share_instagram)
    protected void shareCaptureOnInstagram(CompoundButton view, boolean isChecked) {
        if (!InstagramUtil.isInstagramAvailable()) {
            showToastError(R.string.error_no_instagram);
            view.setChecked(false);
            return;
        }
        if (isChecked) {
            mMakePrivateButton.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.make_private)
    protected void makeCapturePrivate(CompoundButton view, boolean isChecked) {
        // Unselect everything if make private is true
        if (isChecked) {
            mShareFacebookButton.setChecked(false);
            mShareTwitterButton.setChecked(false);
            mShareInstagramButton.setChecked(false);
        }
    }

    @Override
    public void queryHashtag(final String query) {
        queryDelayed(new Runnable() {
            @Override
            public void run() {
                mHashtagController.searchHashtags(query, 0, HASHTAG_SEARCH_LIMIT);
            }
        });
    }

    @Override
    public void queryMention(final String query) {
        queryDelayed(new Runnable() {
            @Override
            public void run() {
                // FIXME pass capture id
                mAccountController.searchAccountsContextually(query, 0, MENTION_SEARCH_LIMIT, null);
            }
        });
    }

    private void queryDelayed(Runnable runnable) {
        // clear queued up queries and submit a new one
        mQueryHandler.removeCallbacks(mQueryTask);
        mQueryTask = runnable;
        mQueryHandler.postDelayed(runnable, QUERY_DELAY_MS);
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

    /**
     * This fragment can be initialized with either a pendingCapture or bitmap of the capture.
     */
    public static class RateCaptureInitEvent {

        private PendingCapture mPendingCapture;

        private Bitmap mCaptureImage;

        /**
         * When coming from UserProfileFragment, this fragment gets initialized with a
         * pendingCapture.
         */
        public RateCaptureInitEvent(PendingCapture pendingCapture) {
            mPendingCapture = pendingCapture;
        }

        /**
         * When coming from WineProfileInstantFragment, this fragment gets initialized with a
         * Bitmap.
         */
        public RateCaptureInitEvent(Bitmap bitmap) {
            mCaptureImage = bitmap;
        }

        public PendingCapture getPendingCapture() {
            return mPendingCapture;
        }

        public Bitmap getCaptureImage() {
            return mCaptureImage;
        }
    }
}

