package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.home.fragment.CaptureListFragment;
import com.delectable.mobile.util.Animate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.widget.TextView;

public class FeedActivity extends BaseActivity {

    private static final String TAG = FeedActivity.class.getSimpleName();

    private String mListKey;

    private String mListType;

    private String mListTitle;

    public static Intent newIntent(Context context, String listKey, String listType,
            String listTitle) {
        Intent intent = new Intent();
        intent.putExtra(CaptureListFragment.LIST_KEY, listKey);
        intent.putExtra(CaptureListFragment.LIST_TYPE, listType);
        intent.putExtra(CaptureListFragment.LIST_TITLE, listTitle);
        intent.setClass(context, FeedActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mListKey = args.getString(CaptureListFragment.LIST_KEY);
            mListType = args.getString(CaptureListFragment.LIST_TYPE);
            mListTitle = args.getString(CaptureListFragment.LIST_TITLE);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,
                            CaptureListFragment.newInstance(mListKey, mListType, mListTitle))
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        TextView toolbarTitleView = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitleView.setText(mListTitle);
        ViewCompat.setElevation(getActionBarToolbar(), Animate.ELEVATION);
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
