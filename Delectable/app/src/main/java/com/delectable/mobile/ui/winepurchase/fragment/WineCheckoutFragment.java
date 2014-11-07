package com.delectable.mobile.ui.winepurchase.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class WineCheckoutFragment extends BaseFragment {

    private static final String ARGS_VINTAGE_ID = "ARGS_VINTAGE_ID";

    @Inject
    protected BaseWineController mBaseWineController;

    @Inject
    protected WineSourceModel mWineSourceModel;

    private String mVintageId;

    private WineProfileMinimal mWineProfile;

    private PurchaseOffer mPurchaseOffer;

    public static WineCheckoutFragment newInstance(String vintageId) {
        WineCheckoutFragment fragment = new WineCheckoutFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_VINTAGE_ID, vintageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        Bundle args = getArguments();
        if (args != null) {
            mVintageId = args.getString(ARGS_VINTAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_checkout, null, false);
        ButterKnife.inject(this, view);

        // Always Fetch Latest Wine Source
        fetchWineSource();
        
        return view;
    }

    //region Load Local Data
    private void loadPricingData() {
        mWineProfile = mWineSourceModel.getMinWineWithPrice(mVintageId);
        mPurchaseOffer = mWineSourceModel.getPurchaseOffer(mVintageId);
    }
    //endregion

    //region Fetch Remote Data
    private void fetchWineSource() {
        mBaseWineController.fetchWineSource(mVintageId);
    }
    //endregion

    //region EventBus
    public void onEventMainThread(FetchedWineSourceEvent event) {
        if (!mVintageId.equalsIgnoreCase(event.getWineId())) {
            return;
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        loadPricingData();
    }
    //endregion
}
