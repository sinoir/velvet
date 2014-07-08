package com.delectable.mobile.api.requests;

public class CaptureFeedRequest extends BaseCaptureFeedListingRequest {

    public CaptureFeedRequest(String contextType) {
        super(contextType);
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/captures";
    }
}
