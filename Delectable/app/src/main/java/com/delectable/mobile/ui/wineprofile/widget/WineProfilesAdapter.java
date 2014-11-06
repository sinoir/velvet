package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.WineProfileSubProfile;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Used for the rows in the dialog to filter a base wine by it's vintage. The first row shows all
 * years, and is backed by a {@link BaseWine} object. The rest of the rows show the vintages, which
 * are backed by {@link WineProfileSubProfile} objects.
 */
public class WineProfilesAdapter extends BaseAdapter {

    private ArrayList<VintageWineInfo> mVintageWineInfos = new ArrayList<VintageWineInfo>();

    private WinePriceView.WinePriceViewActionsCallback mWinePriceViewActionsCallback;

    public WineProfilesAdapter(
            WinePriceView.WinePriceViewActionsCallback winePriceViewActionsCallback) {
        mWinePriceViewActionsCallback = winePriceViewActionsCallback;
    }

    public void setData(ArrayList<VintageWineInfo> vintageWineInfos) {
        mVintageWineInfos = vintageWineInfos;
    }

    @Override
    public int getCount() {
        return mVintageWineInfos.size();
    }

    /**
     * @return Can return a {@link BaseWine} (if the first item was clicked) or a {@link
     * WineProfileSubProfile}.
     */
    @Override
    public Parcelable getItem(int position) {
        return mVintageWineInfos.get(position).getWineProfileMinimal();
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
            row = (ChooseVintageDialogRow) inflater
                    .inflate(R.layout.row_dialog_choose_vintage_with_sizing, parent, false);
            row.setWinePriceActionCallback(mWinePriceViewActionsCallback);
        }
        if (mVintageWineInfos.size() > 0) {
            row.updateData(mVintageWineInfos.get(position));
        }
        return row;
    }
}
