package com.delectable.mobile.api.endpointmodels.pendingcaptures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class SetBaseWineRequest extends BaseRequest {

    private Payload payload;

    public SetBaseWineRequest(String pendingCaptureId, String baseWineId) {
        payload = new Payload(pendingCaptureId, baseWineId);
    }

    public static class Payload {

        private String id;

        private String base_wine_id;

        public Payload(String id, String base_wine_id) {
            this.id = id;
            this.base_wine_id = base_wine_id;
        }
    }
}
