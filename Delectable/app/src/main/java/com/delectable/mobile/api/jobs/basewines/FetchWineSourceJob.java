package com.delectable.mobile.api.jobs.basewines;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceRequest;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceResponse;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.TransitionState;
import com.delectable.mobile.util.USStates;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchWineSourceJob extends BaseJob {

    private static final String TAG = FetchWineSourceJob.class.getSimpleName();

    @Inject
    protected WineSourceModel mWineSourceModel;

    @Inject
    protected CaptureDetailsModel mCaptureDetailsModel;

    //we keep track of the capture id so we know to update it when the request returns successfully
    private String mCaptureId;

    private String mWineId;

    private String mState;

    public FetchWineSourceJob(String captureId, String wineId, String state) {
        this(wineId, state);
        mCaptureId = captureId;
    }

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
        Account account = UserInfo.getAccountPrivate();
        if (account != null && selectedState == null) {
            String savedShippingState = account.getSourcingState();
            selectedState = USStates.stateByNameOrAbbreviation(savedShippingState);
        }

        // If user hasn't Specified a state yet in their account, nor passed up a state, use CA by default.
        if (selectedState == null) {
            selectedState = USStates.CA;
        }

        mState = selectedState.getStateAbbreviation();
    }

    @Override
    public void onAdded() {
        //update capture details model to transacting if capture id was provided
        if (mCaptureId == null) {
            return;
        }
        CaptureDetails capture = mCaptureDetailsModel.getCapture(mCaptureId);
        if (capture == null) {
            return;
        }
        capture.setTransacting(true);
        capture.setTransitionState(TransitionState.UPDATING);
        capture.setTransactionKey(CaptureDetails.TRANSACTION_KEY_PRICE);

        //TODO event currently does not broadcast after successful write to model, but maybe it should. If so, remember to account for event handlers in fragments
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/wine_profiles/source";
        WineProfilesSourceRequest request = new WineProfilesSourceRequest(mWineId, mState);
        WineProfilesSourceResponse response = getNetworkClient().post(endpoint, request,
                WineProfilesSourceResponse.class);

        mWineSourceModel.saveWineSource(response.getPayload());

        //update capture details model if capture id was provided
        CaptureDetails capture = null;
        if (mCaptureId != null) {
            capture = mCaptureDetailsModel.getCapture(mCaptureId);

            //set fetched wineProfile with price as wine profile in capture
            if (capture != null) {
                capture.setWineProfile(response.getPayload().getWineProfile());
                capture.setTransacting(false);
                capture.setTransitionState(TransitionState.SYNCED);
                capture.setTransactionKey(null);
            }
        }
        mEventBus.post(new FetchedWineSourceEvent(capture, mWineId));
    }

    @Override
    protected void onCancel() {
        //update capture details model to transacting if capture id was provided
        if (mCaptureId != null) {
            CaptureDetails capture = mCaptureDetailsModel.getCapture(mCaptureId);
            if (capture != null) {
                capture.setTransacting(false);
                capture.setTransitionState(TransitionState.SYNCED);
                capture.setTransactionKey(null);
            }
            mEventBus.post(new FetchedWineSourceEvent(getErrorMessage(), getErrorCode(), capture,
                    mWineId));
            return;
        }

        //no captureId, job was invoked from WineProfile screen with just the WineProfile
        mEventBus.post(new FetchedWineSourceEvent(getErrorMessage(), getErrorCode(), mCaptureId,
                mWineId));
    }
}
