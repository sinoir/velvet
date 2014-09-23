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

    @Override
    protected String getBaseUrl() {
        return API_ENDPOINT;
    }
}
