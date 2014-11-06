package com.delectable.mobile.api.models;

import com.delectable.mobile.api.cache.localmodels.CacheListing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Base class for Listings. This was created because the captures_and_pending_captures endpoint
 * returns delete hashes as it's {@link #deletes} array. All other listings return an array of
 * String ids.
 *
 * @param <T> The type that the Listing is for.
 * @param <D> The type for the {@link #deletes} array.
 */
public class Listing<T extends IDable, D> {

    private static final String TAG = Listing.class.getSimpleName();

    protected Boundaries boundaries;

    protected ArrayList<T> before;

    protected ArrayList<T> after;

    protected ArrayList<T> updates;

    protected ArrayList<D> deletes;

    protected boolean more;

    protected String e_tag;

    protected String context;

    public Listing() {

    }

    /**
     * Used to construct a Listing from a CacheListing, for when retrieving from cache.
     */
    public Listing(CacheListing cacheListing, ArrayList<T> updates) {
        boundaries = cacheListing.getBoundaries();
        more = cacheListing.getMore();
        e_tag = cacheListing.getETag();
        this.updates = updates;
    }

    public String getBoundariesFromBefore() {
        if (boundaries != null && boundaries.getFrom() != null) {
            return boundaries.getFrom().getBefore();
        }
        return null;
    }

    public String getBoundariesFromAfter() {
        if (boundaries != null && boundaries.getFrom() != null) {
            return boundaries.getFrom().getAfter();
        }
        return null;
    }

    public String getBoundariesFromSince() {
        if (boundaries != null && boundaries.getFrom() != null) {
            return boundaries.getFrom().getSince();
        }
        return null;
    }

    public String getBoundariesToBefore() {
        if (boundaries != null && boundaries.getTo() != null) {
            return boundaries.getTo().getBefore();
        }
        return null;
    }

    public String getBoundariesToAfter() {
        if (boundaries != null && boundaries.getTo() != null) {
            return boundaries.getTo().getAfter();
        }
        return null;
    }

    public String getBoundariesToSince() {
        if (boundaries != null && boundaries.getTo() != null) {
            return boundaries.getTo().getSince();
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

    public ArrayList<String> getUpdatesIds() {
        ArrayList<String> ids = new ArrayList<String>();
        for (T update : updates) {
            ids.add(update.getId());
        }
        return ids;
    }

    public void setUpdates(ArrayList<T> updates) {
        this.updates = updates;
    }

    public ArrayList<D> getDeletes() {
        return deletes;
    }

    public void setDeletes(ArrayList<D> deletes) {
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
     * Clears the before, after, updates, and deletes lists. Used when saving a Listing to cache, so
     * that we can then in turn set our entire current list as our updates list.
     */
    public void clearLists() {
        before.clear();
        after.clear();
        updates.clear();
        deletes.clear();
    }

    /**
     * Combines the provided items with the items from the response. If invalidate is true, it will
     * dump all the items in the array and add all the updates into the items array.
     */
    public ArrayList<T> combineInto(ArrayList<T> items, boolean invalidate) {

        //initialize array in case user passes in null value
        if (items == null) {
            items = new ArrayList<T>();
        }

        if (invalidate) {
            items.clear();
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

    private void performUpdatesAndDeletes(ArrayList<T> items, ArrayList<D> deletes) {
        //1st pass: construct hashmap of key=id and value=arrayPositions
        HashMap<String, Integer> positionsMap = new HashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            positionsMap.put(item.getId(), i);
        }

        HashSet<Integer> updatedItems = new HashSet<Integer>();

        //perform updates
        for (T update : updates) {
            if (positionsMap.containsKey(update.getId())) {
                Integer position = positionsMap.get(update.getId());
                items.set(position, update);
                updatedItems.add(position);
            }
        }

        //perform deletes: replace items to delete with null
        for (D delete : deletes) {
            String deleteId = getId(delete);
            if (positionsMap.containsKey(deleteId)) {
                Integer position = positionsMap.get(deleteId);
                if (updatedItems.contains(position)) {
                    //if this deleteId was an updated item, do not allow it to be deleted
                    //when a pendingCapture becomes a capture, the pendingCapture id will show up in deletes, and the capture object will show up in updates
                    continue;
                }
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

    /**
     * Allows subclass to provide the object id given a delete object. Most of the time the delete
     * object is just the id string, which can be returned directly in this method. For the
     * captures_and_pending_captures endpoint however, the delete object is a DeleteHash, and logic
     * must be provided to retrieve the delete id from the hash.
     */
    protected String getId(D delete) {
        return delete.toString();
    }

    @Override
    public String toString() {
        return "AbsListing{" +
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
}
