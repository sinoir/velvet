package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

import java.util.LinkedList;
import java.util.List;

public class AccountsFollowRequest extends BaseRequest {

    private Payload payload;

    public AccountsFollowRequest(String accountId, boolean isFollowing) {
        payload = new Payload(accountId, isFollowing);
    }

    public static class Payload {

        private List<String> ids;

        private boolean action;

        public Payload(List<String> ids, boolean action) {
            this.ids = ids;
            this.action = action;
        }

        public Payload(String id, boolean action) {
            List<String> ids = new LinkedList<String>();
            ids.add(id);
            this.ids = ids;
            this.action = action;
        }
    }

}
