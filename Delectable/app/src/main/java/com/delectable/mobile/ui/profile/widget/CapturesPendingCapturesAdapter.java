package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CapturesPendingCapturesAdapter extends InfiniteScrollAdapter<BaseListingElement> {

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;
    private MinimalPendingCaptureRow.ActionsHandler mPendingCaptureActionsHandler;

    private String mAccountId;

    private static final int PENDING_CAPTURE = 0;

    private static final int CAPTURE_DETAILS = 1;

    private static final int[] VIEWS = {
            PENDING_CAPTURE,
            CAPTURE_DETAILS};


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

        if (getItemViewType(position)==PENDING_CAPTURE) {
            return getPendingCaptureRow(position, convertView, parent);
        }

        return getCaptureDetailRow(position, convertView, parent);
    }

    public View getPendingCaptureRow(int position, View convertView, ViewGroup parent) {

        MinimalPendingCaptureRow rowView = (MinimalPendingCaptureRow) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (MinimalPendingCaptureRow) inflater.inflate(R.layout.row_minimal_pending_capture_impl,
                    parent, false);
            rowView.setActionsHandler(mPendingCaptureActionsHandler);
        }
        PendingCapture capture = (PendingCapture)mItems.get(position);
        rowView.updateData(capture);
        return rowView;
    }

    public View getCaptureDetailRow(int position, View convertView, ViewGroup parent) {

        MinimalCaptureDetailRow rowView = (MinimalCaptureDetailRow) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (MinimalCaptureDetailRow) inflater.inflate(R.layout.row_minimal_capture_detail_impl,
                    parent, false);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        CaptureDetails capture = (CaptureDetails)mItems.get(position);
        rowView.updateData(capture, mAccountId);
        return rowView;
    }


    public void setAccountId(String accountId) {
        mAccountId = accountId;
    }

}
