package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.AssociateFacebookEvent;
import com.delectable.mobile.api.events.accounts.AssociateTwitterEvent;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.activity.WebViewActivity;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.settings.activity.NotificationsActivty;
import com.delectable.mobile.ui.settings.dialog.SetProfilePicDialog;
import com.delectable.mobile.util.CameraUtil;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.FontEnum;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.delectable.mobile.util.NameUtil;
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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;


public class SettingsFragment extends BaseFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

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
                facebookConnect();
                return;
            }

            //logout event
            if (state.isClosed()) {
                return;
            }

            // TODO: Handle more errors and other conditions.
            showToastError(getString(R.string.error_facebook_connect_failed));
        }
    };

    private Callback<TwitterSession> TwitterCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {

            TwitterUtil.TwitterInfo twitterInfo = TwitterUtil.getTwitterInfo(twitterSessionResult);

            //refreshing view before we make the call for immediate UI feed back
            mUserAccount.setTwId(twitterInfo.twitterId);
            mUserAccount.setTwScreenName(twitterInfo.screenName);
            mUserAccount.setTwToken(twitterInfo.token);
            mUserAccount.setTwTokenSecret(twitterInfo.tokenSecret);
            updateUI();

            mAccountController
                    .associateTwitter(ASSOCIATE_TWITTER, twitterInfo.twitterId, twitterInfo.token,
                            twitterInfo.tokenSecret, twitterInfo.screenName);
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

    private static final String UDPATE_PROFILE_PICTURE = TAG + "_UDPATE_PROFILE_PICTURE";

    private static final String UDPATE_PROFILE = TAG + "_UDPATE_PROFILE";

    private static final String REMOVE_IDENTIFIER = TAG + "_REMOVE_IDENTIFIER";

    private static final String FACEBOOK_CONNECT = TAG + "_FACEBOOK_CONNECT";

    private static final String FACEBOOKIFY_PROFILE_PHOTO = TAG + "_FACEBOOKIFY_PROFILE_PHOTO";

    private static final String ASSOCIATE_TWITTER = TAG + "_ASSOCIATE_TWITTER";

    private static final String FETCH_ACCOUNT = TAG + "_FETCH_ACCOUNT";

    //so that we can figure out whether the updatedAccountEvent was from a request that we initialized
    private static final HashSet<String> REQUEST_KEYS = new HashSet<String>();

    static {
        REQUEST_KEYS.add(UDPATE_PROFILE_PICTURE);
        REQUEST_KEYS.add(UDPATE_PROFILE);
        REQUEST_KEYS.add(REMOVE_IDENTIFIER);
        REQUEST_KEYS.add(FACEBOOK_CONNECT);
        REQUEST_KEYS.add(FACEBOOKIFY_PROFILE_PHOTO);
        REQUEST_KEYS.add(ASSOCIATE_TWITTER);
        REQUEST_KEYS.add(FETCH_ACCOUNT);
    }

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
    //endregion

    @InjectView(R.id.delectable_version)
    TextView mVersionText;

    private Typeface mWhitneyBookFont;

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

    private UiLifecycleHelper mFacebookUiHelper;

    private boolean mUpdatingViaDoneClick = false;

    /**
     * Flag that helps us stop fetchAccount from executing if we're returning to this scsreen from a
     * select photo action.
     */
    private boolean mResumedFromSelectPhotoAction = false;

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

        mWhitneyBookFont = FontEnum.WHITNEY_BOOK.getTypeface(getActivity());
    }

    //region Lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.inject(this, view);

        mRealFacebookLoginButton.setFragment(this);

        //need to manually set typeface instead of using FontEditText because of support library limitations with subclassing EditText
        mNameField.setTypeface(mWhitneyBookFont);
        mShortBioField.setTypeface(mWhitneyBookFont);
        mWebsiteField.setTypeface(mWhitneyBookFont);

        mHiddenTwitterLoginButton.setCallback(TwitterCallback);
        mVersionText.setText(getString(R.string.settings_delectable_version, getAppVersion()));

        return view;
    }

    @OnEditorAction(
            {R.id.name, R.id.short_bio, R.id.website, R.id.email_value, R.id.phone_number_value})
    protected boolean onKeyboardDoneAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            EditText editText = (EditText) v;
            String text = editText.getText().toString();
            mUpdatingViaDoneClick = true;
            updateInfo(editText, text);

            hideKeyboard();
            return true;
        }
        return false;
    }

    @OnFocusChange(
            {R.id.name, R.id.short_bio, R.id.website, R.id.email_value, R.id.phone_number_value})
    protected void onFocusLoss(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText) v;
            String text = editText.getText().toString();
            //don't double send the same request if it was already invoked by a done click
            if (mUpdatingViaDoneClick) {
                mUpdatingViaDoneClick = false;
                return;
            }
            updateInfo(editText, text);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFacebookUiHelper.onResume();
        if (mUserAccount == null) {
            mUserAccount = UserInfo.getAccountPrivate(getActivity());
        }
        boolean refreshPhoto = !mResumedFromSelectPhotoAction;
        updateUI(refreshPhoto);

        if (!mResumedFromSelectPhotoAction) {
            //fetch most recent account private from API
            mAccountController.fetchAccountPrivate(FETCH_ACCOUNT);
            mResumedFromSelectPhotoAction = false;
        }

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
        mFacebookUiHelper.onStop();
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
            mResumedFromSelectPhotoAction = true;
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
                    selectedImage = CameraUtil
                            .loadBitmapFromUri(selectedImageUri, CameraUtil.MAX_SIZE_PROFILE_IMAGE);
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

    //region Events
    public void onEventMainThread(UpdatedAccountEvent event) {
        if (!REQUEST_KEYS.contains(event.getRequestId())) {
            return;
        }

        mUserAccount = event.getAccount();
        updateUI();

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }
    }

    /**
     * Calls back to {@link #onEventMainThread(UpdatedAccountEvent)}
     */
    private void facebookifyProfilePhoto() {
        mAccountController.facebookifyProfilePhoto(FACEBOOKIFY_PROFILE_PHOTO);
    }

    //endregion Events

    /**
     * Calls back to {@link #onEventMainThread(UpdatedAccountEvent)}
     */
    private void updateProfilePicture(Bitmap photo) {
        mAccountController.updateProfilePhoto(UDPATE_PROFILE_PICTURE, photo);
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

        mAccountController.updateProfile(UDPATE_PROFILE, fName, lName, url, bio);
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        // The API only accepts 10 or 11 digit numbers and ignore validating clear field, since phone number is optional
        if (phoneNumber.length() != 10 && phoneNumber.length() != 11 && phoneNumber.length() != 0) {
            showToastError("Invalid phone number entered");
            updatePhoneNumberUI();
            return false;
        }
        return true;
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

    private void modifyPhone(String number) {
        number = number.replaceAll("[^0-9]", "");
        if (validatePhoneNumber(number)) {
            modifyIdentifier(mPhoneIdentifier, number, Identifier.Type.PHONE);
        }
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
        mAccountController.removeIdentifier(REMOVE_IDENTIFIER, identifier);
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
        mAccountController.associateFacebook(FACEBOOK_CONNECT, session.getAccessToken(),
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
            TwitterUtil.clearSession();
        }
        updateUI(); //ui reverts back to original state if error
    }

    @OnClick(R.id.notifications_row)
    protected void onNotificationsRowClick() {
        String title = getString(R.string.settings_notifications);
        startActivity(NotificationsActivty.newIntent(getActivity(), title));
    }

    @OnClick({R.id.send_feedback,
            R.id.terms_of_use,
            R.id.sign_out})
    void onAboutRowClick(View v) {
        switch (v.getId()) {
            case R.id.send_feedback:
                launchEmailFeedback();
                break;
            case R.id.terms_of_use:
                String url = getString(R.string.terms_url);
                String title = getString(R.string.signup_in_terms_of_use);
                startActivity(WebViewActivity.newIntent(getActivity(), url, title));
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

    private void updateUI() {
        updateUI(true);
    }

    /**
     * Updates the UI with the current {@link #mUserAccount} object.
     */
    private void updateUI(boolean refreshPhoto) {

        if (mUserAccount == null) {
            return;
        }

        //TODO temporary hack, if e_tag exists, then we know the account object we have a handle on is a context profile account
        if (mUserAccount.getETag() != null) {
            return;
        }

        //profile info
        if (refreshPhoto) {
            ImageLoaderUtil.loadImageIntoView(getActivity(), mUserAccount.getPhoto().getBestThumb(),
                    mProfileImage);
        }
        mNameField.setText(mUserAccount.getFullName());
        mShortBioField.setText(mUserAccount.getBio());
        mWebsiteField.setText(mUserAccount.getUrl());

        //account
        mEmailField.setText(mUserAccount.getEmail());

        //grab primary identifier for user's email
        mPrimaryEmailIdentifier = mUserAccount.getPrimaryEmailIdentifier();

        updatePhoneNumberUI();

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
    }

    private void updatePhoneNumberUI() {
        mPhoneIdentifier = mUserAccount.getPhoneIdentifier();
        String mPhoneNumber = mPhoneIdentifier == null ? null : mPhoneIdentifier.getString();
        mPhoneNumberField.setText(mPhoneNumber);
        PhoneNumberUtils.formatNumber(mPhoneNumberField.getText(), PhoneNumberUtils.FORMAT_NANP);
    }

}