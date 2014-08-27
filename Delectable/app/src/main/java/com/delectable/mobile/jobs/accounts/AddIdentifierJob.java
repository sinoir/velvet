package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsAddIdentifierRequest;
import com.delectable.mobile.model.api.accounts.AccountsIdentifiersListingResponse;
import com.path.android.jobqueue.Params;

public class AddIdentifierJob extends BaseJob {

    private static final String TAG = AddIdentifierJob.class.getSimpleName();

    private String mString;

    private String mType;

    private String mUserCountryCode;

    public AddIdentifierJob(String string, String type, String userCountryCode) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mString = string;
        mType = type;
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
