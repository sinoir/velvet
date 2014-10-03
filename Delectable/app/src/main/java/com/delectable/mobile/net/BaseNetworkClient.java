package com.delectable.mobile.net;

import com.google.gson.Gson;

import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.util.DelException;
import com.delectable.mobile.util.HelperUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public abstract class BaseNetworkClient {

    protected Gson mGson = new Gson();

    protected OkHttpClient mClient = new OkHttpClient();

    private String TAG = this.getClass().getSimpleName();

    protected abstract String getBaseUrl();


    public <T extends BaseResponse> T get(String path, HashMap<String, String> params,
            Class<T> responseClass) throws IOException, DelException {
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
            Class<T> responseClass) throws IOException, DelException {
        //handle HTTP errors
        if (!response.isSuccessful()) {
            String errorMessage = "HTTP Request Error " + response.code() + ": " + response
                    .toString();
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

            //Construct meaningful error message
            int errorCode = responseObj.getError().getCode();
            String errorMessage = "API Error " + errorCode;
            if (responseObj.getError().getMessage() != null) {
                errorMessage += ": " + responseObj.getError().getMessage();
            } else {
                //there was no message in the error object, see if it exists in the ErrorUtil enum
                ErrorUtil error = ErrorUtil.valueOfCode(errorCode);
                if (error != ErrorUtil.UNDOCUMENTED_ERROR_CODE) {
                    errorMessage += ": " + error.toString().toLowerCase();
                }
            }

            Log.i(requestName, "error: " + errorMessage);
            throw new DelException(errorMessage, errorCode);
        }
        return responseObj;
    }

}
