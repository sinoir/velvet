package com.delectable.mobile.util;


import android.os.AsyncTask;

import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.LifecycleListener;

abstract public class SafeAsyncTask<Result> extends AsyncTask<Void, Void, Result> implements LifecycleListener {

    private BaseFragment lifecycleProvider;
    private Throwable runException;

    protected SafeAsyncTask(BaseFragment context) {
        lifecycleProvider = context;
        context.registerLifecycleListener(this);
    }

    @Override
    final protected Result doInBackground(Void... params) {
        try {
            return safeDoInBackground(params);
        } catch (Throwable t) {
            runException = t;
        }
        return null;
    }

    protected abstract Result safeDoInBackground(Void[] params);

    protected abstract void safeOnPostExecute(Result result);

    @Override
    final protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        final BaseFragment fragment = lifecycleProvider;
        cleanup();
        if (runException == null) {
            safeOnPostExecute(result);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        cleanup();
    }

    @Override
    protected void onCancelled(Result result) {
        super.onCancelled(result);
        cleanup();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        cancel(false);
        cleanup();
    }

    private void cleanup() {
        if (lifecycleProvider != null) {
            lifecycleProvider.unregister(this);
            lifecycleProvider = null;
        }
    }

}