package com.delectable.mobile.ui.settings.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.settings.fragment.NotificationsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class NotificationsActivty extends BaseActivity {

    private static final String TAG = NotificationsActivty.class.getSimpleName();

    public static final String TITLE = "title";

    public static Intent newIntent(Context packageContext, String title) {
        Intent intent = new Intent();
        intent.putExtra(TITLE, title);
        intent.setClass(packageContext, NotificationsActivty.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            String title = args.getString(TITLE);
            setTitle(title);
        }

        setContentView(R.layout.activity_toolbar_fragment_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NotificationsFragment())
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}