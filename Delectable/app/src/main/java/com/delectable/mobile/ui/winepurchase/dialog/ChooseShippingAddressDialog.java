package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.RemovedShippingAddressEvent;
import com.delectable.mobile.api.models.ShippingAddress;
import com.delectable.mobile.ui.common.widget.CancelSaveButtons;
import com.delectable.mobile.ui.winepurchase.widget.ChooseShippingAddressAdapter;
import com.delectable.mobile.ui.winepurchase.widget.ChooseShippingAddressDialogRow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ChooseShippingAddressDialog extends DialogFragment
        implements CancelSaveButtons.ActionsHandler, ChooseShippingAddressDialogRow.ActionsHandler,
        ChooseShippingAddressAdapter.ActionsHandler {

    public static final String EXTRAS_SHIPPING_ADDRESS_ID = "EXTRAS_SHIPPING_ADDRESS_ID";

    public static final int RESULT_SHIPPING_ADDRESS_SELECTED = 1000;

    private static final int REQUEST_ADD_SHIPPING_ADDRESS_DIALOG = 1000;

    private static final String ARGS_SELECTED_SHIPPING_ADDRESS_ID
            = "ARGS_SELECTED_SHIPPING_ADDRESS_ID";

    private static final String TAG = ChooseShippingAddressDialog.class.getSimpleName();

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    @InjectView(R.id.dialog_title)
    protected TextView mDialogTitle;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    @InjectView(R.id.action_buttons)
    protected CancelSaveButtons mActionButtons;

    private ChooseShippingAddressAdapter mAdapter;

    private String mSelectedShippingAddressId;

    private ArrayList<ShippingAddress> mShippingAddressList;

    // TODO: Will Remove items on "Save"
    private ArrayList<ShippingAddress> mRemoveShippingAddressList
            = new ArrayList<ShippingAddress>();

    public static ChooseShippingAddressDialog newInstance(String shippingAddressId) {
        ChooseShippingAddressDialog f = new ChooseShippingAddressDialog();
        Bundle args = new Bundle();
        args.putString(ARGS_SELECTED_SHIPPING_ADDRESS_ID, shippingAddressId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        mSelectedShippingAddressId = getArguments().getString(ARGS_SELECTED_SHIPPING_ADDRESS_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_checkoutitem_listing, container, false);
        ButterKnife.inject(this, view);

        mDialogTitle.setText(R.string.shippingaddress_choose_title);

        mActionButtons.setActionsHandler(this);

        mAdapter = new ChooseShippingAddressAdapter();
        mAdapter.setRowActionsHandler(this);
        mAdapter.setActionsHandler(this);
        mListView.setAdapter(mAdapter);

        loadExistingShippingAddress();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
            // no-op
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SHIPPING_ADDRESS_DIALOG
                && resultCode == AddShippingAddressDialog.RESULT_SHIPPING_ADDRESS_SAVED) {
            loadExistingShippingAddress();
        }
    }

    private void dismissWithSelectedId(String id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_SHIPPING_ADDRESS_ID, mSelectedShippingAddressId);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                RESULT_SHIPPING_ADDRESS_SELECTED,
                intent);
        dismiss();
    }

    //region UpdateUI
    private void updateUIWithNewData() {
        mAdapter.setSelectedItemById(mSelectedShippingAddressId);
        mAdapter.notifyDataSetChanged();
    }
    //endregion

    //region Saving/Loading
    private void loadExistingShippingAddress() {
        mShippingAddressList = (ArrayList<ShippingAddress>) mShippingAddressModel
                .getAllShippingAddresses();

        // Run though Shipping Addresses to remove
        for (ShippingAddress address : mRemoveShippingAddressList) {
            mShippingAddressList.remove(address);
        }

        mAdapter.setData(mShippingAddressList);
        updateUIWithNewData();
    }

    public void syncRemovedItems() {
        showLoader();
        ArrayList<String> addressIds = new ArrayList<String>();
        for (ShippingAddress address : mRemoveShippingAddressList) {
            addressIds.add(address.getId());
        }
        mAccountController.removeShippingAddresses(addressIds);
    }
    //endregion

    //region onClicks
    @Override
    public void onSaveClicked() {
        if (mRemoveShippingAddressList.size() > 0) {
            syncRemovedItems();
        } else {
            dismissWithSelectedId(mSelectedShippingAddressId);
        }
    }

    @Override
    public void onCancelClicked() {
        dismiss();
    }

    @Override
    public void onEditClicked(String shippingAddressId) {
        showAddShippingAddressDialog(shippingAddressId);
    }

    @Override
    public void onDeleteClicked(String shippingAddressId) {
        // Remove Item From List
        ShippingAddress addressToRemove = null;
        for (ShippingAddress address : mShippingAddressList) {
            if (address.getId().equalsIgnoreCase(shippingAddressId)) {
                addressToRemove = address;
                break;
            }
        }

        mRemoveShippingAddressList.add(addressToRemove);
        mShippingAddressList.remove(addressToRemove);

        // Update Shipping Address ID if it was the item removed

        if (mSelectedShippingAddressId.equalsIgnoreCase(shippingAddressId)) {
            if (mShippingAddressList.size() > 0) {
                mSelectedShippingAddressId = mShippingAddressList.get(0).getId();
            } else {
                mSelectedShippingAddressId = null;
            }
        }
        mAdapter.setSelectedItemById(mSelectedShippingAddressId);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddAnotherClicked() {
        showAddShippingAddressDialog(null);
    }

    @Override
    public void onRowClicked(String shippingAddressId) {
        mSelectedShippingAddressId = shippingAddressId;
        updateUIWithNewData();
    }
    //endregion

    //region EventBus
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RemovedShippingAddressEvent event) {
        hideLoader();
        if (event.isSuccessful()) {
            dismissWithSelectedId(mSelectedShippingAddressId);
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), event.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //endregion

    private void showAddShippingAddressDialog(String id) {
        AddShippingAddressDialog dialog = AddShippingAddressDialog.newInstance(id);
        dialog.setTargetFragment(this, REQUEST_ADD_SHIPPING_ADDRESS_DIALOG);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
    }
}
