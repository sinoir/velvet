package com.delectable.mobile.api.jobs.scanwinelabel;

import com.delectable.mobile.api.cache.PendingCapturesModel;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.CreatePendingCaptureRequest;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.CreatePendingCaptureResponse;
import com.delectable.mobile.api.events.scanwinelabel.CreatedPendingCaptureEvent;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.util.CameraUtil;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

public class CreatePendingCaptureJob extends BasePhotoUploadJob {

    private static final String TAG = CreatePendingCaptureJob.class.getSimpleName();

    @Inject
    PendingCapturesModel mPendingCapturesModel;

    private String mLabelScanId;

    public CreatePendingCaptureJob(Bitmap bitmap, String labelScanId) {
        super(bitmap);

        mLabelScanId = labelScanId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        String endpoint = "/pending_captures/create";
        ProvisionCapture provisionCapture = uploadImage();
        CreatePendingCaptureRequest request = new CreatePendingCaptureRequest(provisionCapture,
                mLabelScanId);
        CreatePendingCaptureResponse response = getNetworkClient().post(endpoint, request,
                CreatePendingCaptureResponse.class);

        Log.d(TAG, "Created Pending Capture: " + response.getPendingCapture());
        if (response.getPendingCapture() != null) {
            mPendingCapturesModel.saveCapture(response.getPendingCapture());
        }
        getEventBus().post(new CreatedPendingCaptureEvent(response.getPendingCapture()));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus().post(new CreatedPendingCaptureEvent(getErrorMessage(), getErrorCode()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }

    @Override
    public byte[] compressImage(final Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, CameraUtil.JPEG_QUALITY, os);
        return os.toByteArray();
    }

    @Override
    public String getProvisionEndpoint() {
        return "/pending_captures/provision_photo_upload";
    }
}
