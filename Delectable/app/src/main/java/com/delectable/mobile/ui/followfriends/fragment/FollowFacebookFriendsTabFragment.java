package com.delectable.mobile.ui.followfriends.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.followfriends.widget.ContactsAdapter;

public class FollowFacebookFriendsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowFacebookFriendsTabFragment.class.getSimpleName();

    private ContactsAdapter mAdapter = new ContactsAdapter(this);

    @Override
    public ContactsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected String getEventId() {
        return TAG;
    }

    @Override
    protected void fetchAccounts() {
        if (UserInfo.getAccountPrivate(getActivity()).isFacebookConnected()) {
            mAccountController.fetchFacebookSuggestions(getEventId());
            mEmptyTextButtonView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyTextButtonView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_facebook);
        mEmptyTextView.setText(R.string.empty_facebook);

        mConnectButton.setBackgroundResource(R.drawable.btn_facebook);
        mConnectButton.setText(R.string.connect_facebook_button);
        mConnectButton.setTextColor(getResources().getColor(R.color.com_facebook_blue));
        mConnectButton.setIconDrawable(getResources().getDrawable(R.drawable.ic_fb));
        return view;
    }


}
