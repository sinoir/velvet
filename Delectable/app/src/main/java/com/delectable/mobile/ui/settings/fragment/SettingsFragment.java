package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.controllers.S3ImageUploadController;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.FetchAccountFailedEvent;
import com.delectable.mobile.events.accounts.ProvisionProfilePhotoEvent;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.events.accounts.UpdatedProfileEvent;
import com.delectable.mobile.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.events.accounts.UpdatedSettingEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.settings.dialog.SetProfilePicDialog;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.delectable.mobile.util.NameUtil;
import com.delectable.mobile.util.SafeAsyncTask;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SettingsFragment extends BaseFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private static final int SELECT_PHOTO_REQUEST = 0;

    private static final int CAMERA_REQUEST = 1;

    @Inject
    AccountController mAccountController;

    @Inject
    AccountModel mAccountModel;

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

    @InjectView(R.id.delectable_version)
    TextView mVersionText;

    private String mUserId;
    //endregion

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

        mAccountController.fetchPrivateAccount(mUserId);
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

        mVersionText.setText(getString(R.string.settings_delectable_version, getAppVersion()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFacebookUiHelper.onResume();
        if (mUserAccount == null) {
            loadCachedAccount();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Bitmap bitmap = getImage(selectedImageUri);
            if (bitmap == null) {
                return; //unable to retrieve image from phone, don't do anything
            }
            mProfileImage.setImageBitmap(bitmap);
            provisionProfilePhoto(bitmap);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mProfileImage.setImageBitmap(photo);
            provisionProfilePhoto(photo);
        }
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
    //endregion

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

            String[] name = NameUtil.getSplitName(mNameField.getText().toString());
            String fName = name[NameUtil.FIRST_NAME];
            String lName = name[NameUtil.LAST_NAME];
            String url = mWebsiteField.getText().toString();
            String bio = mShortBioField.getText().toString();

            updateProfile(fName, lName, url, bio);
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
    //endregion


    //region Events
    public void onEventMainThread(UpdatedAccountEvent event) {
        if (!mUserId.equals(event.getAccountId())) {
            return;
        }
        loadCachedAccount();
    }
    //endregion

    public void onEventMainThread(FetchAccountFailedEvent event) {
        // TODO show error dialog
    }

    //region API Requests

    private void loadCachedAccount() {
        // Asynchronously retreive profile from local model
        new SafeAsyncTask<Account>(this) {
            @Override
            protected Account safeDoInBackground(Void[] params) {
                return mAccountModel.getAccount(mUserId);
            }

            @Override
            protected void safeOnPostExecute(Account account) {
                if (account != null) {
                    mUserAccount = account;
                    updateUI();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //region Setting Profile Photo Endpoints

    /**
     * Calls back to {@link #onEventMainThread(UpdatedProfilePhotoEvent)}
     */
    private void facebookifyProfilePhoto() {
        mAccountController.facebookifyProfilePhoto();
    }

    /**
     * The callback for {@link #facebookifyProfilePhoto()} and {@link #updateProfilePicture(ProvisionCapture)}
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

    private void provisionProfilePhoto(final Bitmap photo) {
        mPhoto = photo;
        mAccountController.provisionProfilePhoto();
    }

    public void onEventMainThread(ProvisionProfilePhotoEvent event) {
        if (event.isSuccessful()) {
            ProvisionCapture provision = event.getProvisionCapture();
            sendPhotoToS3(mPhoto, provision);
            mPhoto = null;
            return;
        }
        showToastError(event.getErrorMessage());
    }

    private void sendPhotoToS3(Bitmap photo, final ProvisionCapture provision) {
        S3ImageUploadController mImageUploadController = new S3ImageUploadController(getActivity(),
                provision);
        mImageUploadController.uploadImage(photo,
                new BaseNetworkController.SimpleRequestCallback() {
                    @Override
                    public void onSucess() {
                        Log.d(TAG, "Image Upload Done!");
                        updateProfilePicture(provision);
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        String message = S3ImageUploadController.TAG + " failed: " +
                                error.getCode() + " error: " + error.getMessage();
                        Log.d(TAG, message);
                        showToastError(message);
                        //TODO figure out how to handle error UI wise
                    }
                }
        );
    }

    /**
     * Calls back to {@link #onEventMainThread(UpdatedProfilePhotoEvent)}
     */
    private void updateProfilePicture(ProvisionCapture provision) {
        mAccountController.updateProfilePhoto(provision);
    }
    //endregion


    //region Profile Updates
    private void updateProfile(String fname, String lname, String url, String bio) {
        mAccountController.updateProfile(fname, lname, url, bio);
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

    private void addIdentifier(String string, String type) {
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
        //TODO hide facebook row if user is not facebook connected
        listItems.add(FACEBOOK, getString(R.string.settings_import_from_facebook));

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

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void setFacebookPhotoAsProfile() {
        facebookifyProfilePhoto();
    }
    //endregion
    //endregion


    //region Button Click Actions
    @OnClick(R.id.facebook_value)
    protected void onFacebookConnectClick(View v) {
        mRealFacebookLoginButton.performClick();
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
                .loadImageIntoView(getActivity(), mUserAccount.getPhoto().getUrl(), mProfileImage);
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

        if (mUserAccount.getFbId() != null) {
            mFacebookField.setText(R.string.settings_facebook_connected);
            mFacebookField.setSelected(true);
            mFacebookField.setClickable(false);
        } else {
            mFacebookField.setText(R.string.settings_facebook_connect);
            mFacebookField.setSelected(false);
            mFacebookField.setClickable(true);
        }
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

    public void facebookConnect() {
        Session session = Session.getActiveSession();

        //TODO associate fb endpoint
    }

}