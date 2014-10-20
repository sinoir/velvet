package com.delectable.mobile.api.endpointmodels.scanwinelabels;

import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class LabelScanResponse extends BaseResponse {

    private LabelScanPayload payload;

    public LabelScan getLabelScan() {
        if (payload == null) {
            return null;
        }

        return payload.label_scan;
    }

    public static class LabelScanPayload {

        private LabelScan label_scan;
    }
}
