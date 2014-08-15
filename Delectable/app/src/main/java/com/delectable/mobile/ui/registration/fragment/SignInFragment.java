package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.controllers.RegistrationController;
import com.delectable.mobile.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.ui.navigation.activity.NavActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.OnClick;

public class SignInFragment extends BaseSignUpInFragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    @Inject
    RegistrationController mRegistrationController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

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
            if (emptyFieldExists()) {
                getDoneButton().setEnabled(false);
                return;
            }
            getDoneButton().setEnabled(true);
        }
    };

    private boolean emptyFieldExists() {
        if (getEmailField().getText().toString().trim().equals("")) {
            return true;
        }
        if (getPasswordField().getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    @Override
    protected void onKeyboardDoneButtonClick() {
        onDoneButtonClick();
    }

    @Override
    protected void onDoneButtonClick() {
        if (emptyFieldExists()) {
            return;
        }
        String email = getEmailField().getText().toString().trim();
        String password = getPasswordField().getText().toString().trim();

        mRegistrationController.login(email, password);
    }

    public void onEventMainThread(LoginRegisterEvent registerEvent) {
        if (registerEvent.isSuccessful()) {
            Log.d(TAG, "Successfully Logged in!");
            startActivity(new Intent(getActivity(), NavActivity.class));
            getActivity().finish();
            return;
        }

        Toast.makeText(getActivity(), registerEvent.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.forgot_textview)
    protected void onForgotTextClick() {
        Log.d(TAG, "onForgotTextClick");
    }

    @Override
    protected void onFacebookButtonClick() {
        Log.d(TAG, "onFacebookButtonClick");

    }

    @Override
    protected void onGoogleButtonClick() {
        Log.d(TAG, "onGoogleButtonClick");


    }


}
