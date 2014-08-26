package com.delectable.mobile.jobs.accounts;

public class FetchFacebookSuggestionsJob extends BaseFetchFriendSuggestionsJob {

    private static final String TAG = FetchFacebookSuggestionsJob.class.getSimpleName();

    @Override
    public String getEndpoint() {
        return "/accounts/facebook_suggestions";
    }
}
