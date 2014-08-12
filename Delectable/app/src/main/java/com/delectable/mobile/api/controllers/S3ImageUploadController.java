package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class S3ImageUploadController {

    public static final String TAG = S3ImageUploadController.class.getSimpleName();

    private static String sS3BaseURL = "https://s3.amazonaws.com";

    private ProvisionCapture mProvision;

    private Context mContext;

    private AsyncHttpClient mClient;

    public S3ImageUploadController(Context context, ProvisionCapture provision) {
        mContext = context;
        mProvision = provision;

        mClient = new AsyncHttpClient();
        setupHeaders();
    }

    private void setupHeaders() {
        mClient.addHeader("Cache-Control", mProvision.getHeaders().getCacheControl());
        mClient.addHeader("Authorization", mProvision.getHeaders().getAuthorization());
        mClient.addHeader("Date", mProvision.getHeaders().getDate());
        mClient.addHeader("url", mProvision.getHeaders().getUrl());
    }

    public void uploadImage(Bitmap image, BaseNetworkController.SimpleRequestCallback callback) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();

        uploadImage(imageData, callback);
    }

    public void uploadImage(byte[] imageData,
            final BaseNetworkController.SimpleRequestCallback callback) {
        String url = sS3BaseURL + mProvision.getHeaders().getUrl();

        ByteArrayEntity entity = new ByteArrayEntity(imageData);
        entity.setContentType(mProvision.getHeaders().getContentType());

        mClient.put(mContext, url, entity, mProvision.getHeaders().getContentType(),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        if (callback != null) {
                            callback.onSucess();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                            Throwable error) {
                        super.onFailure(statusCode, headers, responseBody, error);
                        RequestError requestError = new RequestError();
                        requestError.setCode(statusCode);
                        requestError.setMessage(error.getLocalizedMessage());
                        if (callback != null) {
                            callback.onFailed(requestError);
                        }
                        Log.e(TAG, "Failed to upload Image", error);
                    }
                }
        );
    }
}
