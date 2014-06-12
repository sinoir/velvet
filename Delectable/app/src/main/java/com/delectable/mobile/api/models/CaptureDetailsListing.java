package com.delectable.mobile.api.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CaptureDetailsListing extends ListingResponse {

    ArrayList<CaptureDetails> before;

    ArrayList<CaptureDetails> after;

    ArrayList<CaptureDetails> updates;

    Map<String, CaptureDetails> mAllCombinedDataMap;

    @Override
    public void updateCombinedData() {
        if (mAllCombinedDataMap == null) {
            mAllCombinedDataMap = new HashMap<String, CaptureDetails>();
        }
        if (after != null) {
            addCapturesToDataMap(after);
        }
        if (before != null) {
            addCapturesToDataMap(before);
        }
        if (updates != null) {
            addCapturesToDataMap(updates);
        }
        if (deletes != null && deletes.size() > 0) {
            for (String deleteId : deletes) {
                mAllCombinedDataMap.remove(deleteId);
            }
        }
    }

    private void addCapturesToDataMap(ArrayList<CaptureDetails> captures) {
        for (CaptureDetails capture : captures) {
            mAllCombinedDataMap.put(capture.getId(), capture);
        }
    }

    @Override
    public void combineWithPreviousListing(ListingResponse previousListing) {
        mAllCombinedDataMap = ((CaptureDetailsListing) previousListing).getAllCombinedDataMap();
        updateCombinedData();
    }

    public Map<String, CaptureDetails> getAllCombinedDataMap() {
        return mAllCombinedDataMap;
    }

    public ArrayList<CaptureDetails> getSortedCombinedData() {
        ArrayList<CaptureDetails> sortedList = new ArrayList<CaptureDetails>(
                mAllCombinedDataMap.values());
        Collections.sort(sortedList, CaptureDetails.CreatedAtDescendingComparator);
        return sortedList;
    }

    @Override
    public ArrayList<CaptureDetails> getBefore() {
        return before;
    }

    @Override
    public void setBefore(ArrayList<? extends BaseResponse> before) {
        this.before = (ArrayList<CaptureDetails>) before;
    }

    @Override
    public ArrayList<CaptureDetails> getAfter() {
        return after;
    }

    @Override
    public void setAfter(ArrayList<? extends BaseResponse> after) {
        this.after = (ArrayList<CaptureDetails>) after;
    }

    @Override
    public ArrayList<CaptureDetails> getUpdates() {
        return updates;
    }

    @Override
    public void setUpdates(ArrayList<? extends BaseResponse> updates) {
        this.updates = (ArrayList<CaptureDetails>) updates;
    }

    @Override
    public String toString() {
        return "CaptureDetailsListing{" +
                "before=" + before +
                ", after=" + after +
                ", updates=" + updates +
                "} " + super.toString();
    }
}
