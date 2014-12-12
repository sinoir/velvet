package com.delectable.mobile.ui.wineprofile.viewmodel;

import com.delectable.mobile.api.models.RatingsSummaryHash;
import com.delectable.mobile.api.models.WineProfileSubProfile;

/**
 * View Model to help display Vintage / Pricing Info based on PurchaseOffer object or WineProfile
 * object
 */
public class VintageWineInfo {

    private WineProfileSubProfile mWineProfileMinimal;

    private RatingsSummaryHash mRatingsSummaryHash;

    private boolean mIsLoading;

    public VintageWineInfo(WineProfileSubProfile wineProfileMinimal) {
        this(wineProfileMinimal, null);
    }

    public VintageWineInfo(WineProfileSubProfile wineProfileMinimal,
            RatingsSummaryHash ratingsSummaryHash) {
        mWineProfileMinimal = wineProfileMinimal;

        if (ratingsSummaryHash == null) {
            mRatingsSummaryHash = wineProfileMinimal.getRatingsSummary();
        } else {
            mRatingsSummaryHash = ratingsSummaryHash;
        }
    }

    public boolean isSoldOut() {
        // Wine is Sold Out when the price is set to -1.
        return mWineProfileMinimal != null && mWineProfileMinimal.getPrice() != null
                && mWineProfileMinimal.getPrice() < 0;
    }

    public boolean hasPrice() {
        return hasWineProfilePrice();
    }

    public String getPriceText() {
        if (hasWineProfilePrice()) {
            return mWineProfileMinimal.getPriceText();
        }

        return "";
    }

    public String getYear() {
        return mWineProfileMinimal.getVintage();
    }

    public int getRatingCount() {
        if (mRatingsSummaryHash == null) {
            return 0;
        }
        return mRatingsSummaryHash.getAllCount();
    }

    public double getRating() {
        if (mRatingsSummaryHash == null) {
            return -1;
        }
        return mRatingsSummaryHash.getAllAvg();
    }

    private boolean hasWineProfilePrice() {
        return mWineProfileMinimal != null && mWineProfileMinimal.getPriceText() != null;
    }

    public WineProfileSubProfile getWineProfileMinimal() {
        return mWineProfileMinimal;
    }

    public String getWineId() {
        return mWineProfileMinimal.getId();
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
