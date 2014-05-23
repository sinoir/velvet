package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.NetworkClient;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.models.Resource;
import com.delectable.mobile.data.UserInfo;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by abednarek on 5/21/14.
 */
public class BaseNetworkController {

    public static final String TAG = "BaseNetworkController";

    private Context mContext;

    public BaseNetworkController(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void performActionOnResource(final Resource resource, final int action,
            final RequestActionCallback callback) {
        JSONObject payload = resource.buildPayloadForAction(action);
        String resourceUri = resource.getResourceUrlForAction(action);
        Log.d(TAG, "Sending Resource: " + resource.getClass().getSimpleName() + " Action: " + action
                + " Payload: " + payload.toString());
        try {
            StringEntity postEntity = buildPostEntityFromPayload(payload);
            NetworkClient.post(
                    getContext(),
                    resourceUri,
                    postEntity,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d(TAG, "Received Response!" + response);
                            boolean isSuccess = response.optBoolean("success");
                            if (isSuccess && response.optJSONObject("payload") != null) {
                                Resource result = resource.parsePayloadForAction(
                                        response, action);
                                if (callback != null) {
                                    callback.onSuccess(result, action);
                                }
                            } else {
                                RequestError error = RequestError.buildFromJson(response);
                                if (callback != null) {
                                    callback.onFailed(error);
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            RequestError requestError = new RequestError();
                            requestError.setCode(statusCode);
                            requestError.setMessage("Internal Server Error");
                            if (callback != null) {
                                callback.onFailed(requestError);
                            }
                            super.onFailure(statusCode, error, content);
                        }
                    }
            );
        } catch (Exception ex) {
            Log.wtf(getClass().getSimpleName(), "Failed to send Request", ex);
            RequestError requestError = new RequestError();
            requestError.setCode(-1);
            requestError.setMessage("Internal Error");
            if (callback != null) {
                callback.onFailed(requestError);
            }
        }
    }

    /**
     * Build out request object with session info
     *
     * @param payload - Payload to attach to the request
     * @return JSON Request Object with Session info
     */
    public JSONObject buildRequestWithSession(JSONObject payload) throws JSONException {
        JSONObject requestObject = new JSONObject();
        requestObject.put("sessionType", "mobile");
        if (UserInfo.isSignedIn(getContext())) {
            requestObject.put("sessionKey", UserInfo.getSessionKey(getContext()));
            requestObject.put("sessionToken", UserInfo.getSessionToken(getContext()));
        }
        requestObject.put("payload", payload);
        return requestObject;
    }

    /**
     * Builds String Entity for posting
     *
     * @param payload - Payload object
     * @return String Entity for posting
     */
    public StringEntity buildPostEntityFromPayload(JSONObject payload)
            throws UnsupportedEncodingException, JSONException {
        StringEntity entity = new StringEntity(buildRequestWithSession(payload).toString());
        return entity;
    }

    public interface RequestActionCallback {

        public void onSuccess(Resource result, int action);

        public void onFailed(RequestError error);
    }

    public interface SimpleRequestCallback {

        public void onSucess();

        public void onFailed(RequestError error);
    }
}
