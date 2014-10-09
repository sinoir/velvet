package com.delectable.mobile.api.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListingResponse<T extends BaseListingElement> extends BaseListingResponse<T>{

    private static final String TAG = ListingResponse.class.getSimpleName();

    private transient Map<String, T> mAllCombinedDataMap;

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
                "boundaries=" + boundaries +
                ", before=" + before +
                ", after=" + after +
                ", updates=" + updates +
                ", deletes=" + deletes +
                ", more=" + more +
                ", e_tag='" + e_tag + '\'' +
                ", context='" + context + '\'' +
                ", mAllCombinedDataMap=" + mAllCombinedDataMap +
                '}';
    }
}
