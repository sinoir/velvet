package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.fragment.CameraFragment;
import com.delectable.mobile.ui.common.widget.CameraView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class WineCaptureCameraFragment extends CameraFragment {

    public static final int SELECT_PHOTO = 100;

    private static final String TAG = "WineCaptureCameraFragment";

    private View mView;

    private CameraView mCameraPreview;

    private View mCameraContainer;

    private ImageButton mCameraRollButton;

    private ImageButton mCaptureButton;

    private ImageButton mFlashButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.ab_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_camera, container, false);

        mCameraPreview = (CameraView) mView.findViewById(R.id.camera_preview);
        mCameraContainer = mView.findViewById(R.id.camera_container);
        setupCameraSurface(mCameraPreview);
        mCameraPreview.setScaleToFitY(true);

        mCameraRollButton = (ImageButton) mView.findViewById(R.id.camera_roll_button);
        mCaptureButton = (ImageButton) mView.findViewById(R.id.capture_button);
        mFlashButton = (ImageButton) mView.findViewById(R.id.flash_button);

        setupButtonListeners();

        return mView;
    }

    private void setupButtonListeners() {
        mCameraRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCameraRoll();
            }
        });

        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureCameraImage();
            }
        });

        mFlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlash();
            }
        });

        mCameraContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO: Display Focus icon
                PointF pointToFocusOn = new PointF(event.getX(), event.getY());
                RectF bounds = new RectF(0, 0, mCameraPreview.getWidth(),
                        mCameraPreview.getHeight());
                focusOnPoint(pointToFocusOn, bounds);
                return true;
            }
        });
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

    private void launchCameraRoll() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    private void captureCameraImage() {
        takeJpegCroppedPicture(new PictureTakenCallback() {
            @Override
            public void onBitmapCaptured(Bitmap bitmap) {
                launchOptionsScreen(bitmap);
            }
        });
    }

    @Override
    public boolean toggleFlash() {
        boolean isFlashOn = super.toggleFlash();
        // TODO: Toggle Icon to indicate flash is on or off
        return isFlashOn;
    }

    private void launchOptionsScreen(Bitmap imageData) {
        WineCaptureOptionsFragment fragment = WineCaptureOptionsFragment.newInstance(imageData);
        launchNextFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "OptionsItemSelected: " + item);
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == getActivity().RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                if (getActivity() != null) {
                    // TODO: Background thread here or in resize screen?
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), selectedImageUri);
                    // TODO: Launch in a resizing UI as iOS ?
                    launchOptionsScreen(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Failed to open image", e);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }
}
