package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.registration.dialog.LoadingCircleDialog;
import com.delectable.mobile.events.registrations.ResetPasswordEvent;
import com.delectable.mobile.ui.registration.dialog.ResetPasswordDialog;
import com.delectable.mobile.util.HelperUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.OnClick;

//TODO implement something to show that requests are loading
public class SignInFragment extends BaseSignUpInFragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    private ResetPasswordDialog mResetPasswordDialog;

    /**
     * Sets whether the done button is enabled or not depending on whether the fields are all filled
     * out.
     */
    private TextWatcher TextValidationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged: " + s.toString());
            //only enable done button if all fields are filled
            if (invalidFieldExists()) {
                getDoneButton().setEnabled(false);
                return;
            }
            getDoneButton().setEnabled(true);
        }
    };

    private static final int RESET_PASSWORD_DIALOG = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        getTitleTextView().setText(R.string.sign_in_title);
        getDoneButton().setEnabled(false);
        getNameFieldContainer().setVisibility(View.GONE);
        getFacebookTextView().setText(R.string.signup_in_sign_in_using_facebook);
        getGoogleTextView().setText(R.string.signup_in_sign_in_using_google);
        getTermsPrivacyContainer().setVisibility(View.INVISIBLE);

        getEmailField().addTextChangedListener(TextValidationWatcher);
        getPasswordField().addTextChangedListener(TextValidationWatcher);
        return rootView;
    }

    @Override
    protected void onKeyboardDoneButtonClick() {
        onDoneButtonClick();
    }

    @Override
    protected void onDoneButtonClick() {
        if (invalidFieldExists()) {
            return;
        }

        mLoadingDialog = new LoadingCircleDialog();
        mLoadingDialog.show(getFragmentManager(), LoadingCircleDialog.TAG);

        String email = getEmailField().getText().toString().trim();
        String password = getPasswordField().getText().toString().trim();

        mRegistrationController.login(email, password);
    }

    @OnClick(R.id.forgot_textview)
    protected void onForgotTextClick() {
        Log.d(TAG, "onForgotTextClick");
        mResetPasswordDialog = ResetPasswordDialog.newInstance(getPhoneEmail());
        mResetPasswordDialog
                .setTargetFragment(this, RESET_PASSWORD_DIALOG); //callback goes to onActivityResult
        mResetPasswordDialog
                .show(getFragmentManager(), mResetPasswordDialog.getClass().getSimpleName());
    }

    @Override
    protected void onGoogleButtonClick() {
        Log.d(TAG, "onGoogleButtonClick");
        //TODO implement google sign in
    }

    private boolean invalidFieldExists() {
        if (!HelperUtil.isEmailValid(getEmailField().getText().toString().trim())) {
            return true;
        }
        if (getPasswordField().getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    public void onEventMainThread(ResetPasswordEvent event) {
        String msg;

        if (event.isSuccessful()) {
            msg = String
                    .format(getResources().getString(R.string.reset_password_dialog_msg_success),
                            event.mEmail);
            mResetPasswordDialog.dismiss();
        } else {
            msg = String.format(getResources().getString(R.string.reset_password_dialog_msg_failed),
                    event.mEmail);
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
