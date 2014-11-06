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

    private static final int FIRST_ROW = 0;

    private static final int FIRST_ROW_OFFSET = 1;

    private BaseWine mBaseWine;

    private ArrayList<VintageWineInfo> mVintageWineInfos = new ArrayList<VintageWineInfo>();

    public WineProfilesAdapter() {

    }

    public void setBaseWine(BaseWine baseWine) {
        mBaseWine = baseWine;
        mVintageWineInfos.clear();
        for (WineProfileSubProfile wineProfile : baseWine.getWineProfiles()) {
            mVintageWineInfos.add(new VintageWineInfo(wineProfile));
        }
    }

    @Override
    public int getCount() {
        return mVintageWineInfos.size() + FIRST_ROW_OFFSET; //+1 for the all years row
    }

    /**
     * @return Can return a {@link BaseWine} (if the first item was clicked) or a {@link
     * WineProfileSubProfile}.
     */
    @Override
    public Parcelable getItem(int position) {
        if (position == FIRST_ROW) {
            return mBaseWine;
        }
        position -= FIRST_ROW_OFFSET;
        return mVintageWineInfos.get(position).getWineProfileMinimal();
    }

    @Override
    public long getItemId(int position) {
        position -= FIRST_ROW_OFFSET;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChooseVintageDialogRow row = (ChooseVintageDialogRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (ChooseVintageDialogRow) inflater
                    .inflate(R.layout.row_dialog_choose_vintage_with_sizing, parent, false);
        }

        // TODO: Remove First Row..
        if (position == FIRST_ROW && mBaseWine != null) {
            row.updateData(mBaseWine);
        } else if (mVintageWineInfos.size() > 0) {
            position -= FIRST_ROW_OFFSET; //adjust position so show the correct wine profile data
            row.updateData(mVintageWineInfos.get(position));
        }
        return row;
    }

}
