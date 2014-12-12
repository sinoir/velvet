package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.events.accounts.FetchedShippingAddressesEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchShippingAddressesJob extends BaseJob {

    private static final String TAG = FetchShippingAddressesJob.class.getSimpleName();

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    public FetchShippingAddressesJob() {
        super(new Params(Priority.SYNC.value()));
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/shipping_addresses";
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        ShippingAddressesResponse response = getNetworkClient().post(endpoint, request,
                ShippingAddressesResponse.class);
        mShippingAddressModel.clear();
        mShippingAddressModel.saveShippingAddresses(response.getPayload().getShippingAddresses());
        getEventBus().post(new FetchedShippingAddressesEvent());
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedShippingAddressesEvent(getErrorMessage(), getErrorCode()));
    }
}
