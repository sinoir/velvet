package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetailsListing;

import org.json.JSONObject;

import java.util.ArrayList;

public class AccountsFollowerFeedRequest extends BaseRequest {

    public static final String CONTEXT_DETAILS = "details";

    public static final String CONTEXT_MINIMAL = "minimal";

    String before;

    String after;

    Boolean suppress_before;

    private CaptureDetailsListing mCurrentListing;

    /**
     * Make request to get Follower Capture Feed
     *
     * @param contextType - Must be either Details or Minimal
     */
    public AccountsFollowerFeedRequest(String contextType) {
        this(contextType, null, true);
    }

    /**
     * Makes request for updates / paging
     *
     * @param currentListing  - Current listing that was loaded
     * @param isPullToRefresh - Checks if we're refreshing.  Turns suppress_before on
     */
    public AccountsFollowerFeedRequest(CaptureDetailsListing currentListing,
            boolean isPullToRefresh) {
        this(currentListing.getContext(), currentListing, isPullToRefresh);
    }

    /**
     * Makes request for updates / paging
     *
     * @param contextType     - Must be either Details or Minimal
     * @param currentListing  - Current listing that was loaded
     * @param isPullToRefresh - Checks if we're refreshing.  Turns suppress_before on
     */
    public AccountsFollowerFeedRequest(String contextType, CaptureDetailsListing currentListing,
            boolean isPullToRefresh) {
        mCurrentListing = currentListing;
        context = contextType;
        if (currentListing != null) {
            // TODO: Check invalidate to reset / get new data without etag.
            e_tag = currentListing.getETag();
            context = currentListing.getContext();
            before = currentListing.getBoundariesToBefore();
            after = currentListing.getBoundariesToAfter();
        }
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
        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/follower_feed";
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
