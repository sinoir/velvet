package com.delectable.mobile.api.models;

import com.delectable.mobile.R;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class BaseWine extends BaseWineMinimal implements Parcelable, Ratingsable {

    private static final int MAX_REGION_PATHS = 4;

    private RatingsSummaryHash ratings_summary;

    private String region_id;

    private ArrayList<RegionPath> region_path = new ArrayList<RegionPath>();

    private ArrayList<VarietalsHash> varietal_composition = new ArrayList<VarietalsHash>();

    private ArrayList<WineProfileSubProfile> wine_profiles = new ArrayList<WineProfileSubProfile>();

    private String default_wine_profile_id;

    private String description;

    private boolean carbonation;

    private String sweetness;

    private String color;

    private String forwarded_id;

    private String producer_id;

    /**
     * Load up Default WineProfile
     * @return
     */
    public WineProfileSubProfile getDefaultWineProfile() {
        return getWineProfileByWineId(default_wine_profile_id);
    }

    /**
     * Load up a Wine Profile within the BaseWine by ID
     * @param wineId
     * @return
     */
    public WineProfileSubProfile getWineProfileByWineId(String wineId) {
        if (wine_profiles == null) {
            return null;
        }
        if (wineId == null) {
            return null;
        }
        for (WineProfileSubProfile wineProfile : wine_profiles) {
            if (wineId.equalsIgnoreCase(wineProfile.getId())) {
                return wineProfile;
            }
        }
        return null;
    }

    public RatingsSummaryHash getRatingsSummary() {
        return ratings_summary;
    }

    public void setRatingsSummary(RatingsSummaryHash ratings_summary) {
        this.ratings_summary = ratings_summary;
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
        String[] regions = new String[region_path.size()];
        int last = region_path.size()-1;
        int position = 0; //count forward to populate new array
        for (int i = last; i >= 0; i--) {
            regions[position] = region_path.get(i).getName();
            position++;
        }

        int stringResource = R.string.wine_profile_region_path_1_node;
        if (regions.length == 3) {
            stringResource = R.string.wine_profile_region_path_3_nodes;
        }
        if (regions.length == 2) {
            stringResource = R.string.wine_profile_region_path_2_nodes;
        }
        //regions.length==1 is already taken care of from original assignment

        return c.getResources().getString(stringResource, (Object[]) regions);
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

    public ArrayList<WineProfileSubProfile> getWineProfiles() {
        return wine_profiles;
    }

    public void setWineProfiles(ArrayList<WineProfileSubProfile> wine_profiles) {
        this.wine_profiles = wine_profiles;
    }

    public String getDefaultWineProfileId() {
        return default_wine_profile_id;
    }

    public void setDefaultWineProfileId(String default_wine_profile_id) {
        this.default_wine_profile_id = default_wine_profile_id;
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
        return forwarded_id;
    }

    public void setForwardId(String forward_id) {
        this.forwarded_id = forward_id;
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
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", producer_name='" + getProducerName() + '\'' +
                ", photo=" + getPhoto() +
                ", context=" + getContext() +
                ", e_tag=" + getETag() +
                "ratings_summary=" + ratings_summary +
                ", region_id='" + region_id + '\'' +
                ", region_path=" + region_path +
                ", varietal_composition=" + varietal_composition +
                ", wine_profiles=" + wine_profiles +
                ", default_wine_profile_id='" + default_wine_profile_id + '\'' +
                ", description='" + description + '\'' +
                ", carbonation=" + carbonation +
                ", sweetness='" + sweetness + '\'' +
                ", color='" + color + '\'' +
                ", forwarded_id='" + forwarded_id + '\'' +
                ", producer_id='" + producer_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.ratings_summary, 0);
        dest.writeString(this.region_id);
        dest.writeTypedList(this.region_path);
        dest.writeTypedList(this.varietal_composition);
        dest.writeTypedList(this.wine_profiles);
        dest.writeString(this.default_wine_profile_id);
        dest.writeString(this.description);
        dest.writeByte(carbonation ? (byte) 1 : (byte) 0);
        dest.writeString(this.sweetness);
        dest.writeString(this.color);
        dest.writeString(this.forwarded_id);
        dest.writeString(this.producer_id);
    }

    private BaseWine(Parcel in) {
        super(in);
        this.ratings_summary = in.readParcelable(RatingsSummaryHash.class.getClassLoader());
        this.region_id = in.readString();
        in.readTypedList(this.region_path, RegionPath.CREATOR);
        in.readTypedList(this.varietal_composition, VarietalsHash.CREATOR);
        in.readTypedList(this.wine_profiles, WineProfileSubProfile.CREATOR);
        this.default_wine_profile_id = in.readString();
        this.description = in.readString();
        this.carbonation = in.readByte() != 0;
        this.sweetness = in.readString();
        this.color = in.readString();
        this.forwarded_id = in.readString();
        this.producer_id = in.readString();
    }

    public static final Creator<BaseWine> CREATOR = new Creator<BaseWine>() {
        public BaseWine createFromParcel(Parcel source) {
            return new BaseWine(source);
        }

        public BaseWine[] newArray(int size) {
            return new BaseWine[size];
        }
    };
}
