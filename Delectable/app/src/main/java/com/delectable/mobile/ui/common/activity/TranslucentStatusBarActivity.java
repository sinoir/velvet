package com.delectable.mobile.ui.common.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.common.widget.DrawInsetsFrameLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TranslucentStatusBarActivity extends BaseActivity {

    public static final String TAG = TranslucentStatusBarActivity.class.getSimpleName();

    private DrawInsetsFrameLayout mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container_translucent);
        mContainerView = (DrawInsetsFrameLayout) findViewById(R.id.container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOrHideStatusBar(true);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DrawInsetsFrameLayout.OnInsetsCallback) {
            addOnInsetsCallback((DrawInsetsFrameLayout.OnInsetsCallback) fragment);
        }
    }

    protected void addOnInsetsCallback(DrawInsetsFrameLayout.OnInsetsCallback callback) {
        if (mContainerView != null) {
            mContainerView.addOnInsetsCallback(callback);
        }
    }

}
