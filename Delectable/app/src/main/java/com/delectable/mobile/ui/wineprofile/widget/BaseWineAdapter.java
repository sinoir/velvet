

package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class BaseWineAdapter extends BaseAdapter {

//    private static final int FOOTER_OFFSET = 1;

    private int mFooterPosition = 0;

    private ArrayList<BaseWine> mMatches = new ArrayList<BaseWine>();

    public void setMatches(ArrayList<BaseWine> matches) {
        mMatches = matches;
        mFooterPosition = matches.size();
    }

    @Override
    public int getCount() {
        return mMatches.size();// + FOOTER_OFFSET; //+1 for the footer to report an incorrect wine
    }

    /**
     * @return Can return a {@link com.delectable.mobile.api.models.BaseWine} (if the first item was
     * clicked) or a {@link com.delectable.mobile.api.models.WineProfileSubProfile}.
     */
    @Override
    public Parcelable getItem(int position) {
        if (position == mFooterPosition) {
            // TODO chosing the footer should report the wine as incorrect
            return null;
        }
        return mMatches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EditBaseWineDialogRow row = (EditBaseWineDialogRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (EditBaseWineDialogRow) inflater
                    .inflate(R.layout.row_dialog_edit_base_wine_impl, parent, false);
        }

        if (position == mFooterPosition) {
            // FIXME what to do here?
//            row.updateData(mBaseWine);
        } else {
            BaseWine baseWine = mMatches.get(position);
            row.updateData(baseWine.getProducerName(), baseWine.getName());
        }
        return row;
    }

}

