package com.delectable.mobile.ui.winepurchase.viewmodel;

import com.delectable.mobile.api.models.PaymentMethod;
import com.delectable.mobile.api.models.Pricing;
import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.ShippingAddress;
import com.delectable.mobile.api.models.WineProfileMinimal;

public class CheckoutData {

    private PurchaseOffer mPurchaseOffer;

    private WineProfileMinimal mWineProfileMinimal;

    private Pricing mCurrentPrice;

    private ShippingAddress mSelectedShippingAddress;

    private PaymentMethod mSelectedPaymentMethod;

    private int mQuantity = 1;

    private int mMinNumBottles = 1;

    private int mMaxNumBottles = 1;

    public CheckoutData() {
    }

    //region Getters/Setters
    public String getProducerName() {
        return mWineProfileMinimal.getProducerName();
    }

    public String getWineName() {
        return mWineProfileMinimal.getName();
    }

    public ShippingAddress getSelectedShippingAddress() {
        return mSelectedShippingAddress;
    }

    public void setSelectedShippingAddress(ShippingAddress selectedShippingAddress) {
        mSelectedShippingAddress = selectedShippingAddress;
    }

    public String getAddressDisplayText() {
        if (mSelectedShippingAddress == null) {
            return "";
        }
        String addr2 = mSelectedShippingAddress.getAddr2() == null ? ""
                : " " + mSelectedShippingAddress.getAddr2();
        return mSelectedShippingAddress.getAddr1() + addr2 +
                ", " + mSelectedShippingAddress.getCity()
                + "," + mSelectedShippingAddress.getState();
    }

    public PaymentMethod getSelectedPaymentMethod() {
        return mSelectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
        mSelectedPaymentMethod = selectedPaymentMethod;
    }

    public String getPaymentMethidDisplayText() {
        if (mSelectedPaymentMethod == null) {
            return "";
        }
        return mSelectedPaymentMethod.getLastFour();
    }

    // TODO: Payment Icon by Type

    public int getQuantity() {
        return mQuantity;
    }

    public String getPerBottleText() {
        return mWineProfileMinimal.getPriceText();
    }

    public String getTotalPriceText() {
        return mCurrentPrice.getTotal();
    }

    public String getShippingPriceText() {
        return mCurrentPrice.getShipping();
    }

    public String getTaxPriceText() {
        return mCurrentPrice.getTax();
    }
    //endregion

    public String getPromoPriceText() {
        // TODO: Get Promo Amount Text once Promo Code is Implemented
        return null;
    }

    //region modifiers
    public void updateWithData(PurchaseOffer purchaseOffer,
            WineProfileMinimal wineProfileMinimal) {
        mPurchaseOffer = purchaseOffer;
        mWineProfileMinimal = wineProfileMinimal;

        mMinNumBottles = mPurchaseOffer.getMinQuant();
        mMaxNumBottles = mPurchaseOffer.getMaxQuant();
        mQuantity = mPurchaseOffer.getDefaultQuant();
        updatePricingData();
    }

    public void addBottle() {
        if (mQuantity == mMaxNumBottles) {
            return;
        }
        mQuantity++;
        updatePricingData();
    }

    public void removeBottle() {
        if (mQuantity == mMinNumBottles) {
            return;
        }
        mQuantity--;
        updatePricingData();
    }
    //endregion

    //region helpers
    private void updatePricingData() {
        if (mPurchaseOffer == null || mPurchaseOffer.getPricing() == null) {
            return;
        }

        for (Pricing pricing : mPurchaseOffer.getPricing()) {
            if (pricing.getQuantity() == mQuantity) {
                mCurrentPrice = pricing;
                break;
            }
        }
    }
    //endregion
}
