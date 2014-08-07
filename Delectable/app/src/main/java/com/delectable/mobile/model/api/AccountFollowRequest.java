package com.delectable.mobile.model.api;

import java.util.LinkedList;
import java.util.List;

public class AccountFollowRequest extends BaseRequest {

    AccountFollowPayload payload;

    public AccountFollowRequest(AccountFollowPayload payload) {
        this.payload = payload;
    }

    public static class AccountFollowPayload {

        List<String> ids;

        boolean action;

        public AccountFollowPayload(List<String> ids, boolean action) {
            this.ids = ids;
            this.action = action;
        }

        public AccountFollowPayload(String id, boolean action) {
            List<String> ids = new LinkedList<String>();
            ids.add(id);
            this.ids = ids;
            this.action = action;
        }
    }

}
