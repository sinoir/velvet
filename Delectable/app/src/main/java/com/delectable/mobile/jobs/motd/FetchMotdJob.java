package com.delectable.mobile.jobs.motd;

import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.api.models.Motd;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.motd.FetchedMotdEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.MotdResponse;
import com.delectable.mobile.net.MotdNetworkClient;
import com.path.android.jobqueue.Params;

import android.os.Build;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

public class FetchMotdJob extends BaseJob {

    private static final String TAG = FetchMotdJob.class.getSimpleName();

    private final String mSessionKey;

    @Inject
    protected MotdNetworkClient mNetworkClient;

    private String mDeviceId;

    public FetchMotdJob(String sessionKey, String deviceId) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mSessionKey = sessionKey;
        mDeviceId = deviceId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        String endpoint = "";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sk", mSessionKey);
        params.put("u", mDeviceId); //member var bc need context in order to retrieve this
        params.put("cuv", BuildConfig.VERSION_NAME);
        params.put("cv", String.valueOf(BuildConfig.VERSION_CODE));
        params.put("os", String.valueOf(Build.VERSION.RELEASE));
        params.put("d", Build.MANUFACTURER +" " + Build.MODEL);
        params.put("l", Locale.getDefault().toString());

        MotdResponse response = mNetworkClient.get(endpoint, params,
                MotdResponse.class);

        //cache the response
        Motd motd = response.getPayload();
        UserInfo.setMotd(motd);

        getEventBus().post(new FetchedMotdEvent(motd));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedMotdEvent(getErrorMessage()));
    }
}
