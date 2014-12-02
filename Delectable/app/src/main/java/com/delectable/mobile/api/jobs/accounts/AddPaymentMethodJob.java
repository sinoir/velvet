package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.endpointmodels.accounts.AddPaymentMethodRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PaymentMethodResponse;
import com.delectable.mobile.api.events.accounts.AddedPaymentMethodEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.PaymentMethod;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class AddPaymentMethodJob extends BaseJob {

    private static final String TAG = AddPaymentMethodJob.class.getSimpleName();

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    private boolean mIsPrimary;

    private PaymentMethod mPaymentMethod;

    public AddPaymentMethodJob(PaymentMethod paymentMethod, boolean isPrimary) {
        super(new Params(Priority.SYNC.value()));
        mIsPrimary = isPrimary;
        mPaymentMethod = paymentMethod;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/add_payment_method";
        AddPaymentMethodRequest request = new AddPaymentMethodRequest(mPaymentMethod, mIsPrimary);
        PaymentMethodResponse response = getNetworkClient()
                .post(endpoint, request, PaymentMethodResponse.class);
        mPaymentMethodModel.clear();
        mPaymentMethodModel.savePaymentMethods(response.getPayload().getPaymentMethods());
        getEventBus().post(new AddedPaymentMethodEvent());
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new AddedPaymentMethodEvent(getErrorMessage(), getErrorCode()));
    }
}
