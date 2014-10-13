package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.AssociateFacebookEvent;
import com.delectable.mobile.events.accounts.AssociateTwitterEvent;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.events.accounts.UpdatedProfileEvent;
import com.delectable.mobile.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.events.accounts.UpdatedSettingEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.settings.dialog.SetProfilePicDialog;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.delectable.mobile.util.NameUtil;
import com.delectable.mobile.util.PhotoUtil;
import com.delectable.mobile.util.TwitterUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SettingsFragment extends BaseFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG + ".Facebook", "Session State: " + session.getState());
            Log.d(TAG + ".Facebook", "Session:" + session);
            Log.d(TAG + ".Facebook", "Exception:" + exception);
            // TODO: Handle errors and other conditions.
            if (state.isOpened()) {
                facebookConnect();
            }
        }
    };

    private Callback<TwitterSession> TwitterCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {

            long twitterId = twitterSessionResult.data.getUserId();
            String screenName = twitterSessionResult.data.getUserName();

            //TODO improve when Twitter SDK is better documented
            //This is ghetto bc there were no docs when I made this, didn't know how to use the data.getAuthToken().getAuthHeaders() method
            //looks like this:
            //authtoken: token=[TOKEN_VALUE],secret=[SECRET_VALUE]
            String authCreds = twitterSessionResult.data.getAuthToken().toString();
            String[] splitAuthCreds = authCreds.split(",");
            String token = splitAuthCreds[0].split("token=")[1];
            String tokenSecret = splitAuthCreds[1].split("secret=")[1];

            //refreshing view before we make the call for immediate UI feed back
            mUserAccount.setTwId(twitterId);
            mUserAccount.setTwScreenName(screenName);
            mUserAccount.setTwToken(token);
            mUserAccount.setTwTokenSecret(tokenSecret);
            updateUI();

            mAccountController.associateTwitter(twitterId, token, tokenSecret, screenName);
        }

        @Override
        public void failure(TwitterException e) {
            //TODO debug this exception and show error, but don't show error if user clicked back intentionally
            Log.d(TAG, "Twitter auth error", e);
            Log.d(TAG, "Twitter error message:" + e.getMessage());
            //showToastError("Twitter authentication failed");
        }
    };

    private static final int SELECT_PHOTO_REQUEST = 0;

    private static final int CAMERA_REQUEST = 1;

    private static final int DISCONNECT_TWITTER = 2;

    private static final int DISCONNECT_FACEBOOK = 3;

    @Inject
    AccountController mAccountController;

    //region Views
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

    @InjectView(R.id.facebook_sign_in)
    LoginButton mRealFacebookLoginButton;

    @InjectView(R.id.facebook_value)
    TextView mFacebookField;

    @InjectView(R.id.twitter_login_button)
    TwitterLoginButton mHiddenTwitterLoginButton;

    @InjectView(R.id.twitter_value)
    FontTextView mTwitterField;

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
    //endregion

    @InjectView(R.id.delectable_version)
    TextView mVersionText;

    private String mUserId;

    private Account mUserAccount;

    /**
     * Used to keep track of which identifier is the primary email
     */
    private Identifier mPrimaryEmailIdentifier;

    /**
     * Used to keep track of the user's original phone number.
     */
    private Identifier mPhoneIdentifier;

    /**
     * Used to keep track of which photo to send to S3 after provisioning
     */
    private Bitmap mPhoto;

    private UiLifecycleHelper mFacebookUiHelper;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    //region Life Cycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        mFacebookUiHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mFacebookUiHelper.onCreate(savedInstanceState);

        mUserId = UserInfo.getUserId(getActivity());

    }

    //region Lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.inject(this, view);

        mRealFacebookLoginButton.setFragment(this);

        //listens for done button on soft keyboard
        TextView.OnEditorActionListener doneListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = (EditText) v;
                    String text = editText.getText().toString();
                    updateInfo(editText, text);

                    hideKeyboard();
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

        mHiddenTwitterLoginButton.setCallback(TwitterCallback);

        mVersionText.setText(getString(R.string.settings_delectable_version, getAppVersion()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFacebookUiHelper.onResume();
        if (mUserAccount == null) {
            mUserAccount = UserInfo.getAccountPrivate(getActivity());
        }
        updateUI();

        //fetch most recent account private from API
        mAccountController.fetchAccountPrivate(mUserId);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFacebookUiHelper.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //cached out account object in case identifiers/profile items were modified
        UserInfo.setAccountPrivate(mUserAccount);
    }
    //endregion

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

    private void updateInfo(EditText v, String text) {
        if (v.getId() == R.id.phone_number_value) {
            PhoneNumberUtils
                    .formatNumber(mPhoneNumberField.getText(), PhoneNumberUtils.FORMAT_NANP);
            modifyPhone(text);
        }
        if (v.getId() == R.id.email_value) {
            modifyEmail(text);
        }

        if (v.getId() == R.id.name ||
                v.getId() == R.id.short_bio ||
                v.getId() == R.id.website) {
            updateProfile();
        }
    }

    private boolean userProfileChanged() {
        if (!mUserAccount.getFullName().equals(mNameField.getText().toString())) {
            return true;
        }

        String urlField = mWebsiteField.getText().toString();

        //textview's text value is "" even if null is set to is
        if (mUserAccount.getUrl() == null) {
            if (!urlField.trim().equals("")) {
                return true;
            }
        }
        if (mUserAccount.getUrl() != null) {
            if (!mUserAccount.getUrl().equals(urlField)) {
                return true;
            }
        }

        String bioField = mShortBioField.getText().toString();

        //textview's text value is "" even if null is set to is
        if (mUserAccount.getBio() == null) {
            if (!bioField.trim().equals("")) {
                return true;
            }
        }
        if (mUserAccount.getBio() != null) {
            if (!mUserAccount.getBio().equals(bioField)) {
                return true;
            }
        }
        return false;
    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);
        mHiddenTwitterLoginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            updateProfileImageWithUri(selectedImageUri);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mProfileImage.setImageBitmap(photo);
            updateProfilePicture(photo);
        }

        if (requestCode == DISCONNECT_TWITTER && resultCode == Activity.RESULT_OK) {
            TwitterUtil.clearSession();
            Identifier twitterIdentifier = mUserAccount.getTwitterIdentifier();
            if (twitterIdentifier != null) {
                removeIdentifier(twitterIdentifier);
            }
        }
        if (requestCode == DISCONNECT_FACEBOOK && resultCode == Activity.RESULT_OK) {
            // Close FB Session
            Session session = Session.getActiveSession();
            if (session != null) {
                session.closeAndClearTokenInformation();
            }
            Identifier facebookIdentifier = mUserAccount.getFacebookIdentifier();
            if (facebookIdentifier != null) {
                removeIdentifier(facebookIdentifier);
            }
        }
    }

    private void updateProfileImageWithUri(final Uri selectedImageUri) {
        if (getActivity() == null) {
            return;
        }

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap selectedImage = null;
                try {
                    selectedImage = PhotoUtil.loadBitmapFromUri(selectedImageUri, 1024);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to open image", e);
                }
                return selectedImage;
            }

            @Override
            protected void onPostExecute(Bitmap selectedImage) {
                super.onPostExecute(selectedImage);
                if (selectedImage != null) {
                    mProfileImage.setImageBitmap(selectedImage);
                    updateProfilePicture(selectedImage);
                } else {
                    showToastError("Failed to load image");
                }
            }
        }.execute();
    }

    //endregion

    //region API Requests

    //region Setting Profile Photo Endpoints

    /**
     * @return Returns null if the image couldn't be retrieved.
     */
    private Bitmap getImage(Uri selectedImage) {
        try {
            if (getActivity() == null) {
                return null;
            }
            // TODO: Background thread here?
            Bitmap bm = MediaStore.Images.Media
                    .getBitmap(getActivity().getContentResolver(), selectedImage);
            return bm;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to open image", e);
            Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //region Events
    public void onEventMainThread(UpdatedAccountEvent event) {
        if (!mUserId.equals(event.getAccount().getId())) {
            return;
        }

        if (event.isSuccessful()) {
            mUserAccount = event.getAccount();
            updateUI();
            return;
        }
        showToastError(event.getErrorMessage());

    }

    /**
     * Calls back to {@link #onEventMainThread(UpdatedProfilePhotoEvent)}
     */
    private void facebookifyProfilePhoto() {
        mAccountController.facebookifyProfilePhoto();
    }

    //endregion

    /**
     * Calls back to {@link #onEventMainThread(UpdatedProfilePhotoEvent)}
     */
    private void updateProfilePicture(Bitmap photo) {
        mPhoto = photo;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] rawImageData = byteArrayOutputStream.toByteArray();
        mAccountController.updateProfilePhoto(rawImageData);
    }

    /**
     * The callback for {@link #facebookifyProfilePhoto()}
     */
    public void onEventMainThread(UpdatedProfilePhotoEvent event) {
        if (event.isSuccessful()) {
            PhotoHash photoHash = event.getPhoto();
            mUserAccount.setPhoto(photoHash);
            updateUI();
            return;
        }
        showToastError(event.getErrorMessage());
    }

    //region Profile Updates

    /**
     * Updates Profile if changed or fields are validated
     */
    private void updateProfile() {
        if (!userProfileChanged() || !validateNameField()) {
            return; //no need to call update
        }

        //profile fields
        String[] name = NameUtil.getSplitName(mNameField.getText().toString());
        String fName = name[NameUtil.FIRST_NAME];
        String lName = name[NameUtil.LAST_NAME];
        String url = mWebsiteField.getText().toString();
        String bio = mShortBioField.getText().toString();

        mAccountController.updateProfile(fName, lName, url, bio);
        return;
    }

    private boolean validateNameField() {
        String fullName = mNameField.getText().toString();
        if (fullName.trim().equals("")) {
            showToastError("Name cannot be blank");
            mNameField.setText(mUserAccount.getFullName());
            return false;
        }
        return true;
    }

    public void onEventMainThread(UpdatedProfileEvent event) {
        if (event.isSuccessful()) {
            mUserAccount.setFname(event.getFname());
            mUserAccount.setLname(event.getLname());
            mUserAccount.setUrl(event.getUrl());
            mUserAccount.setBio(event.getBio());
        } else {
            showToastError(event.getErrorMessage());
        }
        updateUI(); //ui reverts back to original state if error
    }

    private void modifyPhone(String number) {
        number = number.replaceAll("[^0-9]", "");
        modifyIdentifier(mPhoneIdentifier, number, Identifier.Type.PHONE);
    }

    private void modifyEmail(String email) {
        modifyIdentifier(mPrimaryEmailIdentifier, email, Identifier.Type.EMAIL);
    }

    /**
     * Used to modify email and phone numbers. Determines whether an identifier should be added,
     * updated or deleted.
     *
     * @param type see {@link Identifier.Type} values. Only used for adding an identifier.
     */
    private void modifyIdentifier(Identifier identifier, String replacementValue,
            Identifier.Type type) {
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
            addIdentifier(replacementValue, type);
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

    private void addIdentifier(String string, Identifier.Type type) {
        mAccountController.addIdentifier(string, type);
    }

    private void updateIdentifier(Identifier identifier, String string) {
        mAccountController.updateIdentifier(identifier, string);
    }

    private void removeIdentifier(Identifier identifier) {
        mAccountController.removeIdentifier(identifier);
    }

    public void onEventMainThread(UpdatedIdentifiersListingEvent event) {
        if (event.isSuccessful()) {
            mUserAccount.setIdentifiers(event.getIdentifiers());
        } else {
            showToastError(event.getErrorMessage());
        }
        updateUI(); //ui reverts back to original state if error
    }
    //endregion

    private void updateAccountSettings(AccountConfig.Key key, boolean setting) {
        mAccountController.updateSetting(key, setting);
    }

    public void onEventMainThread(UpdatedSettingEvent event) {
        if (event.isSuccessful()) {
            event.getKey();
            mUserAccount.getAccountConfig().setSetting(event.getKey(), event.getSetting());
        } else {
            showToastError(event.getErrorMessage());
        }
        updateUI(); //ui reverts back to original state if error
    }

    //region Profile Image Actions
    @OnClick(R.id.profile_image)
    void onProfileImageClick() {

        //show dialog to choose new photo source
        final int PHOTO_LIBRARY = 0;
        final int TAKE_PHOTO = 1;
        final int FACEBOOK = 2;

        ArrayList<String> listItems = new ArrayList<String>();
        listItems.add(PHOTO_LIBRARY, getString(R.string.settings_choose_from_library));
        listItems.add(TAKE_PHOTO, getString(R.string.settings_take_photo));
        if (mUserAccount.getFbId() != null) {
            listItems.add(FACEBOOK, getString(R.string.settings_import_from_facebook));
        }

        SetProfilePicDialog dialog = SetProfilePicDialog.newInstance(listItems);
        dialog.setCallback(new SetProfilePicDialog.Callback() {
            @Override
            public void onDialogItemClick(int position) {
                switch (position) {
                    case PHOTO_LIBRARY:
                        launchPhotoLibrary();
                        break;
                    case TAKE_PHOTO:
                        launchCamera();
                        break;
                    case FACEBOOK:
                        setFacebookPhotoAsProfile();
                        break;
                }
            }
        });
        dialog.show(getFragmentManager(), "dialog");
    }

    private void launchPhotoLibrary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        //alternative way of making intent, use if strange things happen with intent above
        //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, SELECT_PHOTO_REQUEST);
    }
    //endregion
    //endregion

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void setFacebookPhotoAsProfile() {
        facebookifyProfilePhoto();
    }

    //region Button Click Actions
    @OnClick(R.id.facebook_value)
    protected void onFacebookConnectClick(View v) {
        if (!v.isSelected()) {
            mRealFacebookLoginButton.performClick();
        } else {
            showConfirmationNoTitle(getString(R.string.settings_disconnect_facebook),
                    getString(R.string.settings_disconnect), null, DISCONNECT_FACEBOOK);
        }
    }

    public void facebookConnect() {
        Session session = Session.getActiveSession();
        mAccountController.associateFacebook(session.getAccessToken(),
                DateHelperUtil.doubleFromDate(session.getExpirationDate()));
    }

    public void onEventMainThread(AssociateFacebookEvent event) {
        if (event.isSuccessful()) {
            mUserAccount = event.getAcount();
        } else {
            showToastError(event.getErrorMessage());
        }
        updateUI(); //ui reverts back to original state if error
    }

    @OnClick(R.id.twitter_value)
    protected void onTwitterConnectClick(FontTextView view) {
        if (!view.isSelected()) {
            //bring user to twitter login
            mHiddenTwitterLoginButton.performClick();
        } else {
            showConfirmationNoTitle(getString(R.string.settings_disconnect_twitter),
                    getString(R.string.settings_disconnect), null, DISCONNECT_TWITTER);
        }
    }

    public void onEventMainThread(AssociateTwitterEvent event) {
        if (event.isSuccessful()) {
            mUserAccount = event.getAcount();
        } else {
            showToastError(event.getErrorMessage());
        }
        updateUI(); //ui reverts back to original state if error
    }


    @OnClick({R.id.following_phone_notification,
            R.id.comment_phone_notification,
            R.id.tagged_phone_notification,
            R.id.following_email_notification,
            R.id.comment_email_notification,
            R.id.tagged_email_notification})
    void onNotificationClick(View v) {

        v.setSelected(!v.isSelected());

        AccountConfig.Key key = null;
        switch (v.getId()) {
            case R.id.following_phone_notification:
                key = AccountConfig.Key.PN_NEW_FOLLOWER;
                break;
            case R.id.comment_phone_notification:
                key = AccountConfig.Key.PN_COMMENT_ON_OWN_WINE;
                break;
            case R.id.tagged_phone_notification:
                key = AccountConfig.Key.PN_TAGGED;
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
        TextView tv = (TextView) v;
        String text = tv.getText().toString();

        switch (v.getId()) {
            case R.id.contact:
                launchEmailFeedback();
                break;
            case R.id.terms_of_use:
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                //TODO go to terms of use
                break;
            case R.id.privacy_policy:
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                //TODO go to privacy policy
                break;
            case R.id.sign_out:
                signout();
                break;
        }
    }

    public void launchEmailFeedback() {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText =
                "mailto:" + Uri.encode(getString(R.string.settings_contact_email)) +
                        "?subject=" + Uri.encode(getString(R.string.settings_contact_subject)) +
                        "&body=" + Uri.encode(prepareEmailBody());
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        startActivity(Intent.createChooser(send,
                getString(R.string.settings_contact_email_intent_dialog_title)));
    }

    private String prepareEmailBody() {

        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.settings_contact_myAndroidDevice, Build.MANUFACTURER,
                Build.MODEL))
                .append("\n")
                .append(getString(R.string.settings_contact_myAndroidVersion,
                        Build.VERSION.RELEASE))
                .append("\n")
                .append(getString(R.string.settings_contact_myAppVersion, getAppVersion()))
                .append("\n")
                .append(getString(R.string.settings_contact_user,
                        UserInfo.getUserId(getActivity())))
                .append("\n\n")
                .append(getString(R.string.settings_contact_explanation))
                .append("\n\n")
                .append("--")
                .append("\n\n");
        return builder.toString();
    }

    private String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    //endregion

    /**
     * Updates the UI with the current {@link #mUserAccount} object.
     */
    private void updateUI() {

        if (mUserAccount == null) {
            return;
        }

        //TODO temporary hack, if e_tag exists, then we know the account object we have a handle on is a context profile account
        if (mUserAccount.getETag() != null) {
            return;
        }

        //profile info
        ImageLoaderUtil
                .loadImageIntoView(getActivity(), mUserAccount.getPhoto().getBestThumb(),
                        mProfileImage);
        mNameField.setText(mUserAccount.getFullName());
        mShortBioField.setText(mUserAccount.getBio());
        mWebsiteField.setText(mUserAccount.getUrl());

        //account
        mEmailField.setText(mUserAccount.getEmail());

        //grab primary identifier for user's email
        mPrimaryEmailIdentifier = mUserAccount.getPrimaryEmailIdentifier();

        mPhoneIdentifier = mUserAccount.getPhoneIdentifier();
        String mPhoneNumber = mPhoneIdentifier == null ? null : mPhoneIdentifier.getString();
        mPhoneNumberField.setText(mPhoneNumber);
        PhoneNumberUtils.formatNumber(mPhoneNumberField.getText(), PhoneNumberUtils.FORMAT_NANP);

        if (mUserAccount.isFacebookConnected()) {
            mFacebookField.setText(R.string.settings_facebook_connected);
            mFacebookField.setSelected(true);
        } else {
            mFacebookField.setText(R.string.settings_facebook_connect);
            mFacebookField.setSelected(false);
        }

        if (mUserAccount.isTwitterConnected() && TwitterUtil.isLoggedIn()) {
            mTwitterField.setText("@" + mUserAccount.getTwScreenName());
            mTwitterField.setSelected(true);
        } else {
            mTwitterField.setText(R.string.settings_facebook_connect);
            mTwitterField.setSelected(false);
        }

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