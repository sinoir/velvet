package com.delectable.mobile.jobs;

import android.content.Context;

import com.delectable.mobile.App;
import com.path.android.jobqueue.BaseJob;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;

public class MyJobManager extends JobManager {

    public MyJobManager(Context context) {
        super(context, new Configuration.Builder(context)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(BaseJob job) {
                        App.injectMembers(job);
                    }
                }).build());
    }

}
