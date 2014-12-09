package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.accounts.PhotoHashResponse;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.PhotoUploadRequest;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.jobs.scanwinelabel.BasePhotoUploadJob;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.util.CameraUtil;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class UpdateProfilePhotoJob extends BasePhotoUploadJob {

    private static final String TAG = UpdateProfilePhotoJob.class.getSimpleName();

    public UpdateProfilePhotoJob(String requestId, Bitmap bitmap) {
        super(bitmap, Priority.UX);
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile_photo";
        ProvisionCapture provision = uploadImage();

        PhotoUploadRequest request = new PhotoUploadRequest(provision);
        PhotoHashResponse response = getNetworkClient()
                .post(endpoint, request, PhotoHashResponse.class);
        Account account = UserInfo.getAccountPrivate();
        account.setPhoto(response.getPayload().getPhoto());
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new UpdatedAccountEvent(mRequestId, account));
    }

    @Override
    protected void onCancel() {
        Account account = UserInfo.getAccountPrivate();
        mEventBus.post(new UpdatedAccountEvent(mRequestId, account, TAG + " " + getErrorMessage()));
    }

    @Override
    public String getProvisionEndpoint() {
        return "/accounts/provision_profile_photo";
    }

    @Override
    public byte[] compressImage(final Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, CameraUtil.JPEG_QUALITY, os);
        return os.toByteArray();
    }
}
