package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.data.DeviceContactsModel;
import com.delectable.mobile.events.accounts.FetchedAccountsFromContactsEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountContactListSuggestionsRequest;
import com.delectable.mobile.model.api.accounts.AccountMinimalListResponse;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;
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
        AccountMinimalListResponse response = getNetworkClient()
                .post(endpoint, request, AccountMinimalListResponse.class);

        ArrayList<AccountMinimal> accounts = response.getPayload().getAccounts();
        // Note: No way to remove any contact from the contacts list
        getEventBus().post(new FetchedAccountsFromContactsEvent(accounts, contacts));
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