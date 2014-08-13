package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.registrations.LoginFacebookJob;
import com.delectable.mobile.jobs.registrations.LoginJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;


public class RegistrationController {

    @Inject
    JobManager mJobManager;

    public void login(String email, String password) {
        mJobManager.addJobInBackground(new LoginJob(email, password));
    }

    public void facebookLogin(String facebookToken, double facebookTokenExpiration) {
        mJobManager
                .addJobInBackground(new LoginFacebookJob(facebookToken, facebookTokenExpiration));
    }
}
