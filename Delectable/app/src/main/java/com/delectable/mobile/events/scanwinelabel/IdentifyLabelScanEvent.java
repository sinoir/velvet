package com.delectable.mobile.events.scanwinelabel;

import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.events.BaseEvent;

public class IdentifyLabelScanEvent extends BaseEvent {

    private LabelScan mLabelScan;

    public IdentifyLabelScanEvent(LabelScan labelScan) {
        super(true);
        mLabelScan = labelScan;
    }

    public IdentifyLabelScanEvent(String errorMessage) {
        super(errorMessage);
    }
}
