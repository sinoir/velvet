package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.endpointmodels.accounts.ModifyPaymentMethodRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PaymentMethodResponse;
import com.delectable.mobile.api.events.accounts.RemovePaymentMethodEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;

import javax.inject.Inject;

public class RemovePaymentMethodJob extends BaseJob {

    private static final String TAG = RemovePaymentMethodJob.class.getSimpleName();

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    private ArrayList<String> mPaymentMethodIds;

    public RemovePaymentMethodJob(String paymentId) {
        super(new Params(Priority.SYNC));
        mPaymentMethodIds = new ArrayList<String>();
        mPaymentMethodIds.add(paymentId);
    }

    public RemovePaymentMethodJob(ArrayList<String> paymentIds) {
        super(new Params(Priority.SYNC));
        mPaymentMethodIds = paymentIds;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/remove_payment_method";

        PaymentMethodResponse lastResponse = null;
        for (String paymentId : mPaymentMethodIds) {
            ModifyPaymentMethodRequest request = new ModifyPaymentMethodRequest(paymentId);
            lastResponse = getNetworkClient().post(endpoint, request, PaymentMethodResponse.class);
        }
        mPaymentMethodModel.clear();
        mPaymentMethodModel.savePaymentMethods(lastResponse.getPayload().getPaymentMethods());
        getEventBus().post(new RemovePaymentMethodEvent());
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new RemovePaymentMethodEvent(getErrorMessage(), getErrorCode()));
    }
}
