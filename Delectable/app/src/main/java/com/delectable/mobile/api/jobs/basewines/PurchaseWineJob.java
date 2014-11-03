package com.delectable.mobile.api.jobs.basewines;

import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesPurchaseRequest;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesPurchaseResponse;
import com.delectable.mobile.api.events.wines.PurchasedWineEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

public class PurchaseWineJob extends BaseJob {

    private static final String TAG = PurchaseWineJob.class.getSimpleName();

    private String mWineId;

    private String mPurchaseOfferId;

    private String mPaymentMethodId;

    private String mShippingAddressId;

    private String mAdditionalComments;

    private int mQuantity;

    public PurchaseWineJob(String wineId, String purchaseOfferId, String paymentMethodId,
            String shippingAddressId, int quantity, String additionalComments) {
        super(new Params(Priority.SYNC));
        mWineId = wineId;
        mPurchaseOfferId = purchaseOfferId;
        mPaymentMethodId = paymentMethodId;
        mShippingAddressId = shippingAddressId;
        mQuantity = quantity;
        mAdditionalComments = additionalComments;
    }

    @Override
    public void onRun() throws Throwable {
        // TODO: Test out once shipping/payment components are put together
        String endpoint = "/wine_profiles/purchase";
        WineProfilesPurchaseRequest request = new WineProfilesPurchaseRequest(mWineId,
                mPurchaseOfferId,
                mPaymentMethodId, mShippingAddressId, mQuantity, mAdditionalComments);
        WineProfilesPurchaseResponse response = getNetworkClient().post(endpoint, request,
                WineProfilesPurchaseResponse.class);
        getEventBus()
                .post(new PurchasedWineEvent(response.getPayload().getPurchaseOrderId(), mWineId));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new PurchasedWineEvent(getErrorMessage(), getErrorCode(), mWineId));
    }
}
