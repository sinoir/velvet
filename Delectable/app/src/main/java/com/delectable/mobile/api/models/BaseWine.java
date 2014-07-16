package com.delectable.mobile.api.models;

import com.delectable.mobile.R;

import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;


public class BaseWine extends BaseResponse implements Parcelable {

    private static final int MAX_REGION_PATHS = 4;

    String id;

    RatingsSummaryHash ratings_summary;

    String producer_name;

    String name;

    String region_id;

    ArrayList<RegionPath> region_path;

    ArrayList<VarietalsHash> varietal_composition;

    ArrayList<WineProfile> wine_profiles;

    String default_wine_profile_id;

    PhotoHash photo;

    String description;

    boolean carbonation;

    String sweetness;

    String color;

    String forward_id;

    String producer_id;


    public BaseWine() {
        //no paramter constructor
    }

    @Override
    public BaseResponse buildFromJson(JSONObject jsonObj) {
        JSONObject payloadObj = jsonObj.optJSONObject("payload");
        BaseWine newResource = null;
        if (payloadObj != null && payloadObj.optJSONObject("base_wine") != null) {
            newResource = buildFromJson(payloadObj.optJSONObject("base_wine"),
                    this.getClass());
        }

        return newResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RatingsSummaryHash getRatingsSummary() {
        return ratings_summary;
    }

    public void setRatingsSummary(RatingsSummaryHash ratings_summary) {
        this.ratings_summary = ratings_summary;
    }

    public String getProducerName() {
        return producer_name;
    }

    public void setProducerName(String producer_name) {
        this.producer_name = producer_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionId() {
        return region_id;
    }

    public void setRegionId(String region_id) {
        this.region_id = region_id;
    }

    public ArrayList<RegionPath> getRegionPath() {
        return region_path;
    }

    /**
     * The region path display text will show at most three region paths. Typically, there are four
     * region paths.
     */
    public String getRegionPathDisplayText(Context c) {

        //this shouldn't happen, that there are more than 4 region paths
        if (region_path.size() > MAX_REGION_PATHS) {
            throw new RuntimeException("Unexpected data, there are more than 4 region paths.");
        }

        //we don't ever show USA text
        if (region_path.get(0).getName().equals("USA")) {
            region_path.remove(0);
        }

        //trim most micro region path because we only show three regions max
        if (region_path.size() == MAX_REGION_PATHS) {
            region_path.remove(MAX_REGION_PATHS - 1);
        }

        //region path comes back from API in macro->micro order, we reverse it to make our paramaterized string
        Collections.reverse(region_path);
        String[] regions = new String[region_path.size()];
        for (int i = 0; i < region_path.size(); i++) {
            regions[i] = region_path.get(i).getName();
        }

        int stringResource = R.string.wine_profile_region_path_1_node;
        if(regions.length==3) {
            stringResource = R.string.wine_profile_region_path_3_nodes;
        }
        if(regions.length==2) {
            stringResource = R.string.wine_profile_region_path_2_nodes;
        }
        //regions.length==1 is already taken care of from original assignment

        return c.getResources().getString(stringResource, regions);
    }

    public void setRegionPath(ArrayList<RegionPath> region_path) {
        this.region_path = region_path;
    }

    public ArrayList<VarietalsHash> getVarietalComposition() {
        return varietal_composition;
    }

    public void setVarietalComposition(ArrayList<VarietalsHash> varietal_composition) {
        this.varietal_composition = varietal_composition;
    }

    public ArrayList<WineProfile> getWineProfiles() {
        return wine_profiles;
    }

    public void setWineProfiles(ArrayList<WineProfile> wine_profiles) {
        this.wine_profiles = wine_profiles;
    }

    public String getDefaultWineProfileId() {
        return default_wine_profile_id;
    }

    public void setDefaultWineProfileId(String default_wine_profile_id) {
        this.default_wine_profile_id = default_wine_profile_id;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getCarbonation() {
        return carbonation;
    }

    public void setCarbonation(boolean carbonation) {
        this.carbonation = carbonation;
    }

    public String getSweetness() {
        return sweetness;
    }

    public void setSweetness(String sweetness) {
        this.sweetness = sweetness;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getForwardId() {
        return forward_id;
    }

    public void setForwardId(String forward_id) {
        this.forward_id = forward_id;
    }

    public String getProducerId() {
        return producer_id;
    }

    public void setProducerId(String producer_id) {
        this.producer_id = producer_id;
    }

    @Override
    public String toString() {
        return "BaseWine{" +
                "id='" + id + '\'' +
                ", ratings_summary=" + ratings_summary +
                ", producer_name='" + producer_name + '\'' +
                ", name='" + name + '\'' +
                ", region_id='" + region_id + '\'' +
                ", region_path=" + region_path +
                ", varietal_composition=" + varietal_composition +
                ", wine_profiles=" + wine_profiles +
                ", default_wine_profile_id='" + default_wine_profile_id + '\'' +
                ", photo=" + photo +
                ", description='" + description + '\'' +
                ", carbonation=" + carbonation +
                ", sweetness='" + sweetness + '\'' +
                ", color='" + color + '\'' +
                ", forward_id='" + forward_id + '\'' +
                ", producer_id='" + producer_id + '\'' +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.ratings_summary, 0);
        dest.writeString(this.producer_name);
        dest.writeString(this.name);
        dest.writeString(this.region_id);
        dest.writeTypedList(this.region_path);
        dest.writeTypedList(this.varietal_composition);
        dest.writeTypedList(this.wine_profiles);
        dest.writeString(this.default_wine_profile_id);
        dest.writeParcelable(this.photo, 0);
        dest.writeString(this.description);
        dest.writeByte(carbonation ? (byte) 1 : (byte) 0);
        dest.writeString(this.sweetness);
        dest.writeString(this.color);
        dest.writeString(this.forward_id);
        dest.writeString(this.producer_id);
        dest.writeString(this.context);
        dest.writeString(this.e_tag);
    }

    private BaseWine(Parcel in) {
        this.id = in.readString();
        this.ratings_summary = in.readParcelable(RatingsSummaryHash.class.getClassLoader());
        this.producer_name = in.readString();
        this.name = in.readString();
        this.region_id = in.readString();
        if (region_path == null) {
            region_path = new ArrayList<RegionPath>();
        }
        in.readTypedList(this.region_path, RegionPath.CREATOR);
        if (varietal_composition == null) {
            varietal_composition = new ArrayList<VarietalsHash>();
        }
        in.readTypedList(this.varietal_composition, VarietalsHash.CREATOR);
        if (wine_profiles == null) {
            wine_profiles = new ArrayList<WineProfile>();
        }
        in.readTypedList(this.wine_profiles, WineProfile.CREATOR);
        this.default_wine_profile_id = in.readString();
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
        this.description = in.readString();
        this.carbonation = in.readByte() != 0;
        this.sweetness = in.readString();
        this.color = in.readString();
        this.forward_id = in.readString();
        this.producer_id = in.readString();
        this.context = in.readString();
        this.e_tag = in.readString();
    }

    public static final Parcelable.Creator<BaseWine> CREATOR = new Parcelable.Creator<BaseWine>() {
        public BaseWine createFromParcel(Parcel source) {
            return new BaseWine(source);
        }

        public BaseWine[] newArray(int size) {
            return new BaseWine[size];
        }
    };
}
