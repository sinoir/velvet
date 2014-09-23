package com.delectable.mobile.net;

public class MotdNetworkClient extends BaseNetworkClient {

    private static final String TAG = MotdNetworkClient.class.getSimpleName();

    private static final String API_ENDPOINT = "http://motd.delectable.com/";

    @Override
    protected String getBaseUrl() {
        return API_ENDPOINT;
    }
}
