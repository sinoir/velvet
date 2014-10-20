package com.delectable.mobile.jobs.scanwinelabel;

import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.data.BaseWineModel;
import com.delectable.mobile.events.scanwinelabel.IdentifyLabelScanEvent;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.LabelScanResponse;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.PhotoUploadRequest;
import com.delectable.mobile.util.KahunaUtil;

import android.util.Log;

import java.util.Calendar;

import javax.inject.Inject;

public class IdentifyLabelJob extends BasePhotoUploadJob {

    private static final String TAG = IdentifyLabelJob.class.getSimpleName();

    @Inject
    BaseWineModel mBaseWineModel;

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
        request.setContext("profile");
        LabelScanResponse response = getNetworkClient().post(endpoint, request,
                LabelScanResponse.class);

        if (response.getLabelScan() != null
                && response.getLabelScan().getBaseWineMatches() != null) {
            for (BaseWine baseWine : response.getLabelScan().getBaseWineMatches()) {
                // API sometimes returns a null value within the matches...
                if (baseWine != null) {
                    mBaseWineModel.saveBaseWine(baseWine);
                }
            }
        }
        Log.d(TAG, "Scanneed label Payload: " + response.getLabelScan());
        getEventBus().post(new IdentifyLabelScanEvent(response.getLabelScan()));
        KahunaUtil.trackScanBottle(Calendar.getInstance().getTime());
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus().post(new IdentifyLabelScanEvent(getErrorMessage(), getErrorCode()));
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
