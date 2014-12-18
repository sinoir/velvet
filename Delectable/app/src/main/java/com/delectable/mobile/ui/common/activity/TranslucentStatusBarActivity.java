package com.delectable.mobile.ui.common.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.common.widget.DrawInsetsFrameLayout;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

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
        // make status bar translucent on v19+
        int flags = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ? (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                : View.SYSTEM_UI_FLAG_VISIBLE;
        mContainerView.setSystemUiVisibility(flags);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DrawInsetsFrameLayout.OnInsetsCallback) {
            setOnInsetsCallback((DrawInsetsFrameLayout.OnInsetsCallback) fragment);
        }
    }

    protected void setOnInsetsCallback(DrawInsetsFrameLayout.OnInsetsCallback callback) {
        mContainerView.addOnInsetsCallback(callback);
    }

}
