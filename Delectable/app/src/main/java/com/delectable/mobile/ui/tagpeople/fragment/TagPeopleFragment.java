package com.delectable.mobile.ui.tagpeople.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchedDelectafriendsEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.tagpeople.widget.TagPeopleAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    private static final String TAG = TagPeopleFragment.class.getSimpleName();

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.with_container)
    protected View mWithContainerView;

    @InjectView(R.id.with_text)
    protected TextView mWithTextView;

    @Inject
    AccountController mAccountController;

    ArrayList<AccountMinimal> mDelectaFriends;

    ArrayList<AccountMinimal> mSelectedItems;

    private View mView;

    private TagPeopleAdapter mAdapter;

    public static TagPeopleFragment newInstance(Fragment targetFragment, int requestCode) {
        TagPeopleFragment fragment = new TagPeopleFragment();
        fragment.setTargetFragment(targetFragment, requestCode);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        mDelectaFriends = new ArrayList<AccountMinimal>();
        mSelectedItems = new ArrayList<AccountMinimal>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_tag_people, container, false);
        ButterKnife.inject(this, mView);

        mAdapter = new TagPeopleAdapter(mDelectaFriends);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountMinimal obj = mAdapter.getItem(position);
                if (mSelectedItems.contains(obj)) {
                    mSelectedItems.remove(obj);
                } else {
                    mSelectedItems.add(obj);
                }
                updateWithFriendsCount();
            }
        });

        return mView;
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
            // TODO: Pass in Selected Friends to Bundle
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

    public void onEventMainThread(FetchedDelectafriendsEvent event) {
        if (event.isSuccessful()) {
            mDelectaFriends.clear();
            mDelectaFriends.addAll(event.getAccounts());
            mAdapter.notifyDataSetChanged();
            showToastError("You have " + event.getAccounts().size() + " Friends");
        } else {
            showToastError("Error: Failed to find friends.");
        }
    }
}
