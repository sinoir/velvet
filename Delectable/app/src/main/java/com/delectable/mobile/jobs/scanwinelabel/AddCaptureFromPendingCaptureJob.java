package com.delectable.mobile.jobs.scanwinelabel;

import com.delectable.mobile.events.scanwinelabel.AddedCaptureFromPendingCaptureEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureDetailsResponse;
import com.delectable.mobile.model.api.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import android.util.Log;

import java.util.Calendar;

public class AddCaptureFromPendingCaptureJob extends BaseJob {

    private static final String TAG = AddCaptureFromPendingCaptureJob.class.getSimpleName();

    private AddCaptureFromPendingCaptureRequest mRequest;

    public AddCaptureFromPendingCaptureJob(AddCaptureFromPendingCaptureRequest request) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        // TODO: Possibly Save the request and then load the request from a cache to perform "async" requests?
        mRequest = request;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        String endpoint = "/captures/from_pending_capture";
        mRequest.setContext("details");
        CaptureDetailsResponse response = getNetworkClient().post(endpoint, mRequest,
                CaptureDetailsResponse.class);

        Log.d(TAG, "Added new Capture: " + response.getCapturePayload());
        getEventBus().post(new AddedCaptureFromPendingCaptureEvent(response.getCapturePayload()));
        KahunaUtil.trackCreateCapture(Calendar.getInstance().getTime(),
                response.getCapturePayload().getId());
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus().post(new AddedCaptureFromPendingCaptureEvent(getErrorMessage()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
