package com.delectable.mobile.api.net;

import com.delectable.mobile.api.cache.ServerInfo;

public class MotdNetworkClient extends BaseNetworkClient {

    private static final String TAG = MotdNetworkClient.class.getSimpleName();

    @Override
    protected String getBaseUrl() {
        return  ServerInfo.getEnvironment().getMotdUrl();
    }
}
