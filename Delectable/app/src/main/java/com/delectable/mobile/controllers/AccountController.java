package com.delectable.mobile.controllers;

import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.jobs.accounts.AddIdentifierJob;
import com.delectable.mobile.jobs.accounts.AssociateFacebookJob;
import com.delectable.mobile.jobs.accounts.FacebookifyProfilePhotoJob;
import com.delectable.mobile.jobs.accounts.FetchAccountJob;
import com.delectable.mobile.jobs.accounts.FetchAccountsFromContactsJob;
import com.delectable.mobile.jobs.accounts.FetchDelectafriendsJob;
import com.delectable.mobile.jobs.accounts.FetchFacebookSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FetchInfluencerSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.jobs.accounts.RemoveIdentifierJob;
import com.delectable.mobile.jobs.accounts.SearchAccountsJob;
import com.delectable.mobile.jobs.accounts.UpdateIdentifierJob;
import com.delectable.mobile.jobs.accounts.UpdateProfileJob;
import com.delectable.mobile.jobs.accounts.UpdateProfilePhotoJob;
import com.delectable.mobile.jobs.accounts.UpdateSettingJob;
import com.delectable.mobile.jobs.accounts.oldFollowAccountJob;
import com.delectable.mobile.jobs.basewines.SearchWinesJob;
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

    public void fetchInfluencerSuggestions(String id) {
        mJobManager.addJobInBackground(new FetchInfluencerSuggestionsJob(id));
    }

    public void fetchFacebookSuggestions(String id) {
        mJobManager.addJobInBackground(new FetchFacebookSuggestionsJob(id));
    }

    public void fetchDelectafriends() {
        mJobManager.addJobInBackground(new FetchDelectafriendsJob());
    }

    public void fetchAccountsFromContacts() {
        mJobManager.addJobInBackground(new FetchAccountsFromContactsJob());
    }

    public void searchAccounts(String query, int offset, int limit) {
        mJobManager.addJobInBackground(new SearchAccountsJob(query, offset, limit));
    }

    //region Settings Screen
    public void facebookifyProfilePhoto() {
        mJobManager.addJobInBackground(new FacebookifyProfilePhotoJob());
    }

    public void updateProfilePhoto(byte[] imageData) {
        mJobManager.addJobInBackground(new UpdateProfilePhotoJob(imageData));
    }

    public void updateProfile(String fname, String lname, String url, String bio) {
        mJobManager.addJobInBackground(new UpdateProfileJob(fname, lname, url, bio));
    }

    public void updateSetting(AccountConfig.Key key, boolean setting) {
        mJobManager.addJobInBackground(new UpdateSettingJob(key, setting));
    }

    public void addIdentifier(String string, String type) {
        mJobManager.addJobInBackground(new AddIdentifierJob(string, type, null));
    }

    public void updateIdentifier(Identifier identifier, String string) {
        mJobManager.addJobInBackground(new UpdateIdentifierJob(identifier, string, null));
    }

    public void removeIdentifier(Identifier identifier) {
        mJobManager.addJobInBackground(new RemoveIdentifierJob(identifier));
    }

    public void associateFacebook(String facebookToken, double facebookTokenExpiration) {
        mJobManager.addJobInBackground(new AssociateFacebookJob(facebookToken, facebookTokenExpiration));
    }
    //endregion

}
