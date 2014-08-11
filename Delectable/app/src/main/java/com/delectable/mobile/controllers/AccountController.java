package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.accounts.FetchAccountJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;


public class AccountController {

    @Inject
    JobManager mJobManager;

    public void fetchProfile(String id) {
        mJobManager.addJobInBackground(new FetchAccountJob(id, false));
    }

    public void fetchPrivateAccount(String id) {
        mJobManager.addJobInBackground(new FetchAccountJob(id, true));
    }

    public void followAccount(String id, boolean follow) {
        mJobManager.addJobInBackground(new FollowAccountJob(id, follow));
    }

}
