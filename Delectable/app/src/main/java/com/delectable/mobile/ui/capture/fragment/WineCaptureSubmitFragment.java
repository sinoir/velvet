package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.RatingSeekBar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class WineCaptureSubmitFragment extends BaseFragment {

    private static final String sArgsImageData = "sArgsImageData";

    private Bitmap mCapturedImageBitmap;

    private View mView;

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

    private int mCurrentRating;

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
        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.ab_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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

        setupPreviewImage();
        setupButtonListeners();
        setupRatingSeekBar();

        return mView;
    }

    private void setupPreviewImage() {
        if (mCapturedImageBitmap != null) {
            mPreviewImageView.setImageBitmap(mCapturedImageBitmap);
        }
    }

    private void setupButtonListeners() {
        mRatingBarHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mRatingSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        mCurrentRating = CaptureDetails.MAX_RATING_VALUE / 2;

        mRatingSeekBar.setProgress(mCurrentRating);
        mRatingSeekBar.setOnRatingChangeListener(new RatingSeekBar.OnRatingsChangeListener() {
            @Override
            public void onRatingsChanged(int rating) {
                mCurrentRating = rating;
            }
        });
    }

    private void selectDrinkingPartners() {

    }

    private void selectDrinkingLocation() {

    }

    private void shareCaptureOnFacebook() {

    }

    private void shareCaptureOnTwitter() {

    }

    private void shareCaptureOnInstagram() {

    }

    private void makeCaputrePrivate() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // TODO: Post button top right
    }
}

