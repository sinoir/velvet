package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.Registration;
import com.delectable.mobile.api.requests.RegistrationsLogin;
import com.delectable.mobile.data.UserInfo;

import android.content.Context;

public class RegistrationController extends BaseNetworkController {

    public RegistrationController(Context context) {
        super(context);
    }

    public void loginUser(RegistrationsLogin loginRequest,
            final SimpleRequestCallback callback) {

        performRequest(loginRequest, new RequestCallback() {

            @Override
            public void onSuccess(BaseResponse result) {
                UserInfo.onSignIn(getContext(), (Registration) result);
                // TODO: Persist the whole account object
                if (callback != null) {
                    callback.onSucess();
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
