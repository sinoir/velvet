package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.AddedShippingAddressEvent;
import com.delectable.mobile.api.models.BaseAddress;
import com.delectable.mobile.util.NameUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddShippingAddressDialog extends DialogFragment {

    public static final String EXTRAS_SHIPPING_ADDRESS_ID = "EXTRAS_SHIPPING_ADDRESS_ID";

    public static final int RESULT_SHIPPING_ADDRESS_SAVED = 1000;

    private static final String TAG = AddShippingAddressDialog.class.getSimpleName();

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    @InjectView(R.id.name)
    protected EditText mName;

    @InjectView(R.id.address1)
    protected EditText mAddress1;

    @InjectView(R.id.address2)
    protected EditText mAddress2;

    @InjectView(R.id.city)
    protected EditText mCity;

    @InjectView(R.id.state)
    protected EditText mState;

    @InjectView(R.id.zip_code)
    protected EditText mZipCode;

    @InjectView(R.id.phone_number)
    protected EditText mPhoneNumber;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    public static AddShippingAddressDialog newInstance() {
        AddShippingAddressDialog f = new AddShippingAddressDialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
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

    private void dismissWithSelectedId(String id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_SHIPPING_ADDRESS_ID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                RESULT_SHIPPING_ADDRESS_SAVED,
                intent);
        dismiss();
    }

    //region Helpers
    private boolean isFormValid() {
        if (isFieldEmpty(mName) ||
                isFieldEmpty(mAddress1) ||
                isFieldEmpty(mCity) ||
                isFieldEmpty(mState) ||
                isFieldEmpty(mZipCode)) {
            return false;
        }

        return true;
    }

    private boolean isFieldEmpty(EditText editText) {
        return mName.getText().toString().trim().length() == 0;
    }

    private BaseAddress buildAddress() {
        BaseAddress address = new BaseAddress();
        String[] name = NameUtil.getSplitName(mName.getText().toString());
        String fName = name[NameUtil.FIRST_NAME];
        String lName = name[NameUtil.LAST_NAME];

        address.setFname(fName);
        address.setLname(lName);
        address.setAddr1(mAddress1.getText().toString());
        address.setAddr2(mAddress2.getText().toString());
        address.setCity(mCity.getText().toString());
        address.setState(mState.getText().toString());
        address.setZip(mZipCode.getText().toString());
        address.setPhone(mPhoneNumber.getText().toString());

        return address;
    }
    //endregion

    //region Saving/Loading

    private void saveShippingAddress() {
        BaseAddress address = buildAddress();
        mAccountController.addShippingAddress(address, true);
    }

    //endregion

    //region onClicks
    @OnClick(R.id.save_button)
    protected void saveClicked() {
        if (!isFormValid()) {
            return;
        }
        // TODO: Add Update Ability
        showLoader();
        saveShippingAddress();
    }

    @OnClick(R.id.cancel_button)
    protected void cancelClicked() {
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
    //endregion

    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
    }
}
