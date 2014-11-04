package com.delectable.mobile.api.jobs;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.ListingRequest;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.models.IDable;
import com.delectable.mobile.api.models.Listing;
import com.path.android.jobqueue.Params;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This is an abstraction for all context listing jobs. Implementations of this class just need to
 * implement the abstract methods for a successful job. <br> The job will handle the combining of
 * lists, and will always return a {@link Listing} with just the {@link Listing#getUpdates()} array
 * populated through the {@link UpdatedListingEvent} at the end of the Job. The {@link
 * UpdatedListingEvent} will be broadcast with the type {@code T} generic that was provided to this
 * Job. <br>
 */
public abstract class BaseFetchListingJob<T extends IDable> extends BaseJob {

    private static final String TAG = BaseFetchListingJob.class.getSimpleName();

    protected String mContext;

    /**
     * Is usually an accountId for captureDetails, or a wineId for captureNotes
     */
    protected String mDataItemId;

    protected String mBefore;

    protected String mAfter;

    protected String mETag;

    protected Boolean mIsPullToRefresh;

    private String mRequestId;

    /**
     * @param dataItemId      dataItem that you want to fetch a listing for. Usually an accountId
     *                        for CaptureDetails or wineId for captureNotes.
     * @param listing         The previous {@link Listing} if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public BaseFetchListingJob(String requestId, String context, String dataItemId,
            Listing<T> listing, Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC));
        mRequestId = requestId;
        mContext = context;
        mDataItemId = dataItemId;
        if (listing != null) {
            mBefore = listing.getBoundariesToBefore();
            mAfter = listing.getBoundariesToAfter();
            mETag = listing.getETag();
        }
        mIsPullToRefresh = isPullToRefresh;
    }

    public BaseFetchListingJob(String requestId, String context, String dataItemId, String etag,
            String before, String after, Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mRequestId = requestId;
        mContext = context;
        mDataItemId = dataItemId;
        mETag = etag;
        mBefore = before;
        mAfter = after;
        mIsPullToRefresh = isPullToRefresh;
    }

    /**
     * If providing a custom {@link BaseRequest} object, via {@link #getRequestObject()}, using this
     * constructor is sufficient.
     *
     * @param requestId Used to keep track of how it w
     */
    public BaseFetchListingJob(String requestId, String dataItemId) {
        super(new Params(Priority.SYNC));
        mRequestId = requestId;
        mDataItemId = dataItemId;
    }


    /**
     * The endpoint where we're to retrieve out listing from.
     */
    protected abstract String getEndpoint();

    /**
     * Lets the subclass handle the retrieval of it's cached listing.
     */
    protected abstract Listing<T> getCachedListing(String dataItemId);

    /**
     * Lets the subclass handle saving the listing to cache. The ListingResponse provided here will
     * be pass onwards to the success event. If there are any changes made to the object during the
     * save to cache, they will be reflected in the event.
     */
    protected abstract void saveListingToCache(String dataItemId, Listing<T> listing);

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

    /**
     * Allows subclasses to provide a custom listing request object.
     */
    protected BaseRequest getRequestObject() {
        return new ListingRequest(mContext, mETag, mDataItemId,
                mBefore, mAfter, mIsPullToRefresh);
    }

    ;

    @Override

    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = getEndpoint();

//        ListingRequest request = new ListingRequest(mContext, mETag, mAccountId,
//                mBefore, mAfter, mIsPullToRefresh);
        BaseRequest request = getRequestObject();

        Type type = getResponseType();

        ListingResponse<T> response = getNetworkClient().post(endpoint, request, type);

        Listing<T> apiListing = response.getPayload();
        // note: Sometimes payload may be null
        // maybe there are no captures
        // maybe list is completely up to date and e_tag_match is true

        //update cache if the listing exists
        if (apiListing != null) {

            //grab cached listing if it exist
            Listing<T> cachedListing = getCachedListing(mDataItemId);
            ArrayList<T> cachedItems = new ArrayList<T>();
            if (cachedListing != null) {
                cachedItems = cachedListing.getUpdates();
            }

            //combine listing into current items list, and then set that List as the updates array
            apiListing.combineInto(cachedItems, response.isInvalidate());
            apiListing.clearLists();
            apiListing.setUpdates(cachedItems);

            saveListingToCache(mDataItemId, apiListing);
        }

        //if apiListing returns null to the event, means list is up to date!
        mEventBus.post(new UpdatedListingEvent<T>(mRequestId, mDataItemId, apiListing));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        String jobName = this.getClass().getSimpleName();
        mEventBus.post(new UpdatedListingEvent<T>(mRequestId, mDataItemId,
                jobName + " " + getErrorMessage(), getErrorCode()));

    }
}
