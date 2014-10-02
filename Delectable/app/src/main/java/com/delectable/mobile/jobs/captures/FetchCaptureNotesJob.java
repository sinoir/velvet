package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.events.accounts.UpdatedSettingEvent;
import com.delectable.mobile.events.captures.FetchedCaptureNotesEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureNotesResponse;
import com.delectable.mobile.model.api.captures.CapturesNotesRequest;
import com.path.android.jobqueue.Params;

public class FetchCaptureNotesJob extends BaseJob {

    private static final String TAG = FetchCaptureNotesJob.class.getSimpleName();

    private String mBaseWineId;

    private String mWineProfileId;

    private String mBefore;

    private String mAfter;

    private String mIncludeCaptureNote;

    public FetchCaptureNotesJob(String baseWineId, String wineProfileId, String before, String after,
            String includeCaptureNote) {
        super(new Params(Priority.SYNC).requireNetwork().persist());

        mBaseWineId = baseWineId;
        mWineProfileId = wineProfileId;
        mBefore = before;
        mAfter = after;
        mIncludeCaptureNote = includeCaptureNote;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/notes";
        CapturesNotesRequest request = new CapturesNotesRequest(mBaseWineId, mWineProfileId,
                mBefore, mAfter, mIncludeCaptureNote);
        CaptureNotesResponse response = getNetworkClient()
                .post(endpoint, request, CaptureNotesResponse.class);
        getEventBus().post(new FetchedCaptureNotesEvent(response.getPayload()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedCaptureNotesEvent(TAG + " " + getErrorMessage()));
    }


}