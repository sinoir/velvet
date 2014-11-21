package com.delectable.mobile.ui.registration.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.RegistrationController;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;
import com.delectable.mobile.util.FontEnum;
import com.delectable.mobile.util.HelperUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ResetPasswordDialog extends BaseDialogFragment {

    public static final String EMAIL = "EMAIL";

    private static final String TAG = ResetPasswordDialog.class.getSimpleName();

    @Inject
    RegistrationController mRegistrationController;

    @InjectView(R.id.email_address_field)
    EditText mEmailField;

    private String mPhoneEmail;

    private Typeface mWhitneyBookFont;

    /**
     * Brings up the soft keyboard right when the dialog shows up.
     */
    private View.OnFocusChangeListener ShowKeyboardOnFocusListener
            = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                getDialog().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    };

    /**
     * Listens for done button on soft keyboard.
     */
    private TextView.OnEditorActionListener DoneActionListener
            = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onResetPasswordClick();

                //hide keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
                return true;
            }
            return false;
        }
    };

    /**
     * @param email The email that you want to prepopulate the email field with. Typically the
     *              phone's account email.
     */
    public static ResetPasswordDialog newInstance(String email) {
        ResetPasswordDialog f = new ResetPasswordDialog();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        if (getArguments() != null) {
            mPhoneEmail = getArguments().getString(EMAIL);
        }
        mWhitneyBookFont = FontEnum.WHITNEY_BOOK.getTypeface(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reset_password, container, false);
        ButterKnife.inject(this, view);

        mEmailField.setText(mPhoneEmail);
        mEmailField.setTypeface(mWhitneyBookFont);
        mEmailField.setOnEditorActionListener(DoneActionListener);
        mEmailField.setOnFocusChangeListener(ShowKeyboardOnFocusListener);

        return view;
    }

    @OnClick(R.id.cancel_textview)
    protected void onCancelClick() {
        if (getTargetFragment() != null) {
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        }
        dismiss();
    }

    @OnClick(R.id.reset_password_textview)
    protected void onResetPasswordClick() {
        if (HelperUtil.isEmailValid(mEmailField.getText().toString().trim())) {
            mRegistrationController.resetPassword(mEmailField.getText().toString().trim());
            // Dialog is dismissed by SignInFragment when password reset was successful
        } else {
            Toast.makeText(getActivity(), R.string.reset_password_dialog_invalid_email,
                    Toast.LENGTH_LONG).show();
        }
    }

}

