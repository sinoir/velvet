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
        return getCaptureRow(position, convertView, parent, mRowType);
    }

    private View getCaptureRow(int position, View convertView, ViewGroup parent, RowType rowType) {

        View rowView = convertView;
        CaptureDetailsView captureDetailsRow = null;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.row_feed_card,
                    parent, false);
            captureDetailsRow = (CaptureDetailsView) rowView
                    .findViewById(R.id.capture_details_view);
            captureDetailsRow.setActionsHandler(mCaptureActionsHandler);
        }
        if (captureDetailsRow == null) {
            captureDetailsRow = (CaptureDetailsView) rowView
                    .findViewById(R.id.capture_details_view);
        }
        CaptureDetails capture = mItems.get(position);
        if (RowType.DETAIL == rowType) {
            captureDetailsRow.updateData(capture);
        } else if (RowType.PURCHASE == rowType) {
            captureDetailsRow.updateData(capture, false, true);
        }
        return rowView;
    }

    public void setAccountId(String accountId) {
        mAccountId = accountId;
    }

    public void setRowType(RowType rowType) {
        mRowType = rowType;
    }
}
