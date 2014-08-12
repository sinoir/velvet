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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WineCaptureConfirmFragment extends BaseFragment {

    private static final String sArgsImageData = "sArgsImageData";

    private static final String TAG = WineCaptureConfirmFragment.class.getSimpleName();

    @InjectView(R.id.loading_progress_container)
    protected View mLoadingProgressContainer;

    @InjectView(R.id.preview_image)
    protected ImageView mPreviewImage;

    @InjectView(R.id.confirm_button)
    protected ImageButton mConfirmButton;

    @InjectView(R.id.cancel_button)
    protected ImageButton mCancelButton;

    private View mView;

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
        getActivity().getActionBar().hide();
        mNetworkController = new BaseNetworkController(getActivity());
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

    private void scanOnly() {
        // TODO: Remove Scan Only (Refactor the Camera flow)
        loadPhotoProvision();
        mLoadingProgressContainer.setVisibility(View.VISIBLE);
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
    }
}

