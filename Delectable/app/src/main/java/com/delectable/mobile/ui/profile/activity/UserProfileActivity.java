package com.delectable.mobile.ui.profile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;

import android.content.Intent;
import android.net.Uri;
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
        }

        // Deep Link params
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri deepLinkData = intent.getData();
        if (deepLinkData != null) {
            mUserId = deepLinkData.getQueryParameter("account_id");
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
                // TODO : DeepLink: If UserProfile was opened from another app, we should launch the MainActivity if it wasn't launched already.
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
