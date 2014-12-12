package com.delectable.mobile.api.jobs.builddatecheck;

import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.api.events.builddatecheck.BuildDateCheckedEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.net.MotdNetworkClient;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

public class FetchVersionPropsJob extends BaseJob {

    private static final String TAG = FetchVersionPropsJob.class.getSimpleName();


    @Inject
    protected MotdNetworkClient mNetworkClient;

    private String mDeviceId;

    public FetchVersionPropsJob() {
        super(new Params(Priority.UX.value()).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://s3.amazonaws.com/fermentationtank/android/version.properties")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String body = response.body().string();
        String key = "BUILD_DATE=";

        if (!body.contains(key)) {
            throw new IOException("version.properties did not contain BUILD_DATE key");
        }

        boolean shouldUpdate;

        //getting value for key
        int start = body.indexOf(key) + key.length();
        String value = body.substring(start).trim();

        long serverBuildDate = Long.parseLong(value, 10);

        if (serverBuildDate > BuildConfig.BUILD_DATE) {
            shouldUpdate = true;
        } else {
            shouldUpdate = false;
        }

        getEventBus().post(new BuildDateCheckedEvent(shouldUpdate));

    }

    @Override
    protected void onCancel() {
        getEventBus().post(new BuildDateCheckedEvent(getErrorMessage()));
    }
}
