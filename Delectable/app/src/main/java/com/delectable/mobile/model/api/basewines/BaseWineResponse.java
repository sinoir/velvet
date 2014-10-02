package com.delectable.mobile.model.api.basewines;


import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.model.api.BaseResponse;

public class BaseWineResponse extends BaseResponse {

    private Payload payload;

    public BaseWine getBaseWine() {
        if (payload == null) {
            return null;
        }
        return payload.base_wine;
    }

    public static class Payload {

        private BaseWine base_wine;
    }
}
