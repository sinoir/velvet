package com.delectable.mobile.api;

import com.delectable.mobile.data.ServerInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import android.content.Context;

public class NetworkClient {

    // FIXME: IMPORTANT: Set to false when SSL Server side gets Fixed.
    private static AsyncHttpClient sClient = new AsyncHttpClient(true, 80, 443);

    public static void post(Context context, String url, StringEntity params,
            AsyncHttpResponseHandler responseHandler) {
        sClient.post(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String path) {
        return ServerInfo.getEnvironment().getUrl() + path;
    }
}
