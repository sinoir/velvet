package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsIdentifiersListingResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsRemoveIdentifierRequest;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.events.accounts.UpdatedIdentifiersListingEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Identifier;
import com.path.android.jobqueue.Params;

import android.util.Log;

public class RemoveIdentifierJob extends BaseJob {

    private static final String TAG = RemoveIdentifierJob.class.getSimpleName();

    private String mIdentifierId;


    public RemoveIdentifierJob(String requestId, Identifier identifier) {
        super(requestId, new Params(Priority.SYNC.value()).requireNetwork().persist());
        mIdentifierId = identifier.getId();
    }

    @Override
    public void onAdded() {
        Account account = UserInfo.getAccountPrivate(App.getInstance());
        UserInfo.setTempAccount(account);

        Identifier identifier = account.getIdentifier(mIdentifierId);
        if (identifier == null) {
            Log.wtf(TAG, "identifier id does not exist in identifiers list");
            return;
        }

        //primary identifier shouldn't be allow to be removed, just like in API
        if (identifier.getPrimary()) {
            return;
        }

        //hold onto original state of account
        UserInfo.setTempAccount(account);

        //remove from list
        account.getIdentifiers().remove(identifier);

        //if identifier is twitter, we need to remove blank out twitter fields as well
        if (identifier.getType().equalsIgnoreCase(Identifier.Type.TWITTER.toString())) {
            account.setTwTokenSecret(null);
            account.setTwId(0);
            account.setTwToken(null);
            account.setTwScreenName(null);
        }

        //if identifier is facebook, we need to remove blank out facebook fields as well
        if (identifier.getType().equalsIgnoreCase(Identifier.Type.FACEBOOK.toString())) {
            account.setFbId(null);
            account.setFbToken(null);
            account.setFbTokenExp(0);
        }

        UserInfo.setAccountPrivate(account);

        UpdatedAccountEvent event = new UpdatedAccountEvent(mRequestId, account);
        mEventBus.post(event);
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/accounts/remove_identifier";
        AccountsRemoveIdentifierRequest request = new AccountsRemoveIdentifierRequest(
                mIdentifierId);
        AccountsIdentifiersListingResponse response = getNetworkClient().post(endpoint, request,
                AccountsIdentifiersListingResponse.class);

        //update cached account with new identifiers list, send out updated account event
        Account account = UserInfo.getAccountPrivate(App.getInstance());
        account.setIdentifiers(response.getPayload().getIdentifiers());
        UserInfo.setAccountPrivate(account);
        UserInfo.clearTempAccount();

        UpdatedAccountEvent event = new UpdatedAccountEvent(mRequestId, account);
        mEventBus.post(event);
    }

    @Override
    protected void onCancel() {
        //retrieve og account and set as real account again
        Account account = UserInfo.getTempAccountPrivate();
        UserInfo.setAccountPrivate(account);
        UserInfo.clearTempAccount();
        UpdatedAccountEvent event = new UpdatedAccountEvent(mRequestId, account, TAG + " " + getErrorMessage());
        mEventBus.post(event);
        mEventBus.post(new UpdatedIdentifiersListingEvent(TAG + " " + getErrorMessage()));
    }
}
