package com.delectable.mobile.ui.winepurchase.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.winepurchase.fragment.WineCheckoutFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class WineCheckoutActivity extends BaseActivity {

    private static final String TAG = WineCheckoutActivity.class.getSimpleName();

    private static final String PARAMS_VINTAGE_ID = "PARAMS_VINTAGE_ID";

    //Deep Link keys
    private static final String DEEP_BASE_VINTAGE_ID = "vintage_id";

    private Toolbar mErrorTooblar;

    private boolean mErrorBarShown = false;

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
        String vintageId = null;
        if (args != null) {
            vintageId = args.getString(PARAMS_VINTAGE_ID);
        }

        if (savedInstanceState == null) {

            WineCheckoutFragment fragment = WineCheckoutFragment.newInstance(vintageId);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        showOrHideActionBar(true);
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

    public Toolbar getErrorTooblar() {
        if (mErrorTooblar == null) {
            mErrorTooblar = (Toolbar) findViewById(R.id.error_toolbar);
        }
        return mErrorTooblar;
    }

    public void showOrHideErrorBar(boolean show, String error) {
        showOrHideErrorBar(show, 0, error);
    }

    public void showOrHideErrorBar(final boolean show, final int delay, final String error) {
        final Toolbar toolbar = getErrorTooblar();

        if (show == mErrorBarShown) {
            return;
        }

        if (delay > 0) {
            mErrorTooblar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (error != null) {
                        toolbar.setTitle(error);
                    }
                    mErrorBarShown = show;
                    onErrorBarShowOrHide(show);
                }
            }, delay);
        } else {
            if (error != null) {
                toolbar.setTitle(error);
            }
            mErrorBarShown = show;
            onErrorBarShowOrHide(show);
        }
    }

    public void onErrorBarShowOrHide(boolean shown) {
        Toolbar toolbar = getErrorTooblar();
        if (shown) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }
}
