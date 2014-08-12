package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.controllers.S3ImageUploadController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.IdentifiersListing;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.AccountsAddIdentifierRequest;
import com.delectable.mobile.api.requests.AccountsFacebookifyProfilePhotoRequest;
import com.delectable.mobile.api.requests.AccountsProvisionProfilePhotoRequest;
import com.delectable.mobile.api.requests.AccountsRemoveIdentifierRequest;
import com.delectable.mobile.api.requests.AccountsUpdateIdentifierRequest;
import com.delectable.mobile.api.requests.AccountsUpdateProfilePhotoRequest;
import com.delectable.mobile.api.requests.AccountsUpdateProfileRequest;
import com.delectable.mobile.api.requests.AccountsUpdateSettingRequest;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.FetchAccountFailedEvent;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.delectable.mobile.ui.settings.dialog.SetProfilePicDialog;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.delectable.mobile.util.SafeAsyncTask;
import com.facebook.Session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SettingsFragment extends BaseFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private static final int FIRST_NAME = 0;

    private static final int LAST_NAME = 1;

    private static final int SELECT_PHOTO_REQUEST = 0;

    private static final int CAMERA_REQUEST = 1;

    private String mUserId;

    private AccountsNetworkController mAccountsNetworkController;

    private BaseNetworkController mNetworkController;

    private Account mUserAccount;

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

    @InjectView(R.id.delectable_version)
    TextView mVersionText;
    //endregion

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

    //region Life Cycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
        mNetworkController = new AccountsNetworkController(getActivity());
        mUserId = UserInfo.getUserId(getActivity());

        mAccountController.fetchPrivateAccount(mUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
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

        mVersionText.setText(getString(R.string.settings_delectable_version, getAppVersion()));

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onResume() {
        super.onResume();
        if (mUserAccount == null) {
            loadCachedAccount();
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

    public void onEventMainThread(FetchAccountFailedEvent event) {
        // TODO show error dialog
    }
    //endregion


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

    //region API Requests

    //region Setting Profile Photo Endpoints
    private void facebookifyProfilePhoto() {
        AccountsFacebookifyProfilePhotoRequest request
                = new AccountsFacebookifyProfilePhotoRequest();
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        PhotoHash photoHash = (PhotoHash) result;
                        Log.d(TAG, "facebookify profile photo successful: " + photoHash.getUrl());
                        mUserAccount.setPhoto(photoHash);
                        updateUI();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        String message = AccountsFacebookifyProfilePhotoRequest.TAG + " failed: " +
                                error.getCode() + " error: " + error.getMessage();
                        Log.d(TAG, message);
                        showToastError(message);
                        //TODO figure out how to handle error UI wise
                    }
                });
    }

    private void provisionProfilePhoto(final Bitmap photo) {
        AccountsProvisionProfilePhotoRequest request = new AccountsProvisionProfilePhotoRequest();
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        ProvisionCapture provision = (ProvisionCapture) result;
                        Log.d(TAG, "provision successful: " + provision.getHeaders().getUrl());
                        sendPhotoToS3(photo, provision);
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        String message = AccountsProvisionProfilePhotoRequest.TAG + " failed: " +
                                error.getCode() + " error: " + error.getMessage();
                        Log.d(TAG, message);
                        showToastError(message);
                        //TODO figure out how to handle error UI wise
                    }
                }
        );
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

    private void updateProfilePicture(ProvisionCapture provision) {
        AccountsUpdateProfilePhotoRequest request = new AccountsUpdateProfilePhotoRequest(
                provision.getBucket(), provision.getFilename());
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        PhotoHash photoHash = (PhotoHash) result;
                        Log.d(TAG, "updateProfilePicture successful: " + photoHash.getUrl());
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        String message = AccountsUpdateProfilePhotoRequest.TAG + " failed: " +
                                error.getCode() + " error: " + error.getMessage();
                        Log.d(TAG, message);
                        showToastError(message);
                        //TODO figure out how to handle error UI wise
                    }
                }
        );
    }
    //endregion

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
                        String message = AccountsUpdateProfileRequest.TAG + " failed: " +
                                error.getCode() + " error: " + error.getMessage();
                        Log.d(TAG, message);
                        showToastError(message);
                        //TODO figure out how to handle error UI wise
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
            String message = " Accounts Add/Update/Remove Identifier failed: " +
                    error.getCode() + " error: " + error.getMessage();
            Log.d(TAG, message);
            showToastError(message);
            //TODO figure out how to handle error UI wise
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
                        String message = AccountsUpdateSettingRequest.TAG + " failed: " +
                                error.getCode() + " error: " + error.getMessage();
                        Log.d(TAG, message);
                        showToastError(message);
                        //TODO figure out how to handle error UI wise
                    }
                }
        );
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


    //region Button Click Actions
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


    public void signout() {
        // TODO: Some Common Logout function- grabbed straight from HomeFragment
        Session session = Session.getActiveSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
        }
        UserInfo.onSignOut(getActivity());
        Intent launchIntent = new Intent();
        launchIntent.setClass(getActivity(), LoginActivity.class);
        startActivity(launchIntent);
        getActivity().finish();
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