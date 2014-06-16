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

    private CaptureDetailsListing mCurrentListing;

    /**
     * Make request to get Follower Capture Feed
     *
     * @param contextType - Must be either Details or Minimal
     */
    public AccountsFollowerFeedRequest(String contextType) {
        context = contextType;
    }

    /**
     * Makes request for updates / paging
     *
     * @param currentListing - Current listing that was loaded
     * @param isPullToRefresh - Checks if we're refreshing.  Turns supress_before on
     */
    public AccountsFollowerFeedRequest(CaptureDetailsListing currentListing, boolean isPullToRefresh) {
        // TODO: Check invalidate to reset / get new data without etag.
        mCurrentListing = currentListing;
        e_tag = currentListing.getETag();
        context = currentListing.getContext();
        before = currentListing.getBoundariesToBefore();
        after = currentListing.getBoundariesToAfter();

        // TODO: Add supress_before when pulling to refresh
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

}
