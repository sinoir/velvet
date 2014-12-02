package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

public class WineProfileInstantFragment extends WineProfileFragment {

    public static WineProfileInstantFragment newInstance(@Nullable BaseWine baseWine) {
        WineProfileInstantFragment fragment = new WineProfileInstantFragment();
        Bundle args = new Bundle();
        if (baseWine != null) {
            args.putString(BASE_WINE_ID, baseWine.getId());
        }
        fragment.setArguments(args);
        return fragment;
    }

    public void init(BaseWineMinimal baseWine) {
        init(baseWine, null);
    }

    public void init(BaseWineMinimal baseWine, Bitmap previewImage) {
        getArguments().putString(BASE_WINE_ID, baseWine.getId());
        mFetchingId = mBaseWineId = baseWine.getId();
        mBaseWineMinimal = baseWine;
        updateBannerData(previewImage);
        onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_rate:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
