package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.widget.LikingPeopleAdapter;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LikingPeopleFragment extends BaseFragment {

    private static final String TAG = LikingPeopleFragment.class.getSimpleName();

    public static final String PARAMS_LIKING_PEOPLE = "PARAMS_LIKING_PEOPLE";

    @InjectView(R.id.list_view)
    protected ListView mListView;

    List<AccountMinimal> mLikingPeople;

    private LikingPeopleAdapter mAdapter;

    public static LikingPeopleFragment newInstance(ArrayList<AccountMinimal> likingPeople) {
        LikingPeopleFragment fragment = new LikingPeopleFragment();
        Bundle args = new Bundle();
        if (likingPeople != null) {
            args.putParcelableArrayList(PARAMS_LIKING_PEOPLE, likingPeople);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLikingPeople = getArguments().getParcelableArrayList(PARAMS_LIKING_PEOPLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.inject(this, view);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getResources()
                .getQuantityString(R.plurals.cap_feed_likes_count, mLikingPeople.size(),
                        mLikingPeople.size());
        setActionBarTitle(title);

        mAdapter = new LikingPeopleAdapter(mLikingPeople);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountMinimal account = mAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(UserProfileActivity.PARAMS_USER_ID, account.getId());
                intent.setClass(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
