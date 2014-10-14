package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.profile.widget.CaptureSimpleItemRow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CaptureDetailsAdapter extends InfiniteScrollAdapter<CaptureDetails> {

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;

    private String mAccountId;

    public CaptureDetailsAdapter(CaptureDetailsView.CaptureActionsHandler captureActionsHandler,
            ActionsHandler actionsHandler, String accountId) {
        super(actionsHandler);
        mCaptureActionsHandler = captureActionsHandler;
        mAccountId = accountId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        View row = getSimpleListingView(position, convertView, parent);
        return row;
    }

    public View getSimpleListingView(int position, View convertView, ViewGroup parent) {

        CaptureSimpleItemRow rowView = (CaptureSimpleItemRow) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (CaptureSimpleItemRow) inflater.inflate(R.layout.row_simple_wine_detail_impl,
                    parent, false);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        CaptureDetails capture = mItems.get(position);
        rowView.updateData(capture, mAccountId);
        return rowView;
    }

    public void setAccountId(String accountId) {
        mAccountId = accountId;
    }
}
