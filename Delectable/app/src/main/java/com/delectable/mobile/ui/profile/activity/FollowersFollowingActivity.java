package com.delectable.mobile.ui.profile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.profile.fragment.FollowersFragment;
import com.delectable.mobile.ui.profile.fragment.FollowingFragment;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class FollowersFollowingActivity extends BaseActivity {

    public enum Type {
        FOLLOWERS, FOLLOWING;
    }

    public static final String TYPE = "type";
    public static final String ACCOUNT_ID = "accountId";


    private static final String TAG = FollowersFollowingActivity.class.getSimpleName();

    private Type mType;

    private String mAccountId;

    /**
     * Inits UserProfileActivity with the provided userAccountId.
     */
    public static Intent newIntent(Context packageContext, Type type, String accountId) {
        Intent intent = new Intent();
        intent.putExtra(TYPE, type.ordinal());
        intent.putExtra(ACCOUNT_ID, accountId);

        intent.setClass(packageContext, FollowersFollowingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args == null) {
            throw new RuntimeException(TAG + " must be instantiated with a fragment type and accountId");
        }

        int position = args.getInt(TYPE);
        mType = Type.values()[position];
        mAccountId = args.getString(ACCOUNT_ID);

        if (savedInstanceState == null) {
            BaseFragment fragment;
            if (mType == Type.FOLLOWING) {
                fragment = FollowingFragment.newInstance(mAccountId);
            } else {
                //default is followers
                fragment = FollowersFragment.newInstance(mAccountId);
            }
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishDeepLinkActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
