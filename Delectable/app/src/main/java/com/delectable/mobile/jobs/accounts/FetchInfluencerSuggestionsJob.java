package com.delectable.mobile.jobs.accounts;

public class FetchInfluencerSuggestionsJob extends BaseFetchFriendSuggestionsJob {

    private static final String TAG = FetchInfluencerSuggestionsJob.class.getSimpleName();

    @Override
    public String getEndpoint() {
        return "/accounts/influencer_suggestions";
    }
}
