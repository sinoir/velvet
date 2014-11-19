package com.delectable.mobile.ui.winepurchase.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.events.accounts.FetchedPaymentMethodsEvent;
import com.delectable.mobile.api.events.accounts.FetchedShippingAddressesEvent;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.events.wines.PurchasedWineEvent;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.winepurchase.activity.WineCheckoutActivity;
import com.delectable.mobile.ui.winepurchase.dialog.AddPaymentMethodDialog;
import com.delectable.mobile.ui.winepurchase.dialog.AddShippingAddressDialog;
import com.delectable.mobile.ui.winepurchase.dialog.ChoosePaymentMethodDialog;
import com.delectable.mobile.ui.winepurchase.dialog.ChooseShippingAddressDialog;
import com.delectable.mobile.ui.winepurchase.viewmodel.CheckoutData;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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

    private static final int REQUEST_ADD_SHIPPING_ADDRESS_DIALOG = 100;

    private static final int REQUEST_CHOOSE_SHIPPING_ADDRESS_DIALOG = 200;

    private static final int REQUEST_ADD_PAYMENT_METHOD = 300;

    private static final int REQUEST_CHOOSE_PAYMENT_METHOD_DIALOG = 400;


    private static final int DELAY_ERROR_DISPLAY = 2000;

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

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    private String mVintageId;

    private CheckoutData mData;

    private boolean mHasLaodedShippingAddresses = false;

    private boolean mHasLaodedPaymentMethods = false;

    private Toolbar mErrorToolbar;

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

        fetchUserData();

        mData = new CheckoutData();

        updateNumBottles();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // configure ActionBar
        enableBackButton(true);
        getActionBarToolbar()
                .setBackgroundColor(getResources().getColor(R.color.d_off_white_translucent));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SHIPPING_ADDRESS_DIALOG
                && resultCode == AddShippingAddressDialog.RESULT_SHIPPING_ADDRESS_SAVED) {
            loadShippingAddress(data
                    .getStringExtra(AddShippingAddressDialog.EXTRAS_SHIPPING_ADDRESS_ID));
        } else if (requestCode == REQUEST_ADD_PAYMENT_METHOD
                && resultCode == AddPaymentMethodDialog.RESULT_PAYMENT_METHOD_SAVED) {
            loadPaymentMethod(data.getStringExtra(AddPaymentMethodDialog.EXTRAS_PAYMENT_METHOD_ID));
        } else if (requestCode == REQUEST_CHOOSE_SHIPPING_ADDRESS_DIALOG) {
            loadShippingAddress(data.getStringExtra(
                    ChooseShippingAddressDialog.EXTRAS_SHIPPING_ADDRESS_ID));
        } else if (requestCode == REQUEST_CHOOSE_PAYMENT_METHOD_DIALOG) {
            loadPaymentMethod(
                    data.getStringExtra(ChoosePaymentMethodDialog.EXTRAS_PAYMENT_METHOD_ID));
        }
    }

    private void startPurchaseWine() {
        showLoader();
        mBaseWineController.purchaseWine(mData.getWineId(), mData.getPurchaseOfferId(),
                mData.getPaymentMethodId(), mData.getShippingAddressId(), mData.getQuantity(), "");
    }

    private void showConfirmation() {
        replaceWithNewFragment(ConfirmationFragment.newInstance());
    }

    private void showError(String error) {
        if (!(getActivity() instanceof WineCheckoutActivity)) {
            return;
        }
        ((WineCheckoutActivity) getActivity()).showOrHideErrorBar(true, 0, error);
        ((WineCheckoutActivity) getActivity()).showOrHideErrorBar(false, DELAY_ERROR_DISPLAY, "");
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

        mProducerName.setText(mData.getProducerName().toLowerCase());
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
            mPaymentMethodLastDigits.setText(mData.getPaymentMethodDisplayText());
            int paymentMethodResId = mData.getPaymentMethodIconResId();
            if (paymentMethodResId != 0) {
                mPaymentMethodCCImage.setImageResource(paymentMethodResId);
            } else {
                mPaymentMethodCCImage.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showPaymentInfo() {
        mPaymentMethodCCImage.setVisibility(View.VISIBLE);
        mPaymentMethodLastDigits.setVisibility(View.VISIBLE);
        mAddPaymentMethod.setVisibility(View.GONE);
    }

    private void hidePaymentInfo() {
        mPaymentMethodCCImage.setVisibility(View.GONE);
        mPaymentMethodLastDigits.setVisibility(View.GONE);
        mAddPaymentMethod.setVisibility(View.VISIBLE);
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

    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
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

        // Fetch Wine Prices when Shipping address changes
        fetchWineSource();
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
        showLoader();
        mBaseWineController.fetchWineSource(mVintageId, mData.getShippingState());
    }

    private void fetchUserData() {
        mAccountController.fetchShippingAddresses();
        mAccountController.fetchPaymentMethods();
    }
    //endregion

    //region EventBus
    public void onEventMainThread(PurchasedWineEvent event) {
        hideLoader();

        if (event.isSuccessful()) {
            showConfirmation();
        } else {
            handleError(event.getErrorCode(), event.getErrorMessage());
        }
    }

    public void onEventMainThread(FetchedWineSourceEvent event) {
        hideLoader();
        if (!mVintageId.equalsIgnoreCase(event.getWineId())) {
            return;
        }

        if (!event.isSuccessful()) {
            handleError(event.getErrorCode(), event.getErrorMessage());
        }

        loadWineAndPricingData();
    }

    public void onEventMainThread(FetchedShippingAddressesEvent event) {
        mHasLaodedShippingAddresses = true;
        if (!event.isSuccessful()) {
            handleError(event.getErrorCode(), event.getErrorMessage());
        }

        loadShippingAddress(null);
    }

    public void onEventMainThread(FetchedPaymentMethodsEvent event) {
        mHasLaodedPaymentMethods = true;
        if (!event.isSuccessful()) {
            handleError(event.getErrorCode(), event.getErrorMessage());
        }

        loadPaymentMethod(null);
    }
    //endregion

    //region onClicks
    @OnClick(R.id.shipping_address_container)
    public void onShippingAddressClicked() {
        // Disabled when we haven't loaded shipping address yet
        if (!mHasLaodedShippingAddresses) {
            return;
        }
        if (mData.getSelectedShippingAddress() == null) {
            showAddShippingAddressDialog(null);
        } else {
            showChooseShippingAddressDialog(mData.getSelectedShippingAddress().getId());
        }
    }

    @OnClick(R.id.payment_container)
    public void onPaymentMethodClicked() {
        // Disabled when we haven't loaded Payment Methods yet
        if (!mHasLaodedPaymentMethods) {
            return;
        }
        if (mData.getSelectedPaymentMethod() == null) {
            showAddPaymentMethodDialog();
        } else {
            showChoosePaymentMethodDialog(mData.getSelectedPaymentMethod().getId());
        }
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
        if (mData.isDataValid()) {
            startPurchaseWine();
        }
    }
    //endregion

    //region Dialog Helpers
    private void showAddShippingAddressDialog(String id) {
        AddShippingAddressDialog dialog = AddShippingAddressDialog.newInstance(id);
        dialog.setTargetFragment(this, REQUEST_ADD_SHIPPING_ADDRESS_DIALOG);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showChooseShippingAddressDialog(String id) {
        ChooseShippingAddressDialog dialog = ChooseShippingAddressDialog.newInstance(id);
        dialog.setTargetFragment(this, REQUEST_CHOOSE_SHIPPING_ADDRESS_DIALOG);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showAddPaymentMethodDialog() {
        AddPaymentMethodDialog dialog = AddPaymentMethodDialog.newInstance();
        dialog.setTargetFragment(this, REQUEST_ADD_PAYMENT_METHOD);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showChoosePaymentMethodDialog(String id) {
        ChoosePaymentMethodDialog dialog = ChoosePaymentMethodDialog.newInstance(id);
        dialog.setTargetFragment(this, REQUEST_CHOOSE_PAYMENT_METHOD_DIALOG);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialog");
    }
    //endregion


    protected Toolbar getActionBarToolbar() {
        if (mErrorToolbar == null && (getActivity() instanceof WineCheckoutActivity)) {
            mErrorToolbar = ((WineCheckoutActivity) getActivity()).getActionBarToolbar();
        }
        return mErrorToolbar;
    }

    private void handleError(ErrorUtil errorCode, String errorMessage) {
        if (errorCode == ErrorUtil.NO_NETWORK_ERROR) {
            errorMessage = getString(R.string.checkout_no_internet);
        }

        showError(errorMessage);
    }
}
