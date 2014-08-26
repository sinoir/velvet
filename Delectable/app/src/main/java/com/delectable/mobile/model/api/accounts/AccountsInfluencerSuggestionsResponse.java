package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.model.api.BaseResponse;

import java.util.ArrayList;

/**
 * The /accounts/facebook_suggestions endpoint also uses this response object.
 */
public class AccountsInfluencerSuggestionsResponse extends BaseResponse {

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
