package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WineCaptureConfirmFragment extends BaseFragment {

    private static final String sArgsImageData = "sArgsImageData";

    private static final String TAG = WineCaptureConfirmFragment.class.getSimpleName();

    @InjectView(R.id.preview_image)
    protected ImageView mPreviewImage;

    @InjectView(R.id.confirm_button)
    protected ImageButton mConfirmButton;

    @InjectView(R.id.cancel_button)
    protected ImageButton mCancelButton;

    private View mView;

    private Bitmap mCapturedImageBitmap;

    public static WineCaptureConfirmFragment newInstance(Bitmap imageData) {
        WineCaptureConfirmFragment fragment = new WineCaptureConfirmFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_options, container, false);
        ButterKnife.inject(this, mView);

        // TODO: Make previewImage Height = Width a square

        displayCapturedImage();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActionBar().hide();
    }

    private void displayCapturedImage() {
        if (mCapturedImageBitmap != null) {
            mPreviewImage.setImageBitmap(mCapturedImageBitmap);
        }
    }

    @OnClick(R.id.cancel_button)
    protected void cancelScan() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm_button)
    protected void scanAndSaveCapture() {
        WineCaptureSubmitFragment fragment = WineCaptureSubmitFragment
                .newInstance(mCapturedImageBitmap);
        launchNextFragment(fragment);
    }
}

