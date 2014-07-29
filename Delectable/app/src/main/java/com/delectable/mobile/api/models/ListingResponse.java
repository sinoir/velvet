package com.delectable.mobile.api.models;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListingResponse<T extends BaseListingElement> extends BaseResponse {

    private static final String TAG = ListingResponse.class.getSimpleName();

    Boolean more;

    boolean invalidate;

    Boundaries boundaries;

    ArrayList<T> before;

    ArrayList<T> after;

    ArrayList<T> updates;

    ArrayList<String> deletes;

    Map<String, T> mAllCombinedDataMap;

    // Class Type is required for json parsing
    public ListingResponse(Type classType) {
        setClassType(classType);
    }

    @Override
    public BaseResponse buildFromJson(JSONObject jsonObj) {
        JSONObject payload = jsonObj.optJSONObject("payload");
        ListingResponse<T> newRegistration = buildFromJson(payload, getClassType());
        if (newRegistration != null) {
            newRegistration.invalidate = jsonObj.optBoolean("invalidate");
        }
        return newRegistration;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public boolean getInvalidate() {
        return invalidate;
    }

    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    public String getBoundariesFromBefore() {
        if (boundaries != null && boundaries.from != null) {
            return boundaries.from.before;
        }
        return null;
    }

    public String getBoundariesFromAfter() {
        if (boundaries != null && boundaries.from != null) {
            return boundaries.from.after;
        }
        return null;
    }

    public String getBoundariesFromSince() {
        if (boundaries != null && boundaries.from != null) {
            return boundaries.from.since;
        }
        return null;
    }

    public String getBoundariesToBefore() {
        if (boundaries != null && boundaries.to != null) {
            return boundaries.to.before;
        }
        return null;
    }

    public String getBoundariesToAfter() {
        if (boundaries != null && boundaries.to != null) {
            return boundaries.to.after;
        }
        return null;
    }

    public String getBoundariesToSince() {
        if (boundaries != null && boundaries.to != null) {
            return boundaries.to.since;
        }
        return null;
    }

    public Boundaries getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(Boundaries boundaries) {
        this.boundaries = boundaries;
    }

    public ArrayList<T> getBefore() {
        return before;
    }

    public void setBefore(ArrayList<T> before) {
        this.before = before;
    }

    public ArrayList<T> getAfter() {
        return after;
    }

    public void setAfter(ArrayList<T> after) {
        this.after = after;
    }

    public ArrayList<T> getUpdates() {
        return updates;
    }

    public void setUpdates(ArrayList<T> updates) {
        this.updates = updates;
    }

    public ArrayList<String> getDeletes() {
        return deletes;
    }

    public void setDeletes(ArrayList<String> deletes) {
        this.deletes = deletes;
    }

    public void updateCombinedData() {
        if (mAllCombinedDataMap == null) {
            mAllCombinedDataMap = new HashMap<String, T>();
        }
        if (after != null) {
            addElementToDataMap(after);
        }
        if (before != null) {
            addElementToDataMap(before);
        }
        if (updates != null) {
            addElementToDataMap(updates);
        }
        if (deletes != null && deletes.size() > 0) {
            for (String deleteId : deletes) {
                mAllCombinedDataMap.remove(deleteId);
            }
        }
    }

    private void addElementToDataMap(ArrayList<T> elements) {
        for (T element : elements) {
            mAllCombinedDataMap.put(element.getId(), element);
        }
    }

    public ArrayList<T> getSortedCombinedData() {
        ArrayList<T> sortedList = new ArrayList<T>(mAllCombinedDataMap.values());
        Collections.sort(sortedList, T.CreatedAtDescendingComparator);
        return sortedList;
    }

    public void combineWithPreviousListing(ListingResponse previousListing) {
        mAllCombinedDataMap = previousListing.getAllCombinedDataMap();
        updateCombinedData();
    }

    public Map<String, T> getAllCombinedDataMap() {
        return mAllCombinedDataMap;
    }

    @Override
    public String toString() {
        return "ListingResponse{" +
                "more=" + more +
                ", boundaries=" + boundaries +
                ", deletes=" + deletes +
                "} " + super.toString();
    }

    public static class Boundaries {

        Boundary from;

        Boundary to;

        @Override
        public String toString() {
            return "Boundaries{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

    public static class Boundary {

        String before;

        String after;

        String since;

        @Override
        public String toString() {
            return "Boundary{" +
                    "before='" + before + '\'' +
                    ", after='" + after + '\'' +
                    ", since=" + since +
                    '}';
        }
    }
}
