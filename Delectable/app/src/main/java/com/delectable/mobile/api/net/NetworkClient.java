package com.delectable.mobile.api.net;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.ServerInfo;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.util.DelException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Type;

public class NetworkClient extends BaseNetworkClient {

    private static final String TAG = NetworkClient.class.getSimpleName();

    private static final String USER_AGENT = "DelectableAndroid";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected String getBaseUrl() {
        return ServerInfo.getEnvironment().getUrl() + ServerInfo.API_VERSION;
    }

    /**
     * Convenience method that calls {@link #post(String, BaseRequest, Class, Type, boolean)} with
     * authentication set to true. Generally all API requests call this method except for
     * registrations/register and registrations/login.
     */
    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject,
            Class<T> responseClass) throws IOException, DelException {
        return post(endpoint, requestObject, responseClass, null, true);
    }

    /**
     * Convenience method for post requests that have a response that employ generics. If using
     * generics, {@code Type} needs to be passed in instead of the usual {@code Class}. Also, see
     * {@link #post(String, BaseRequest, Class, Type, boolean)}
     */
    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject,
            Type responseType) throws IOException, DelException {
        return post(endpoint, requestObject, null, responseType, true);
    }

    /**
     * Convenience method for Login/Register requests, allows implementers to toggle off
     * authentication so that sessionKey and sessionToken are explicitly not passed in with the
     * request.
     */
    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject,
            Class<T> responseClass, boolean authenticate) throws IOException, DelException {
        return post(endpoint, requestObject, responseClass, null, authenticate);
    }

    /**
     * @param endpoint      the url endpoint
     * @param requestObject this object should have all the fields required to complete the post
     *                      request
     * @param responseClass pass in the response's {@code Class} (or alternatively, {@code Type} in
     *                      next field)
     * @param responseType  pass in the response's {@code Type} (or alternatively, {@code Class} in
     *                      previous field)
     * @param authenticate  <code>true</code> if you want to pass in sessionKey and sessionToken
     *                      with the request.
     */
    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject,
            Class<T> responseClass, Type responseType, boolean authenticate)
            throws IOException, DelException {
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

        return handleResponse(response, requestName, responseClass, responseType);
    }

    private void authenticateRequest(BaseRequest request) {
        String sessionKey = UserInfo.getSessionKey(App.getInstance());
        String sessionToken = UserInfo.getSessionToken(App.getInstance());
        request.authenticate(sessionKey, sessionToken);
    }

}
