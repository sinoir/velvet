package com.delectable.mobile.api.jobs.basewines;

import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceRequest;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceResponse;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchWineSourceJob extends BaseJob {

    private static final String TAG = FetchWineSourceJob.class.getSimpleName();

    @Inject
    protected WineSourceModel mWineSourceModel;

    private String mWineId;

    private String mState;

    public FetchWineSourceJob(String wineId, String state) {
        super(new Params(Priority.SYNC));
        mWineId = wineId;
        mState = state;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/wine_profiles/source";
        WineProfilesSourceRequest request = new WineProfilesSourceRequest(mWineId, mState);
        WineProfilesSourceResponse response = getNetworkClient().post(endpoint, request,
                WineProfilesSourceResponse.class);

        mWineSourceModel.saveWineSource(response.getPayload());
        getEventBus().post(new FetchedWineSourceEvent(mWineId));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedWineSourceEvent(getErrorMessage(), getErrorCode(), mWineId));
    }
}
