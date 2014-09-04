package com.delectable.mobile.model.api.scanwinelabels;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.model.api.BaseRequest;

public class CreatePendingCaptureRequest extends BaseRequest {

    private Payload payload = new Payload();

    public CreatePendingCaptureRequest(ProvisionCapture provision) {
        this.payload.bucket = provision.getBucket();
        this.payload.filename = provision.getFilename();
    }

    public static class Payload {

        private String bucket;

        private String filename;

        // TODO: are these required?
        private String label_scan_id;

        private boolean from_camera_roll;

        private int capture_longitude;

        private int capture_latitude;
    }

}