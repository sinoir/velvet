package com.delectable.mobile.ui.tagpeople.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.FetchedDelectafriendsEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.tagpeople.widget.TagPeopleAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TagPeopleFragment extends BaseFragment {

    public static final String RESULT_SELECTED_CONTACTS = "result_selected_contacts";

    private static final String PARAMS_SELECTED_CONTACTS = "params_selected_contacts";

    private static final String TAG = TagPeopleFragment.class.getSimpleName();

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.with_container)
    protected View mWithContainerView;

    @InjectView(R.id.with_text)
    protected TextView mWithTextView;

    @Inject
    AccountController mAccountController;

    ArrayList<TaggeeContact> mDelectaFriends;

    ArrayList<TaggeeContact> mSelectedItems;

    private View mView;

    private TagPeopleAdapter mAdapter;

    public static TagPeopleFragment newInstance(Fragment targetFragment, int requestCode,
            ArrayList<TaggeeContact> selectedContacts) {
        TagPeopleFragment fragment = new TagPeopleFragment();
        Bundle args = new Bundle();
        if (selectedContacts != null) {
            args.putParcelableArrayList(PARAMS_SELECTED_CONTACTS, selectedContacts);
        }
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, requestCode);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        mDelectaFriends = new ArrayList<TaggeeContact>();

        Bundle args = getArguments();
        if (args != null) {
            mSelectedItems = args.getParcelableArrayList(PARAMS_SELECTED_CONTACTS);
        }

        if (mSelectedItems == null) {
            mSelectedItems = new ArrayList<TaggeeContact>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_tag_people, container, false);
        ButterKnife.inject(this, mView);

        mActionBar.setDisplayHomeAsUpEnabled(true);

        mAdapter = new TagPeopleAdapter(mDelectaFriends);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaggeeContact obj = mAdapter.getItem(position);
                if (mSelectedItems.contains(obj)) {
                    mSelectedItems.remove(obj);
                } else {
                    mSelectedItems.add(obj);
                }
                updateWithFriendsCount();
            }
        });

        View emptyView = mView.findViewById(R.id.empty_view_tag_people);
        View connectButton = emptyView.findViewById(R.id.connect_facebook_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO connect to facebook
                mEventBus.post(new NavigationEvent(NavHeader.NAV_SETTINGS));
            }
        });
        mListView.setEmptyView(emptyView);

        return mView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_CANCELED, null);
                }
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.fetchDelectafriends();
    }

    @OnClick(R.id.with_container)
    public void onTagSelectionCompleted() {
        if (getTargetFragment() != null) {
            Intent data = new Intent();
            Bundle extras = new Bundle();
            extras.putParcelableArrayList(RESULT_SELECTED_CONTACTS, mSelectedItems);
            data.putExtras(extras);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        }
        getActivity().onBackPressed();
    }

    private void updateWithFriendsCount() {
        int numItemsChecked = mSelectedItems.size();
        if (mSelectedItems.size() > 0) {
            mWithContainerView.setVisibility(View.VISIBLE);
            mWithTextView.setText(getResources().getQuantityString(R.plurals.with_friends,
                    numItemsChecked, numItemsChecked));
        } else {
            mWithContainerView.setVisibility(View.GONE);
        }
    }

    /**
     * Updates Selected friends checks when we pass selected friends from previous screen
     */
    private void updateSelectedFriends() {
        if (mSelectedItems == null) {
            return;
        }
        // When coming back from another fragment with checked items, mark them as checked
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mSelectedItems.contains(mAdapter.getItem(i))) {
                mListView.setItemChecked(i, true);
            }
        }
        updateWithFriendsCount();
    }

    public void onEventMainThread(FetchedDelectafriendsEvent event) {
        if (event.isSuccessful()) {
            mDelectaFriends.clear();
            for (AccountMinimal account : event.getAccounts()) {
                mDelectaFriends.add(new TaggeeContact(account));
            }
            mAdapter.notifyDataSetChanged();
            updateSelectedFriends();
        } else if (event.getErrorMessage() != null) {
            showToastError(event.getErrorMessage());
        }
    }
}
