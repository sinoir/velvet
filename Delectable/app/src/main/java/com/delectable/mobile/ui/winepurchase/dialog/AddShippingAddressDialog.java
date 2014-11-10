package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddShippingAddressDialog extends DialogFragment {

    private static final String TAG = AddShippingAddressDialog.class.getSimpleName();

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

    public static AddShippingAddressDialog newInstance() {
        AddShippingAddressDialog f = new AddShippingAddressDialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    //endregion

    //region onClicks
    @OnClick(R.id.save_button)
    protected void saveClicked() {
        if (!isFormValid()) {
            return;
        }
    }

    @OnClick(R.id.cancel_button)
    protected void cancelClicked() {
        dismiss();
    }
    //endregion
}
