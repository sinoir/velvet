package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class WineCaptureCameraFragment extends BaseFragment {

    private View mView;

    private SurfaceView mCameraSurfaceView;

    private ImageButton mCameraRollButton;

    private ImageButton mCaptureButton;

    private ImageButton mFlashButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_camera, container, false);
        mCameraSurfaceView = (SurfaceView) mView.findViewById(R.id.camera_preview);

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
    }


    private void launchCameraRoll() {
        // TODO: Launch Camera Roll
    }

    private void captureCameraImage() {
        // TODO: Capture Image from Camera
        launchOptionsScreen();
    }

    private void toggleFlash() {
        // TODO: Toggle Flash
    }

    private void launchOptionsScreen() {
        // TODO Pass up image that was captured
        WineCaptureOptionsFragment fragment = new WineCaptureOptionsFragment();
        launchNextFragment(fragment);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Customize with X
        super.onCreateOptionsMenu(menu, inflater);
    }
}
