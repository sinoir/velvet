package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetailsListing;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract class BaseCaptureFeedListingRequest extends BaseRequest {

    public static final String CONTEXT_DETAILS = "details";

    public static final String CONTEXT_MINIMAL = "minimal";

    String id;

    String before;

    String after;

    Boolean suppress_before;

    private CaptureDetailsListing mCurrentListing;

    /**
     * Make request to get Follower Capture Feed
     *
     * @param contextType - Must be either Details or Minimal
     */
    public BaseCaptureFeedListingRequest(String contextType) {
        context = contextType;
    }

    /**
     * Current Listing that was already loaded, used for updating existing list
     */
    public void setCurrentListing(CaptureDetailsListing currentListing) {
        mCurrentListing = currentListing;
        if (currentListing != null) {
            // TODO: Check invalidate to reset / get new data without etag.
            e_tag = currentListing.getETag();
            context = currentListing.getContext();
            before = currentListing.getBoundariesToBefore();
            after = currentListing.getBoundariesToAfter();
        }
    }

    /**
     * Wrapper for readability / understandability
     */
    public void setIsPullToRefresh(boolean isPullToRefresh) {
        suppress_before = isPullToRefresh;
    }

    @Override
    public String[] getPayloadFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();
        if (before != null) {
            fieldsList.add("before");
        }
        if (after != null) {
            fieldsList.add("after");
        }
        if (suppress_before != null && suppress_before) {
            fieldsList.add("suppress_before");
        }
        if (id != null) {
            fieldsList.add("id");
        }
        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        CaptureDetailsListing resForParsing = new CaptureDetailsListing();
        CaptureDetailsListing parsedListing = (CaptureDetailsListing) resForParsing
                .buildFromJson(jsonObject);
        // When combining data for suppressed before requests, it may be null:
        parsedListing = parsedListing != null ? parsedListing : mCurrentListing;
        if (parsedListing != null) {
            if (mCurrentListing != null) {
                parsedListing.combineWithPreviousListing(mCurrentListing);
                parsedListing.setETag(mCurrentListing.getETag());
            } else {
                parsedListing.updateCombinedData();
            }
        }
        return parsedListing;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Boolean getSuppressBefore() {
        return suppress_before;
    }

    public void setSuppressBefore(Boolean suppress_before) {
        this.suppress_before = suppress_before;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccountsFollowerFeedRequest{" +
                "before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", suppress_before=" + suppress_before +
                ", mCurrentListing=" + mCurrentListing +
                "} " + super.toString();
    }

}
