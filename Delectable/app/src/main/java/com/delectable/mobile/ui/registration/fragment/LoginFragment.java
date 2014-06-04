package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.controllers.RegistrationController;
import com.delectable.mobile.api.requests.BaseRegistrations;
import com.delectable.mobile.api.requests.RegistrationsFacebook;
import com.delectable.mobile.api.requests.RegistrationsLogin;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.home.activity.HomeActivity;
import com.delectable.mobile.util.DateHelperUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int sLoaderEmailAutocomplete = 0;

    private static final String TAG = "LoginFragment";

    private Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG, "FB State:" + state);
            Log.d(TAG, "FB Session:" + session);
            Log.d(TAG, "FB exception:" + exception);
            // TODO: Handle errors and other conditions.
            if (state.isOpened()) {
                facebookLogin();
            }
        }
    };

    private ProgressBar mLoginProgress;

    private AutoCompleteTextView mEmailView;

    private EditText mPasswordView;

    private RegistrationController mRegistrationController;

    private UiLifecycleHelper mFacebookUiHelper;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationController = new RegistrationController(getActivity());

        mFacebookUiHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mFacebookUiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginProgress = (ProgressBar) rootView.findViewById(R.id.login_progress);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) rootView.findViewById(R.id.email);

        mPasswordView = (EditText) rootView.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) rootView.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        LoginButton fbLoginButton = (LoginButton) rootView.findViewById(R.id.facebook_sign_in);
        fbLoginButton.setFragment(this);
        fbLoginButton.setReadPermissions(
                Arrays.asList("user_work_history", "email", "user_birthday", "user_interests",
                        "user_activities")
        );

        getLoaderManager().initLoader(sLoaderEmailAutocomplete, null, this);

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

    public void attemptLogin() {
        // TODO: Hide Keyboard...
        if (mLoginProgress.getVisibility() != View.GONE) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            performLogin();
        }
    }

    public void facebookLogin() {
        Session session = Session.getActiveSession();
        Log.d(TAG, "FBLogin SessionState: " + session.getState());
        Log.d(TAG, "FBLogin SessionState: " + session.getState());
        mLoginProgress.setVisibility(View.VISIBLE);
        RegistrationsFacebook facebookRequest = new RegistrationsFacebook();
        facebookRequest.setFacebookToken(session.getAccessToken());
        facebookRequest.setFacebookTokenExpiration(
                DateHelperUtil.doubleFromDate(session.getExpirationDate()));
        registerWithRequest(facebookRequest);
    }

    private void performLogin() {
        RegistrationsLogin loginRequest = new RegistrationsLogin();
        loginRequest.setEmail(mEmailView.getText().toString());
        loginRequest.setPassword(mPasswordView.getText().toString());
        registerWithRequest(loginRequest);
    }

    private void registerWithRequest(BaseRegistrations request) {
        mLoginProgress.setVisibility(View.VISIBLE);
        mRegistrationController
                .registerUser(request, new BaseNetworkController.SimpleRequestCallback() {
                    @Override
                    public void onSucess() {
                        mLoginProgress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Successfully Logged in!", Toast.LENGTH_LONG)
                                .show();
                        Intent launchIntent = new Intent();
                        launchIntent.setClass(getActivity(), HomeActivity.class);
                        startActivity(launchIntent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        mLoginProgress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Failed Logging in!", Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
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
