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
import android.graphics.Matrix;
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

        // scale
        Matrix matrix = new Matrix();
        float scaleFactor = PhotoUtil.MAX_SIZE_INSTANT / (float) bitmap.getHeight();
        matrix.setScale(scaleFactor, scaleFactor);

        Bitmap finalBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getHeight(),
                bitmap.getHeight(),
                matrix,
                true);

        // compress
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, PhotoUtil.JPEG_QUALITY_INSTANT, os);
        return os.toByteArray();
    }

    @Override
    public String getProvisionEndpoint() {
        return "/label_scans/provision_photo_upload";
    }
}
