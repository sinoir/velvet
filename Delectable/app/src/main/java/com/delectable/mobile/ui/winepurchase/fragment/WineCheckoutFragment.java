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
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WineCheckoutFragment extends BaseFragment {

    private static final String ARGS_VINTAGE_ID = "ARGS_VINTAGE_ID";

    @Inject
    protected BaseWineController mBaseWineController;

    @Inject
    protected WineSourceModel mWineSourceModel;

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    @InjectView(R.id.per_bottle_price)
    protected TextView mPerBottlePriceText;

    @InjectView(R.id.shipping_address)
    protected TextView mShippingAddress;

    @InjectView(R.id.add_shipping_address)
    protected View mAddShippingAddress;

    @InjectView(R.id.payment_method_cc_image)
    protected ImageView mPaymentMethodCCImage;

    @InjectView(R.id.payment_method_last_digits)
    protected TextView mPaymentMethodLastDigits;

    @InjectView(R.id.add_payment_method)
    protected TextView mAddPaymentMethod;

    @InjectView(R.id.quantity_amount)
    protected TextView mQuantityAmountText;

    @InjectView(R.id.credit_applied_amount)
    protected TextView mCreditAppliedAmount;

    @InjectView(R.id.add_promo_code)
    protected View mAddPromoCode;

    @InjectView(R.id.shipping_tax_amount)
    protected TextView mShippingAndTaxAmount;

    @InjectView(R.id.total_amount)
    protected TextView mTotalAmount;

    @InjectView(R.id.confirm_button)
    protected View mConfirmButton;

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

    //region Update UI methods
    private void updateWineDetails() {
        if (mWineProfile == null) {
            return;
        }

        mProducerName.setText(mWineProfile.getProducerName());
        mWineName.setText(mWineProfile.getName());
        String pricePerBottleText = getString(R.string.winecheckout_per_bottle,
                mWineProfile.getPriceText());
        mPerBottlePriceText.setText(pricePerBottleText);
    }

    //endregion

    //region Load Local Data
    private void loadWineAndPricingData() {
        mWineProfile = mWineSourceModel.getMinWineWithPrice(mVintageId);
        mPurchaseOffer = mWineSourceModel.getPurchaseOffer(mVintageId);

        updateWineDetails();
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

        loadWineAndPricingData();
    }
    //endregion

    //region onClicks

    @OnClick(R.id.shipping_address_container)
    public void onShippingAddressClicked() {
        // TODO: Implement Shipping Address (select / add dialog)
    }

    @OnClick(R.id.payment_container)
    public void onPaymentMethodClicked() {
        // TODO: Implement Payment Method (select / add dialog)
    }

    @OnClick(R.id.plus_quantity)
    public void onAddBottleClicked() {
        // TODO: Implement Adding Bottle
    }

    @OnClick(R.id.minus_quantity)
    public void onSubtractBottleClicked() {
        // TODO: Implement Removing Bottle
    }

    @OnClick(R.id.add_promo_code)
    public void onAddPromoCodeClicked() {
        // TODO: Implement adding promo code
    }

    @OnClick(R.id.confirm_button)
    public void onConfirmClicked() {
        // TODO: Implement Confirm / Pay
    }

    //endregion
}
