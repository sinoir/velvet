package com.delectable.mobile.ui.winepurchase.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ShippingAddress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChooseShippingAddressAdapter extends BaseAdapter {

    private List<ShippingAddress> mData = new ArrayList<ShippingAddress>();

    private ChooseShippingAddressDialogRow.ActionsHandler mActionsHandler;

    private int mSelectedPosition = -1;

    public void setData(List<ShippingAddress> data) {
        mData = data;
    }

    public void setActionsHandler(ChooseShippingAddressDialogRow.ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setSelectedItemPosition(int position) {
        mSelectedPosition = position;
    }

    public void setSelectedItemById(String id) {
        if (id == null) {
            setSelectedItemPosition(-1);
        }
        for (int i = 0; i < getCount(); i++) {
            if (id.equalsIgnoreCase(getItem(i).getId())) {
                setSelectedItemPosition(i);
                break;
            }
        }
    }

    public ShippingAddress getSelectedItem() {
        if (mSelectedPosition < 0 || mSelectedPosition > getCount()) {
            return null;
        }
        return getItem(mSelectedPosition);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public ShippingAddress getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChooseShippingAddressDialogRow row = (ChooseShippingAddressDialogRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (ChooseShippingAddressDialogRow) inflater
                    .inflate(R.layout.row_choose_address_impl, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        if (getCount() > 0) {
            row.updateData(getItem(position));
        }

        row.setChecked(position == mSelectedPosition);

        // Show last row item "Add Another"
        row.shouldShowAddAnother(getCount() - 1 == position);

        return row;
    }
}
