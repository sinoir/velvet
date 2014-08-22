package com.delectable.mobile.net;

import com.google.gson.Gson;

import com.delectable.mobile.model.api.BaseResponse;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;

public class BaseNetworkClient {

    private String TAG = this.getClass().getSimpleName();

    private Gson mGson = new Gson();

    public <T extends BaseResponse> T handleResponse(Response response, String requestName,
            Class<T> responseClass) throws IOException {
        //handle HTTP errors
        if (!response.isSuccessful()) {
            String errorMessage = "Request Error " + response.code() + ": " + response.toString();
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

            String errorMessage = "Error " + responseObj.getError().getCode() + ": " +
                    responseObj.getError().getMessage();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }
        return responseObj;
    }

}
