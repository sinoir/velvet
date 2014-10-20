package com.delectable.mobile.api.endpointmodels;

public class BaseListingRequest extends BaseRequest {

    private Payload payload;

    public BaseListingRequest(String context, String e_tag, String id, String before, String after,
            Boolean supress_before) {
        super(context, e_tag);
        payload = new Payload(id, before, after, supress_before);
    }

    public static class Payload {

        // optional - If no id is sent, the current user's account is assumed.
        private String id;

        // optional – The first time a user's list is retrieved, this will be empty
        private String before;

        // optional – The first time a user's list is retrieved, this will be empty
        private String after;

        //used for pull to refresh
        private Boolean suppress_before;


        public Payload(String id, String before, String after, Boolean suppress_before) {
            this.id = id;
            this.before = before;
            this.after = after;
            this.suppress_before = suppress_before;
        }
    }
}
