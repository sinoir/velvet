package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.endpointmodels.accounts.AccountsAddIdentifierRequest;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsIdentifiersListingResponse;
import com.delectable.mobile.api.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Identifier;
import com.path.android.jobqueue.Params;

public class AddIdentifierJob extends BaseJob {

    private static final String TAG = AddIdentifierJob.class.getSimpleName();

    private String mString;

    private String mType;

    private String mUserCountryCode;

    public AddIdentifierJob(String string, Identifier.Type type, String userCountryCode) {
        super(new Params(Priority.SYNC.value()).requireNetwork().persist());
        mString = string;
        mType = type.toString();
        mUserCountryCode = userCountryCode;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/add_identifier";
        AccountsAddIdentifierRequest request = new AccountsAddIdentifierRequest(
                mString, mType, mUserCountryCode);
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
