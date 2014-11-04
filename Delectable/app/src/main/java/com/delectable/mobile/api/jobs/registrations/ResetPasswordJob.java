package com.delectable.mobile.api.jobs.registrations;

import com.delectable.mobile.api.endpointmodels.registrations.ResetPasswordRequest;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.events.registrations.ResetPasswordEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.path.android.jobqueue.Params;


public class ResetPasswordJob extends BaseJob {

    private static final String TAG = ResetPasswordJob.class.getSimpleName();

    private String mEmail;

    public ResetPasswordJob(String email) {
        super(new Params(Priority.UX));
        mEmail = email;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/send_reset_password_instructions";
        ResetPasswordRequest request = new ResetPasswordRequest(mEmail);
        BaseResponse response = mNetworkClient
                .post(endpoint, request, BaseResponse.class, false);

        mEventBus.post(new ResetPasswordEvent(mEmail, true));

    }

    @Override
    protected void onCancel() {
        mEventBus.post(new ResetPasswordEvent(mEmail, getErrorMessage(), getErrorCode()));
    }

}
