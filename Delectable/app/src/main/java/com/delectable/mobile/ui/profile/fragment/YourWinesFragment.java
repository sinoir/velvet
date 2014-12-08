package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class YourWinesFragment extends UserProfileFragment {

    private static final String TAG = YourWinesFragment.class.getSimpleName();

    public YourWinesFragment() {
        Bundle args = new Bundle();
        args.putString(USER_ID, UserInfo.getUserId(App.getInstance()));
        setArguments(args);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
    }

    @Override
    protected boolean isFragmentEmbedded() {
        return true;
    }
}
