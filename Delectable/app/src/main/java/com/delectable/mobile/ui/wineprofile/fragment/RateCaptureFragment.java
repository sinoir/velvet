package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.WineScanController;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.events.scanwinelabel.AddedCaptureFromPendingCaptureEvent;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.fragment.FoursquareVenueSelectionFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.NumericRatingSeekBar;
import com.delectable.mobile.ui.common.widget.RatingSeekBar;
import com.delectable.mobile.ui.tagpeople.fragment.TagPeopleFragment;
import com.delectable.mobile.util.InstagramUtil;
import com.delectable.mobile.util.TwitterUtil;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class RateCaptureFragment extends BaseFragment {

    private static final String TAG = RateCaptureFragment.class.getSimpleName();

    private static final String PENDING_CAPTURE = "PENDING_CAPTURE";

    private static final int REQUEST_TAG_FRIENDS = 1;

    private static final int REQUEST_LOCATION = 2;

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
    protected EditText mCommentEditText;

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

    protected View mActionView;

    protected FontTextView mRateButton;

    private Account mUserAccount;

    private String mPendingCaptureId;

    private int mCurrentRating = -1;

    private ArrayList<TaggeeContact> mTaggeeContacts;

    private String mLocationName;

    private String mFoursquareId;

    private AddCaptureFromPendingCaptureRequest mCaptureRequest;


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

        View view = inflater.inflate(R.layout.fragment_wine_capture_submit, container, false);
        mActionView = inflater.inflate(R.layout.action_menu_button, null, false);
        mRateButton = (FontTextView) mActionView.findViewById(R.id.action_button);

        ButterKnife.inject(this, view);

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


    public void rateCapture() {
        mRateButton.setEnabled(false);
        mCaptureRequest = new AddCaptureFromPendingCaptureRequest(mPendingCaptureId);
        updateCaptureRequestWithFormData(mCaptureRequest);
        mWineScanController.addCaptureFromPendingCapture(mCaptureRequest);
    }

    private void updateCaptureRequestWithFormData(AddCaptureFromPendingCaptureRequest request) {
        if (request == null) {
            return;
        }

        String comment = mCommentEditText.getText().toString();
        // TODO: New API doesn't have note / comment field?
        if (comment.length() > 0) {
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

    public void onEventMainThread(AddedCaptureFromPendingCaptureEvent event) {
        if (!event.isSuccessful()) {
            handleEventErrorMessage(event);
            return;
        }

        if (mShareTwitterButton.isChecked()) {
            String tweet = event.getCaptureDetails().getTweet();
            String shortUrl = event.getCaptureDetails().getShortShareUrl();
            TwitterUtil.tweet(tweet + " " + shortUrl, TwitterCallback);
        }

        //TODO instagram stuff
//        if (mShareInstagramButton.isChecked()) {
//            InstagramUtil.shareBitmapInInstagram(getActivity(), mCapturedImageBitmap,
//                    mCommentEditText.getText().toString());
//        }

        launchUserProfile(true);
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
}

