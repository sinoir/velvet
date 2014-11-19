package com.delectable.mobile.ui.winepurchase.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ShippingAddress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChooseShippingAddressAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_FOOTER = 0;

    private static final int VIEW_TYPE_ITEM = 1;

    private List<ShippingAddress> mData = new ArrayList<ShippingAddress>();

    private ChooseShippingAddressDialogRow.ActionsHandler mRowActionsHandler;

    private ActionsHandler mActionsHandler;

    private int mSelectedPosition = -1;

    public void setData(List<ShippingAddress> data) {
        mData = data;
    }

    public void setRowActionsHandler(
            ChooseShippingAddressDialogRow.ActionsHandler rowActionsHandler) {
        mRowActionsHandler = rowActionsHandler;
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setSelectedItemPosition(int position) {
        mSelectedPosition = position;
    }

    public void setSelectedItemById(String id) {
        if (id == null) {
            setSelectedItemPosition(-1);
            return;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getCount() {
        return mData.size() + 1;
    }

    public ShippingAddress getItem(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            row = getAddressRowView(position, convertView, parent);
        } else if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            row = getFooterRowView(position, convertView, parent);
        }

        return row;
    }

    private View getAddressRowView(int position, View convertView, ViewGroup parent) {
        ChooseShippingAddressDialogRow row = (ChooseShippingAddressDialogRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (ChooseShippingAddressDialogRow) inflater
                    .inflate(R.layout.row_choose_address_impl, parent, false);
            row.setActionsHandler(mRowActionsHandler);
        }
        if (getCount() > 0) {
            row.updateData(getItem(position));
        }

        row.setChecked(position == mSelectedPosition);

        return row;
    }

    private View getFooterRowView(int position, View convertView, ViewGroup parent) {
        TextView row = (TextView) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (TextView) inflater.inflate(R.layout.widget_add_another_footer, parent, false);
            row.setText(R.string.shippingaddress_add_another);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionsHandler != null) {
                        mActionsHandler.onAddAnotherClicked();
                    }
                }
            });
        }
        return row;
    }

    public interface ActionsHandler {

        public void onAddAnotherClicked();
    }
}
