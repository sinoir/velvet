package com.delectable.mobile.api.jobs;

import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.api.net.NetworkClient;
import com.delectable.mobile.api.util.DelException;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.net.UnknownHostException;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BaseJob extends Job {

    private static final int RETRY_LIMIT = 3;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected NetworkClient mNetworkClient;

    private String mErrorMessage;

    private ErrorUtil mErrorCode;

    private String TAG = this.getClass().getSimpleName();

    protected BaseJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
    }

    @Override
    protected void onCancel() {
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        mErrorMessage = throwable.getMessage();
        if (throwable instanceof DelException) {
            int error = ((DelException) throwable).getErrorCode();
            mErrorCode = ErrorUtil.valueOfCode(error);
            return false;
        }
        if (throwable instanceof UnknownHostException) {
            // Unknown Host will most likely mean there's no network / internet available
            mErrorCode = ErrorUtil.NO_NETWORK_ERROR;
            return false;
        }
        return true;
    }

    @Override
    protected int getRetryLimit() {
        return RETRY_LIMIT;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public ErrorUtil getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(ErrorUtil errorCode) {
        mErrorCode = errorCode;
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    public NetworkClient getNetworkClient() {
        return mNetworkClient;
    }

}
