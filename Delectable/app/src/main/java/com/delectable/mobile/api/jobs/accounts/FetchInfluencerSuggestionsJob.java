package com.delectable.mobile.api.jobs.accounts;

public class FetchInfluencerSuggestionsJob extends BaseFetchFriendSuggestionsJob {

    private static final String TAG = FetchInfluencerSuggestionsJob.class.getSimpleName();

    public FetchInfluencerSuggestionsJob(String id) {
        super(id);
    }

    @Override
    public String getEndpoint() {
        return "/accounts/influencer_suggestions";
    }
}
