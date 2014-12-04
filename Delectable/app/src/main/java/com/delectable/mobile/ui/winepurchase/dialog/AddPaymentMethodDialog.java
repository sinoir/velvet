package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.AddedPaymentMethodEvent;
import com.delectable.mobile.api.models.PaymentMethod;
import com.delectable.mobile.ui.common.dialog.BaseEventBusDialogFragment;
import com.delectable.mobile.ui.common.widget.CancelSaveButtons;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnFocusChange;

public class AddPaymentMethodDialog extends BaseEventBusDialogFragment
        implements CancelSaveButtons.ActionsHandler {

    public static final String EXTRAS_PAYMENT_METHOD_ID = "EXTRAS_PAYMENT_METHOD_ID";

    public static final int RESULT_PAYMENT_METHOD_SAVED = 1000;

    private static final String TAG = AddPaymentMethodDialog.class.getSimpleName();

    private static final String ARGS_PAYMENT_METHOD_ID = "ARGS_PAYMENT_METHOD_ID";

    private static final int MAX_YEAR_DIFF = 30;

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    @InjectView(R.id.dialog_title)
    protected TextView mDialogTitle;

    @InjectView(R.id.name)
    protected EditText mName;

    @InjectView(R.id.credit_card_number)
    protected EditText mCreditCardNumber;

    @InjectView(R.id.expiration_month)
    protected EditText mExpirationMonth;

    @InjectView(R.id.expiration_year)
    protected EditText mExpirationYear;

    @InjectView(R.id.cvc)
    protected EditText mCVC;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    @InjectView(R.id.action_buttons)
    protected CancelSaveButtons mActionButtons;

    private int mCurrent2DigitYear;

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

        buildCurrent2DigitYear();
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

        mActionButtons.setActionsHandler(this);

        return view;
    }

    private void dismissWithSelectedId(String id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_PAYMENT_METHOD_ID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                RESULT_PAYMENT_METHOD_SAVED,
                intent);
        dismiss();
    }

    //region Form Validators
    private boolean isFormValid() {
        return validateNameField(true) &&
                validateCreditCardField(true) &&
                validateMonthField(true) &&
                validateYearField(true) &&
                validateCVCField(true);
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private boolean validateNameField(boolean requestFocus) {
        boolean isValid = true;
        if (isFieldEmpty(mName)) {
            mName.setError("Name is Required");
            isValid = false;
            if (requestFocus) {
                requestFocusWithCursorAtEnd(mName);
            }
        }
        return isValid;
    }

    private boolean validateCreditCardField(boolean requestFocus) {
        boolean isValid = true;
        if (isFieldEmpty(mCreditCardNumber)) {
            mCreditCardNumber.setError("Credit Card Number is Required");
            isValid = false;
            if (requestFocus) {
                requestFocusWithCursorAtEnd(mCreditCardNumber);
            }
        }
        return isValid;
    }

    private boolean validateYearField(boolean requestFocus) {
        boolean isValid = true;
        String yearText = mExpirationYear.getText().toString();
        int yearInt = 0;
        int maxYearsOut = mCurrent2DigitYear + MAX_YEAR_DIFF;
        try {
            yearInt = Integer.valueOf(yearText);
        } catch (NumberFormatException ex) {
            // no-op, month int will be 0 and fail as invalid month
        }

        if (yearInt == 0) {
            mExpirationYear.setError("Year is Required");
            isValid = false;
        } else if (yearInt < mCurrent2DigitYear || yearInt > maxYearsOut) {
            mExpirationYear.setError("Year is Invalid");
            isValid = false;
        }
        if (!isValid && requestFocus) {
            requestFocusWithCursorAtEnd(mExpirationYear);
        }
        return isValid;
    }

    private boolean validateMonthField(boolean requestFocus) {
        boolean isValid = true;
        String monthText = mExpirationMonth.getText().toString();
        int monthInt = 0;
        try {
            monthInt = Integer.valueOf(monthText);
        } catch (NumberFormatException ex) {
            // no-op, month int will be 0 and fail as invalid month
        }

        if (monthInt == 0) {
            mExpirationMonth.setError("Month is Required");
            isValid = false;
        } else if (monthInt > 12) {
            mExpirationMonth.setError("Month is Invalid");
            isValid = false;
        }
        if (!isValid && requestFocus) {
            requestFocusWithCursorAtEnd(mExpirationMonth);
        }

        return isValid;
    }

    private boolean validateCVCField(boolean requestFocus) {
        boolean isValid = true;
        if (isFieldEmpty(mCVC)) {
            mCVC.setError("CVC is Required");
            if (requestFocus) {
                requestFocusWithCursorAtEnd(mCVC);
            }
            isValid = false;
        }
        return isValid;
    }

    //endregion

    //region Helpers

    /**
     * Builds 2 digit year from Date Formatter.  We call this once in onCreate, so we don't call
     * this every time user enters data inside the year field
     */
    private void buildCurrent2DigitYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String formattedDate = sdf.format(Calendar.getInstance().getTime());
        mCurrent2DigitYear = Integer.valueOf(formattedDate);
    }

    private PaymentMethod buildPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();

        String month = mExpirationMonth.getText().toString();
        String year = mExpirationYear.getText().toString();

        String creditCardNumber = mCreditCardNumber.getText().toString().trim().replace(" ", "");
        paymentMethod.setExpMonth(month);
        paymentMethod.setExpYear(year);

        paymentMethod.setNumber(creditCardNumber);
        paymentMethod.setCvc(mCVC.getText().toString());

        return paymentMethod;
    }

    /**
     * Requests focus on a field if it doesn't has focus, and adds cursor to end of line
     *
     * Used for form validations when user clicks save and a field is invalid
     */
    private void requestFocusWithCursorAtEnd(EditText field) {
        if (!field.hasFocus()) {
            field.requestFocus();
            field.setSelection(field.getText().toString().length());
        }
    }
    //endregion

    //region Saving/Loading
    private void savePaymentMethod() {
        PaymentMethod paymentMethod = buildPaymentMethod();
        mAccountController.addPaymentMethod(paymentMethod, true);
    }
    //endregion

    //region onTextChanged
    @OnFocusChange(value = R.id.name)
    protected void onFocusChangedForName(boolean focused) {
        if (!focused) {
            validateNameField(false);
        }
    }

    @OnFocusChange(value = R.id.credit_card_number)
    protected void onFocusChangedForCCNum(boolean focused) {
        if (!focused) {
            validateCreditCardField(false);
        }
    }

    @OnFocusChange(value = R.id.expiration_month)
    protected void onFocusChangedForMonth(boolean focused) {
        if (!focused) {
            validateMonthField(false);
        }
    }

    @OnFocusChange(value = R.id.expiration_year)
    protected void onFocusChangedForYear(boolean focused) {
        if (!focused) {
            validateYearField(false);
        }
    }

    @OnFocusChange(value = R.id.cvc)
    protected void onFocusChangedForCVC(boolean focused) {
        if (!focused) {
            validateCVCField(false);
        }
    }
    //endregion

    //region onClicks
    @Override
    public void onSaveClicked() {
        if (!isFormValid()) {
            return;
        }
        showLoader();
        savePaymentMethod();
    }

    @Override
    public void onCancelClicked() {
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
