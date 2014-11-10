package com.delectable.mobile.ui.common.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.common.fragment.WebViewFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WebViewActivty extends BaseActivity {

    private static final String TAG = WebViewActivty.class.getSimpleName();

    public static final String URL = "url";
    public static final String TITLE = "title";


    public static Intent newIntent(Context packageContext, String url, String title) {
        Intent intent = new Intent();
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        intent.setClass(packageContext, WebViewActivty.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        if (args == null) {
            throw new RuntimeException(TAG + "needs to be instantiated with a url");
        }

        String title = args.getString(TITLE);
        setTitle(title);

        setContentView(R.layout.activity_fragment_container);

        String url = args.getString(URL);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, WebViewFragment.newInstance(url))
                    .commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}