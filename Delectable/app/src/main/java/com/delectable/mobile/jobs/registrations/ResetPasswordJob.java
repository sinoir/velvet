package com.delectable.mobile.jobs.registrations;

import com.delectable.mobile.events.registrations.ResetPasswordEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.registrations.ResetPasswordRequest;
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
        ResetPasswordRequest request = new ResetPasswordRequest(
                new ResetPasswordRequest.Payload(mEmail));
        BaseResponse response = mNetworkClient
                .post(endpoint, request, BaseResponse.class, false);

        mEventBus.post(new ResetPasswordEvent(mEmail, true));

    }

    @Override
    protected void onCancel() {
        mEventBus.post(new ResetPasswordEvent(mEmail, getErrorMessage(), getErrorCode()));
    }

}
