package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.models.Registration;
import com.delectable.mobile.api.models.Resource;
import com.delectable.mobile.data.UserInfo;

import android.content.Context;

/**
 * Created by abednarek on 5/22/14.
 */
public class RegistrationController extends BaseNetworkController {

    public RegistrationController(Context context) {
        super(context);
    }

    public void loginUser(Registration registration, final SimpleRequestCallback callback) {
        performActionOnResource(registration, Registration.A_LOGIN, new RequestActionCallback() {
            @Override
            public void onSuccess(Resource result, int action) {
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
