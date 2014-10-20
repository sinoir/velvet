package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsIdentifiersListingResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsUpdateIdentifierRequest;
import com.path.android.jobqueue.Params;

public class UpdateIdentifierJob extends BaseJob {

    private static final String TAG = UpdateIdentifierJob.class.getSimpleName();

    private String mIdentifierId;

    private String mString;

    private String mUserCountryCode;

    public UpdateIdentifierJob(Identifier identifier, String string, String userCountryCode) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mIdentifierId = identifier.getId();
        mString = string;
        mUserCountryCode = userCountryCode;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_identifier";
        AccountsUpdateIdentifierRequest request = new AccountsUpdateIdentifierRequest(
                mIdentifierId, mString, mUserCountryCode);
        AccountsIdentifiersListingResponse response = getNetworkClient().post(endpoint, request,
                AccountsIdentifiersListingResponse.class);
        getEventBus()
                .post(new UpdatedIdentifiersListingEvent(response.getPayload().getIdentifiers()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedIdentifiersListingEvent(TAG + " " + getErrorMessage()));
    }
}
