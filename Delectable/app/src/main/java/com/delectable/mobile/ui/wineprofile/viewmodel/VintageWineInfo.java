package com.delectable.mobile.ui.wineprofile.viewmodel;

import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.WineProfileSubProfile;

/**
 * View Model to help display Vintage / Pricing Info based on PurchaseOffer object or WineProfile
 * object
 */
public class VintageWineInfo {

    private PurchaseOffer mPurchaseOffer;

    private WineProfileSubProfile mWineProfileMinimal;

    private boolean mIsLoading;

    public VintageWineInfo(WineProfileSubProfile wineProfileMinimal) {
        this(wineProfileMinimal, null);
    }

    public VintageWineInfo(WineProfileSubProfile wineProfileMinimal, PurchaseOffer purchaseOffer) {
        mPurchaseOffer = purchaseOffer;
        mWineProfileMinimal = wineProfileMinimal;
    }

    public boolean isSoldOut() {
        // Wine is Sold Out when the price is set to -1.
        return mWineProfileMinimal != null && mWineProfileMinimal.getPrice() != null
                && mWineProfileMinimal.getPrice() < 0;
    }

    public boolean hasPrice() {
        return hasPurchaseOfferPrice() || hasWineProfilePrice();
    }

    public String getPriceText() {
        if (hasPurchaseOfferPrice()) {
            return mPurchaseOffer.getPricing().get(0).getPerBbottle();
        }

        if (hasWineProfilePrice()) {
            return mWineProfileMinimal.getPriceText();
        }

        return "";
    }

    public String getYear() {
        return mWineProfileMinimal.getVintage();
    }

    public int getRatingCount() {
        if (mWineProfileMinimal == null || mWineProfileMinimal.getRatingsSummary() == null) {
            return 0;
        }
        return mWineProfileMinimal.getRatingsSummary().getAllCount();
    }

    public double getRating() {
        if (mWineProfileMinimal == null || mWineProfileMinimal.getRatingsSummary() == null) {
            return -1;
        }
        return mWineProfileMinimal.getRatingsSummary().getAllAvgOfTen();
    }

    private boolean hasPurchaseOfferPrice() {
        return mPurchaseOffer != null && mPurchaseOffer.getPricing() != null
                && mPurchaseOffer.getPricing().size() > 0;
    }

    private boolean hasWineProfilePrice() {
        return mWineProfileMinimal != null && mWineProfileMinimal.getPriceText() != null;
    }

    public WineProfileSubProfile getWineProfileMinimal() {
        return mWineProfileMinimal;
    }

    public void setWineProfileMinimal(WineProfileSubProfile wineProfileMinimal) {
        mWineProfileMinimal = wineProfileMinimal;
    }

    /**
     * Update Existing WineProfile with Wine that contains latest price.
     *
     * Note: have to do this, because we get wine with price as a sub profile, and some fields are
     * removed,such as ratings
     */
    public void updateWineWithPrice(WineProfileSubProfile wineProfileSubProfile) {
        mWineProfileMinimal.setPrice(wineProfileSubProfile.getPrice());
        mWineProfileMinimal.setPriceStatus(wineProfileSubProfile.getPriceStatus());
        mWineProfileMinimal.setPriceText(wineProfileSubProfile.getPriceText());
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }
}
