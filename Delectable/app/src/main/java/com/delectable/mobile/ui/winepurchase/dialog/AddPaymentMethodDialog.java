package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.AddedPaymentMethodEvent;
import com.delectable.mobile.api.models.PaymentMethod;

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
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddPaymentMethodDialog extends DialogFragment {

    public static final String EXTRAS_PAYMENT_METHOD_ID = "EXTRAS_PAYMENT_METHOD_ID";

    public static final int RESULT_PAYMENT_METHOD_SAVED = 1000;

    private static final String TAG = AddPaymentMethodDialog.class.getSimpleName();

    private static final String ARGS_PAYMENT_METHOD_ID = "ARGS_PAYMENT_METHOD_ID";

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    @InjectView(R.id.dialog_title)
    protected TextView mDialogTitle;

    @InjectView(R.id.name)
    protected EditText mName;

    @InjectView(R.id.credit_card_number)
    protected EditText mCreditCardNumber;

    @InjectView(R.id.expiration)
    protected EditText mExpiration;

    @InjectView(R.id.cvc)
    protected EditText mCVC;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    public static AddPaymentMethodDialog newInstance() {
        AddPaymentMethodDialog f = new AddPaymentMethodDialog();
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
        View view = inflater.inflate(R.layout.dialog_add_payment_method, container, false);
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
        intent.putExtra(EXTRAS_PAYMENT_METHOD_ID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                RESULT_PAYMENT_METHOD_SAVED,
                intent);
        dismiss();
    }

    //region Helpers
    private boolean isFormValid() {
        // TODO: Validate Form Properly
        // TODO: Implement formatter
        if (isFieldEmpty(mName) ||
                isFieldEmpty(mCreditCardNumber) ||
                isFieldEmpty(mExpiration) ||
                isFieldEmpty(mCVC)) {
            return false;
        }

        return true;
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private PaymentMethod buildPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();

        String expiration = mExpiration.getText().toString();

        // TODO: Split Month / Year Properly
        String month = "04";
        String year = "15";

        String creditCardNumber = mCreditCardNumber.getText().toString().trim().replace(" ", "");
        paymentMethod.setExpMonth(month);
        paymentMethod.setExpYear(year);

        paymentMethod.setNumber(creditCardNumber);
        paymentMethod.setCvc(mCVC.getText().toString());

        return paymentMethod;
    }
    //endregion

    //region Saving/Loading
    private void savePaymentMethod() {
        PaymentMethod paymentMethod = buildPaymentMethod();
        mAccountController.addPaymentMethod(paymentMethod, true);
    }
    //endregion

    //region onClicks
    @OnClick(R.id.save_button)
    protected void saveClicked() {
        if (!isFormValid()) {
            return;
        }
        showLoader();
        savePaymentMethod();
    }

    @OnClick(R.id.cancel_button)
    protected void cancelClicked() {
        dismiss();
    }
    //endregion

    //region EventBus
    public void onEventMainThread(AddedPaymentMethodEvent event) {
        hideLoader();
        if (event.isSuccessful()) {
            String paymentMethodId = mPaymentMethodModel.getPrimaryPaymentMethod().getId();
            dismissWithSelectedId(paymentMethodId);
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
