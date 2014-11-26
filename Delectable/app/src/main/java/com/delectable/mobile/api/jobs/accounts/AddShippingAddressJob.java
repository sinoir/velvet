package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.accounts.AddShippingAddressRequest;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.events.accounts.AddedShippingAddressEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.BaseAddress;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class AddShippingAddressJob extends BaseJob {

    private static final String TAG = AddShippingAddressJob.class.getSimpleName();

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    private BaseAddress mAddress;

    private boolean mIsPrimary;

    public AddShippingAddressJob(BaseAddress address, boolean isPrimary) {
        super(new Params(Priority.SYNC.value()));
        mAddress = address;
        mIsPrimary = isPrimary;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/add_shipping_address";
        AddShippingAddressRequest request = new AddShippingAddressRequest(mAddress, mIsPrimary);
        ShippingAddressesResponse response = getNetworkClient().post(endpoint, request,
                ShippingAddressesResponse.class);
        mShippingAddressModel.clear();
        mShippingAddressModel.saveShippingAddresses(response.getPayload().getShippingAddresses());
        getEventBus().post(new AddedShippingAddressEvent());
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new AddedShippingAddressEvent(getErrorMessage(), getErrorCode()));
    }
}
