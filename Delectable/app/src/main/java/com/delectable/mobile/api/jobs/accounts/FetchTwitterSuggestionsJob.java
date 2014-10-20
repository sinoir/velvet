package com.delectable.mobile.api.jobs.accounts;

public class FetchTwitterSuggestionsJob extends BaseFetchFriendSuggestionsJob {

    private static final String TAG = FetchTwitterSuggestionsJob.class.getSimpleName();

    public FetchTwitterSuggestionsJob(String id) {
        super(id);
    }

    @Override
    public String getEndpoint() {
        return "/accounts/twitter_suggestions";
    }
}
