package com.delectable.mobile.ui.wineprofile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;
import com.delectable.mobile.ui.wineprofile.fragment.WineProfileFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class WineProfileActivity extends BaseActivity {

    public static final String PARAMS_WINE_PROFILE = "PARAMS_WINE_PROFILE";
    public static final String PARAMS_CAPTURE_PHOTO_HASH = "PARAMS_CAPTURE_PHOTO_HASH";

    private WineProfile mWineProfile;
    private PhotoHash mCapturePhotoHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mWineProfile = args.getParcelable(PARAMS_WINE_PROFILE);
            mCapturePhotoHash = args.getParcelable(PARAMS_CAPTURE_PHOTO_HASH);
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, WineProfileFragment.newInstance(mWineProfile, mCapturePhotoHash)).commit();
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
