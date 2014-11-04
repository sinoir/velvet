package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

import java.util.ArrayList;

public class AccountMinimalListResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private ArrayList<AccountMinimal> accounts;

        public ArrayList<AccountMinimal> getAccounts() {
            return accounts;
        }
    }
}
