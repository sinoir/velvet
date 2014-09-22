package com.delectable.mobile.jobs.scanwinelabel;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.events.scanwinelabel.CreatedPendingCaptureEvent;
import com.delectable.mobile.model.api.scanwinelabels.CreatePendingCaptureRequest;
import com.delectable.mobile.model.api.scanwinelabels.CreatePendingCaptureResponse;

import android.util.Log;

public class CreatePendingCaptureJob extends BasePhotoUploadJob {

    private static final String TAG = CreatePendingCaptureJob.class.getSimpleName();

    private String mLabelScanId;

    public CreatePendingCaptureJob(byte[] imageData, String labelScanId) {
        super(imageData);

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
        getEventBus().post(new CreatedPendingCaptureEvent(response.getPendingCapture()));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus().post(new CreatedPendingCaptureEvent(getErrorMessage()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }

    @Override
    public byte[] getResizedImage() {
        // TODO: Resize Photo to 300x300 for Instant and 1280x1280 for Pending Capture
        return getImageData();
    }

    @Override
    public String getProvisionEndpoint() {
        return "/pending_captures/provision_photo_upload";
    }
}
