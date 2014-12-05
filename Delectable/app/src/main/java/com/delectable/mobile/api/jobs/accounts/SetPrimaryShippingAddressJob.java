package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.accounts.ModifyShippingAddressRequest;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.events.accounts.SetPrimaryShippingAddressEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class SetPrimaryShippingAddressJob extends BaseJob {

    private static final String TAG = SetPrimaryShippingAddressJob.class.getSimpleName();

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    private String mAddressId;

    public SetPrimaryShippingAddressJob(String addressId) {
        super(new Params(Priority.SYNC.value()));
        mAddressId = addressId;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/set_primary_shipping_address";
        ModifyShippingAddressRequest request = new ModifyShippingAddressRequest(mAddressId);
        ShippingAddressesResponse response = getNetworkClient().post(endpoint, request,
                ShippingAddressesResponse.class);
        mShippingAddressModel.clear();
        mShippingAddressModel.saveShippingAddresses(response.getPayload().getShippingAddresses());
        getEventBus().post(new SetPrimaryShippingAddressEvent(mAddressId));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new SetPrimaryShippingAddressEvent(getErrorMessage(), getErrorCode(),
                mAddressId));
    }
}
