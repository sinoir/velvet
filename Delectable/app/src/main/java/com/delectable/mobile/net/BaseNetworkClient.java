package com.delectable.mobile.net;

import com.google.gson.Gson;

import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.util.HelperUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public abstract class BaseNetworkClient {

    private String TAG = this.getClass().getSimpleName();

    protected Gson mGson = new Gson();

    protected OkHttpClient mClient = new OkHttpClient();

    protected abstract String getBaseUrl();


    public <T extends BaseResponse> T get(String path, HashMap<String, String> params,
            Class<T> responseClass) throws IOException {
        String requestUrl = HelperUtil.buildUrlWithParameters(getBaseUrl() + path, params);
        String requestName = TAG + ".GET";
        Log.i(requestName, "url: " + requestUrl);
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        Response response = mClient.newCall(request).execute();

        return handleResponse(response, requestName, responseClass);
    }

    public <T extends BaseResponse> T handleResponse(Response response, String requestName,
            Class<T> responseClass) throws IOException {
        //handle HTTP errors
        if (!response.isSuccessful()) {
            String errorMessage = "HTTP Request Error " + response.code() + ": " + response.toString();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }
        //be careful, calling this ResponseBody.string() method more than once wipes out the string
        String responseBody = response.body().string();
        Log.i(requestName, "response: " + responseBody);
        T responseObj = mGson.fromJson(responseBody, responseClass);

        //handle Gson parsing errors
        if (responseObj == null) {
            String errorMessage = "Error parsing response into JSON";
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }

        //handle API errors
        if (!responseObj.isSuccess()) {

            if (responseObj.getError() == null) {
                String errorMessage = "API Error with no message.";
                Log.i(requestName, "error: " + errorMessage);
                throw new IOException(errorMessage);
            }

            String errorMessage = "API Error " + responseObj.getError().getCode() + ": " +
                    responseObj.getError().getMessage();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }
        return responseObj;
    }

}
