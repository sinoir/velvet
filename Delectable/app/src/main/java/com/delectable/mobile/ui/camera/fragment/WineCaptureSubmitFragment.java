package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.controllers.S3ImageUploadController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.CaptureRequest;
import com.delectable.mobile.api.requests.ProvisionCaptureRequest;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.RatingSeekBar;

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
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class WineCaptureSubmitFragment extends BaseFragment {

    private static final String TAG = WineCaptureSubmitFragment.class.getSimpleName();

    private static final String sArgsImageData = "sArgsImageData";

    @InjectView(R.id.comment_edit_text)
    protected EditText mCommentEditText;

    @InjectView(R.id.rate_seek_bar)
    protected RatingSeekBar mRatingSeekBar;

    @InjectView(R.id.drinking_with_who)
    protected View mDrinkingWithWhoButton;

    @InjectView(R.id.drinking_where)
    protected View mDrinkingWhereButton;

    @InjectView(R.id.share_facebook)
    protected Switch mShareFacebookButton;

    @InjectView(R.id.share_twitter)
    protected Switch mShareTwitterButton;

    @InjectView(R.id.share_instagram)
    protected Switch mShareInstagramButton;

    @InjectView(R.id.make_private)
    protected Switch mMakePrivateButton;

    private Bitmap mCapturedImageBitmap;

    private View mView;

    private Button mPostButton;

    private int mCurrentRating = -1;

    private BaseNetworkController mNetworkController;

    private S3ImageUploadController mImageUploadController;

    private ProvisionCaptureRequest mProvisionRequest;

    private ProvisionCapture mProvisionCapture;

    private CaptureRequest mCaptureRequest;

    // If provision capture hasn't been recieved yet before the user clicks post, set this to true
    private boolean mIsWaitingOnProvisionCapture = false;

    private boolean mIsWaitingOnImageUplaodToFinish = true;

    private boolean mIsWaitingOnDataUplaodToFinish = true;

    private CaptureDetails mCaptureResult;

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
        Bundle args = getArguments();
        if (args != null) {
            mCapturedImageBitmap = args.getParcelable(sArgsImageData);
        }
        mNetworkController = new BaseNetworkController(getActivity());

        loadCaptureProvision();
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

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // If the posting finished while the app was in the background, should launch details
        shouldLaunchCaptureDetails();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().show();
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

    private void loadCaptureProvision() {
        mProvisionRequest = new ProvisionCaptureRequest();
        mNetworkController
                .performRequest(mProvisionRequest, new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        // TODO: Synchronize
                        mProvisionCapture = (ProvisionCapture) result;
                        sendCapturedImage();
                        Log.d(TAG, "Capture Provision: " + mProvisionCapture);
                        if (mIsWaitingOnProvisionCapture) {
                            mIsWaitingOnProvisionCapture = false;
                            sendCaptureData();
                        }
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        requestFailed(error);
                    }
                });
    }

    private void sendCapturedImage() {
        if (mProvisionCapture != null) {
            mImageUploadController = new S3ImageUploadController(getActivity(), mProvisionCapture);
            mImageUploadController.uploadImage(mCapturedImageBitmap,
                    new BaseNetworkController.SimpleRequestCallback() {
                        @Override
                        public void onSucess() {
                            Log.d(TAG, "Image Upload Done!");
                            mIsWaitingOnImageUplaodToFinish = false;
                            if (!mIsWaitingOnDataUplaodToFinish) {
                                // Finish activity
                            }
                        }

                        @Override
                        public void onFailed(RequestError error) {
                            requestFailed(error);
                        }
                    }
            );
        }
    }

    private void sendCaptureData() {
        if (mProvisionCapture != null) {
            mCaptureRequest = new CaptureRequest(mProvisionCapture);
            updateCaptureRequestWithFormData();
            mNetworkController
                    .performRequest(mCaptureRequest, new BaseNetworkController.RequestCallback() {
                        @Override
                        public void onSuccess(BaseResponse result) {
                            mCaptureResult = (CaptureDetails) result;
                            mIsWaitingOnDataUplaodToFinish = false;
                            shouldLaunchCaptureDetails();
                        }

                        @Override
                        public void onFailed(RequestError error) {
                            requestFailed(error);
                        }
                    });
        }
    }

    private void shouldLaunchCaptureDetails() {
        if (!mIsWaitingOnImageUplaodToFinish && mCaptureResult != null) {
            // TODO: Finish, goto capture info screen
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private void requestFailed(RequestError error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCaptureRequestWithFormData() {
        if (mCaptureRequest == null) {
            return;
        }

        String comment = mCommentEditText.getText().toString();
        if (comment.length() > 0) {
            mCaptureRequest.setNote(comment);
        }

        if (!mMakePrivateButton.isChecked()) {
            mCaptureRequest.setPrivate(true);
        } else {
            mCaptureRequest.setShareFb(mShareFacebookButton.isSelected());
            mCaptureRequest.setShareTw(mShareTwitterButton.isSelected());
        }

        mCaptureRequest.setRating(mCurrentRating);

        // TODO: Add Label Scan ID ?
        // TODO: Add Foursquare ID
        // TODO: Add Coordinates
        // TODO: Add Taggees
    }

    private void postCapture() {
        // TODO: Display some Loading indicator / Ability to cancel?
        // TODO: Or have all this in a service separate from the main app, and use a bus to connect the data
        sendCaptureData();
    }

    @OnClick(R.id.drinking_with_who)
    protected void selectDrinkingPartners() {
        // TODO: Drinking Partners Screen
    }

    @OnClick(R.id.drinking_where)
    protected void selectDrinkingLocation() {
        // TODO: Location Listing
    }

    @OnCheckedChanged(R.id.share_facebook)
    protected void shareCaptureOnFacebook(CompoundButton view, boolean isChecked) {
        // TODO: Facebook Connect
        // TODO: What happens to Private when now it's "Post to Delectable?"
        if (isChecked) {
            mMakePrivateButton.setChecked(true);
        }
    }

    @OnCheckedChanged(R.id.share_twitter)
    protected void shareCaptureOnTwitter(CompoundButton view, boolean isChecked) {
        // TODO: Login With Twitter
        mShareTwitterButton.setSelected(!mShareTwitterButton.isSelected());
        if (isChecked) {
            mMakePrivateButton.setChecked(true);
        }
    }


    @OnCheckedChanged(R.id.share_instagram)
    protected void shareCaptureOnInstagram(CompoundButton view, boolean isChecked) {
        // TODO: Login with Instagram
        mShareInstagramButton.setSelected(!mShareInstagramButton.isSelected());
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

