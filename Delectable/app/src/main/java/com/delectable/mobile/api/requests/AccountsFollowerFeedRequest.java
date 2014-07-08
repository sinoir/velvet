package com.delectable.mobile.api.requests;

public class AccountsFollowerFeedRequest extends BaseCaptureFeedListingRequest {

    public AccountsFollowerFeedRequest(String contextType) {
        super(contextType);
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/follower_feed";
    }
}
