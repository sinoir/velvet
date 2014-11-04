package com.delectable.mobile.api.net;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;

import java.io.IOException;

public class S3ImageUploadNetworkClient {

    private static final String TAG = S3ImageUploadNetworkClient.class.getSimpleName();

    private static final String S3_BASE_URL = "https://s3.amazonaws.com";

    private OkHttpClient mClient = new OkHttpClient();

    public Response uploadImage(byte[] imageData, ProvisionCapture provision) throws IOException {
        String url = S3_BASE_URL + provision.getUrl();
        String requestName = TAG + ".S3Uplaod";

        Log.i(requestName, "url: " + url);
        Log.i(requestName, "provision: " + provision.toString());

        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), imageData);

        Headers.Builder headerBuilder = new Headers.Builder();

        for (String headerKey : provision.getHeaders().keySet()) {
            headerBuilder = headerBuilder.add(headerKey, provision.getHeaders().get(headerKey));
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(headerBuilder.build())
                .put(body)
                .build();

        Log.i(requestName, "request:" + request);
        Log.i(requestName, "request headers:" + request.headers());

        Response response = mClient.newCall(request).execute();

        //4 possible ways s3 can error out

        //response not successful, obviously
        if (!response.isSuccessful()) {
            String errorMessage = "S3 HTTP Request Error " + response.code() + ": " + response.toString();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }

        //if the response code isn't 200
        if (response.code() != 200) {
            String errorMessage = "S3 HTTP Response not 200. Error " + response.code() + ": " + response.toString();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }

        //if there's no etag in the header
        String etag = response.header("ETag");
        if (etag == null) {
            String errorMessage = "S3 HTTP Response had no ETag. Error " + response.code() + ": " + response.toString();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }

        String responseBody = response.body().string();
        Log.i(requestName, "response: " + responseBody);

        //if the response body has text, means that it's an error
        if (!responseBody.isEmpty()) {
            String errorMessage = "S3 HTTP Response body was not empty. Error " + response.code() + ": " + response.toString();
            Log.i(requestName, "error: " + errorMessage);
            throw new IOException(errorMessage);
        }

        return response;
    }
}
