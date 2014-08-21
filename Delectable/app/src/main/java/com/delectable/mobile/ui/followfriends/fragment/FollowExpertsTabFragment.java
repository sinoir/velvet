package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FollowExpertsTabFragment extends BaseFragment{

    private static final String TAG = FollowExpertsTabFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        TextView tv = new TextView(getActivity());
        tv.setText(TAG);
        return tv;

    }
}
