package com.delectable.mobile.net;

import com.google.gson.Gson;

import com.delectable.mobile.App;
import com.delectable.mobile.Config;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.BaseResponse;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;

public class NetworkClient {

    private static final String TAG = NetworkClient.class.getSimpleName();


    private static final String USER_AGENT = "DelectableAndroid";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mClient = new OkHttpClient();

    private Gson mGson = new Gson();

    /**
     * Convenience method that calls {@link #post(String, BaseRequest, Class, boolean)} with
     * authentication set to true. Generally all API requests call this method except for
     * registrations/register and registrations/login.
     */
    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject,
            Class<T> responseClass) throws IOException {
        return post(endpoint, requestObject, responseClass, true);
    }

    /**
     * @param authenticate <code>true</code> if you want to pass in sessionKey and sessionToken with
     *                     the request.
     */
    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject,
            Class<T> responseClass, boolean authenticate) throws IOException {
        if (authenticate) {
            authenticateRequest(requestObject);
        }
        String requestString = mGson.toJson(requestObject);
        String requestName = TAG + "." + requestObject.getClass().getSimpleName();
        Log.i(requestName, "url: " + getAbsolutePath() + endpoint);
        Log.i(requestName, "body: " + requestString);
        RequestBody body = RequestBody.create(JSON, requestString);
        Request request = new Request.Builder()
                .url(getAbsolutePath() + endpoint)
                .addHeader("User-Agent", USER_AGENT)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();

        //handle HTTP errors
        if (!response.isSuccessful()) {
            // TODO error handling, throw exception that gets handled in job queue
            throw new IOException(
                    "Request Error " + response.code() + ": " + response.toString());
        }
        //be careful, calling this ResponseBody.string() method more than once wipes out the string
        String responseBody = response.body().string();
        Log.i(requestName, "response: " + responseBody);
        T responseObj = mGson.fromJson(responseBody, responseClass);

        //handle Gson parsing errors
        if (responseObj == null) {
            throw new IOException("Error parsing response into JSON");
        }

        //handle API errors
        if (!responseObj.success) {

            if (responseObj.getError() == null) {
                throw new IOException("API Error with no message.");
            }

            throw new IOException(
                    "Error " + responseObj.getError().getCode() + ": " + responseObj.getError()
                            .getMessage());
        }

        return responseObj;
    }

    private String getAbsolutePath() {
        return Config.ServerInfo.SERVER_MOBILE_URL + Config.API_VERSION;
    }

    private void authenticateRequest(BaseRequest request) {
        String sessionKey = UserInfo.getSessionKey(App.getInstance());
        String sessionToken = UserInfo.getSessionToken(App.getInstance());
        request.authenticate(sessionKey, sessionToken);
    }

}
