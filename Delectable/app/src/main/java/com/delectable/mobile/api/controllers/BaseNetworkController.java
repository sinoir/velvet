package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.NetworkClient;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.requests.BaseRequest;
import com.delectable.mobile.data.UserInfo;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class BaseNetworkController {

    public static final String TAG = "BaseNetworkController";

    private Context mContext;

    public BaseNetworkController(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void performRequest(final BaseRequest requestObject,
            final RequestCallback callback) {
        String resourceUri = requestObject.getResourceUrl();

        Log.d(TAG, "Sending Resource: " + requestObject.getClass().getSimpleName() + " Payload: "
                + requestObject.buildPayload());
        try {
            StringEntity postEntity = buildPostEntityFromResource(requestObject);
            NetworkClient.post(
                    getContext(),
                    resourceUri,
                    postEntity,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d(TAG, "Received Response!" + response);
                            boolean isSuccess = response.optBoolean("success");
                            if (isSuccess) {
                                BaseResponse result = requestObject.buildResopnseFromJson(response);
                                if (callback != null) {
                                    callback.onSuccess(result);
                                }
                            } else {
                                RequestError error = RequestError.buildFromJson(response);
                                if (callback != null) {
                                    callback.onFailed(error);
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                Throwable error) {
                            RequestError requestError = new RequestError();
                            requestError.setCode(statusCode);
                            requestError.setMessage("Internal Server Error");
                            Log.e(TAG, "Request Failed", error);
                            if (callback != null) {
                                callback.onFailed(requestError);
                            }
                            super.onFailure(statusCode, headers, responseBody, error);
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
     * Build out request object with bottom tier meta params
     */
    public JSONObject buildPayloadWithMetaParams(BaseRequest request)
            throws JSONException {
        JSONObject payload = request.buildPayload();
        JSONObject requestObject = request.buildMetaParams();
        // Default Session stuff
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
     */
    public StringEntity buildPostEntityFromResource(BaseRequest request)
            throws UnsupportedEncodingException, JSONException {
        StringEntity entity = new StringEntity(
                buildPayloadWithMetaParams(request).toString());
        return entity;
    }

    public interface RequestCallback {

        public void onSuccess(BaseResponse result);

        public void onFailed(RequestError error);
    }

    public interface SimpleRequestCallback {

        public void onSucess();

        public void onFailed(RequestError error);
    }
}
