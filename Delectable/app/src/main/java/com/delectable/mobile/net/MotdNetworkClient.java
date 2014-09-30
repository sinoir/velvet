package com.delectable.mobile.net;

import com.delectable.mobile.data.ServerInfo;

public class MotdNetworkClient extends BaseNetworkClient {

    private static final String TAG = MotdNetworkClient.class.getSimpleName();

    @Override
    protected String getBaseUrl() {
        return  ServerInfo.getEnvironment().getMotdUrl();
    }
}
