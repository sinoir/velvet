package com.delectable.mobile.ui.camera.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.controllers.S3ImageUploadController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.IdentifyRequest;
import com.delectable.mobile.api.requests.ProvisionPhotoUploadRequest;
import com.delectable.mobile.ui.BaseFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class WineCaptureConfirmFragment extends BaseFragment {

    private static final String sArgsImageData = "sArgsImageData";

    private static final String TAG = WineCaptureConfirmFragment.class.getSimpleName();

    private View mView;

    private View mLoadingProgressContainer;

    private ImageView mPreviewImage;

    private Button mScanOnlyButton;

    private Button mScanAndSaveButton;

    private Bitmap mCapturedImageBitmap;

    // Used only for Scanning
    // TODO: Consolidate the scanning / capturing logic stuff somewhere if possible

    private BaseNetworkController mNetworkController;

    private S3ImageUploadController mImageUploadController;

    private LabelScan mLabelScan;

    private ProvisionPhotoUploadRequest mProvisionPhotoUploadRequest;

    private ProvisionCapture mProvision;

    private IdentifyRequest mIdentifyRequest;

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
        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.ab_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mNetworkController = new BaseNetworkController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wine_capture_options, container, false);

        mLoadingProgressContainer = mView.findViewById(R.id.loading_progress_container);
        // TODO: Make previewImage Height = Width a square
        mPreviewImage = (ImageView) mView.findViewById(R.id.preview_image);
        mScanOnlyButton = (Button) mView.findViewById(R.id.scan_only_button);
        mScanAndSaveButton = (Button) mView.findViewById(R.id.scan_save_button);

        setupButtonListeners();
        displayCapturedImage();
        return mView;
    }

    private void displayCapturedImage() {
        if (mCapturedImageBitmap != null) {
            mPreviewImage.setImageBitmap(mCapturedImageBitmap);
        }
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
                mScanOnlyButton.setEnabled(false);
                scanOnly();
            }
        });

        mScanAndSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanAndSaveCapture();
            }
        });
    }

    private void scanOnly() {
        loadPhotoProvision();
        mLoadingProgressContainer.setVisibility(View.VISIBLE);
    }

    private void scanAndSaveCapture() {
        WineCaptureSubmitFragment fragment = WineCaptureSubmitFragment
                .newInstance(mCapturedImageBitmap);
        launchNextFragment(fragment);
    }

    private void loadPhotoProvision() {
        mProvisionPhotoUploadRequest = new ProvisionPhotoUploadRequest();
        mNetworkController.performRequest(mProvisionPhotoUploadRequest,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        // TODO: Synchronize
                        mProvision = (ProvisionCapture) result;
                        sendCapturedImage();

                        Log.d(TAG, "Provision: " + mProvision);
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        showRequestError(error);
                    }
                }
        );
    }

    private void sendCapturedImage() {
        if (mProvision != null) {
            mImageUploadController = new S3ImageUploadController(getActivity(), mProvision);
            mImageUploadController.uploadImage(mCapturedImageBitmap,
                    new BaseNetworkController.SimpleRequestCallback() {
                        @Override
                        public void onSucess() {
                            Log.d(TAG, "Image Upload Done!");
                            loadScannedWineData();
                        }

                        @Override
                        public void onFailed(RequestError error) {
                            showRequestError(error);
                        }
                    }
            );
        }
    }

    private void loadScannedWineData() {
        if (mProvision != null) {
            mIdentifyRequest = new IdentifyRequest(mProvision);
            mNetworkController
                    .performRequest(mIdentifyRequest, new BaseNetworkController.RequestCallback() {
                                @Override
                                public void onSuccess(BaseResponse result) {
                                    mLabelScan = (LabelScan) result;
                                    showScannedWine();
                                }

                                @Override
                                public void onFailed(RequestError error) {
                                    showRequestError(error);
                                }
                            }
                    );
        }
    }

    private void showRequestError(RequestError error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        scanningFinished();
    }

    private void showScannedWine() {
        // TODO: Show Wine info screen
        Log.d(TAG, "Scan Success:" + mLabelScan);
        if (getActivity() != null) {
            if (mLabelScan.getBaseWine() == null) {
                Toast.makeText(getActivity(), "No Match", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Found Match!", Toast.LENGTH_SHORT).show();

            }
        }
        scanningFinished();
    }

    private void scanningFinished() {
        mLoadingProgressContainer.setVisibility(View.GONE);
        mProvision = null;
        mLabelScan = null;
        mScanOnlyButton.setEnabled(true);
    }
}

