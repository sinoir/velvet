package com.delectable.mobile.net;

import com.google.gson.Gson;

import com.delectable.mobile.App;
import com.delectable.mobile.Config;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.util.HelperUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class NetworkClient extends BaseNetworkClient {

    private static final String TAG = NetworkClient.class.getSimpleName();

    private static final String USER_AGENT = "DelectableAndroid";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected String getBaseUrl() {
        return Config.ServerInfo.SERVER_PROD_URL + Config.API_VERSION;
    }

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
        Log.i(requestName, "url: " + getBaseUrl() + endpoint);
        Log.i(requestName, "body: " + requestString);
        RequestBody body = RequestBody.create(JSON, requestString);
        Request request = new Request.Builder()
                .url(getBaseUrl() + endpoint)
                .addHeader("User-Agent", USER_AGENT)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();

        return handleResponse(response, requestName, responseClass);
    }

    private void authenticateRequest(BaseRequest request) {
        String sessionKey = UserInfo.getSessionKey(App.getInstance());
        String sessionToken = UserInfo.getSessionToken(App.getInstance());
        request.authenticate(sessionKey, sessionToken);
    }

}
