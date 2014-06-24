package com.delectable.mobile.ui.capture.fragment;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WineCaptureSubmitFragment extends BaseFragment {

    private static final String TAG = "WineCaptureSubmitFragment";

    private static final String sArgsImageData = "sArgsImageData";

    private Bitmap mCapturedImageBitmap;

    private View mView;

    private Button mPostButton;

    private ImageView mPreviewImageView;

    private EditText mCommentEditText;

    private View mRatingBarHint;

    private RatingSeekBar mRatingSeekBar;

    private View mDrinkingWithWhoButton;

    private View mDrinkingWhereButton;

    private View mShareFacebookButton;

    private View mShareTwitterButton;

    private View mShareInstagramButton;

    private View mMakePrivateButton;

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

        mPreviewImageView = (ImageView) mView.findViewById(R.id.capture_image_preview);
        mCommentEditText = (EditText) mView.findViewById(R.id.comment_edit_text);
        mRatingBarHint = mView.findViewById(R.id.rating_hint_text);
        mRatingSeekBar = (RatingSeekBar) mView.findViewById(R.id.rate_seek_bar);
        mDrinkingWithWhoButton = mView.findViewById(R.id.drinking_with_who);
        mDrinkingWhereButton = mView.findViewById(R.id.drinking_where);
        mShareFacebookButton = mView.findViewById(R.id.share_facebook);
        mShareTwitterButton = mView.findViewById(R.id.share_twitter);
        mShareInstagramButton = mView.findViewById(R.id.share_instagram);
        mMakePrivateButton = mView.findViewById(R.id.make_private);

        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.ab_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        setupPostButtonToActionBar();

        setupPreviewImage();
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
        mPostButton.setBackgroundColor(getResources().getColor(R.color.d_link_blue));
    }


    private void setupPreviewImage() {
        if (mCapturedImageBitmap != null) {
            mPreviewImageView.setImageBitmap(mCapturedImageBitmap);
        }
    }

    private void setupButtonListeners() {
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCapture();
            }
        });

        mDrinkingWithWhoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDrinkingPartners();
            }
        });
        mDrinkingWhereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDrinkingLocation();
            }
        });
        mShareFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCaptureOnFacebook();
            }
        });
        mShareTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCaptureOnTwitter();
            }
        });
        mShareInstagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCaptureOnInstagram();
            }
        });
        mMakePrivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCaputrePrivate();
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
                if (mRatingBarHint.getVisibility() != View.INVISIBLE) {
                    mRatingBarHint.setVisibility(View.INVISIBLE);
                }
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

        if (mMakePrivateButton.isSelected()) {
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

    private void selectDrinkingPartners() {
        // TODO: Drinking Partners Screen
    }

    private void selectDrinkingLocation() {
        // TODO: Location Listing
    }

    private void shareCaptureOnFacebook() {
        // TODO: Facebook Connect
        mShareFacebookButton.setSelected(!mShareFacebookButton.isSelected());
        mMakePrivateButton.setSelected(false);
    }

    private void shareCaptureOnTwitter() {
        // TODO: Login With Twitter
        mShareTwitterButton.setSelected(!mShareTwitterButton.isSelected());
        mMakePrivateButton.setSelected(false);
    }

    private void shareCaptureOnInstagram() {
        // TODO: Login with Instagram
        mShareInstagramButton.setSelected(!mShareInstagramButton.isSelected());
        mMakePrivateButton.setSelected(false);
    }

    private void makeCaputrePrivate() {
        mShareFacebookButton.setSelected(false);
        mShareTwitterButton.setSelected(false);
        mShareInstagramButton.setSelected(false);
        mMakePrivateButton.setSelected(!mMakePrivateButton.isSelected());
    }
}

