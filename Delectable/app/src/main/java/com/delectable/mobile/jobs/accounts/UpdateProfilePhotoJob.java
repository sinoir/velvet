package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.jobs.scanwinelabel.BasePhotoUploadJob;
import com.delectable.mobile.api.endpointmodels.accounts.PhotoHashResponse;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.PhotoUploadRequest;

public class UpdateProfilePhotoJob extends BasePhotoUploadJob {

    private static final String TAG = UpdateProfilePhotoJob.class.getSimpleName();

    public UpdateProfilePhotoJob(byte[] imageData) {
        super(imageData);
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile_photo";
        ProvisionCapture provision = uploadImage();

        PhotoUploadRequest request = new PhotoUploadRequest(provision);
        PhotoHashResponse response = getNetworkClient()
                .post(endpoint, request, PhotoHashResponse.class);
        getEventBus().post(new UpdatedProfilePhotoEvent(response.getPayload().getPhoto()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfilePhotoEvent(TAG + " " + getErrorMessage()));
    }

    @Override
    public String getProvisionEndpoint() {
        return "/accounts/provision_profile_photo";
    }

    @Override
    public byte[] getResizedImage() {
        return getImageData();
    }
}
