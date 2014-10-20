package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.followfriends.widget.ContactsAdapter;
import com.delectable.mobile.ui.navigation.widget.NavHeader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater
                .inflate(R.layout.fragment_listview_no_divider, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_facebook);
        listView.setAdapter(mAdapter);

        View emptyView = view.findViewById(R.id.empty_view_facebook);
        View followButton = emptyView.findViewById(R.id.connect_facebook_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO connect to facebook
                mEventBus.post(new NavigationEvent(NavHeader.NAV_SETTINGS));
            }
        });
        listView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAccounts == null) {
            mAccountController.fetchFacebookSuggestions(getEventId());
        }
    }

}
