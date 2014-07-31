package com.delectable.mobile.api.requests;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.ListingResponse;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActivityFeedRequest extends BaseRequest {

    String before;

    String after;

    private ListingResponse<ActivityRecipient> mCurrentListing;

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
        return API_VER + "/accounts/activity_feed";
    }

    @Override
    public ListingResponse<ActivityRecipient> buildResopnseFromJson(JSONObject jsonObject) {
        Type classType = new TypeToken<ListingResponse<ActivityRecipient>>() {
        }.getType();
        ListingResponse<ActivityRecipient> resForParsing = new ListingResponse<ActivityRecipient>(
                classType);
        ListingResponse<ActivityRecipient> parsedListing = resForParsing.buildFromJson(jsonObject);
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

    /**
     * Current Listing that was already loaded, used for updating existing list
     */
    public void setCurrentListing(ListingResponse<ActivityRecipient> currentListing) {
        mCurrentListing = currentListing;
        if (currentListing != null) {
            // TODO: Check invalidate to reset / get new data without etag.
            e_tag = currentListing.getETag();
            context = currentListing.getContext();
            before = currentListing.getBoundariesToBefore();
            after = currentListing.getBoundariesToAfter();
        }
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

    @Override
    public String toString() {
        return "ActivityFeedRequest{" +
                "before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", mCurrentListing=" + mCurrentListing +
                '}';
    }
}
