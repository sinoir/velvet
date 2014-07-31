package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.models.ProvisionCapture;

import org.json.JSONObject;

public class IdentifyRequest extends BaseScanRequest {

    public IdentifyRequest(ProvisionCapture provision) {
        super(provision);
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "bucket",
                "filename",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/label_scans/identify";
    }

    @Override
    public LabelScan buildResopnseFromJson(JSONObject jsonObject) {
        return LabelScan.buildFromJson(jsonObject);
    }
}
