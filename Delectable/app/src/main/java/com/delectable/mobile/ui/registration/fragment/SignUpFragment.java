package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.util.NameUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.OnClick;

public class SignUpFragment extends BaseSignUpInFragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getTitleTextView().setText(R.string.sign_up_title);
        getDoneButton().setEnabled(false);
        getForgotTextView().setVisibility(View.GONE);
        getFacebookTextView().setText(R.string.signup_in_sign_up_using_facebook);
        getGoogleTextView().setText(R.string.signup_in_sign_up_using_google);

        getNameField().addTextChangedListener(TextValidationWatcher);
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
        if (getNameField().getText().toString().trim().equals("")) {
            return true;
        }
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
        String nameEntered = getNameField().getText().toString().trim();
        String email = getEmailField().getText().toString().trim();
        String password = getPasswordField().getText().toString().trim();

        String[] name = NameUtil.getSplitName(nameEntered);
        String fName = name[NameUtil.FIRST_NAME];
        String lName = name[NameUtil.LAST_NAME];

        mRegistrationController.register(email, password, fName, lName);
    }

    public void onEventMainThread(LoginRegisterEvent registerEvent) {
        if (registerEvent.isSuccessful()) {
            startActivity(new Intent(getActivity(), NavActivity.class));
            getActivity().finish();
            return;
        }
        Toast.makeText(getActivity(), registerEvent.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onGoogleButtonClick() {
        Log.d(TAG, "onGoogleButtonClick");
        //TODO Google sign up
    }

    @OnClick(R.id.terms_textview)
    protected void goToTermsOfUse() {
        Log.d(TAG, "goToTermsOfUse");
        //TODO need link/text for terms of use
    }

    @OnClick(R.id.privacy_textview)
    protected void goToPrivacyPolicy() {
        Log.d(TAG, "goToPrivacyPolicy");
        //TODO need link/text for privacy policy
    }
}