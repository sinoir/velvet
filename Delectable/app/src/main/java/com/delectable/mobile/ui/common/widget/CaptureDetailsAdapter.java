package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CaptureDetailsAdapter extends InfiniteScrollAdapter<CaptureDetails> {

    public enum RowType {
        SOCIAL, COMMERCIAL;
    }

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;

    private RowType mRowType = RowType.SOCIAL;

    public CaptureDetailsAdapter(CaptureDetailsView.CaptureActionsHandler captureActionsHandler,
            ActionsHandler actionsHandler) {
        super(actionsHandler);
        mCaptureActionsHandler = captureActionsHandler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // super updates position for infinite scroll adapter
        super.getView(position, convertView, parent);
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
        switch (rowType) {
            case SOCIAL:
                captureDetailsRow.updateData(capture);
                break;
            case COMMERCIAL:
                captureDetailsRow.updateData(capture, false, true);
                break;
        }
        return rowView;
    }

    public void setRowType(RowType rowType) {
        mRowType = rowType;
    }
}
