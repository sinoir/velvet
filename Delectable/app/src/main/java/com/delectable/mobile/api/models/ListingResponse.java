package com.delectable.mobile.api.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListingResponse<T extends BaseListingElement> {

    private static final String TAG = ListingResponse.class.getSimpleName();

    private Boundaries boundaries;

    private ArrayList<T> before;

    private ArrayList<T> after;

    private ArrayList<T> updates;

    private ArrayList<String> deletes;

    private boolean more;

    private String e_tag;

    private String context;

    private transient Map<String, T> mAllCombinedDataMap;


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

    public boolean getMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
        if (mAllCombinedDataMap == null) {
            updateCombinedData();
        }
        ArrayList<T> sortedList = new ArrayList<T>(mAllCombinedDataMap.values());
        Collections.sort(sortedList, T.CreatedAtDescendingComparator);
        return sortedList;
    }

    public void combineWithPreviousListing(ListingResponse previousListing) {
        previousListing.updateCombinedData();
        mAllCombinedDataMap = previousListing.getAllCombinedDataMap();
        updateCombinedData();
    }

    public List<String> getAllIds() {
        updateCombinedData();
        ArrayList<String> ids = new ArrayList<String>();
        ids.addAll(getAllCombinedDataMap().keySet());
        return ids;
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
