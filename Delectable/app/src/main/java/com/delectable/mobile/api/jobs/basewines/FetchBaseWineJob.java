package com.delectable.mobile.api.jobs.basewines;

import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.basewines.BaseWineResponse;
import com.delectable.mobile.api.events.wines.UpdatedBaseWineEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.BaseWine;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchBaseWineJob extends BaseJob {

    @Inject
    BaseWineModel mBaseWineModel;

    private String mBaseWineId;

    public FetchBaseWineJob(String baseWineId) {
        super(new Params(Priority.UX.value()).requireNetwork().persist());
        mBaseWineId = baseWineId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/base_wines/context";

        BaseWine cachedBaseWine = mBaseWineModel.getBaseWine(mBaseWineId);

        ActionRequest request = new ActionRequest(mBaseWineId);
        request.setContext("profile");

        if (cachedBaseWine != null) {
            request.setETag(cachedBaseWine.getETag());
        }

        BaseWineResponse response = getNetworkClient()
                .post(endpoint, request, BaseWineResponse.class);

        if (!response.isETagMatch()) {
            BaseWine baseWine = response.getBaseWine();
            mBaseWineModel.saveBaseWine(baseWine);
        }
        getEventBus().post(new UpdatedBaseWineEvent(true, mBaseWineId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new UpdatedBaseWineEvent(getErrorMessage(), mBaseWineId));
        } else {
            getEventBus().post(new UpdatedBaseWineEvent(false, mBaseWineId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
