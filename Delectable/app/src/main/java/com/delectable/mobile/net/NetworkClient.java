package com.delectable.mobile.net;

import com.delectable.mobile.App;
import com.delectable.mobile.Config;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.BaseResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class NetworkClient {

    private static final String USER_AGENT = "DelectableAndroid";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mClient = new OkHttpClient();
    private Gson mGson = new Gson();

    public <T extends BaseResponse> T post(String endpoint, BaseRequest requestObject, Class<T> responseClass) throws IOException {

        authenticateRequest(requestObject);
        String requestString = mGson.toJson(requestObject);

        RequestBody body = RequestBody.create(JSON, requestString);
        Request request = new Request.Builder()
                .url(getAbsolutePath() + endpoint)
                .addHeader("User-Agent", USER_AGENT)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            // TODO error handling, throw exception that gets handled in job queue
            throw new IOException("request not successfull: " + response);
        }

        return mGson.fromJson(response.body().string(), responseClass);
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
