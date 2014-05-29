package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.requests.AccountsFollowerFeedRequest;

import android.content.Context;

public class AccountsNetworkController extends BaseNetworkController {

    public AccountsNetworkController(Context context) {
        super(context);
    }

    // TODO: Figure out how the before / after should work... then pass it up?
    public void loadFollowerFeed(final RequestActionCallback callback) {
        AccountsFollowerFeedRequest request = new AccountsFollowerFeedRequest(
                AccountsFollowerFeedRequest.CONTEXT_DETAILS);
        performActionOnResource(request, new RequestActionCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onFailed(RequestError error) {
                if (callback != null) {
                    callback.onFailed(error);
                }
            }
        });
    }
}
