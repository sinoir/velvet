package com.delectable.mobile.ui.tagpeople.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.FetchedDelectafriendsEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.tagpeople.widget.TagPeopleAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

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

    /**
     * This view is a child of the empty view container, and covers the progress bar when it's set to visible.
     */
    @InjectView(R.id.empty_text_view)
    protected TextView mEmptyTextView;

    @Inject
    protected AccountController mAccountController;

    private ArrayList<TaggeeContact> mDelectaFriends = new ArrayList<TaggeeContact>();

    private ArrayList<TaggeeContact> mSelectedItems;

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

        mAdapter = new TagPeopleAdapter(mDelectaFriends);

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

        View view = inflater.inflate(R.layout.fragment_tag_people, container, false);
        ButterKnife.inject(this, view);

        setHasOptionsMenu(true);
        overrideHomeIcon(R.drawable.btn_ab_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() != null) {
                    getTargetFragment()
                            .onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,
                                    null);
                }
                getActivity().onBackPressed();
            }
        });

        mListView.setAdapter(mAdapter);

        View emptyView = view.findViewById(R.id.empty_view);
        mListView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //hide emptyTextView in order to show progress dialog
        mEmptyTextView.setVisibility(View.GONE);
        mAccountController.fetchDelectafriends();
    }

    @OnItemClick(R.id.list_view)
    protected void onItemClick(int position) {
        TaggeeContact obj = mAdapter.getItem(position);
        if (mSelectedItems.contains(obj)) {
            mSelectedItems.remove(obj);
        } else {
            mSelectedItems.add(obj);
        }
        updateWithFriendsCount();
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
        mEmptyTextView.setVisibility(View.VISIBLE);
        if (!event.isSuccessful()) {
            mEmptyTextView.setText(R.string.tag_friends_error);
            Log.d(TAG, event.getErrorMessage());
            return;
            //TODO if there already data, and the subsequent request is made and fails, how do we let the user know that it failed?
        }

        //event successful!
        mDelectaFriends.clear();
        mEmptyTextView.setText(R.string.tag_friends_empty_state_text);
        for (AccountMinimal account : event.getAccounts()) {
            mDelectaFriends.add(new TaggeeContact(account));
        }
        mAdapter.notifyDataSetChanged();
        updateSelectedFriends();
    }
}
