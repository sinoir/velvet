package com.delectable.mobile.net;

import com.google.gson.Gson;

import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.util.HelperUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

//TODO: Someday should clean up Foursquare and maybe add it to its own package, etc. 
public class FoursquareNetworkClient extends BaseNetworkClient {

    private static final String TAG = FoursquareNetworkClient.class.getSimpleName();

    private static final String API_ENDPOINT = "https://api.foursquare.com/v2/";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mClient = new OkHttpClient();

    private Gson mGson = new Gson();

    public <T extends BaseResponse> T get(String path, HashMap<String, String> params,
            Class<T> responseClass) throws IOException {
        String requestUrl = HelperUtil.buildUrlWithParameters(API_ENDPOINT + path, params);
        String requestName = TAG + ".Foursquare";
        Log.i(requestName, "url: " + requestUrl);
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        Response response = mClient.newCall(request).execute();

        return handleResponse(response, requestName, responseClass);
    }
}
