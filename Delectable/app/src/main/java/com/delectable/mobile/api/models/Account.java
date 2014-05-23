package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

// TODO: Possible PersistedResource base model?
public class Account extends Resource {

    String id;

    String email;

    String fname;

    String lname;

    PhotoHash photo;

    Boolean influencer;

    ArrayList<String> influencer_titles;

    String bio;

    Integer follower_count;

    Integer wishlist_count;

    String sourcing_state;

    String fb_id;

    String fb_token;

    Float fb_token_exp;

    ArrayList<PaymentMethod> payment_methods;

    Integer following_count;

    String url;

    Integer capture_count;

    Integer region_count;

    AccountConfig account_config;

    @SerializedName("client_state.activity_feed_ts_last")
    Integer activity_feed_ts_last;

    Boolean ftue_completed;

    ArrayList<ShippingAddress> shipping_addresses;

    ArrayList<Identifier> identifiers;

    LocalNotifications local_notifs;

    public static Account parsePrivateAccount(JSONObject payload) {
        Account account = buildFromJson(payload, Account.class);
        if (payload.optJSONObject("client_state") != null) {
            account.setActivityFeedTsLast(payload.optJSONObject("client_state").optInt("activity_feed_ts_last"));
        }
        if (payload.optJSONObject("tutorial_state") != null) {
            account.setFtueCompleted(payload.optJSONObject("tutorial_state").optBoolean("ftue_completed"));
        }
        return account;
    }

    @Override
    public String[] getPayloadFieldsForAction(int action) {
        return new String[0];
    }

    @Override
    public String getResourceUrlForAction(int action) {
        return null;
    }

    @Override
    public Resource parsePayloadForAction(JSONObject payload, int action) {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public Boolean getInfluencer() {
        return influencer;
    }

    public void setInfluencer(Boolean influencer) {
        this.influencer = influencer;
    }

    public ArrayList<String> getInfluencerTitles() {
        return influencer_titles;
    }

    public void setInfluencerTitles(ArrayList<String> influencer_titles) {
        this.influencer_titles = influencer_titles;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getFollowerCount() {
        return follower_count;
    }

    public void setFollowerCount(Integer follower_count) {
        this.follower_count = follower_count;
    }

    public Integer getWishlistCount() {
        return wishlist_count;
    }

    public void setWishlistCount(Integer wishlist_count) {
        this.wishlist_count = wishlist_count;
    }

    public String getSourcingState() {
        return sourcing_state;
    }

    public void setSourcingState(String sourcing_state) {
        this.sourcing_state = sourcing_state;
    }

    public String getFbId() {
        return fb_id;
    }

    public void setFbId(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getFbToken() {
        return fb_token;
    }

    public void setFbToken(String fb_token) {
        this.fb_token = fb_token;
    }

    public Float getFbTokenExp() {
        return fb_token_exp;
    }

    public void setFbTokenExp(Float fb_token_exp) {
        this.fb_token_exp = fb_token_exp;
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return payment_methods;
    }

    public void setPaymentMethods(ArrayList<PaymentMethod> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public Integer getFollowingCount() {
        return following_count;
    }

    public void setFollowing_count(Integer following_count) {
        this.following_count = following_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCaptureCount() {
        return capture_count;
    }

    public void setCapture_count(Integer capture_count) {
        this.capture_count = capture_count;
    }

    public Integer getRegionCount() {
        return region_count;
    }

    public void setRegion_count(Integer region_count) {
        this.region_count = region_count;
    }

    public AccountConfig getAccountConfig() {
        return account_config;
    }

    public void setAccountConfig(AccountConfig account_config) {
        this.account_config = account_config;
    }

    public Integer getActivityFeedTsLast() {
        return activity_feed_ts_last;
    }

    public void setActivityFeedTsLast(Integer activity_feed_ts_last) {
        this.activity_feed_ts_last = activity_feed_ts_last;
    }

    public Boolean getFtueCompleted() {
        return ftue_completed;
    }

    public void setFtueCompleted(Boolean ftue_completed) {
        this.ftue_completed = ftue_completed;
    }

    public ArrayList<ShippingAddress> getShippingAddresses() {
        return shipping_addresses;
    }

    public void setShippingAddresses(ArrayList<ShippingAddress> shipping_addresses) {
        this.shipping_addresses = shipping_addresses;
    }

    public ArrayList<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ArrayList<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public LocalNotifications getLocalNotifs() {
        return local_notifs;
    }

    public void setLocalNotifs(LocalNotifications local_notifs) {
        this.local_notifs = local_notifs;
    }
}
