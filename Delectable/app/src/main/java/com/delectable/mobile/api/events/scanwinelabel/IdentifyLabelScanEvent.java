package com.delectable.mobile.api.events.scanwinelabel;

import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.api.events.BaseEvent;

public class IdentifyLabelScanEvent extends BaseEvent {

    private LabelScan mLabelScan;

    public IdentifyLabelScanEvent(LabelScan labelScan) {
        super(true);
        mLabelScan = labelScan;
    }

    public IdentifyLabelScanEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }

    public LabelScan getLabelScan() {
        return mLabelScan;
    }
}
