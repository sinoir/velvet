package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.models.TransitionState;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CapturesPendingCapturesAdapter extends InfiniteScrollAdapter<BaseListingElement> {

    public enum Type {
        ALL,
        WITHOUT_DELETING;
    }

    private static final int NOT_FOUND = -1;

    private static final int PENDING_CAPTURE = 0;

    private static final int CAPTURE_DETAILS = 1;

    private static final int[] VIEWS = {
            PENDING_CAPTURE,
            CAPTURE_DETAILS
    };

    private Type mType = Type.ALL;

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;

    private MinimalPendingCaptureRow.ActionsHandler mPendingCaptureActionsHandler;

    private String mAccountId;

    /**
     * Used to hold onto reference of original array of items, before any filtering is applied
     */
    private ArrayList<BaseListingElement> mOriginalItems = new ArrayList<BaseListingElement>();


    public CapturesPendingCapturesAdapter(
            CaptureDetailsView.CaptureActionsHandler captureActionsHandler,
            MinimalPendingCaptureRow.ActionsHandler pendingCaptureActionsHandler,
            ActionsHandler actionsHandler, String accountId) {
        super(actionsHandler);
        mCaptureActionsHandler = captureActionsHandler;
        mPendingCaptureActionsHandler = pendingCaptureActionsHandler;
        mAccountId = accountId;
    }

    @Override
    public BaseListingElement getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        BaseListingElement item = getItem(position);
        if (item instanceof PendingCapture) {
            return PENDING_CAPTURE;
        }

        //default to show capture details view
        return CAPTURE_DETAILS;
    }

    @Override
    public int getViewTypeCount() {
        return VIEWS.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);

        if (getItemViewType(position) == PENDING_CAPTURE) {
            return getPendingCaptureRow(position, convertView, parent);
        }

        return getCaptureDetailRow(position, convertView, parent);
    }

    public View getPendingCaptureRow(int position, View convertView, ViewGroup parent) {

        MinimalPendingCaptureRow rowView = (MinimalPendingCaptureRow) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (MinimalPendingCaptureRow) inflater
                    .inflate(R.layout.row_minimal_pending_capture_impl,
                            parent, false);
            rowView.setActionsHandler(mPendingCaptureActionsHandler);
        }
        PendingCapture capture = (PendingCapture) mItems.get(position);
        rowView.updateData(capture);
        return rowView;
    }

    public View getCaptureDetailRow(int position, View convertView, ViewGroup parent) {

        MinimalCaptureDetailRow rowView = (MinimalCaptureDetailRow) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (MinimalCaptureDetailRow) inflater
                    .inflate(R.layout.row_minimal_capture_detail_impl,
                            parent, false);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        CaptureDetails capture = (CaptureDetails) mItems.get(position);
        rowView.updateData(capture, mAccountId);
        return rowView;
    }


    public void setAccountId(String accountId) {
        mAccountId = accountId;
    }

    @Override
    public void setItems(ArrayList<BaseListingElement> items) {
        mOriginalItems = items;
        super.setItems(items);
    }

    public void setType(Type type) {
        mType = type;
        if (mType == Type.ALL) {
            //set to original array
            super.setItems(mOriginalItems);
            return;
        }
        if (mType == Type.WITHOUT_DELETING) {
            //make filtered array without items that are in deleting state
            ArrayList<BaseListingElement> items = new ArrayList<BaseListingElement>();
            for (BaseListingElement item : mOriginalItems) {
                if (item.getTransitionState() == TransitionState.DELETING) {
                    continue;
                }
                items.add(item);
            }
            super.setItems(mOriginalItems);
        }
    }

    /**
     * To be called upon a delete pending capture event success. Removes the data from the list that
     * the UI is displaying. We need to finagle with the data referenced by the UI because the the API simply
     * returns a success boolean, and not your entire list of captures again.
     */
    public void removeItem(String captureId) {

        //first find item to delete, if it exists
        int position = NOT_FOUND;
        for (int i = 0; i < mOriginalItems.size(); i++) {
            BaseListingElement item = mOriginalItems.get(i);
            if (item.getId().equals(captureId)) {
                position = i;
                break;
            }
        }

        //and then remove it from the original list
        if (position != NOT_FOUND) {
            mOriginalItems.remove(position);
        }
    }
}
