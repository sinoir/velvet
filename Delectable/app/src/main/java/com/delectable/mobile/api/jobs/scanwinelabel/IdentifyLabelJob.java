package com.delectable.mobile.api.jobs.scanwinelabel;

import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.LabelScanResponse;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.PhotoUploadRequest;
import com.delectable.mobile.api.events.scanwinelabel.IdentifyLabelScanEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.util.KahunaUtil;
import com.delectable.mobile.util.PhotoUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import javax.inject.Inject;

public class IdentifyLabelJob extends BasePhotoUploadJob {

    private static final String TAG = IdentifyLabelJob.class.getSimpleName();

    @Inject
    BaseWineModel mBaseWineModel;

    public IdentifyLabelJob(Bitmap bitmap) {
        super(bitmap);
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
    public byte[] compressImage(Bitmap bitmap) {

        // to lossless byte array
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] data = os.toByteArray();

        // downsample
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = PhotoUtil.calculateInSampleSize(bitmap.getWidth(),
                bitmap.getHeight(),
                PhotoUtil.MAX_SIZE_INSTANT, PhotoUtil.MAX_SIZE_INSTANT);
        Bitmap downscaledBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // compress
        os = new ByteArrayOutputStream();
        downscaledBitmap.compress(Bitmap.CompressFormat.JPEG, PhotoUtil.JPEG_QUALITY_INSTANT, os);
        return os.toByteArray();
    }

    @Override
    public String getProvisionEndpoint() {
        return "/label_scans/provision_photo_upload";
    }
}
