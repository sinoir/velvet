package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.fragment.CameraFragment;
import com.delectable.mobile.ui.common.widget.CameraView;
import com.delectable.mobile.util.PhotoUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class WineCaptureCameraFragment extends CameraFragment {

    public static final int SELECT_PHOTO = 100;

    private static final String TAG = WineCaptureCameraFragment.class.getSimpleName();

    @InjectView(R.id.camera_preview)
    protected CameraView mCameraPreview;

    @InjectView(R.id.camera_container)
    protected View mCameraContainer;

    @InjectView(R.id.camera_roll_button)
    protected View mCameraRollButton;

    @InjectView(R.id.capture_button)
    protected View mCaptureButton;

    @InjectView(R.id.flash_button)
    protected Button mFlashButton;

    @InjectView(R.id.close_button)
    protected View mCloseButton;

    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_camera, container, false);
        ButterKnife.inject(this, mView);

        setupCameraSurface(mCameraPreview);
        mCameraPreview.setScaleToFitY(true);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().hide();
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
        takeJpegCroppedPicture(new PictureTakenCallback() {
            @Override
            public void onBitmapCaptured(Bitmap bitmap) {
                launchOptionsScreen(bitmap);
            }
        });
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

    @OnClick(R.id.flash_button)
    public void toggleFlashClicked() {
        toggleFlash();
    }

    @Override
    public Bitmap cropRotatedCapturedBitmap(Bitmap bitmap) {
        int frameWidth = mCameraContainer.getWidth();
        int frameHeight = mCameraContainer.getHeight();
        int imageHeight = bitmap.getHeight();
        int imageWidth = bitmap.getWidth();

        // Scale the view height to match the image dpi
        float previewScale = (float) imageWidth / (float) frameWidth;
        int croppedHeight = Math.min((int) (frameHeight * previewScale), imageHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, imageWidth, croppedHeight);
    }

    @Override
    public boolean toggleFlash() {
        boolean isFlashOn = super.toggleFlash();
        mFlashButton.setSelected(isFlashOn);
        String flashText = isFlashOn ? getString(R.string.flash_on) : getString(R.string.flash_off);
        mFlashButton.setText(flashText);
        return isFlashOn;
    }

    private void launchOptionsScreen(Bitmap imageData) {
        WineCaptureConfirmFragment fragment = WineCaptureConfirmFragment.newInstance(imageData);
        launchNextFragment(fragment);
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
                    selectedImage = PhotoUtil.loadBitmapFromUri(selectedImageUri, 1024);
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
                    launchOptionsScreen(selectedImage);
                } else {
                    showToastError("Failed to load image");
                }
            }
        }.execute();
    }
}
