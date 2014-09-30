package com.delectable.mobile.ui.wineprofile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfile;
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

    private static final String PARAMS_BASE_WINE_ID = "PARAMS_BASE_WINE_ID";

    private static final String PARAMS_VINTAGE_ID = "PARAMS_VINTAGE_ID";

    private static final String PARAMS_BASE_WINE_MINIMAL = "PARAMS_BASE_WINE_MINIMAL";


    private WineProfile mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private BaseWineMinimal mBaseWineMinimal;

    private String mBaseWineId;

    private String mVintageId;

    /**
     * see {@link WineProfileFragment#newInstance(WineProfile, PhotoHash)}
     */
    public static Intent newIntent(Context packageContext, WineProfile wineProfile,
            PhotoHash capturePhotoHash) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_WINE_PROFILE, wineProfile);
        intent.putExtra(PARAMS_CAPTURE_PHOTO_HASH, (Parcelable) capturePhotoHash);
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

    /**
     * see {@link WineProfileFragment#newInstance(String baseWineId, String vintageId)}
     */
    public static Intent newIntent(Context packageContext, String baseWineId, String vintageId) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_BASE_WINE_ID, baseWineId);
        intent.putExtra(PARAMS_VINTAGE_ID, vintageId);
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

            mBaseWineMinimal = args.getParcelable(PARAMS_BASE_WINE_MINIMAL);

            mBaseWineId = args.getString(PARAMS_BASE_WINE_ID);
            mVintageId = args
                    .getString(PARAMS_VINTAGE_ID); //TODO debug, vintage doesn't get used currently
        } else {
            // Check if Deep Link params contains data if the bundle args doesn't
            mBaseWineId = getDeepLinkParam("base_wine_id");
            mVintageId = getDeepLinkParam("vintage_id");
        }

        if (savedInstanceState == null) {

            WineProfileFragment fragment = null;

            if (mBaseWineMinimal != null) {
                //from search fragment
                fragment = WineProfileFragment.newInstance(mBaseWineMinimal);
            } else if (mWineProfile != null && mCapturePhotoHash != null) {
                fragment = WineProfileFragment.newInstance(mWineProfile, mCapturePhotoHash);
            } else if (mBaseWineId != null) {
                fragment = WineProfileFragment.newInstance(mBaseWineId, mVintageId);
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
