package com.delectable.mobile.jobs;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.IDable;
import com.delectable.mobile.model.api.BaseListingRequest;
import com.delectable.mobile.model.api.BaseListingWrapperResponse;
import com.path.android.jobqueue.Params;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class BaseFetchListingJob<T extends IDable> extends BaseJob {

    private static final String TAG = BaseFetchListingJob.class.getSimpleName();

    protected String mContext;

    protected String mAccountId;

    protected String mBefore;

    protected String mAfter;

    protected String mETag;

    protected Boolean mIsPullToRefresh;

    /**
     * @param accountId       Account that you want to fetch a listing for.
     * @param listingResponse The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public BaseFetchListingJob(String context, String accountId,
            BaseListingResponse<T> listingResponse, Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mContext = context;
        mAccountId = accountId;
        if (listingResponse != null) {
            mBefore = listingResponse.getBoundariesToBefore();
            mAfter = listingResponse.getBoundariesToAfter();
            mETag = listingResponse.getETag();
        }
        mIsPullToRefresh = isPullToRefresh;
    }

    public BaseFetchListingJob(String context, String accountId, String etag, String before,
            String after, Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mContext = context;
        mAccountId = accountId;
        mETag = etag;
        mBefore = before;
        mAfter = after;
        mIsPullToRefresh = isPullToRefresh;
    }

    /**
     * The endpoint where we're to retrieve out listing from.
     */
    protected abstract String getEndpoint();

    /**
     * Lets the subclass handle the retrieval of it's cached listing.
     */
    protected BaseListingResponse<T> getCachedListing(String accountId) {
        //empty body, allow implementer to handle caching only if they want
        return null; //returning null is the same as if there was nothing in the cache
    }

    /**
     * Lets the subclass handle saving the listing to cache. The ListingResponse provided here will
     * be pass onwards to the success event. If there are any changes made to the object during the
     * save to cache, they will be reflected in the event.
     */
    protected void saveListingToCache(String accountId, BaseListingResponse<T> listing) {
        //empty body, allow implementer to handle caching only if they want
    }


    protected abstract void postSuccessEvent(String accountId, BaseListingResponse<T> cachedListing);

    protected abstract void postFailEvent(String accountId);

    /**
     * The concrete generic type needs to be provided in the subclass. Just use the code below with
     * the listing element type that you want will work:
     * <pre>
     * {@code
     *
     *  Type type = new TypeToken<BaseListingWrapperResponse<LISTING_CLASS>>() {}.getType();
     *  return type;
     * }
     * </pre>
     */
    public abstract Type getResponseType();

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = getEndpoint();

        BaseListingRequest request = new BaseListingRequest(mContext, mETag, mAccountId,
                mBefore, mAfter, mIsPullToRefresh);

        Type type = getResponseType();

        BaseListingWrapperResponse<T> response = getNetworkClient().post(endpoint, request, type);

        BaseListingResponse<T> apiListing = response.getPayload();
        // note: Sometimes payload may be null
        // maybe there are no captures
        // maybe list is completely up to date and e_tag_match is true

        //update cache if the listing exists
        if (apiListing != null) {

            //grab cached listing if it exist
            BaseListingResponse<T> cachedListing = getCachedListing(mAccountId);
            ArrayList<T> cachedItems = new ArrayList<T>();
            if (cachedListing != null) {
                cachedItems = cachedListing.getUpdates();
            }

            //combine listing into current items list, and then set that List as the updates array
            apiListing.combineInto(cachedItems, response.isInvalidate());
            apiListing.clearLists();
            apiListing.setUpdates(cachedItems);

            saveListingToCache(mAccountId, apiListing);
        }

        postSuccessEvent(mAccountId, apiListing);
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        postFailEvent(mAccountId);
    }
}
