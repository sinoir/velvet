package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.accounts.FetchAccountJob;
import com.delectable.mobile.jobs.accounts.FetchDelectafriendsJob;
import com.delectable.mobile.jobs.accounts.FetchFacebookSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FetchInfluencerSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.jobs.accounts.oldFollowAccountJob;
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

    public void oldFollowAccount(String id, boolean follow) {
        mJobManager.addJobInBackground(new oldFollowAccountJob(id, follow));
    }

    public void followAccount(String id, boolean follow) {
        mJobManager.addJobInBackground(new FollowAccountJob(id, follow));
    }

    public void fetchInfluencerSuggestions() {
        mJobManager.addJobInBackground(new FetchInfluencerSuggestionsJob());
    }
    public void fetchFacebookSuggestions() {
        mJobManager.addJobInBackground(new FetchFacebookSuggestionsJob());
    }

    public void fetchDelectafriends() {
        mJobManager.addJobInBackground(new FetchDelectafriendsJob());
    }

}
