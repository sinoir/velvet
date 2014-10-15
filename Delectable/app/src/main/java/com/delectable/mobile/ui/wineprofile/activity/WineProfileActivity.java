package com.delectable.mobile.ui.wineprofile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.wineprofile.fragment.WineProfileFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

public class WineProfileActivity extends BaseActivity {

    private static final String PARAMS_WINE_PROFILE = "PARAMS_WINE_PROFILE";

    private static final String PARAMS_CAPTURE_PHOTO_HASH = "PARAMS_CAPTURE_PHOTO_HASH";

    private static final String PARAMS_BASE_WINE = "PARAMS_BASE_WINE";

    private static final String PARAMS_BASE_WINE_MINIMAL = "PARAMS_BASE_WINE_MINIMAL";

    //Deep Link keys
    private static final String DEEP_BASE_WINE_ID = "base_wine_id";

    private static final String DEEP_BASE_VINTAGE_ID = "vintage_id";


    private WineProfileMinimal mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private BaseWine mBaseWine;

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
     * see {@link WineProfileFragment#newInstance(BaseWine)}
     */
    public static Intent newIntent(Context packageContext, BaseWine baseWine) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_BASE_WINE, baseWine);
        intent.setClass(packageContext, WineProfileActivity.class);
        return intent;
    }

    /**
     * Called from Wine Search, Starts a {@link WineProfileActivity} with a {@link BaseWineMinimal}
     * object.
     */
    public static Intent newIntent(Context packageContext, BaseWineMinimal baseWine) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_BASE_WINE_MINIMAL, baseWine);
        intent.setClass(packageContext, WineProfileActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mWineProfile = args.getParcelable(PARAMS_WINE_PROFILE);
            mCapturePhotoHash = args.getParcelable(PARAMS_CAPTURE_PHOTO_HASH);

            mBaseWine = args.getParcelable(PARAMS_BASE_WINE);

            mBaseWineMinimal = args.getParcelable(PARAMS_BASE_WINE_MINIMAL);
        } else {
            // Check if Deep Link params contains data if the bundle args doesn't
            mBaseWineId = getDeepLinkParam(DEEP_BASE_WINE_ID);
            mVintageId = getDeepLinkParam(DEEP_BASE_VINTAGE_ID);
        }

        if (savedInstanceState == null) {

            WineProfileFragment fragment = null;

            if (mWineProfile != null) {
                //spawned from a Feed Fragment
                fragment = WineProfileFragment.newInstance(mWineProfile, mCapturePhotoHash);
            } else if (mBaseWine != null) {
                //spawned from WineCaptureSubmit
                fragment = WineProfileFragment.newInstance(mBaseWine);
            } else if (mBaseWineMinimal != null) {
                //spawned from search fragment
                fragment = WineProfileFragment.newInstance(mBaseWineMinimal);
            } else if (mBaseWineId != null) {
                //spawned from deep link
                fragment = WineProfileFragment.newInstance(mBaseWineId, mVintageId);
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
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

    // TODO: Options overflow menu
}
