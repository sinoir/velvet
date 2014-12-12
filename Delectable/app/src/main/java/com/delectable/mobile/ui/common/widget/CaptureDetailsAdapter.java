package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CaptureDetailsAdapter extends InfiniteScrollAdapter<CaptureDetails> {

    public enum RowType {
        DETAIL, PURCHASE;
    }

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;

    private String mAccountId;

    private RowType mRowType = RowType.DETAIL;

    public CaptureDetailsAdapter(CaptureDetailsView.CaptureActionsHandler captureActionsHandler,
            ActionsHandler actionsHandler, String accountId) {
        super(actionsHandler);
        mCaptureActionsHandler = captureActionsHandler;
        mAccountId = accountId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        View row;
        switch (mRowType) {
            case PURCHASE:
                row = getPurchaseCaptureRow(position, convertView, parent);
                break;
            case DETAIL:
            default:
                row = getDetailCaptureRow(position, convertView, parent);
                break;
        }
        return row;
    }

    public View getPurchaseCaptureRow(int position, View convertView, ViewGroup parent) {

        CaptureDetailsView rowView = (CaptureDetailsView) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (CaptureDetailsView) inflater.inflate(R.layout.row_feed_wine_detail_impl,
                    parent, false);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        CaptureDetails capture = mItems.get(position);
        rowView.updateData(capture, false, true);
        return rowView;
    }

    public View getDetailCaptureRow(int position, View convertView, ViewGroup parent) {

        CaptureDetailsView rowView = (CaptureDetailsView) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (CaptureDetailsView) inflater.inflate(R.layout.row_feed_wine_detail_impl,
                    parent, false);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        CaptureDetails capture = mItems.get(position);
        rowView.updateData(capture);
        return rowView;
    }

    public void setAccountId(String accountId) {
        mAccountId = accountId;
    }

    public void setRowType(RowType rowType) {
        mRowType = rowType;
    }
}
