package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActivityFeedTabFragment extends BaseFragment {

    private View mView;

    public ActivityFeedTabFragment() {
        // Required empty public constructor
    }

    public static ActivityFeedTabFragment newInstance() {
        ActivityFeedTabFragment fragment = new ActivityFeedTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_activity_feed_tab, container, false);
        return mView;
    }
}