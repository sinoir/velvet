package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.controllers.RegistrationController;
import com.delectable.mobile.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.util.DateHelperUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Sign up and Sign in screens both share the same xml view, this base class takes care of
 * butterknifing the views.
 */
public abstract class BaseSignUpInFragment extends BaseFragment {

    //not making this TAG static in order to get the concrete class'name to show up in Logs
    private final String TAG = this.getClass().getSimpleName();

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

    /**
     * This is merely the UI button, clicking this will programmatically invoke a click on the real facebook button.
     */
    @InjectView(R.id.facebook_button)
    protected RelativeLayout mFacebookButton;

    @InjectView(R.id.facebook_sign_in_text)
    protected FontTextView mFacebookTextView;

    @InjectView(R.id.facebook_sign_in)
    protected LoginButton mRealFacebookButton;

    @InjectView(R.id.google_button)
    protected RelativeLayout mGoogleButton;

    @InjectView(R.id.google_sign_in_text)
    protected FontTextView mGoogleTextView;

    @InjectView(R.id.terms_privacy_container)
    protected LinearLayout mTermsPrivacyContainer;

    private UiLifecycleHelper mFacebookUiHelper;

    @Inject
    RegistrationController mRegistrationController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mFacebookUiHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mFacebookUiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in_sign_up_form, container, false);
        ButterKnife.inject(this, rootView);
        mPasswordField.setOnEditorActionListener(DoneActionListener);

        mRealFacebookButton.setFragment(this);
        //TODO may need to set more permissions onto the facebook login button
        //by default it comes with: email, user_birthday, user_location, user_activities, user_interests, user_work_history, user_friends, user_about_me, basic_info
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFacebookUiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFacebookUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFacebookUiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFacebookUiHelper.onSaveInstanceState(outState);
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
    protected void onFacebookButtonClick() {
        //passing on click to real facebook button
        mRealFacebookButton.performClick();
    }


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

    public LoginButton getRealFacebookButton() {
        return mRealFacebookButton;
    }


    public void onEventMainThread(LoginRegisterEvent registerEvent) {
        if (registerEvent.isSuccessful()) {
            Log.d(this.getClass().getSimpleName(), "Successfully Logged in!");
            startActivity(new Intent(getActivity(), NavActivity.class));
            getActivity().finish();
            return;
        }

        Toast.makeText(getActivity(), registerEvent.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    private Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG + ".Facebook", "Session State: " + session.getState());
            Log.d(TAG + ".Facebook", "Session:" + session);
            Log.d(TAG + ".Facebook", "Exception:" + exception);
            // TODO: Handle errors and other conditions.
            if (state.isOpened()) {
                facebookLogin();
            }
        }
    };

    public void facebookLogin() {
        Session session = Session.getActiveSession();
        mRegistrationController.facebookLogin(session.getAccessToken(),
                DateHelperUtil.doubleFromDate(session.getExpirationDate()));
    }


}
