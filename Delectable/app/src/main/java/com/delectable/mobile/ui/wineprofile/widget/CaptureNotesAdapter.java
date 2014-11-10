package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CaptureNotesAdapter extends InfiniteScrollAdapter<CaptureNote> {

    private WineProfileCommentUnitRow.ActionsHandler mActionsHandler;

    public CaptureNotesAdapter(InfiniteScrollAdapter.ActionsHandler scrollHandler,
            WineProfileCommentUnitRow.ActionsHandler actionsHandler) {
        super(scrollHandler);
        mActionsHandler = actionsHandler;
    }

    @Override
    public CaptureNote getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        WineProfileCommentUnitRow row = (WineProfileCommentUnitRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (WineProfileCommentUnitRow) inflater
                    .inflate(R.layout.row_wine_profile_comment_unit_impl, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData(getItem(position));
        return row;
    }

}
