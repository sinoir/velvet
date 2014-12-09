package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.MotdController;
import com.delectable.mobile.api.controllers.RegistrationController;
import com.delectable.mobile.api.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.Delectabutton;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.registration.dialog.LoadingCircleDialog;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.FontEnum;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Sign up and Sign in screens both share the same xml view, this base class takes care of
 * butterknifing the views.
 */
public abstract class BaseSignUpInFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_EMAIL_AUTOCOMPLETE = 0;

    private static final String FACEBOOK_LOGIN_EVENT = "FACEBOOK_LOGIN_EVENT";

    //not making this TAG static in order to get the concrete class'name to show up in Logs
    private final String TAG = this.getClass().getSimpleName();

    private Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG + ".Facebook", "Session State: " + session.getState());
            Log.d(TAG + ".Facebook", "Session:" + session);
            Log.d(TAG + ".Facebook", "Exception:" + exception);

            if (session.getState().equals(SessionState.OPENING)) {
                return;
            }

            if (state.isOpened()) {
                facebookLogin();
                return;
            }

            // TODO: Handle more errors and other conditions.
            showToastError(getString(R.string.error_facebook_connect_failed));
        }
    };

    //region Views
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
     * This is merely the UI button, clicking this will programmatically invoke a click on the real
     * facebook button.
     */
    @InjectView(R.id.facebook_button)
    protected Delectabutton mFacebookButton;

    @InjectView(R.id.facebook_sign_in)
    protected LoginButton mRealFacebookButton;

    @InjectView(R.id.google_button)
    protected Delectabutton mGoogleButton;
    //endregion

    @InjectView(R.id.terms_privacy_container)
    protected LinearLayout mTermsPrivacyContainer;

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

    protected LoadingCircleDialog mLoadingDialog;

    @Inject
    RegistrationController mRegistrationController;

    @Inject
    MotdController mMotdController;

    private UiLifecycleHelper mFacebookUiHelper;

    /**
     * The default email gotten from the phone, that we use to prepopulate the email field here and
     * in {@link com.delectable.mobile.ui.registration.dialog.ResetPasswordDialog}
     */
    private String mPhoneEmail;

    private Typeface mWhitneyBookFont;

    //region Lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mFacebookUiHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mFacebookUiHelper.onCreate(savedInstanceState);

        mWhitneyBookFont = FontEnum.WHITNEY_BOOK.getTypeface(getActivity());

        //TODO loader is not retrieving email properly for some reason, commented out for now. might be relate to v21 support library
        //getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in_sign_up_form, container, false);
        ButterKnife.inject(this, rootView);
        mPasswordField.setOnEditorActionListener(DoneActionListener);

        mNameField.setTypeface(mWhitneyBookFont);
        mEmailField.setTypeface(mWhitneyBookFont);
        mPasswordField.setTypeface(mWhitneyBookFont);

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
    public void onStop() {
        super.onStop();
        mFacebookUiHelper.onStop();
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
    //endregion


    //region Email Prepopulation
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arguments) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        if (cursor == null) {
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            // Potentially filter on ProfileQuery.IS_PRIMARY
            cursor.moveToNext();
        }

        if (emails.size() > 0) {
            mPhoneEmail = emails.get(0);
            mEmailField.setText(mPhoneEmail);
        }
    }
    //endregion

    //region Button Action Listeners

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

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
    //endregion

    //region Getters
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

    public Delectabutton getFacebookButton() {
        return mFacebookButton;
    }

    public Delectabutton getGoogleButton() {
        return mGoogleButton;
    }

    public LinearLayout getTermsPrivacyContainer() {
        return mTermsPrivacyContainer;
    }

    public LoginButton getRealFacebookButton() {
        return mRealFacebookButton;
    }

    public String getPhoneEmail() {
        return mPhoneEmail;
    }
    //endregion

    public void onEventMainThread(LoginRegisterEvent registerEvent) {

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }

        if (registerEvent.isSuccessful()) {

            //initialize Motd fetch
            String deviceId = Settings.Secure.getString(
                    getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            mMotdController.getMotd(UserInfo.getSessionKey(getActivity()), deviceId);

            startActivity(new Intent(getActivity(), NavActivity.class));
            getActivity().finish();
            return;
        }

        if (ErrorUtil.INVALID_CREDENTIALS == registerEvent.getErrorCode()) {
            showToastError(R.string.error_invalid_username_password);
        } else if (ErrorUtil.NO_NETWORK_ERROR == registerEvent.getErrorCode()) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else {
            showToastError(registerEvent.getErrorMessage());
        }
    }

    public void facebookLogin() {
        mLoadingDialog = new LoadingCircleDialog();
        mLoadingDialog.show(getFragmentManager(), LoadingCircleDialog.TAG);

        Session session = Session.getActiveSession();
        mRegistrationController.facebookLogin(FACEBOOK_LOGIN_EVENT, session.getAccessToken(),
                DateHelperUtil.doubleFromDate(session.getExpirationDate()));
    }

    private interface ProfileQuery {

        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


}
