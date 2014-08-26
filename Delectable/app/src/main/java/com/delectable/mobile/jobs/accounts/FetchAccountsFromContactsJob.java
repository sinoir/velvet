package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.data.DeviceContactsModel;
import com.delectable.mobile.events.accounts.FetchedAccountsFromContactsEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountContactListSuggestionsRequest;
import com.delectable.mobile.model.api.accounts.AccountDelectafriendsResponse;
import com.path.android.jobqueue.Params;

import java.util.List;

public class FetchAccountsFromContactsJob extends BaseJob {

    private static final String TAG = FetchAccountsFromContactsJob.class.getSimpleName();

    DeviceContactsModel mDeviceContactsModel;

    public FetchAccountsFromContactsJob() {
        super(new Params(Priority.UX).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        String endpoint = "/accounts/contact_list_suggestions";

        // Fetch local Contacts
        mDeviceContactsModel = new DeviceContactsModel();
        List<TaggeeContact> contacts = mDeviceContactsModel.loadDeviceContactsAsTageeContacts();

        AccountContactListSuggestionsRequest request = new AccountContactListSuggestionsRequest(
                contacts, null);
        AccountDelectafriendsResponse response = getNetworkClient()
                .post(endpoint, request, AccountDelectafriendsResponse.class);
        // TODO: Pass up Contacts that aren't in the payload to suggest to invite
        getEventBus()
                .post(new FetchedAccountsFromContactsEvent(response.getPayload().getAccounts()));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus().post(new FetchedAccountsFromContactsEvent(getErrorMessage()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
