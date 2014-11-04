package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.accounts.AddPaymentMethodRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PaymentMethodResponse;
import com.delectable.mobile.api.events.accounts.AddedPaymentMethodEvent;
import com.delectable.mobile.api.events.accounts.FetchedPaymentMethodsEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.PaymentMethod;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchPaymentMethodJob extends BaseJob {

    private static final String TAG = FetchPaymentMethodJob.class.getSimpleName();

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    public FetchPaymentMethodJob() {
        super(new Params(Priority.SYNC));
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/payment_methods";
        BaseRequest request = new BaseRequest();
        PaymentMethodResponse response = getNetworkClient()
                .post(endpoint, request, PaymentMethodResponse.class);
        mPaymentMethodModel.clear();
        mPaymentMethodModel.savePaymentMethods(response.getPayload().getPaymentMethods());
        getEventBus().post(new FetchedPaymentMethodsEvent());
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedPaymentMethodsEvent(getErrorMessage(), getErrorCode()));
    }
}
