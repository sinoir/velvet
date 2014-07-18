package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.WineProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Used for the rows in the dialog to filter a base wine by it's vintage.
 */
public class WineProfilesAdapter extends BaseAdapter {


    private ArrayList<WineProfile> mWineProfiles;

    public WineProfilesAdapter(ArrayList<WineProfile> wineProfiles) {
        mWineProfiles = wineProfiles;
    }

    @Override
    public int getCount() {
        return mWineProfiles.size();
    }

    @Override
    public WineProfile getItem(int position) {
        return mWineProfiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChooseVintageDialogRow row = (ChooseVintageDialogRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (ChooseVintageDialogRow) inflater.inflate(R.layout.row_dialog_choose_vintage_with_sizing, parent, false);
        }
        row.updateData(mWineProfiles.get(position));
        return row;
    }
}
