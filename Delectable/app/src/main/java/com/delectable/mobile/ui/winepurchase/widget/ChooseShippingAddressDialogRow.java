package com.delectable.mobile.ui.winepurchase.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ShippingAddress;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChooseShippingAddressDialogRow extends RelativeLayout {

    private static final String TAG = ChooseShippingAddressDialogRow.class.getSimpleName();

    @InjectView(R.id.radio_button)
    protected RadioButton mRadioButton;

    @InjectView(R.id.shipping_address)
    protected TextView mShippingAddressText;

    @InjectView(R.id.add_another)
    protected View mAddAnother;

    private ShippingAddress mShippingAddress;

    private ActionsHandler mActionsHandler;

    public ChooseShippingAddressDialogRow(Context context) {
        this(context, null);
    }

    public ChooseShippingAddressDialogRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseShippingAddressDialogRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_choose_address, this);
        ButterKnife.inject(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionsHandler != null && mShippingAddress != null) {
                    mActionsHandler.onRowClicked(mShippingAddress.getId());
                }
            }
        });
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setChecked(boolean checked) {
        mRadioButton.setChecked(checked);
        Log.d(TAG, "Radio Button Checked? " + checked);
    }

    public void updateData(ShippingAddress address) {
        mShippingAddress = address;
        String shippingAddressText = "";
        shippingAddressText += address.getFname();
        if (address.getLname() != null) {
            shippingAddressText += " " + address.getLname();
        }
        shippingAddressText += "\n";
        shippingAddressText += address.getAddr1() + "\n";

        if (address.getAddr2() != null && !address.getAddr2().trim().equalsIgnoreCase("")) {
            shippingAddressText += address.getAddr2() + "\n";
        }

        shippingAddressText += address.getCity() + "\n";
        shippingAddressText += address.getState() + " " + address.getZip();
        mShippingAddressText.setText(shippingAddressText);
    }

    @OnClick(R.id.edit_button)
    protected void onEditClicked() {
        if (mActionsHandler != null && mShippingAddress != null) {
            mActionsHandler.onEditClicked(mShippingAddress.getId());
        }
    }

    @OnClick(R.id.delete_button)
    protected void onDeleteClicked() {
        if (mActionsHandler != null && mShippingAddress != null) {
            mActionsHandler.onDeleteClicked(mShippingAddress.getId());
        }
    }

    @OnClick(R.id.add_another)
    protected void onAddAnotherClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.onAddAnother();
        }
    }

    public void shouldShowAddAnother(boolean addAnother) {
        if (addAnother) {
            mAddAnother.setVisibility(View.VISIBLE);
        } else {
            mAddAnother.setVisibility(View.GONE);
        }
    }

    public interface ActionsHandler {

        public void onEditClicked(String shippingAddressId);

        public void onDeleteClicked(String shippingAddressId);

        public void onAddAnother();

        public void onRowClicked(String shippingAddressId);
    }
}