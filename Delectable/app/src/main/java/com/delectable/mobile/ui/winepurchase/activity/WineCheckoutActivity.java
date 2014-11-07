package com.delectable.mobile.ui.winepurchase.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.winepurchase.fragment.WineCheckoutFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class WineCheckoutActivity extends BaseActivity {

    private static final String PARAMS_VINTAGE_ID = "PARAMS_VINTAGE_ID";

    //Deep Link keys
    private static final String DEEP_BASE_VINTAGE_ID = "vintage_id";

    private String mVintageId;

    public static Intent newIntent(Context packageContext, String vintageId) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_VINTAGE_ID, vintageId);
        intent.setClass(packageContext, WineCheckoutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mVintageId = args.getString(PARAMS_VINTAGE_ID);
        } else {
            // Check if Deep Link params contains data if the bundle args doesn't
            mVintageId = getDeepLinkParam(DEEP_BASE_VINTAGE_ID);
        }

        if (savedInstanceState == null) {

            WineCheckoutFragment fragment = WineCheckoutFragment.newInstance(mVintageId);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishDeepLinkActivity();
                break;
        }
        return true;
    }
}
