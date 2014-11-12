package com.delectable.mobile.api.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * A custom deserializer for the {@code accounts/captures_and_pending_captures} endpoint because the
 * return array can be a mix of {@link com.delectable.mobile.api.models.PendingCapture} and {@link
 * com.delectable.mobile.api.models.CaptureDetails} objects.
 */
public class BaseListingElementDeserializer
        implements JsonDeserializer<BaseListingElement> {

    private static final String TAG = BaseListingElementDeserializer.class.getSimpleName();

    @Override
    public BaseListingElement deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonItem = json.getAsJsonObject();
        JsonElement listParam = jsonItem.get("list_params");
        if (listParam == null) {
            return context.deserialize(json, typeOfT); //return BaseListingElement
        }
        JsonObject listParamJson = listParam.getAsJsonObject();
        JsonElement type = listParamJson.get("type");
        if (type == null) {
            return context.deserialize(json, typeOfT); //return BaseListingElement
        }
        String typeValue = type.getAsString();

        if ("pending_capture".equals(typeValue)) {
            return context.deserialize(json, PendingCapture.class);
        }

        if ("capture".equals(typeValue)) {
            return context.deserialize(json, CaptureDetails.class);
        }

        //default return BaseListingElement if we didn't have a filter for it's type above
        return context.deserialize(json, typeOfT);
    }
}
