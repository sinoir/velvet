package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.events.BaseEvent;

public class ProvisionProfilePhotoEvent extends BaseEvent {

    private ProvisionCapture mProvisionCapture;

    public ProvisionProfilePhotoEvent(String errorMessage) {
        super(errorMessage);
    }

    public ProvisionProfilePhotoEvent(ProvisionCapture provisionCapture) {
        super(true);
        mProvisionCapture = provisionCapture;
    }

    public ProvisionCapture getProvisionCapture() {
        return mProvisionCapture;
    }

}
