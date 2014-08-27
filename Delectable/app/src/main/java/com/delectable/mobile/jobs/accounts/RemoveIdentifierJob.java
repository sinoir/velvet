package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsIdentifiersListingResponse;
import com.delectable.mobile.model.api.accounts.AccountsRemoveIdentifierRequest;
import com.path.android.jobqueue.Params;

public class RemoveIdentifierJob extends BaseJob {

    private static final String TAG = RemoveIdentifierJob.class.getSimpleName();

    private String mIdentifierId;

    public RemoveIdentifierJob(Identifier identifier) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mIdentifierId = identifier.getId();
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/remove_identifier";
        AccountsRemoveIdentifierRequest request = new AccountsRemoveIdentifierRequest(
                mIdentifierId);
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
