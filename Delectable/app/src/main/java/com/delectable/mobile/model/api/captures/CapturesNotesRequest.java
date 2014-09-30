package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.model.api.BaseRequest;

public class CapturesNotesRequest extends BaseRequest {

    private Payload payload;

    public CapturesNotesRequest(String base_wine_id, String wine_profile_id, String before,
            String after, String include_capture_note) {
        this.payload = new Payload(base_wine_id, wine_profile_id, before, after,
                include_capture_note);
    }

    public static class Payload {

        private String base_wine_id;

        private String wine_profile_id;

        private String before;

        private String after;

        private String include_capture_note;

        public Payload(String base_wine_id, String wine_profile_id, String before, String after,
                String include_capture_note) {
            this.base_wine_id = base_wine_id;
            this.wine_profile_id = wine_profile_id;
            this.before = before;
            this.after = after;
            this.include_capture_note = include_capture_note;
        }
    }

}
