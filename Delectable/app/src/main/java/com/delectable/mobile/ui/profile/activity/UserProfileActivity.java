package com.delectable.mobile.ui.profile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class UserProfileActivity extends BaseActivity {

    public static final String PARAMS_USER_ID = "PARAMS_USER_ID";

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mUserId = args.getString(PARAMS_USER_ID);
        } else {
            // Check if Deep Link params contains data if the bundle args doesn't
            mUserId = getDeepLinkParam("account_id");
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, UserProfileFragment.newInstance(mUserId)).commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
