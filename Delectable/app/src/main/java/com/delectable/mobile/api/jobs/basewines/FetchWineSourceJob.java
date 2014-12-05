package com.delectable.mobile.api.jobs.basewines;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceRequest;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceResponse;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.util.USStates;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchWineSourceJob extends BaseJob {

    private static final String TAG = FetchWineSourceJob.class.getSimpleName();

    @Inject
    protected WineSourceModel mWineSourceModel;

    private String mWineId;

    private String mState;

    /**
     * Fetches Source for Wine and State
     *
     * @param wineId - Wine ID
     * @param state  - Optional State, if null, will use State from User Account or CA
     */
    public FetchWineSourceJob(String wineId, String state) {
        super(new Params(Priority.SYNC.value()));
        mWineId = wineId;

        USStates selectedState = USStates.stateByNameOrAbbreviation(state);

        // If we haven't passed up the optional State, use the Users default saved shipping state
        if (UserInfo.getAccountPrivate(App.getInstance()) != null && selectedState == null) {
            String savedShippingState = UserInfo.getAccountPrivate(App.getInstance())
                    .getSourcingState();
            selectedState = USStates.stateByNameOrAbbreviation(savedShippingState);
        }

        // If user hasn't Specified a state yet in their account, nor passed up a state, use CA by default.
        if (selectedState == null) {
            selectedState = USStates.CA;
        }

        mState = selectedState.getStateAbbreviation();
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
