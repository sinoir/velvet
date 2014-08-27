package com.delectable.mobile.jobs.scanwinelabel;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.events.scanwinelabel.IdentifyLabelScanEvent;
import com.delectable.mobile.model.api.scanwinelabels.PhotoUploadRequest;
import com.delectable.mobile.model.api.scanwinelabels.LabelScanResponse;

import android.util.Log;

public class IdentifyLabelJob extends BasePhotoUploadJob {

    private static final String TAG = IdentifyLabelJob.class.getSimpleName();

    public IdentifyLabelJob(byte[] imageData) {
        super(imageData);
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        String endpoint = "/label_scans/identify";
        ProvisionCapture provisionCapture = uploadImage();
        PhotoUploadRequest request = new PhotoUploadRequest(provisionCapture);
        LabelScanResponse response = getNetworkClient().post(endpoint, request,
                LabelScanResponse.class);

        Log.d(TAG, "Scanneed label Payload: " + response.getLabelScan());
        getEventBus().post(new IdentifyLabelScanEvent(response.getLabelScan()));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus().post(new IdentifyLabelScanEvent(getErrorMessage()));
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
        return "/label_scans/provision_photo_upload";
    }
}
