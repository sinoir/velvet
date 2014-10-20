package com.delectable.mobile.api.jobs;

import com.delectable.mobile.App;
import com.path.android.jobqueue.BaseJob;
import com.path.android.jobqueue.BuildConfig;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.log.CustomLogger;

import android.content.Context;
import android.util.Log;

public class MyJobManager extends JobManager {

    public MyJobManager(Context context) {
        super(context, new Configuration.Builder(context)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(BaseJob job) {
                        App.injectMembers(job);
                    }
                }).customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return BuildConfig.DEBUG;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .build());
    }

}
