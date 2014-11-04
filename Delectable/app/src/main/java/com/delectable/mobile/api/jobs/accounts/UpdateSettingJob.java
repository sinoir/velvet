package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.events.accounts.UpdatedSettingEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsUpdateSettingRequest;
import com.path.android.jobqueue.Params;

public class UpdateSettingJob extends BaseJob {

    private static final String TAG = UpdateSettingJob.class.getSimpleName();

    private AccountConfig.Key mKey;

    private boolean mSetting;

    public UpdateSettingJob(AccountConfig.Key key, boolean setting) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mKey = key;
        mSetting = setting;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_setting";
        AccountsUpdateSettingRequest request = new AccountsUpdateSettingRequest(mKey, mSetting);
        BaseResponse response = getNetworkClient().post(endpoint, request,
                BaseResponse.class);
        getEventBus().post(new UpdatedSettingEvent(mKey, mSetting));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedSettingEvent(TAG + " " + getErrorMessage()));
    }
}
