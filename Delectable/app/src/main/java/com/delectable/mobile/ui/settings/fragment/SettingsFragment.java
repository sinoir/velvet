package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.requests.AccountsContextRequest;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsFragment extends BaseFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private String mUserId;

    private AccountsNetworkController mAccountsNetworkController;

    private Account mUserAccount;

    private CircleImageView mProfileImage;

    private EditText mNameField;

    private EditText mShortBioField;

    private EditText mWebsiteField;

    private EditText mEmailField;

    private EditText mPhoneNumberField;

    private EditText mFacebookField;

    private EditText mTwitterField;

    private ImageButton mFollowingPhoneIcon;

    private ImageButton mCommentPhoneIcon;

    private ImageButton mTaggedPhoneIcon;

    private ImageButton mFollowingEmailIcon;

    private ImageButton mCommentEmailIcon;

    private ImageButton mTaggedEmailIcon;


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
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

        //user info
        mProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        mNameField = (EditText) view.findViewById(R.id.name);
        mShortBioField = (EditText) view.findViewById(R.id.short_bio);
        mWebsiteField = (EditText) view.findViewById(R.id.website);

        //account
        mEmailField = (EditText) view.findViewById(R.id.email_value);
        mPhoneNumberField = (EditText) view.findViewById(R.id.phone_number_value);
        mFacebookField = (EditText) view.findViewById(R.id.facebook_value);
        mTwitterField = (EditText) view.findViewById(R.id.twitter_value);

        //notifications
        mFollowingPhoneIcon = (ImageButton) view.findViewById(R.id.following_phone_notification);
        mCommentPhoneIcon = (ImageButton) view.findViewById(R.id.comment_phone_notification);
        mTaggedPhoneIcon = (ImageButton) view.findViewById(R.id.tagged_phone_notification);

        mFollowingEmailIcon = (ImageButton) view.findViewById(R.id.following_email_notification);
        mCommentEmailIcon = (ImageButton) view.findViewById(R.id.comment_email_notification);
        mTaggedEmailIcon = (ImageButton) view.findViewById(R.id.tagged_email_notification);

        //about category
        view.findViewById(R.id.contact).setOnClickListener(ClickListener);
        view.findViewById(R.id.terms_of_use).setOnClickListener(ClickListener);
        view.findViewById(R.id.privacy_policy).setOnClickListener(ClickListener);
        view.findViewById(R.id.sign_out).setOnClickListener(ClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

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
                    }
                }
        );
    }

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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
                    //TODO go to privacy policy
                    break;
            }
        }
    };

    private void updateUI() {

        //profile info
        ImageLoaderUtil
                .loadImageIntoView(getActivity(), mUserAccount.getPhoto().getUrl(), mProfileImage);
        mNameField.setText(mUserAccount.getFullName());
        mShortBioField.setText(mUserAccount.getBio());
        mWebsiteField.setText(mUserAccount.getUrl());

        //account
        mEmailField.setText(mUserAccount.getEmail());
        //TODO is there a phone number?
        //mPhoneNumberField.setText(mUserAccount.get);
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
