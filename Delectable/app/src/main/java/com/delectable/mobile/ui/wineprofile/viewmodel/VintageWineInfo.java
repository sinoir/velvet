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
}
