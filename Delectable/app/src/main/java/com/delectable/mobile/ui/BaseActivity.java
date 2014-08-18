package com.delectable.mobile.ui;

import com.delectable.mobile.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private boolean mIsFromDeepLink;

    private Uri mDeepLinkUriData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Deep Link stuff
        Intent intent = getIntent();
        // Action not used yet, not sure if we'll need it.  Right now the action only VIEW.
        String action = intent.getAction();
        mDeepLinkUriData = intent.getData();

        if (mDeepLinkUriData != null) {
            mIsFromDeepLink = true;
        } else {
            mIsFromDeepLink = false;
        }
    }

    /**
     * Should be called in place of finish() when the "Up" button is pressed from a deep linked
     * Activity
     */
    public void finishDeepLinkActivity() {
        // TODO: Figure out what kind of task we're in. If the main app hasn't been opened yet, we should open the app and go back to the Main Activity or something, otherwise hitting up will close the app.
        super.finish();
    }

    public void replaceWithFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // TODO: Should we animate?
        transaction.setCustomAnimations(
                android.R.animator.fade_in, android.R.animator.fade_out,
                android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public String getDeepLinkParam(String key) {
        String param = null;
        if (isFromDeepLink() && getDeepLinkUriData() != null) {
            param = mDeepLinkUriData.getQueryParameter(key);
        }
        return param;
    }

    public boolean isFromDeepLink() {
        return mIsFromDeepLink;
    }

    public Uri getDeepLinkUriData() {
        return mDeepLinkUriData;
    }

}
