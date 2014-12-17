package com.delectable.mobile.ui.wineprofile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.common.widget.DrawInsetsFrameLayout;
import com.delectable.mobile.ui.wineprofile.fragment.WineProfileFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;

public class WineProfileActivity extends BaseActivity {

    private static final String PARAMS_WINE_PROFILE = "PARAMS_WINE_PROFILE";

    private static final String PARAMS_CAPTURE_PHOTO_HASH = "PARAMS_CAPTURE_PHOTO_HASH";

    private static final String PARAMS_BASE_WINE_MINIMAL = "PARAMS_BASE_WINE_MINIMAL";

    //Deep Link keys
    private static final String DEEP_BASE_WINE_ID = "base_wine_id";

    private static final String DEEP_BASE_VINTAGE_ID = "vintage_id";

    private DrawInsetsFrameLayout mContainerView;

    private WineProfileMinimal mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private BaseWineMinimal mBaseWineMinimal;

    private String mBaseWineId;

    private String mVintageId;

    /**
     * see {@link WineProfileFragment#newInstance(WineProfileMinimal, PhotoHash)}
     */
    public static Intent newIntent(Context packageContext, WineProfileMinimal wineProfile,
            PhotoHash capturePhotoHash) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_WINE_PROFILE, wineProfile);
        intent.putExtra(PARAMS_CAPTURE_PHOTO_HASH, (Parcelable) capturePhotoHash);
        intent.setClass(packageContext, WineProfileActivity.class);
        return intent;
    }

    /**
     * see {@link WineProfileFragment#newInstance(BaseWineMinimal, PhotoHash)}
     */
    public static Intent newIntent(Context packageContext, BaseWineMinimal baseWine,
            PhotoHash capturePhotoHash) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_BASE_WINE_MINIMAL, baseWine);
        intent.putExtra(PARAMS_CAPTURE_PHOTO_HASH, (Parcelable) capturePhotoHash);
        intent.setClass(packageContext, WineProfileActivity.class);
        return intent;
    }

    /**
     * see {@link WineProfileFragment#newInstance(String, String)}
     */
    public static Intent newIntent(Context packageContext, String baseWineId, String vintageId) {
        Intent intent = new Intent();
        intent.putExtra(DEEP_BASE_WINE_ID, baseWineId);
        intent.putExtra(DEEP_BASE_VINTAGE_ID, vintageId);
        intent.setClass(packageContext, WineProfileActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container_translucent);

        mContainerView = (DrawInsetsFrameLayout) findViewById(R.id.container);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mWineProfile = args.getParcelable(PARAMS_WINE_PROFILE);
            mCapturePhotoHash = args.getParcelable(PARAMS_CAPTURE_PHOTO_HASH);

            mBaseWineMinimal = args.getParcelable(PARAMS_BASE_WINE_MINIMAL);

            //from deep links
            mBaseWineId = args.getString(DEEP_BASE_WINE_ID);
            mVintageId = args.getString(DEEP_BASE_VINTAGE_ID);
        }

        if (savedInstanceState == null) {

            WineProfileFragment fragment = null;

            if (mWineProfile != null) {
                //spawned from a Feed Fragment
                fragment = WineProfileFragment
                        .newInstance(mWineProfile, mCapturePhotoHash);
            } else if (mBaseWineMinimal != null) {
                //spawned from search fragment
                fragment = WineProfileFragment.newInstance(mBaseWineMinimal, mCapturePhotoHash);
            } else if (mBaseWineId != null || mVintageId != null) {
                //spawned from deep link
                fragment = WineProfileFragment.newInstance(mBaseWineId, mVintageId);
            }

            // propagate inset changes to fragment so it can adjust it's padding
            if (fragment instanceof DrawInsetsFrameLayout.OnInsetsCallback) {
                mContainerView
                        .setOnInsetsCallback((DrawInsetsFrameLayout.OnInsetsCallback) fragment);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishDeepLinkActivity();
                break;
        }
        return true;
    }

    // TODO: Options overflow menu
}
