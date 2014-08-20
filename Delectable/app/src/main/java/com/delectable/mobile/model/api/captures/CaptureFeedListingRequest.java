package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.BaseRequest;

public class CaptureFeedListingRequest extends BaseRequest {

    protected static final String CONTEXT_DETAILS = "details";

    CaptureFeedListingPayload payload;

    /**
     * Make request to get Follower Capture Feed
     */
    public CaptureFeedListingRequest() {
        super(CONTEXT_DETAILS);
        payload = new CaptureFeedListingPayload();
    }

    /**
     * Current Listing that was already loaded, used for updating existing list
     */
    public void setCurrentListing(ListingResponse<CaptureDetails> currentListing) {
        if (currentListing != null) {
            // TODO: Check invalidate to reset / get new data without etag.
            setETag(currentListing.getETag());
            payload.before = currentListing.getBoundariesToBefore();
            payload.after = currentListing.getBoundariesToAfter();
        }
    }

    /**
     * Wrapper for readability / understandability
     */
    public void setIsPullToRefresh(boolean isPullToRefresh) {
        payload.suppress_before = isPullToRefresh;
    }

    public String getBefore() {
        return payload.before;
    }

    public void setBefore(String before) {
        payload.before = before;
    }

    public String getAfter() {
        return payload.after;
    }

    public void setAfter(String after) {
        payload.after = after;
    }

    public Boolean getSuppressBefore() {
        return payload.suppress_before;
    }

    public void setSuppressBefore(Boolean suppress_before) {
        payload.suppress_before = suppress_before;
    }

    public String getId() {
        return payload.id;
    }

    public void setId(String id) {
        payload.id = id;
    }


    public static class CaptureFeedListingPayload {

        String id;

        String before;

        String after;

        Boolean suppress_before;
    }
}
