package com.delectable.mobile.model.api.wineprofiles;

import com.delectable.mobile.model.api.BaseRequest;

public class WineProfilesModifyWishlistRequest extends BaseRequest {

    private Payload payload;

    public static enum Action {
        ADD(true), REMOVE(false);

        private boolean mAction;

        private Action(boolean action) {
            mAction = action;
        }

        public boolean getValue() {
            return mAction;
        }
    }

    public WineProfilesModifyWishlistRequest(String id, Action action) {
        super();
        this.payload = new Payload(id, action.getValue());
    }

    public static class Payload {

        private String id;

        private boolean action;

        public Payload(String id, boolean action) {
            this.id = id;
            this.action = action;
        }
    }

}
