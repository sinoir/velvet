package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.endpointmodels.accounts.UpdateShippingAddressRequest;
import com.delectable.mobile.api.events.accounts.UpdatedShippingAddressEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.ShippingAddress;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class UpdateShippingAddressJob extends BaseJob {

    private static final String TAG = UpdateShippingAddressJob.class.getSimpleName();

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    private ShippingAddress mAddress;

    private boolean mIsPrimary;

    public UpdateShippingAddressJob(ShippingAddress address, boolean isPrimary) {
        super(new Params(Priority.SYNC));
        mAddress = address;
        mIsPrimary = isPrimary;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_shipping_address";
        UpdateShippingAddressRequest request = new UpdateShippingAddressRequest(mAddress,
                mIsPrimary);
        ShippingAddressesResponse response = getNetworkClient().post(endpoint, request,
                ShippingAddressesResponse.class);
        mShippingAddressModel.clear();
        mShippingAddressModel.saveShippingAddresses(response.getPayload().getShippingAddresses());
        getEventBus().post(new UpdatedShippingAddressEvent(mAddress.getId()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedShippingAddressEvent(getErrorMessage(), getErrorCode(),
                mAddress.getId()));
    }
}
