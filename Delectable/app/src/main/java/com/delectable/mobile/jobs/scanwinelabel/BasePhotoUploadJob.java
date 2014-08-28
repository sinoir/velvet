package com.delectable.mobile.jobs.scanwinelabel;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.scanwinelabels.ProvisionPhotoResponse;
import com.delectable.mobile.net.S3ImageUploadNetworkClient;
import com.path.android.jobqueue.Params;

import java.io.IOException;

import javax.inject.Inject;

public abstract class BasePhotoUploadJob extends BaseJob {

    @Inject
    protected S3ImageUploadNetworkClient mS3ImageUploadNetworkClient;

    private byte[] mImageData;

    public BasePhotoUploadJob(byte[] imageData) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mImageData = imageData;
    }

    protected byte[] getImageData() {
        return mImageData;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }

    /**
     * Upload Resized Image to S3 and get Provision
     *
     * @return - Provision Capture
     * @throws IOException - If there was an error in loading a bitmap for S3 upload
     */
    protected ProvisionCapture uploadImage() throws IOException {
        ProvisionCapture provisionCapture = provisionPhotoUpload();
        mS3ImageUploadNetworkClient.uploadImage(getResizedImage(), provisionCapture);
        return provisionCapture;
    }

    /**
     * Get Provision for Capture Upload.  Gives us bucket and auth info to upload image to S3
     */
    protected ProvisionCapture provisionPhotoUpload() throws IOException {
        BaseRequest request = new BaseRequest(); // Request with empty payload
        ProvisionPhotoResponse response = getNetworkClient().post(getProvisionEndpoint(), request,
                ProvisionPhotoResponse.class);
        return response.getPayload();
    }

    public abstract String getProvisionEndpoint();

    public abstract byte[] getResizedImage();

}
