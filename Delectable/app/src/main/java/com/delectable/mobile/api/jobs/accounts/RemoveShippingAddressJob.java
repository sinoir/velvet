package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.accounts.ModifyShippingAddressRequest;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.events.accounts.RemovedShippingAddressEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;

import javax.inject.Inject;

public class RemoveShippingAddressJob extends BaseJob {

    private static final String TAG = RemoveShippingAddressJob.class.getSimpleName();

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    private ArrayList<String> mAddressIds;

    public RemoveShippingAddressJob(String addressId) {
        super(new Params(Priority.SYNC));
        mAddressIds = new ArrayList<String>();
        mAddressIds.add(addressId);
    }

    public RemoveShippingAddressJob(ArrayList<String> addressIds) {
        super(new Params(Priority.SYNC));
        mAddressIds = addressIds;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/remove_shipping_address";
        ShippingAddressesResponse latestResponse = null;
        for (String addressId : mAddressIds) {
            ModifyShippingAddressRequest request = new ModifyShippingAddressRequest(addressId);
            latestResponse = getNetworkClient().post(endpoint, request,
                    ShippingAddressesResponse.class);
        }
        mShippingAddressModel.clear();
        mShippingAddressModel.saveShippingAddresses(
                latestResponse.getPayload().getShippingAddresses());
        getEventBus().post(new RemovedShippingAddressEvent());
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new RemovedShippingAddressEvent(getErrorMessage(), getErrorCode()));
    }
}
