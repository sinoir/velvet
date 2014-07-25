package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.api.models.CaptureNote;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CaptureNotesAdapter extends BaseAdapter {

    private ArrayList<CaptureNote> mCaptureNotes;

    private WineProfileCommentUnitRow.ActionsHandler mActionsHandler;

    public CaptureNotesAdapter(ArrayList<CaptureNote> captureNotes, WineProfileCommentUnitRow.ActionsHandler actionsHandler) {
        mCaptureNotes = captureNotes;
        mActionsHandler = actionsHandler;
    }

    @Override
    public int getCount() {
        return mCaptureNotes.size();
    }

    @Override
    public CaptureNote getItem(int position) {
        return mCaptureNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WineProfileCommentUnitRow row = (WineProfileCommentUnitRow) convertView;
        if (row == null) {
            row = new WineProfileCommentUnitRow(parent.getContext());
            row.setActionsHandler(mActionsHandler);
        }
        row.updateCaptureNoteData(mCaptureNotes.get(position));
        return row;
    }
}