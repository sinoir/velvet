package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.endpointmodels.accounts.ModifyPaymentMethodRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PaymentMethodResponse;
import com.delectable.mobile.api.events.accounts.RemovePaymentMethodEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class RemovePaymentMethodJob extends BaseJob {

    private static final String TAG = RemovePaymentMethodJob.class.getSimpleName();

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    private String mPaymentId;

    public RemovePaymentMethodJob(String paymentId) {
        super(new Params(Priority.SYNC));
        mPaymentId = paymentId;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/remove_payment_method";
        ModifyPaymentMethodRequest request = new ModifyPaymentMethodRequest(mPaymentId);
        PaymentMethodResponse response = getNetworkClient()
                .post(endpoint, request, PaymentMethodResponse.class);
        mPaymentMethodModel.clear();
        mPaymentMethodModel.savePaymentMethods(response.getPayload().getPaymentMethods());
        getEventBus().post(new RemovePaymentMethodEvent(mPaymentId));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new RemovePaymentMethodEvent(getErrorMessage(), getErrorCode(),
                mPaymentId));
    }
}
