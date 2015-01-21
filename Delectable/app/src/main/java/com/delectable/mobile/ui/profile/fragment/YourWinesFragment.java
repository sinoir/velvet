package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.search.activity.SearchActivity;
import com.delectable.mobile.ui.search.fragment.SearchFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_action_search:
                startActivity(SearchActivity.newIntent(getActivity(), SearchFragment.WINES));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean isFragmentEmbedded() {
        return true;
    }
}
