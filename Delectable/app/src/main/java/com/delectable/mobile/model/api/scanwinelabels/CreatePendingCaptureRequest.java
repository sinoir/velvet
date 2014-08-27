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

        String bucket;

        String filename;

        // TODO: are these required?
        String label_scan_id;

        Boolean from_camera_roll;

        Integer capture_longitude;

        Integer capture_latitude;
    }

}
