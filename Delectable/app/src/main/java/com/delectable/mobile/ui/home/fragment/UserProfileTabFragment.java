package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class UserProfileTabFragment extends BaseFragment {

    private View mView;

    private ListView mListView;

    public UserProfileTabFragment() {
        // Required empty public constructor
    }

    public static UserProfileTabFragment newInstance() {
        UserProfileTabFragment fragment = new UserProfileTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_user_profile_tab, container, false);

        mListView = (ListView) mView.findViewById(R.id.list_view);

        View profileHeader = inflater.inflate(R.layout.profile_header, null);
        mListView.addHeaderView(profileHeader);

        // TODO: Create/Add Adapter ...

        return mView;
    }
}
