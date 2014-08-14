package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Sign up and Sign in screens both share the same xml view, this base class takes care of
 * butterknifing the views.
 */
public abstract class BaseSignUpInFragment extends BaseFragment {

    @InjectView(R.id.sign_up_sign_in_title)
    protected FontTextView mTitleTextView;

    @InjectView(R.id.done_button)
    protected FontTextView mDoneButton;

    @InjectView(R.id.name_field_container)
    protected RelativeLayout mNameFieldContainer;

    @InjectView(R.id.name_field)
    protected EditText mNameField;

    @InjectView(R.id.email_address_field)
    protected EditText mEmailField;

    @InjectView(R.id.password_field)
    protected EditText mPasswordField;

    @InjectView(R.id.forgot_textview)
    protected FontTextView mForgotTextView;

    @InjectView(R.id.facebook_button)
    protected RelativeLayout mFacebookButton;

    @InjectView(R.id.facebook_sign_in_text)
    protected FontTextView mFacebookTextView;

    @InjectView(R.id.google_button)
    protected RelativeLayout mGoogleButton;

    @InjectView(R.id.google_sign_in_text)
    protected FontTextView mGoogleTextView;

    @InjectView(R.id.terms_privacy_container)
    protected LinearLayout mTermsPrivacyContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in_sign_up_form, container, false);
        ButterKnife.inject(this, rootView);
        mPasswordField.setOnEditorActionListener(DoneActionListener);
        return rootView;
    }

    /**
     * Listens for done button on soft keyboard.
     */
    protected TextView.OnEditorActionListener DoneActionListener
            = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onKeyboardDoneButtonClick();

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
     * Gets called upon pressing done on the keyboard from the password field.
     */
    protected abstract void onKeyboardDoneButtonClick();

    @OnClick(R.id.done_button)
    protected abstract void onDoneButtonClick();

    @OnClick(R.id.facebook_button)
    protected abstract void onFacebookButtonClick();

    @OnClick(R.id.google_button)
    protected abstract void onGoogleButtonClick();


    public FontTextView getTitleTextView() {
        return mTitleTextView;
    }

    public FontTextView getDoneButton() {
        return mDoneButton;
    }

    public RelativeLayout getNameFieldContainer() {
        return mNameFieldContainer;
    }

    public EditText getNameField() {
        return mNameField;
    }

    public EditText getEmailField() {
        return mEmailField;
    }

    public EditText getPasswordField() {
        return mPasswordField;
    }

    public FontTextView getForgotTextView() {
        return mForgotTextView;
    }

    public RelativeLayout getFacebookButton() {
        return mFacebookButton;
    }

    public FontTextView getFacebookTextView() {
        return mFacebookTextView;
    }

    public RelativeLayout getGoogleButton() {
        return mGoogleButton;
    }

    public FontTextView getGoogleTextView() {
        return mGoogleTextView;
    }

    public LinearLayout getTermsPrivacyContainer() {
        return mTermsPrivacyContainer;
    }


}
