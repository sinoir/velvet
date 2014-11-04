package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class CapturesNotesRequest extends BaseRequest {

    private Payload payload;

    /**
     * @param base_wine_id         Either this or wine_profile_id
     * @param wine_profile_id      Either this or base_wine_id
     * @param before               Optional: The first time a user's list is retrieved, this will be
     *                             empty
     * @param after                Optional: The first time a user's list is retrieved, this will be
     *                             empty
     * @param include_capture_note Optional: This will force an invalidation, and force updates
     *                             array to include the CAPTURE.NOTE with the given ID if it exists
     *                             anywhere in the full list.
     * @param suppress_before      true if user pulled to refresh / only refresh for recent items.
     */
    public CapturesNotesRequest(String e_tag, String base_wine_id, String wine_profile_id, String before,
            String after, String include_capture_note, Boolean suppress_before) {
        super(null, e_tag);
        this.payload = new Payload(base_wine_id, wine_profile_id, before, after,
                include_capture_note, suppress_before);
    }

    public static class Payload {

        private String base_wine_id;

        private String wine_profile_id;

        private String before;

        private String after;

        private String include_capture_note;

        //TODO does this have supress_before functionality?
        private Boolean suppress_before;

        public Payload(String base_wine_id, String wine_profile_id, String before, String after,
                String include_capture_note, Boolean suppress_before) {
            this.base_wine_id = base_wine_id;
            this.wine_profile_id = wine_profile_id;
            this.before = before;
            this.after = after;
            this.include_capture_note = include_capture_note;
            this.suppress_before = suppress_before;
        }
    }

}
