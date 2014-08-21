package com.delectable.mobile.jobs;

import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BaseJob extends Job {

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected NetworkClient mNetworkClient;

    private String mErrorMessage;

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
        // TODO check error type and see if a retry makes sense
        mErrorMessage = throwable.getMessage();
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    public NetworkClient getNetworkClient() {
        return mNetworkClient;
    }
}
