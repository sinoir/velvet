package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.fragment.CameraFragment;
import com.delectable.mobile.ui.common.widget.CameraView;
import com.delectable.mobile.util.CameraUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class WineCaptureCameraFragment extends CameraFragment {

    private static enum State {
        CAPTURE, CONFIRM, SUBMIT;
    }

    private int ANIMATION_TRANSLATION;

    public static final int SELECT_PHOTO = 100;

    private static final String TAG = WineCaptureCameraFragment.class.getSimpleName();

    @InjectView(R.id.camera_preview)
    protected CameraView mCameraPreview;

    @InjectView(R.id.camera_container)
    protected View mCameraContainer;

    @InjectView(R.id.preview_image)
    protected ImageView mPreviewImage;

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

    private View mView;

    private State mState = State.CAPTURE;

    private Bitmap mCapturedImageBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ANIMATION_TRANSLATION = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75,
                getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_camera, container, false);
        ButterKnife.inject(this, mView);

        // set camera container height to match screen width
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenSize.x, screenSize.x);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActionBar().hide();
    }

    @OnClick(R.id.close_button)
    public void closeCamera() {
        getActivity().finish();
    }

    @OnClick(R.id.camera_roll_button)
    protected void launchCameraRoll() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
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
        mState = State.CONFIRM;

        mPreviewImage.setVisibility(View.VISIBLE);
        mPreviewImage.animate()
                .alpha(1)
                .setDuration(400)
                .setListener(null)
                .start();

        mCaptureButton.animate()
                .alpha(0)
                .rotation(180)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCaptureButton.setVisibility(View.INVISIBLE);
                    }
                })
                .start();

        mConfirmButton.setVisibility(View.VISIBLE);
        mConfirmButton.setRotation(-180);
        mConfirmButton.animate()
                .alpha(1)
                .rotation(0)
                .setDuration(400)
                .setListener(null)
                .start();

        mFlashButton.animate()
                .alpha(0)
                .translationY(-ANIMATION_TRANSLATION)
                .setDuration(400)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFlashButton.setVisibility(View.GONE);
                    }
                })
                .start();

        mCameraRollButton.animate()
                .alpha(0)
                .translationX(ANIMATION_TRANSLATION)
                .rotation(180)
                .setDuration(400)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCameraRollButton.setVisibility(View.GONE);
                    }
                })
                .start();

        mCloseButton.animate()
                .alpha(0)
                .rotation(180)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCloseButton.setVisibility(View.GONE);
                    }
                })
                .start();

        mRedoButton.setAlpha(0);
        mRedoButton.setRotation(-180);
        mRedoButton.setVisibility(View.VISIBLE);
        mRedoButton.animate()
                .alpha(1)
                .rotation(0)
                .setDuration(400)
                .setListener(null)
                .start();
    }

    private void animateFromConfirmToCapture() {
        mState = State.CAPTURE;

        mCameraPreview.startPreview();

        mPreviewImage.animate()
                .alpha(0)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mPreviewImage.setVisibility(View.GONE);
                    }
                })
                .start();

        mCaptureButton.setEnabled(true);
        mCaptureButton.setVisibility(View.VISIBLE);
        mCaptureButton.animate()
                .alpha(1)
                .rotation(0)
                .setDuration(400)
                .setListener(null)
                .start();

        mConfirmButton.animate()
                .alpha(0)
                .rotation(-180)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mConfirmButton.setVisibility(View.INVISIBLE);
                    }
                })
                .start();

        mFlashButton.setVisibility(View.VISIBLE);
        mFlashButton.animate()
                .alpha(1)
                .translationY(0)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .start();

        mCameraRollButton.setVisibility(View.VISIBLE);
        mCameraRollButton.animate()
                .alpha(1)
                .translationX(0)
                .rotation(0)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .start();

        mCloseButton.setVisibility(View.VISIBLE);
        mCloseButton.animate()
                .alpha(1)
                .rotation(0)
                .setDuration(400)
                .setListener(null)
                .start();

        mRedoButton.animate()
                .alpha(0)
                .rotation(-180)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRedoButton.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    @OnTouch(R.id.camera_container)
    protected boolean focusCamera(View view, MotionEvent event) {
        // TODO: Display Focus icon
        try {
            PointF pointToFocusOn = new PointF(event.getX(), event.getY());
            RectF bounds = new RectF(0, 0, mCameraPreview.getWidth(),
                    mCameraPreview.getHeight());
            // TODO: Figure out why we get RuntimeException when running on some phones
            focusOnPoint(pointToFocusOn, bounds);
        } catch (Exception ex) {
            Log.wtf(TAG, "Failed to Focus", ex);
        }
        return true;
    }

    @OnClick(R.id.cancel_button)
    protected void cancelScan() {
        if (mState == State.CONFIRM) {
            animateFromConfirmToCapture();
        }
    }

    @OnClick(R.id.confirm_button)
    protected void confirmImage() {
        WineCaptureSubmitFragment fragment = WineCaptureSubmitFragment
                .newInstance(mCapturedImageBitmap);
        launchNextFragment(fragment);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == getActivity().RESULT_OK) {
            Uri selectedImageUri = data.getData();
            loadGalleryImage(selectedImageUri);
        }
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
                    selectedImage = CameraUtil.loadBitmapFromUri(selectedImageUri, CameraUtil.MAX_SIZE_PENDING);
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
