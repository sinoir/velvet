package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class WineCaptureOptionsFragment extends BaseFragment {

    private View mView;

    private ImageView mPreviewImage;

    private Button mScanOnlyButton;

    private Button mScanAndSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : Pass up Image from Camera
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_options, container, false);
        mPreviewImage = (ImageView) mView.findViewById(R.id.preview_image);
        mScanOnlyButton = (Button) mView.findViewById(R.id.scan_only_button);
        mScanAndSaveButton = (Button) mView.findViewById(R.id.scan_save_button);

        setupButtonListeners();
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Show Custom back < icon
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupButtonListeners() {
        mScanOnlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanOnlyCapture();
            }
        });

        mScanAndSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanAndSaveCapture();
            }
        });

    }

    private void scanOnlyCapture() {
        // TODO: Scan Only / Launch Wine Profile
    }

    private void scanAndSaveCapture() {
        // TODO: Pass up Image
        WineCaptureSubmitFragment fragment = new WineCaptureSubmitFragment();
        launchNextFragment(fragment);
    }
}

