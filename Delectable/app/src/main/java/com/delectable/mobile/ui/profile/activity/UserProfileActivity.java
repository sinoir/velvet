package com.delectable.mobile.ui.profile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class UserProfileActivity extends BaseActivity {

    public static final String PARAMS_USER_ID = "PARAMS_USER_ID";

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    /**
     * Inits UserProfileActivity with the provided userAccountId.
     */
    public static Intent newIntent(Context packageContext, String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(packageContext, UserProfileActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        String userId = null;
        Bundle args = getIntent().getExtras();
        if (args != null) {
            userId = args.getString(PARAMS_USER_ID);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, UserProfileFragment.newInstance(userId)).commit();
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
