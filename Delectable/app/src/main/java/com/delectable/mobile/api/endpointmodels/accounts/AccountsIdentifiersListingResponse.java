package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

import java.util.ArrayList;

/**
 * These endpoints have this response:
 * /accounts/remove_identifier
 * /accounts/remove_identifier
 * /accounts/remove_identifier
 */
public class AccountsIdentifiersListingResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private ArrayList<Identifier> identifiers;

        public ArrayList<Identifier> getIdentifiers() {
            return identifiers;
        }
    }
}
