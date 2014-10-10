package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class CaptureListingModel {

    private static final String TAG = CaptureListingModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_listing_";

    private static final String TYPE_USER_CAPTURES = KEY_PREFIX + "users_";

    @Inject
    protected CacheManager mCache;

    /**
     * Combines your the freshly retrieved ListResponse with the current list displayed in the UI.
     *
     * Coverts your BaseListingResponse into a CacheListing and write it to cache, returning the
     * CacheListing back to you.
     */
    public BaseListingResponse<CaptureDetails> saveCurrentListing(String accountId,
            BaseListingResponse<CaptureDetails> listing) {

        mCache.put(TYPE_USER_CAPTURES + accountId, listing);
        return listing;
    }

    public BaseListingResponse<CaptureDetails> getListing(String accountId) {
        String key = TYPE_USER_CAPTURES + accountId;

        Type classType = new TypeToken<CaptureDetailsListingResponse>() {
        }.getType();
        BaseListingResponse<CaptureDetails> cachelisting = (CaptureDetailsListingResponse) mCache
                .get(key, CaptureDetailsListingResponse.class, classType);
        return cachelisting;
    }

    private class CaptureDetailsListingResponse extends BaseListingResponse<CaptureDetails> {

    }


}
