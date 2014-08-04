package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.IdentifiersListing;
import com.delectable.mobile.api.requests.AccountsAddIdentifierRequest;
import com.delectable.mobile.api.requests.AccountsContextRequest;
import com.delectable.mobile.api.requests.AccountsRemoveIdentifierRequest;
import com.delectable.mobile.api.requests.AccountsUpdateIdentifierRequest;
import com.delectable.mobile.api.requests.AccountsUpdateProfileRequest;
import com.delectable.mobile.api.requests.AccountsUpdateSettingRequest;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SettingsFragment extends BaseFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private static final int FIRST_NAME = 0;

    private static final int LAST_NAME = 1;

    private String mUserId;

    private AccountsNetworkController mAccountsNetworkController;

    private BaseNetworkController mNetworkController;

    private Account mUserAccount;

    @InjectView(R.id.profile_image)
    CircleImageView mProfileImage;

    @InjectView(R.id.name)
    EditText mNameField;

    @InjectView(R.id.short_bio)
    EditText mShortBioField;

    @InjectView(R.id.website)
    EditText mWebsiteField;

    @InjectView(R.id.email_value)
    EditText mEmailField;

    @InjectView(R.id.phone_number_value)
    EditText mPhoneNumberField;

    @InjectView(R.id.facebook_value)
    EditText mFacebookField;

    @InjectView(R.id.twitter_value)
    EditText mTwitterField;

    @InjectView(R.id.following_phone_notification)
    ImageButton mFollowingPhoneIcon;

    @InjectView(R.id.comment_phone_notification)
    ImageButton mCommentPhoneIcon;

    @InjectView(R.id.tagged_phone_notification)
    ImageButton mTaggedPhoneIcon;

    @InjectView(R.id.following_email_notification)
    ImageButton mFollowingEmailIcon;

    @InjectView(R.id.comment_email_notification)
    ImageButton mCommentEmailIcon;

    @InjectView(R.id.tagged_email_notification)
    ImageButton mTaggedEmailIcon;

    /**
     * Used to keep track of which identifier is the primary email
     */
    private Identifier mPrimaryEmailIdentifier;

    /**
     * Used to keep track of the user's original phone number.
     */
    private Identifier mPhoneIdentifier;


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
        mNetworkController = new AccountsNetworkController(getActivity());
        mUserId = UserInfo.getUserId(getActivity());

        Bundle args = getArguments();
        if (args != null) {
            //no arguments passed into this fragment
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.inject(this, view);

        //listens for done button on soft keyboard
        TextView.OnEditorActionListener doneListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = (EditText) v;
                    String text = editText.getText().toString();
                    updateInfo(editText, text);

                    //hide keyboard
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                    return true;
                }
                return false;
            }
        };
        mNameField.setOnEditorActionListener(doneListener);
        mShortBioField.setOnEditorActionListener(doneListener);
        mWebsiteField.setOnEditorActionListener(doneListener);

        mEmailField.setOnEditorActionListener(doneListener);
        mPhoneNumberField.setOnEditorActionListener(doneListener);

        //listens for focus loss, so we know when to init an update info request
        View.OnFocusChangeListener focusLossListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText editText = (EditText) v;
                    String text = editText.getText().toString();
                    updateInfo(editText, text);
                }
            }
        };
        mNameField.setOnFocusChangeListener(focusLossListener);
        mShortBioField.setOnFocusChangeListener(focusLossListener);
        mWebsiteField.setOnFocusChangeListener(focusLossListener);

        mEmailField.setOnFocusChangeListener(focusLossListener);
        mPhoneNumberField.setOnFocusChangeListener(focusLossListener);

        return view;
    }


    private void updateInfo(EditText v, String text) {
        if (v.getId() == R.id.phone_number_value) {
            modifyPhone(text);
        }
        if (v.getId() == R.id.email_value) {
            modifyEmail(text);
        }
        //profile fields
        if (v.getId() == R.id.name ||
                v.getId() == R.id.short_bio ||
                v.getId() == R.id.website) {

            if (!userProfileChanged()) {
                return; //no need to call update
            }

            String[] name = getSplitName(mNameField.getText().toString());
            String fName = name[FIRST_NAME];
            String lName = name[LAST_NAME];
            String url = mWebsiteField.getText().toString();
            String bio = mShortBioField.getText().toString();

            updateProfile(fName, lName, url, bio);
        }
    }

    private boolean userProfileChanged() {
        if (!mUserAccount.getFullName().equals(mNameField.getText().toString())) {
            return true;
        }
        if (!mUserAccount.getUrl().equals(mWebsiteField.getText().toString())) {
            return true;
        }
        if (!mUserAccount.getBio().equals(mShortBioField.getText().toString())) {
            return true;
        }
        return false;
    }

    private String[] getSplitName(String fullName) {

        String[] name = new String[2];

        //purge new lines
        fullName = fullName.replaceAll("\\r\\n|\\r|\\n", "");

        if (fullName == null || fullName.equals("")) {
            name[FIRST_NAME] = "";
            name[LAST_NAME] = "";
            return name;
        }

        //split name by whitespace
        String[] splitName = fullName.split("\\s+");

        //mononym
        if (splitName.length == 1) {
            name[FIRST_NAME] = fullName;
            //server thing, it doesn't take empty strings as a name, but it does white space
            name[LAST_NAME] = " ";
            return name;
        }

        //rebuild first name(s), excluding last string in splitname
        String[] firstNames = Arrays.copyOfRange(splitName, 0, splitName.length - 1);
        name[FIRST_NAME] = TextUtils.join(" ", firstNames);
        name[LAST_NAME] = splitName[splitName.length - 1];

        return name;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    //region API REQUESTS
    private void loadData() {
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PRIVATE);
        request.setId(mUserId);
        mAccountsNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mUserAccount = (Account) result;
                        updateUI();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        Log.d(TAG, "Results Failed! " + error.getMessage() + " Code:" + error
                                .getCode());
                        showToastError(error.getMessage());
                        updateUI();
                    }
                }
        );
    }

    /**
     * Parameters can be null.
     */
    private void updateProfile(final String fname, final String lname, final String url,
            final String bio) {
        AccountsUpdateProfileRequest request = new AccountsUpdateProfileRequest();
        request.setFname(fname);
        request.setLname(lname);
        request.setUrl(url);
        request.setBio(bio);
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mUserAccount.setFname(fname);
                        mUserAccount.setLname(lname);
                        mUserAccount.setUrl(url);
                        mUserAccount.setBio(bio);
                        updateUI();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        showToastError(error.getCode() + " error: " + error.getMessage());
                        updateUI(); //revert ui back to it's original state
                    }
                }
        );
    }


    private void modifyPhone(String number) {
        modifyIdentifier(mPhoneIdentifier, number, Identifier.Type.PHONE);
    }

    private void modifyEmail(String email) {
        modifyIdentifier(mPrimaryEmailIdentifier, email, Identifier.Type.EMAIL);
    }

    /**
     * Used to modify email and phone numbers. Determines whether an identifier should be added,
     * updated or deleted.
     *
     * @param type see {@link com.delectable.mobile.api.models.Identifier.Type} values. Only used
     *             for adding an identifier.
     */
    private void modifyIdentifier(Identifier identifier, String replacementValue, String type) {
        String currentValue = null;
        //this identifier exists in the user object
        if (identifier != null) {
            currentValue = identifier.getString();
        }
        //there were no changes to the identifier value
        if (replacementValue.equals(currentValue)) {
            return;
        }

        //no identifier originally, user inputted value so we add identifier
        if (currentValue == null || currentValue.equals("")) {
            //no value entered as well, do nothing
            if (replacementValue == null || replacementValue.equals("")) {
                return;
            }
            addIdentifier(currentValue, type);
            return;
        }
        //by this point, null phone identifiers will have been handled

        //if the user wipes out their number, we remove the phone number
        if (replacementValue == null || replacementValue.equals("")) {
            removeIdentifier(identifier);
            return;
        }

        //if all base cases above haven't been met, then the number was changed, so we update
        updateIdentifier(identifier, replacementValue);
    }

    private void addIdentifier(String string, String type) {
        AccountsAddIdentifierRequest request = new AccountsAddIdentifierRequest(string, type);
        mNetworkController.performRequest(request, IdentifierChangeCallback);
    }

    private void updateIdentifier(Identifier identifier, String string) {
        AccountsUpdateIdentifierRequest request = new AccountsUpdateIdentifierRequest(identifier,
                string);
        mNetworkController.performRequest(request, IdentifierChangeCallback);
    }

    private void removeIdentifier(Identifier identifier) {
        AccountsRemoveIdentifierRequest request = new AccountsRemoveIdentifierRequest(identifier);
        mNetworkController.performRequest(request, IdentifierChangeCallback);
    }

    private BaseNetworkController.RequestCallback IdentifierChangeCallback
            = new BaseNetworkController.RequestCallback() {
        @Override
        public void onSuccess(BaseResponse result) {
            Log.d(TAG, "Received Results! " + result);
            //update user object with new identifiers listing
            IdentifiersListing identifiersListing = (IdentifiersListing) result;
            ArrayList<Identifier> identifiers = identifiersListing.getIdentifiers();
            mUserAccount.setIdentifiers(identifiers);
            updateUI();
        }

        @Override
        public void onFailed(RequestError error) {
            Log.d(TAG, "Results Failed! " + error.getMessage() + " Code: " + error.getCode());
            showToastError(error.getCode() + " error: " + error.getMessage());
            updateUI(); //updating ui so that user entered text is reverted back to original
        }
    };


    private void updateAccountSettings(String key, boolean setting) {
        AccountsUpdateSettingRequest request = new AccountsUpdateSettingRequest(key, setting);
        Log.d(TAG, request.toString());
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Log.d(TAG, "Received Results! " + result);
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        //TODO handle settings update error
                        Log.d(TAG, "Results Failed! " + error.getMessage() + " Code:" + error
                                .getCode());
                        showToastError(error.getCode() + " error: " + error.getMessage());
                    }
                }
        );
    }
    //endregion

    @OnClick({R.id.following_phone_notification,
            R.id.comment_phone_notification,
            R.id.tagged_phone_notification,
            R.id.following_email_notification,
            R.id.comment_email_notification,
            R.id.tagged_email_notification})
    void onNotificationClick(View v) {

        v.setSelected(!v.isSelected());

        String key = null;
        switch (v.getId()) {
            case R.id.following_phone_notification:
                key = AccountsUpdateSettingRequest.PN_NEW_FOLLOWER;
                break;
            case R.id.comment_phone_notification:
                key = AccountsUpdateSettingRequest.PN_COMMENT_ON_OWN_WINE;
                break;
            case R.id.tagged_phone_notification:
                key = AccountsUpdateSettingRequest.PN_TAGGED;
                break;
            case R.id.following_email_notification:
                //TODO following_email_notification
                break;
            case R.id.comment_email_notification:
                //TODO comment_email_notification
                break;
            case R.id.tagged_email_notification:
                //TODO tagged_email_notification
                break;
        }

        if (key != null) {
            updateAccountSettings(key, v.isSelected());
        }
    }

    @OnClick({R.id.contact,
            R.id.terms_of_use,
            R.id.privacy_policy,
            R.id.sign_out})
    void onAboutRowClick(View v) {

        //TODO remove later, debugging toast
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            String text = tv.getText().toString();
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }

        switch (v.getId()) {
            case R.id.contact:
                //TODO go to contact
                break;
            case R.id.terms_of_use:
                //TODO go to terms of use
                break;
            case R.id.privacy_policy:
                //TODO go to privacy policy
                break;
            case R.id.sign_out:
                //TODO sign out
                break;
        }
    }

    /**
     * Updates the UI with the current {@link #mUserAccount} object.
     */
    private void updateUI() {

        if (mUserAccount == null) {
            return;
        }
        //profile info
        ImageLoaderUtil
                .loadImageIntoView(getActivity(), mUserAccount.getPhoto().getUrl(), mProfileImage);
        mNameField.setText(mUserAccount.getFullName());
        mShortBioField.setText(mUserAccount.getBio());
        mWebsiteField.setText(mUserAccount.getUrl());

        //account
        mEmailField.setText(mUserAccount.getEmail());
        //grab primary identifier for user's email
        for (Identifier identifier : mUserAccount.getIdentifiers()) {
            if (identifier.getPrimary()) {
                mPrimaryEmailIdentifier = identifier;
                break;
            }
        }
        mPhoneIdentifier = null;
        String mPhoneNumber = null;
        //grab first phone identifier value
        for (Identifier identifier : mUserAccount.getIdentifiers()) {
            if (identifier.getType().equalsIgnoreCase(Identifier.Type.PHONE)) {
                mPhoneIdentifier = identifier;
                mPhoneNumber = identifier.getString();
                break;
            }
        }
        mPhoneNumberField.setText(mPhoneNumber);

        //TODO connect facebook
        //mFacebookField.setText(mUserAccount.getEmail());
        //TODO connect twitter
        //mTwitterField.setText(mUserAccount.getEmail());

        //notifications
        boolean followingYouPhone = mUserAccount.getAccountConfig().getPnNewFollower();
        boolean commentPhone = mUserAccount.getAccountConfig().getPnCommentOnOwnWine();
        boolean taggedPhone = mUserAccount.getAccountConfig().getPnTagged();
        mFollowingPhoneIcon.setSelected(followingYouPhone);
        mCommentPhoneIcon.setSelected(commentPhone);
        mTaggedPhoneIcon.setSelected(taggedPhone);

        //TODO no fields for email notifications yet for API, implement when ready
    }
}