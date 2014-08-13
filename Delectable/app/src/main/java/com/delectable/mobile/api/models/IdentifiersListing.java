package com.delectable.mobile.api.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class IdentifiersListing extends BaseResponse{

    private static final String TAG = IdentifiersListing.class.getSimpleName();

    private ArrayList<Identifier> identifiers;

    public static IdentifiersListing buildFromJson(JSONObject jsonObject) {
        JSONObject payloadObj = jsonObject.optJSONObject("payload");
        ArrayList<Identifier> identifiers = null;
        if (payloadObj != null && payloadObj.optJSONArray("identifiers") != null) {

            JSONArray identifiersJsonArray = payloadObj.optJSONArray("identifiers");

            //TODO directly using GSON builder here because there is no buildFromJson static method yet for JSONArray in BaseResponse. Should build into BaseResponse if there is another list return.
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Type type = new TypeToken<ArrayList<Identifier>>(){}.getType();
            identifiers = gson.fromJson(identifiersJsonArray.toString(), type);
        }
        IdentifiersListing listing = new IdentifiersListing();
        listing.setIdentifiers(identifiers);

        return listing;
    }


    public ArrayList<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ArrayList<Identifier> identifiers) {
        this.identifiers = identifiers;
    }
}
