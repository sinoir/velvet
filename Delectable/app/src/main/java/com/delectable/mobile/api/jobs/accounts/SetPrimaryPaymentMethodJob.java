package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.endpointmodels.accounts.ModifyPaymentMethodRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PaymentMethodResponse;
import com.delectable.mobile.api.events.accounts.SetPrimaryPaymentMethodEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class SetPrimaryPaymentMethodJob extends BaseJob {

    private static final String TAG = SetPrimaryPaymentMethodJob.class.getSimpleName();

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    private String mPaymentId;

    public SetPrimaryPaymentMethodJob(String paymentId) {
        super(new Params(Priority.SYNC));
        mPaymentId = paymentId;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/set_primary_payment_method";
        ModifyPaymentMethodRequest request = new ModifyPaymentMethodRequest(mPaymentId);
        PaymentMethodResponse response = getNetworkClient()
                .post(endpoint, request, PaymentMethodResponse.class);
        mPaymentMethodModel.clear();
        mPaymentMethodModel.savePaymentMethods(response.getPayload().getPaymentMethods());
        getEventBus().post(new SetPrimaryPaymentMethodEvent(mPaymentId));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new SetPrimaryPaymentMethodEvent(getErrorMessage(), getErrorCode(),
                mPaymentId));
    }
}
