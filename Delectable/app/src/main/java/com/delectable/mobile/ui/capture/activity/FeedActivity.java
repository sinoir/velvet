package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.events.ui.HideOrShowFabEvent;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.home.fragment.CaptureListFragment;
import com.delectable.mobile.util.Animate;
import com.melnykov.fab.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FeedActivity extends BaseActivity {

    private static final String TAG = FeedActivity.class.getSimpleName();

    @Inject
    public EventBus mEventBus;

    protected FloatingActionButton mCameraButton;

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
        App.injectMembers(this);
        setContentView(R.layout.activity_feed);
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
        toolbarTitleView.setTextColor(getResources().getColor(R.color.accent));
        ViewCompat.setElevation(getActionBarToolbar(), Animate.ELEVATION);

        mCameraButton = (FloatingActionButton) findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraButton.show(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
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

    public void onEventMainThread(HideOrShowFabEvent event) {
        if (event.show) {
            mCameraButton.show(true);
        } else {
            mCameraButton.hide(true);
        }
    }
}
