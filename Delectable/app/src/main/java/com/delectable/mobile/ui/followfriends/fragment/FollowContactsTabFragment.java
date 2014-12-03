package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.FetchedAccountsFromContactsEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.ui.followfriends.widget.BaseAccountsMinimalAdapter;
import com.delectable.mobile.ui.followfriends.widget.ContactsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import javax.inject.Inject;

public class FollowContactsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowContactsTabFragment.class.getSimpleName();

    @Inject
    AccountController mAccountController;

    private ContactsAdapter mAdapter = new ContactsAdapter(this);

    @Override
    protected BaseAccountsMinimalAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected String getEventId() {
        return TAG;
    }

    @Override
    protected void fetchAccounts() {

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_contacts);
        mConnectButton.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.fetchAccountsFromContacts();
        mEmptyTextButtonView.setVisibility(View.INVISIBLE);
    }

    public void onEventMainThread(FetchedAccountsFromContactsEvent event) {

        mEmptyTextButtonView.setVisibility(View.VISIBLE);
        if (event.isSuccessful()) {
            //remove own account from list if it exists
            int position = -1;
            boolean foundOwnAccount = false;
            for (int i = 0; i < event.getAccounts().size(); i++) {
                AccountMinimal account = event.getAccounts().get(i);
                if (account.isUserRelationshipTypeSelf()) {
                    position = i;
                    foundOwnAccount = true;
                    break;
                }
            }
            if (foundOwnAccount) {
                event.getAccounts().remove(position);
            }


            Collections.sort(event.getAccounts(), new AccountMinimal.FullNameComparator());
            Collections.sort(event.getContacts(), new TaggeeContact.FullNameComparator());
            mAdapter.setAccounts(event.getAccounts());
            mAdapter.setContacts(event.getContacts());
            mAdapter.notifyDataSetChanged();
            return;
        }
        //event error
        showToastError(event.getErrorMessage());
    }
}
