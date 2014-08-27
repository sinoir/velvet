package com.delectable.mobile.model.api.scanwinelabels;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.model.api.BaseRequest;

public class LabelScanRequest extends BaseRequest {

    private LabelScanPayload payload = new LabelScanPayload();

    public LabelScanRequest(ProvisionCapture provision) {
        this.payload.bucket = provision.getBucket();
        this.payload.filename = provision.getFilename();
    }

    public static class LabelScanPayload {

        String bucket;

        String filename;
    }

}
