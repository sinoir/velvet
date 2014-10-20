package com.delectable.mobile.jobs.foursquare;

import com.delectable.mobile.Config;
import com.delectable.mobile.events.foursquare.SearchedFoursquareVenuesEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.foursquare.FoursquareVenueItem;
import com.delectable.mobile.api.endpointmodels.foursquare.FoursquareVenuesSearchResponse;
import com.delectable.mobile.net.FoursquareNetworkClient;
import com.path.android.jobqueue.Params;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class SearchFoursquareVenuesJob extends BaseJob {

    private static final String TAG = SearchFoursquareVenuesJob.class.getSimpleName();

    @Inject
    protected FoursquareNetworkClient mNetworkClient;

    private String mLatLon;

    public SearchFoursquareVenuesJob(String latLon) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mLatLon = latLon;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        // https://developer.foursquare.com/docs/venues/search
        String endpoint = "/venues/search";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", Config.FOURSQUARE_CLIENT_ID);
        params.put("client_secret", Config.FOURSQUARE_CLIENT_SECRET);
        params.put("v", "20140206");
        params.put("intent", "checkin");
        params.put("limit", "50");
        params.put("ll", mLatLon);

        FoursquareVenuesSearchResponse response = mNetworkClient.get(endpoint, params,
                FoursquareVenuesSearchResponse.class);

        List<FoursquareVenueItem> venues = response.getResponse().getVenues();

        getEventBus().post(new SearchedFoursquareVenuesEvent(venues));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new SearchedFoursquareVenuesEvent(getErrorMessage()));
        } else {
            getEventBus().post(new SearchedFoursquareVenuesEvent(false));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }

}
