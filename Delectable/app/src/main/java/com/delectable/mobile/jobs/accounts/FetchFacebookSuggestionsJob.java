package com.delectable.mobile.jobs.accounts;

public class FetchFacebookSuggestionsJob extends BaseFetchFriendSuggestionsJob {

    private static final String TAG = FetchFacebookSuggestionsJob.class.getSimpleName();

    public FetchFacebookSuggestionsJob(String id) {
        super(id);
    }

    @Override
    public String getEndpoint() {
        return "/accounts/facebook_suggestions";
    }
}
