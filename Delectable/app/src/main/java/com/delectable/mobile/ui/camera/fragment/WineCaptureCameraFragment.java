package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.WineScanController;
import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.events.scanwinelabel.IdentifyLabelScanEvent;
import com.delectable.mobile.api.events.ui.InsetsChangedEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.common.fragment.CameraFragment;
import com.delectable.mobile.ui.common.widget.CameraView;
import com.delectable.mobile.ui.wineprofile.fragment.WineProfileInstantFragment;
import com.delectable.mobile.util.Animate;
import com.delectable.mobile.util.CameraUtil;
import com.delectable.mobile.util.ViewUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class WineCaptureCameraFragment extends CameraFragment {

    private static final String WINDOW_INSETS = "WINDOW_INSETS";

    public static final int REQUEST_SELECT_PHOTO = 100;

    public static final int REQUEST_INSTANT_FAILED = 200;

    private static final String TAG = WineCaptureCameraFragment.class.getSimpleName();

    @InjectView(R.id.wine_profile_container)
    protected View mWineProfileContainer;

    @InjectView(R.id.camera_container)
    protected View mCameraContainer;

    @InjectView(R.id.camera_preview)
    protected CameraView mCameraPreview;

    @InjectView(R.id.preview_image)
    protected ImageView mPreviewImage;

    @InjectView(R.id.action_buttons_container)
    protected View mButtonsContainer;

    @InjectView(R.id.camera_roll_button)
    protected View mCameraRollButton;

    @InjectView(R.id.capture_button)
    protected View mCaptureButton;

    @InjectView(R.id.confirm_button)
    protected View mConfirmButton;

    @InjectView(R.id.flash_button)
    protected Button mFlashButton;

    @InjectView(R.id.close_button)
    protected View mCloseButton;

    @InjectView(R.id.cancel_button)
    protected View mRedoButton;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    @InjectView(R.id.hint_label_legible)
    protected View mHintLabelLegible;

    private View mView;

    @Inject
    protected WineScanController mWineScanController;

    private WineProfileInstantFragment mWineProfileFragment;

    private Bitmap mCapturedImageBitmap;

    private boolean mIsIdentifying = false;

    private LabelScan mLabelScanResult;

    private String mPendingCaptureId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setHasOptionsMenu(true);

        // Preload wine profile fragment
        mWineProfileFragment = WineProfileInstantFragment.newInstance(null);
        getFragmentManager().beginTransaction()
                .add(R.id.wine_profile_container, mWineProfileFragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_camera, container, false);
        ButterKnife.inject(this, mView);

        // set camera container height to match screen width
        Point screenSize = ViewUtil.getDisplayDimensions();
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenSize.x,
                screenSize.x);
        mCameraContainer.setLayoutParams(parms);
        FrameLayout.LayoutParams previewImageParms = new FrameLayout.LayoutParams(screenSize.x,
                screenSize.x);
        mPreviewImage.setLayoutParams(previewImageParms);

        setupCameraSurface(mCameraPreview);
        mCameraPreview.setScaleToFitY(true);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Re-enable the Capture Button
        mCaptureButton.setEnabled(true);
        InsetsChangedEvent insetsEvent = mEventBus.getStickyEvent(InsetsChangedEvent.class);
        if (insetsEvent != null) {
            onApplyWindowInsets(insetsEvent.insets);
        }

    }

    private void onApplyWindowInsets(Rect insets) {
        if (insets == null) {
            return;
        }
        mFlashButton.setPadding(0, insets.top, 0, 0);
    }

    @OnClick(R.id.close_button)
    public void closeCamera() {
        mView.post(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.camera_roll_button)
    protected void launchCameraRoll() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_PHOTO);
    }

    @OnClick(R.id.capture_button)
    protected void captureCameraImage() {
        // Prevent user from clicking many times
        mCaptureButton.setEnabled(false);
        takeJpegCroppedPicture(new PictureTakenCallback() {
            @Override
            public void onBitmapCaptured(Bitmap bitmap) {
                onPictureTaken(bitmap);
            }
        });
    }

    private void onPictureTaken(Bitmap bitmap) {
        mCapturedImageBitmap = bitmap;
        mPreviewImage.setImageBitmap(bitmap);
        animateFromCaptureToConfirm();
    }

    private void animateFromCaptureToConfirm() {
        Animate.fadeIn(mPreviewImage);
        Animate.crossfadeRotate(mCaptureButton, mConfirmButton);
        Animate.pushOutUp(mFlashButton);
        Animate.rollOutRight(mCameraRollButton);
        Animate.crossfadeRotate(mCloseButton, mRedoButton);
        Animate.fadeIn(mHintLabelLegible, 3000);
    }

    private void animateFromConfirmToCapture() {
        mCameraPreview.startPreview();
        Animate.fadeOut(mPreviewImage);
        mCaptureButton.setEnabled(true);
        Animate.crossfadeRotate(mCaptureButton, mConfirmButton, true);
        Animate.pushInDown(mFlashButton);
        Animate.rollInRight(mCameraRollButton);
        Animate.crossfadeRotate(mCloseButton, mRedoButton, true);
        Animate.fadeOut(mHintLabelLegible);
    }

    private void animateFromConfirmToIdentify() {
        Animate.rotateOut(mConfirmButton);
        Animate.fadeIn(mProgressBar);
        Animate.rotateOut(mRedoButton);
        Animate.fadeOut(mHintLabelLegible);
    }

    private void animateFromIdentifyToWineProfile() {
        mWineProfileContainer.setVisibility(View.VISIBLE);
//        Animate.fadeOut(mProgressBar);
        Animate.fadeOut(mCameraContainer);
//        Animate.slideOutDown(mButtonsContainer, 600);
        Animate.fadeOut(mButtonsContainer, 600);

        // show status bar
        getBaseActivity().showOrHideStatusBar(true);
    }

    @OnTouch(R.id.camera_preview)
    protected boolean focusCamera(View view, MotionEvent event) {
        if (mPreviewImage.getVisibility() == View.VISIBLE) {
            return true;
        }
        try {
            PointF pointToFocusOn = new PointF(event.getX(), event.getY());
            RectF bounds = new RectF(0, 0, mCameraPreview.getWidth(),
                    mCameraPreview.getHeight());
            // TODO: Display Focus icon
            focusOnPoint(pointToFocusOn, bounds);
        } catch (Exception ex) {
            Log.wtf(TAG, "Failed to Focus", ex);
        }
        return true;
    }

    @OnClick(R.id.cancel_button)
    protected void cancelScan() {
        animateFromConfirmToCapture();
    }

    @OnClick(R.id.confirm_button)
    protected void confirmImage() {
        if (mIsIdentifying) {
            return;
        }
        mIsIdentifying = true;
        releaseCameraAndPreview();
        animateFromConfirmToIdentify();
        mWineScanController.scanLabelInstantly(mCapturedImageBitmap);
    }

    public void onEventMainThread(InsetsChangedEvent event) {
        onApplyWindowInsets(event.insets);
    }

    public void onEventMainThread(IdentifyLabelScanEvent event) {
        if (event.isSuccessful()) {
            mLabelScanResult = event.getLabelScan();
            // Instant match
            if (mLabelScanResult != null) {
                ArrayList<BaseWine> matches = mLabelScanResult.getBaseWineMatches();
                if (matches != null && !matches.isEmpty()) {
                    // Load wine profile into container
                    mWineProfileFragment.init(matches, mCapturedImageBitmap);
                    // TODO release camera here? trying to avoid animation stutter
//                    releaseCameraAndPreview();
                    animateFromIdentifyToWineProfile();
                } else {
                    // Instant failed
                    onInstantFailed();
                }
            }
            // Create pending capture, WineProfileInstantFragment will listen for events
            mWineScanController
                    .createPendingCapture(mCapturedImageBitmap, mLabelScanResult.getId());
        } else {
            // Request error
            Animate.fadeOut(mProgressBar);
            handleEventErrorMessage(event);
            mView.post(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            });
        }
        mIsIdentifying = false;
    }

    public void onInstantFailed() {
        Animate.fadeOut(mProgressBar);
        showConfirmationNoTitle(
                getString(R.string.capture_instant_failed),
                getString(R.string.ok),
                "",
                REQUEST_INSTANT_FAILED);
    }

    private void handleEventErrorMessage(BaseEvent event) {
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(R.string.error_capture_wine_no_network);
        } else {
            showToastError(event.getErrorMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == getActivity().RESULT_OK) {
            Uri selectedImageUri = data.getData();
            loadGalleryImage(selectedImageUri);
        } else if (requestCode == REQUEST_INSTANT_FAILED) {
            launchUserProfile(true);
        }
    }

    @OnClick(R.id.flash_button)
    public void toggleFlashClicked() {
        toggleFlash();
    }

    @Override
    public boolean toggleFlash() {
        boolean isFlashOn = super.toggleFlash();
        mFlashButton.setSelected(isFlashOn);
        String flashText = isFlashOn ? getString(R.string.flash_on) : getString(R.string.flash_off);
        mFlashButton.setText(flashText);
        return isFlashOn;
    }

    private void loadGalleryImage(final Uri selectedImageUri) {
        if (getActivity() == null) {
            return;
        }

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap selectedImage = null;
                try {
                    selectedImage = CameraUtil
                            .loadBitmapFromUri(selectedImageUri, CameraUtil.MAX_SIZE_PENDING);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to open image", e);
                }
                return selectedImage;
            }

            @Override
            protected void onPostExecute(Bitmap selectedImage) {
                super.onPostExecute(selectedImage);
                if (selectedImage != null) {
                    onPictureTaken(selectedImage);
                } else {
                    showToastError("Failed to load image");
                }
            }
        }.execute();
    }

}
