package com.delectable.mobile.api.events.wines;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class PurchasedWineEvent extends BaseEvent {

    private String mWineId;

    private String mPurchaseOrderId;

    public PurchasedWineEvent(String purchaseOrderId, String wineId) {
        super(true);
        mPurchaseOrderId = purchaseOrderId;
        mWineId = wineId;
    }

    public PurchasedWineEvent(String errorMessage, ErrorUtil errorCode, String wineId) {
        super(errorMessage, errorCode);
        mWineId = wineId;
    }

    public String getWineId() {
        return mWineId;
    }

    public String getPurchaseOrderId() {
        return mPurchaseOrderId;
    }
}
