package com.delectable.mobile.api.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseListingResponse<T extends IDable> {

    private static final String TAG = BaseListingResponse.class.getSimpleName();

    protected Boundaries boundaries;

    protected ArrayList<T> before;

    protected ArrayList<T> after;

    protected ArrayList<T> updates;

    protected ArrayList<String> deletes;

    protected boolean more;

    protected String e_tag;

    protected String context;

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

    /**
     * Combines the provided items with the items from the response.
     */
    public ArrayList<T> combineInto(ArrayList<T> items) {

        //if the original array is empty, and before and after are also empty
        //we know that we are starting from scratch
        if (items.size() == 0 && before.size() == 0 && after.size() == 0) {
            items.addAll(updates);
            return items;
        }

        performUpdatesAndDeletes(items, deletes);

        //add afters, added to beginning of array bc most the recent events are at beginning of the array
        items.addAll(0, after);

        //add before, add to end of array bc events further back in time are at the end of the array
        items.addAll(before);

        return items;
    }

    private void performUpdatesAndDeletes(ArrayList<T> items, ArrayList<String> deleteIds) {

        //1st pass: construct hashmap of key=id and value=arrayPositions
        HashMap<String, Integer> positionsMap = new HashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            positionsMap.put(item.getId(), i);
        }

        //perform updates
        for (T update : updates) {
            if (positionsMap.containsKey(update.getId())) {
                Integer position = positionsMap.get(update.getId());
                items.set(position, update);
            }
        }

        //perform deletes: replace items to delete with null
        for (String deleteId : deleteIds) {
            if (positionsMap.containsKey(deleteId)) {
                Integer position = positionsMap.get(deleteId);
                items.set(position, null);
            }
        }
        //2nd pass: complete deletes: strips nulls
        cleanNulls(items);
    }

    private static <T> void cleanNulls(final List<T> list) {
        int pFrom = 0;
        int pTo = 0;
        final int len = list.size();
        //copy all not-null elements towards list head
        while (pFrom < len) {
            if (list.get(pFrom) != null) {
                list.set(pTo++, list.get(pFrom));
            }
            ++pFrom;
        }
        //there are some elements left in the tail - clean them
        list.subList(pTo, len).clear();
    }


    @Override
    public String toString() {
        return "BaseListingResponse{" +
                "boundaries=" + boundaries +
                ", before=" + before +
                ", after=" + after +
                ", updates=" + updates +
                ", deletes=" + deletes +
                ", more=" + more +
                ", e_tag='" + e_tag + '\'' +
                ", context='" + context + '\'' +
                '}';
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
