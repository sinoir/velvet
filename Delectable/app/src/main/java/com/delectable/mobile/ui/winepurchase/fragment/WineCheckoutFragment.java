package com.delectable.mobile.ui.winepurchase.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.events.accounts.FetchedPaymentMethodsEvent;
import com.delectable.mobile.api.events.accounts.FetchedShippingAddressesEvent;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.winepurchase.viewmodel.CheckoutData;

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
    protected AccountController mAccountController;

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

    @InjectView(R.id.credit_applied_label)
    protected View mCreditAppliedLabel;

    @InjectView(R.id.add_promo_code)
    protected View mAddPromoCode;

    @InjectView(R.id.shipping_amount)
    protected TextView mShippingAmount;

    @InjectView(R.id.tax_amount)
    protected TextView mTaxAmount;

    @InjectView(R.id.total_amount)
    protected TextView mTotalAmount;

    @InjectView(R.id.confirm_button)
    protected View mConfirmButton;

    private String mVintageId;

    private CheckoutData mData;

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

        fetchUserData();

        mData = new CheckoutData();

        updateNumBottles();

        return view;
    }

    //region Update UI methods
    private void updateUI() {
        updateWineDetails();
        updateNumBottles();
        updatePricing();
        updatePromoUI();
    }

    private void updateWineDetails() {
        if (mData == null) {
            return;
        }

        mProducerName.setText(mData.getProducerName());
        mWineName.setText(mData.getWineName());
        String pricePerBottleText = getString(R.string.winecheckout_per_bottle,
                mData.getPerBottleText());
        mPerBottlePriceText.setText(pricePerBottleText);
    }

    private void updateNumBottles() {
        String numBottlesText = getResources()
                .getQuantityString(R.plurals.winecheckout_num_bottles, mData.getQuantity(),
                        mData.getQuantity());
        mQuantityAmountText.setText(numBottlesText);
    }

    private void updatePricing() {
        mShippingAmount.setText(mData.getShippingPriceText());
        mTaxAmount.setText(mData.getTaxPriceText());
        mTotalAmount.setText(mData.getTotalPriceText());
    }

    private void updatePromoUI() {
        if (mData.getPromoPriceText() == null) {
            hidePromoCredit();
        } else {
            mCreditAppliedAmount.setText(mData.getPromoPriceText());
            showPromoCredit();
        }
    }

    private void updateShippingAddressUI() {
        if (mData.getSelectedShippingAddress() == null) {
            hideShippingAddressInfo();
        } else {
            showShippingAddressInfo();
            mShippingAddress.setText(mData.getAddressDisplayText());
        }
    }

    private void updatePaymentMethodUI() {
        if (mData.getSelectedPaymentMethod() == null) {
            hidePaymentInfo();
        } else {
            showPaymentInfo();
            mPaymentMethodLastDigits.setText(mData.getPaymentMethidDisplayText());
            // TODO: Set Icon
        }
    }

    private void showPaymentInfo() {
        mPaymentMethodCCImage.setVisibility(View.VISIBLE);
        mPaymentMethodLastDigits.setVisibility(View.VISIBLE);
        mAddPaymentMethod.setVisibility(View.GONE);
    }

    private void hidePaymentInfo() {
        mPaymentMethodCCImage.setVisibility(View.VISIBLE);
        mPaymentMethodLastDigits.setVisibility(View.VISIBLE);
        mAddPaymentMethod.setVisibility(View.GONE);
    }

    private void showShippingAddressInfo() {
        mShippingAddress.setVisibility(View.VISIBLE);
        mAddShippingAddress.setVisibility(View.GONE);
    }

    private void hideShippingAddressInfo() {
        mShippingAddress.setVisibility(View.GONE);
        mAddShippingAddress.setVisibility(View.VISIBLE);
    }

    /**
     * Show Promo Credit Amount and hide the "Add Promo" button
     */
    private void showPromoCredit() {
        mCreditAppliedAmount.setVisibility(View.VISIBLE);
        mCreditAppliedLabel.setVisibility(View.VISIBLE);
        mAddPromoCode.setVisibility(View.GONE);
    }

    /**
     * Hide the Promo Credit and show the Add Promo Button
     */
    private void hidePromoCredit() {
        mCreditAppliedAmount.setVisibility(View.GONE);
        mCreditAppliedLabel.setVisibility(View.GONE);
        mAddPromoCode.setVisibility(View.VISIBLE);
    }
    //endregion

    //region Load Local Data
    private void loadWineAndPricingData() {
        mData.updateWithData(mWineSourceModel.getPurchaseOffer(mVintageId),
                mWineSourceModel.getMinWineWithPrice(mVintageId));

        updateUI();
    }

    private void loadShippingAddress(String shippingAddressId) {
        if (shippingAddressId == null) {
            mData.setSelectedShippingAddress(mShippingAddressModel.getPrimaryShippingAddress());
        } else {
            mData.setSelectedShippingAddress(
                    mShippingAddressModel.getShippingAddress(shippingAddressId));
        }
        updateShippingAddressUI();
    }

    private void loadPaymentMethod(String paymentMethodId) {
        if (paymentMethodId == null) {
            mData.setSelectedPaymentMethod(mPaymentMethodModel.getPrimaryPaymentMethod());
        } else {
            mData.setSelectedPaymentMethod(mPaymentMethodModel.getPaymentMethod(paymentMethodId));
        }
        updatePaymentMethodUI();
    }

    //endregion

    //region Fetch Remote Data
    private void fetchWineSource() {
        mBaseWineController.fetchWineSource(mVintageId);
    }

    private void fetchUserData() {
        mAccountController.fetchShippingAddresses();
        mAccountController.fetchPaymentMethods();
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

    public void onEventMainThread(FetchedShippingAddressesEvent event) {
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        loadShippingAddress(null);
    }

    public void onEventMainThread(FetchedPaymentMethodsEvent event) {
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        loadPaymentMethod(null);
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
        mData.addBottle();
        updateUI();
    }

    @OnClick(R.id.minus_quantity)
    public void onSubtractBottleClicked() {
        mData.removeBottle();
        updateUI();
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
