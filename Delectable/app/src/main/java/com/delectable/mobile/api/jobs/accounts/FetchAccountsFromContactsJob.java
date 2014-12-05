package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.DeviceContactsModel;
import com.delectable.mobile.api.endpointmodels.accounts.AccountMinimalListResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsContactListSuggestionsRequest;
import com.delectable.mobile.api.events.accounts.FetchedAccountsFromContactsEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;
import java.util.List;

public class FetchAccountsFromContactsJob extends BaseJob {

    private static final String TAG = FetchAccountsFromContactsJob.class.getSimpleName();

    DeviceContactsModel mDeviceContactsModel;

    public FetchAccountsFromContactsJob() {
        super(new Params(Priority.UX.value()).requireNetwork().persist());
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

        AccountsContactListSuggestionsRequest request = new AccountsContactListSuggestionsRequest(
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
