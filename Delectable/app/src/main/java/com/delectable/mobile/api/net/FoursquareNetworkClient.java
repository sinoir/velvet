package com.delectable.mobile.api.net;

//TODO: Someday should clean up Foursquare and maybe add it to its own package, etc.
public class FoursquareNetworkClient extends BaseNetworkClient {

    private static final String TAG = FoursquareNetworkClient.class.getSimpleName();

    private static final String API_ENDPOINT = "https://api.foursquare.com/v2/";

    @Override
    protected String getBaseUrl() {
        return API_ENDPOINT;
    }
}
