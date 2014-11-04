package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.accounts.RemoveShippingAddressRequest;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.events.accounts.RemovedShippingAddressEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class RemoveShippingAddressJob extends BaseJob {

    private static final String TAG = RemoveShippingAddressJob.class.getSimpleName();

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    private String mAddressId;

    public RemoveShippingAddressJob(String addressId) {
        super(new Params(Priority.SYNC));
        mAddressId = addressId;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/remove_shipping_address";
        RemoveShippingAddressRequest request = new RemoveShippingAddressRequest(mAddressId);
        ShippingAddressesResponse response = getNetworkClient().post(endpoint, request,
                ShippingAddressesResponse.class);
        mShippingAddressModel.clear();
        mShippingAddressModel.saveShippingAddresses(response.getPayload().getShippingAddresses());
        getEventBus().post(new RemovedShippingAddressEvent(mAddressId));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new RemovedShippingAddressEvent(getErrorMessage(), getErrorCode(),
                mAddressId));
    }
}
