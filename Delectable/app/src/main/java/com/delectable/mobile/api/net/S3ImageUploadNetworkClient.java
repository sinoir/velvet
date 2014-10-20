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

        // TODO: Parse The response XML for Errors...
        Response responseObj = mClient.newCall(request).execute();
        String responseBody = responseObj.body().string();
        Log.i(requestName, "response: " + responseBody);
        return responseObj;
    }
}
