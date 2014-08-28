package com.delectable.mobile.ui.wineprofile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.wineprofile.fragment.WineProfileFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class WineProfileActivity extends BaseActivity {

    public static final String PARAMS_WINE_PROFILE = "PARAMS_WINE_PROFILE";

    public static final String PARAMS_CAPTURE_PHOTO_HASH = "PARAMS_CAPTURE_PHOTO_HASH";

    public static final String PARAMS_BASE_WINE_ID = "PARAMS_BASE_WINE_ID";

    public static final String PARAMS_VINTAGE_ID = "PARAMS_VINTAGE_ID";

    private WineProfile mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private String mBaseWineId;

    private String mVintageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mWineProfile = args.getParcelable(PARAMS_WINE_PROFILE);
            mCapturePhotoHash = args.getParcelable(PARAMS_CAPTURE_PHOTO_HASH);
            mBaseWineId = args.getString(PARAMS_BASE_WINE_ID);
            mVintageId = args.getString(PARAMS_VINTAGE_ID);
        } else {
            // Check if Deep Link params contains data if the bundle args doesn't
            mBaseWineId = getDeepLinkParam("base_wine_id");
            mVintageId = getDeepLinkParam("vintage_id");
        }

        if (savedInstanceState == null) {
            WineProfileFragment fragment = null;
            if (mWineProfile != null && mCapturePhotoHash != null) {
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
                finish();
                break;
        }
        return true;
    }

    // TODO: Options overflow menu
}
