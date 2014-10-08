package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.controllers.WineScanController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.BaseEvent;
import com.delectable.mobile.events.scanwinelabel.AddedCaptureFromPendingCaptureEvent;
import com.delectable.mobile.events.scanwinelabel.CreatedPendingCaptureEvent;
import com.delectable.mobile.events.scanwinelabel.IdentifyLabelScanEvent;
import com.delectable.mobile.model.api.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.RatingSeekBar;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.tagpeople.fragment.TagPeopleFragment;
import com.delectable.mobile.util.InstagramUtil;
import com.delectable.mobile.util.TwitterUtil;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class WineCaptureSubmitFragment extends BaseFragment {

    private static final String TAG = WineCaptureSubmitFragment.class.getSimpleName();

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

    private static final String sArgsImageData = "sArgsImageData";

    private static final int REQUEST_TAG_FRIENDS = 8000;

    private static final int REQUEST_LOCATION = 9000;

    @InjectView(R.id.comment_edit_text)
    protected EditText mCommentEditText;

    @InjectView(R.id.rate_seek_bar)
    protected RatingSeekBar mRatingSeekBar;

    @InjectView(R.id.drinking_with_who)
    protected TextView mDrinkingWithWhoButton;

    @InjectView(R.id.drinking_where)
    protected TextView mDrinkingWhereButton;

    @InjectView(R.id.share_facebook)
    protected Switch mShareFacebookButton;

    @InjectView(R.id.share_twitter)
    protected Switch mShareTwitterButton;

    @InjectView(R.id.share_instagram)
    protected Switch mShareInstagramButton;

    @InjectView(R.id.make_private)
    protected Switch mMakePrivateButton;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    @Inject
    protected WineScanController mWineScanController;

    private Account mUserAccount;

    private Bitmap mCapturedImageBitmap;

    private byte[] mRawImageData;

    private View mView;

    private Button mPostButton;

    private int mCurrentRating = -1;

    private ArrayList<TaggeeContact> mTaggeeContacts;

    private String mLocationName;

    private String mFoursquareId;

    private AddCaptureFromPendingCaptureRequest mCaptureRequest;

    private LabelScan mLabelScanResult;

    private boolean mIsPostingCapture;

    public WineCaptureSubmitFragment() {
    }

    public static WineCaptureSubmitFragment newInstance(Bitmap imageData) {
        WineCaptureSubmitFragment fragment = new WineCaptureSubmitFragment();
        Bundle args = new Bundle();
        args.putParcelable(sArgsImageData, imageData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        Bundle args = getArguments();
        if (args != null) {
            mCapturedImageBitmap = args.getParcelable(sArgsImageData);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mCapturedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            mRawImageData = byteArrayOutputStream.toByteArray();
        }
        mUserAccount = UserInfo.getAccountPrivate(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_submit, container, false);

        ButterKnife.inject(this, mView);

        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.ab_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        setupPostButtonToActionBar();

        setupButtonListeners();
        setupRatingSeekBar();

        // OnCreate gets called after onActivityResult, so we should update the UI accordingly
        updateLocationUI();
        updateWithFriendsUI();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // If the user was posting the capture, and left the app, we'll goto User Profile.
        // Right now there's no way to check if user has created a new capture yet or not.
        // This is a quick and dirty hack
        if (mIsPostingCapture) {
            launchCurrentUserProfile();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().show();
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

    private void setupPostButtonToActionBar() {
        RelativeLayout customView = (RelativeLayout) getActivity().getActionBar().getCustomView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPostButton = new Button(getActivity());
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        customView.addView(mPostButton, params);

        mPostButton.setText(R.string.capture_submit_post);
        // TODO: TouchStates for button
        mPostButton.setTextColor(Color.WHITE);
        mPostButton.setBackgroundColor(getResources().getColor(R.color.d_blue));
    }

    private void setupButtonListeners() {
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentEditText.clearFocus();
                hideKeyboard();
                postCapture();
            }
        });
    }

    private void setupRatingSeekBar() {
        mRatingSeekBar.setMax(CaptureDetails.MAX_RATING_VALUE);

        mRatingSeekBar.setProgress(CaptureDetails.MAX_RATING_VALUE / 2);
        mRatingSeekBar.setOnRatingChangeListener(new RatingSeekBar.OnRatingsChangeListener() {
            @Override
            public void onRatingsChanged(int rating) {
                mCurrentRating = rating;
            }
        });
    }

    private void updateCaptureRequestWithFormData() {
        if (mCaptureRequest == null) {
            return;
        }

        String comment = mCommentEditText.getText().toString();
        // TODO: New API doesn't have note / comment field?
        if (comment.length() > 0) {
            mCaptureRequest.setNote(comment);
        }

        if (!mMakePrivateButton.isChecked()) {
            mCaptureRequest.setPrivate(true);
        } else {
            mCaptureRequest.setShareFb(mShareFacebookButton.isChecked());
            mCaptureRequest.setShareTw(mShareTwitterButton.isChecked());
            if (mShareTwitterButton.isChecked()) {
                mCaptureRequest.setUserTw(comment);
            }
        }

        mCaptureRequest.setRating(mCurrentRating);

        if (mTaggeeContacts != null && mTaggeeContacts.size() > 0) {
            mCaptureRequest.setTaggees(mTaggeeContacts);
        }
        if (mFoursquareId != null) {
            mCaptureRequest.setFoursquareLocationId(mFoursquareId);
        }

        // TODO: Add Label Scan ID ?
        // TODO: Add Coordinates ?
    }

    private void postCapture() {
        if (mIsPostingCapture) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        mIsPostingCapture = true;
        mWineScanController.scanLabelInstantly(mRawImageData);
    }

    public void onEventMainThread(IdentifyLabelScanEvent event) {
        if (event.isSuccessful()) {
            mLabelScanResult = event.getLabelScan();
            mWineScanController.createPendingCapture(mRawImageData, mLabelScanResult.getId());
        } else {
            mIsPostingCapture = false;
            mProgressBar.setVisibility(View.GONE);
            handleEventErrorMessage(event);
        }
    }

    public void onEventMainThread(CreatedPendingCaptureEvent event) {
        if (event.isSuccessful()) {
            mCaptureRequest = new AddCaptureFromPendingCaptureRequest(
                    event.getPendingCapture().getId());
            updateCaptureRequestWithFormData();
            Log.i(TAG, "Adding Request: " + mCaptureRequest);
            mWineScanController.addCaptureFromPendingCapture(mCaptureRequest);
        } else {
            mIsPostingCapture = false;
            mProgressBar.setVisibility(View.GONE);
            handleEventErrorMessage(event);
        }
    }

    public void onEventMainThread(AddedCaptureFromPendingCaptureEvent event) {
        if (event.isSuccessful()) {
            launchCurrentUserProfile();
            if (mShareTwitterButton.isChecked()) {
                String tweet = event.getCaptureDetails().getTweet();
                String shortUrl = event.getCaptureDetails().getShortShareUrl();
                TwitterUtil.tweet(tweet + " " + shortUrl, TwitterCallback);
            }
            if (mShareInstagramButton.isChecked()) {
                InstagramUtil.shareBitmapInInstagram(getActivity(), mCapturedImageBitmap,
                        mCommentEditText.getText().toString());
            }
        } else {
            handleEventErrorMessage(event);
        }
        mIsPostingCapture = false;
        mProgressBar.setVisibility(View.GONE);
    }

    private void handleEventErrorMessage(BaseEvent event) {
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(R.string.error_capture_wine_no_network);
        } else {
            showToastError(event.getErrorMessage());
        }
    }

    private void launchCurrentUserProfile() {
        getActivity().finish();
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, mUserAccount.getId());
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
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

    @OnClick(R.id.progress_bar)
    protected void progressClicked() {
        //no-op -> prevent views below it from being selected
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
            mMakePrivateButton.setChecked(true);
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
            mMakePrivateButton.setChecked(true);
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
            mMakePrivateButton.setChecked(true);
        }
    }

    @OnCheckedChanged(R.id.make_private)
    protected void makeCaputrePrivate(CompoundButton view, boolean isChecked) {
        // TODO: Verify, this was taken from when Post to Delectable was "make private"
        // Unselect everything if Post to Delectable is not selected
        if (!isChecked) {
            mShareFacebookButton.setChecked(false);
            mShareTwitterButton.setChecked(false);
            mShareInstagramButton.setChecked(false);
        }
    }
}

