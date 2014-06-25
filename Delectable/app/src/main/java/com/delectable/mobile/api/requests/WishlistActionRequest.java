package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.WineProfile;

public class WishlistActionRequest extends BaseActionRequest {

    /**
     * @param wineProfile   - The wine to add to / remove from wishlist
     * @param addToWishlist - True if user wants to add wine to wishlist
     */
    public WishlistActionRequest(WineProfile wineProfile, boolean addToWishlist) {
        id = wineProfile.getId();
        action = addToWishlist;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/wine_profiles/wishlist";
    }
}
