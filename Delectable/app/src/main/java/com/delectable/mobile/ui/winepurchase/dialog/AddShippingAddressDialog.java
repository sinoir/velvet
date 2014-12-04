package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.AddedShippingAddressEvent;
import com.delectable.mobile.api.events.accounts.UpdatedShippingAddressEvent;
import com.delectable.mobile.api.models.BaseAddress;
import com.delectable.mobile.api.models.ShippingAddress;
import com.delectable.mobile.ui.common.dialog.BaseEventBusDialogFragment;
import com.delectable.mobile.ui.common.widget.CancelSaveButtons;
import com.delectable.mobile.util.NameUtil;
import com.delectable.mobile.util.USStates;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddShippingAddressDialog extends BaseEventBusDialogFragment
        implements CancelSaveButtons.ActionsHandler {

    public static final String EXTRAS_SHIPPING_ADDRESS_ID = "EXTRAS_SHIPPING_ADDRESS_ID";

    public static final int RESULT_SHIPPING_ADDRESS_SAVED = 1000;

    private static final String TAG = AddShippingAddressDialog.class.getSimpleName();

    private static final String ARGS_SHIPPING_ADDRESS_ID = "ARGS_SHIPPING_ADDRESS_ID";

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    @InjectView(R.id.dialog_title)
    protected TextView mDialogTitle;

    @InjectView(R.id.name)
    protected EditText mName;

    @InjectView(R.id.address1)
    protected EditText mAddress1;

    @InjectView(R.id.address2)
    protected EditText mAddress2;

    @InjectView(R.id.city)
    protected EditText mCity;

    @InjectView(R.id.state)
    protected Spinner mState;

    @InjectView(R.id.zip_code)
    protected EditText mZipCode;

    @InjectView(R.id.phone_number)
    protected EditText mPhoneNumber;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    @InjectView(R.id.action_buttons)
    protected CancelSaveButtons mActionButtons;

    private String mShippingAddressId;

    private ShippingAddress mExistingShippingAddress;

    private ArrayAdapter<String> mStateAdapter;

    public static AddShippingAddressDialog newInstance() {
        AddShippingAddressDialog f = new AddShippingAddressDialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    public static AddShippingAddressDialog newInstance(String shippingAddressId) {
        AddShippingAddressDialog f = new AddShippingAddressDialog();
        Bundle args = new Bundle();
        args.putString(ARGS_SHIPPING_ADDRESS_ID, shippingAddressId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

        mShippingAddressId = getArguments().getString(ARGS_SHIPPING_ADDRESS_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_shipping_address, container, false);
        ButterKnife.inject(this, view);

        setupStateSpinner();

        if (mShippingAddressId != null) {
            mDialogTitle.setText(R.string.shippingaddress_edit_title);
            loadExistingShippingAddress();
        } else {
            updateFormWithUserData();
            mDialogTitle.setText(R.string.shippingaddress_add_title);
        }

        mActionButtons.setActionsHandler(this);

        return view;
    }

    private void dismissWithSelectedId(String id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_SHIPPING_ADDRESS_ID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                RESULT_SHIPPING_ADDRESS_SAVED,
                intent);
        dismiss();
    }

    //region Update UI methods
    private void setupStateSpinner() {
        List<String> stateNames = new ArrayList<String>();
        for (int i = 0; i < USStates.values().length; i++) {
            stateNames.add(USStates.values()[i].getStateName());
        }
        mStateAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.fonted_spinner_item, stateNames);
        mStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState.setAdapter(mStateAdapter);
    }

    private void updateFormWithExistingAddress() {
        if (mExistingShippingAddress == null) {
            return;
        }
        String fullName = mExistingShippingAddress.getFname();
        fullName += mExistingShippingAddress.getFname() == null ? ""
                : " " + mExistingShippingAddress.getLname();

        mName.setText(fullName);
        mName.setSelection(fullName.length());
        mAddress1.setText(mExistingShippingAddress.getAddr1());
        mAddress2.setText(mExistingShippingAddress.getAddr2());
        mCity.setText(mExistingShippingAddress.getCity());
        mZipCode.setText(mExistingShippingAddress.getZip());
        mPhoneNumber.setText(mExistingShippingAddress.getPhone());

        USStates selectedState = USStates
                .stateByNameOrAbbreviation(mExistingShippingAddress.getState());
        if (selectedState != null) {
            mState.setSelection(selectedState.ordinal());
        }
    }


    private void updateFormWithUserData() {
        if (UserInfo.getAccountPrivate(App.getInstance()) == null) {
            return;
        }
        String fullName = UserInfo.getAccountPrivate(App.getInstance()).getFullName();
        String state = UserInfo.getAccountPrivate(App.getInstance()).getSourcingState();

        mName.setText(fullName);
        mName.setSelection(fullName.length());
        USStates selectedState = USStates.stateByNameOrAbbreviation(state);
        if (selectedState != null) {
            mState.setSelection(selectedState.ordinal());
        }
    }
    //endregion

    //region Helpers
    private boolean isFormValid() {
        boolean isStateValid = mState.getSelectedItem() != null &&
                getSelectedState() != null;
        if (isFieldEmpty(mName) ||
                isFieldEmpty(mAddress1) ||
                isFieldEmpty(mCity) ||
                !isStateValid ||
                isFieldEmpty(mZipCode)) {
            return false;
        }

        return true;
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private ShippingAddress buildAddress() {
        ShippingAddress address = new ShippingAddress();
        String[] name = NameUtil.getSplitName(mName.getText().toString());
        String fName = name[NameUtil.FIRST_NAME];
        String lName = name[NameUtil.LAST_NAME];

        address.setFname(fName);
        address.setLname(lName);
        address.setAddr1(mAddress1.getText().toString());
        address.setAddr2(mAddress2.getText().toString());
        address.setCity(mCity.getText().toString());
        address.setZip(mZipCode.getText().toString());
        address.setPhone(mPhoneNumber.getText().toString());

        address.setState(getSelectedState().getStateAbbreviation());
        if (mExistingShippingAddress != null) {
            address.setId(mExistingShippingAddress.getId());
        }

        return address;
    }

    private USStates getSelectedState() {
        return USStates.stateByNameOrAbbreviation((String) mState.getSelectedItem());
    }
    //endregion

    //region Saving/Loading

    private void saveShippingAddress() {
        BaseAddress address = buildAddress();
        mAccountController.addShippingAddress(address, true);
    }

    private void updateShippingAddress() {
        ShippingAddress updatedAddress = buildAddress();
        mAccountController.updateShippingAddress(updatedAddress, true);
    }

    private void loadExistingShippingAddress() {
        mExistingShippingAddress = mShippingAddressModel.getShippingAddress(mShippingAddressId);
        updateFormWithExistingAddress();
    }

    //endregion

    //region onClicks
    @Override
    public void onSaveClicked() {
        if (!isFormValid()) {
            return;
        }
        showLoader();
        if (mExistingShippingAddress == null) {
            saveShippingAddress();
        } else {
            updateShippingAddress();
        }
    }

    @Override
    public void onCancelClicked() {
        dismiss();
    }
    //endregion

    //region EventBus
    public void onEventMainThread(AddedShippingAddressEvent event) {
        hideLoader();
        if (event.isSuccessful()) {
            String shippingAddressId = mShippingAddressModel.getPrimaryShippingAddress().getId();
            dismissWithSelectedId(shippingAddressId);
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), event.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onEventMainThread(UpdatedShippingAddressEvent event) {
        if (!event.getShippingAddressId().equalsIgnoreCase(mShippingAddressId)) {
            return;
        }
        hideLoader();
        if (event.isSuccessful()) {
            dismissWithSelectedId(mShippingAddressId);
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), event.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //endregion

    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
    }
}
