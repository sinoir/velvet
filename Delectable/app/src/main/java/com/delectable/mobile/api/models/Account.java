package com.delectable.mobile.api.models;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Account {

    // TODO avoid wrapper objects for primitive types

    public static transient int RELATION_TYPE_SELF = -1;

    public static transient int RELATION_TYPE_NONE = 0;

    public static transient int RELATION_TYPE_FOLLOWING = 1;

    String id;

    String email;

    String fname;

    String lname;

    PhotoHash photo;

    Boolean influencer;

    List<String> influencer_titles;

    String bio;

    Integer follower_count;

    Integer wishlist_count;

    String sourcing_state;

    String fb_id;

    String fb_token;

    Float fb_token_exp;

    List<PaymentMethod> payment_methods;

    Integer following_count;

    String url;

    Integer capture_count;

    Integer region_count;

    AccountConfig account_config;

    ClientState client_state;

    TutorialState tutorial_state;

    List<ShippingAddress> shipping_addresses;

    List<Identifier> identifiers;

    LocalNotifications local_notifs;

    Integer current_user_relationship;

    List<CaptureSummary> capture_summaries;

    String e_tag;

    public boolean isUserRelationshipTypeSelf() {
        return checkRelationship(RELATION_TYPE_SELF);
    }

    public boolean isUserRelationshipTypeFollowing() {
        return checkRelationship(RELATION_TYPE_FOLLOWING);
    }

    public boolean isUserRelationshipTypeNone() {
        return checkRelationship(RELATION_TYPE_NONE);
    }

    private boolean checkRelationship(int relationshipType) {
        return (getCurrentUserRelationship() != null)
                && (getCurrentUserRelationship().intValue() == relationshipType);
    }

    public String getFullName() {
        return getFname() + " " + getLname();
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

    public List<String> getInfluencerTitles() {
        return influencer_titles;
    }

    public void setInfluencerTitles(List<String> influencer_titles) {
        this.influencer_titles = influencer_titles;
    }

    /**
     * @return Returns influencer titles in a comma delimited String.
     */
    public String getInfluencerTitlesString() {
        return StringUtils.join(influencer_titles, ",");
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

    public List<PaymentMethod> getPaymentMethods() {
        return payment_methods;
    }

    public void setPaymentMethods(List<PaymentMethod> payment_methods) {
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

    public int getActivityFeedTsLast() {
        return client_state != null && client_state.activity_feed_ts_last != null
                ? client_state.activity_feed_ts_last : 0;
    }

    public void setActivityFeedTsLast(Integer activity_feed_ts_last) {
        if (this.client_state == null) {
            this.client_state = new ClientState();
        }
        this.client_state.activity_feed_ts_last = activity_feed_ts_last;
    }

    public boolean getFtueCompleted() {
        return tutorial_state != null ? tutorial_state.ftue_completed : false;
    }

    public void setFtueCompleted(Boolean ftue_completed) {
        if (this.tutorial_state == null) {
            this.tutorial_state = new TutorialState();
        }
        this.tutorial_state.ftue_completed = ftue_completed;
    }

    public List<ShippingAddress> getShippingAddresses() {
        return shipping_addresses;
    }

    public void setShippingAddresses(List<ShippingAddress> shipping_addresses) {
        this.shipping_addresses = shipping_addresses;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public LocalNotifications getLocalNotifs() {
        return local_notifs;
    }

    public void setLocalNotifs(LocalNotifications local_notifs) {
        this.local_notifs = local_notifs;
    }

    public List<CaptureSummary> getCaptureSummaries() {
        return capture_summaries;
    }

    public void setCaptureSummaries(List<CaptureSummary> capture_summaries) {
        this.capture_summaries = capture_summaries;
    }

    public Integer getCurrentUserRelationship() {
        return current_user_relationship;
    }

    public void setCurrentUserRelationship(Integer current_user_relationship) {
        this.current_user_relationship = current_user_relationship;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String eTag) {
        this.e_tag = eTag;
    }

    /**
     * @return Returns null if there is no identifier set as primary.
     */
    public Identifier getPrimaryEmailIdentifier() {
        for (Identifier identifier : identifiers) {
            if (identifier.getPrimary()) {
                return identifier;
            }
        }
        return null;
    }

    /**
     * @return Returns the first phone identifier. Returns null if there is no phone identifier.
     */
    public Identifier getPhoneIdentifier() {
        for (Identifier identifier : identifiers) {
            if (identifier.getType().equalsIgnoreCase(Identifier.Type.PHONE)) {
                return identifier;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", photo=" + photo +
                ", influencer=" + influencer +
                ", influencer_titles=" + influencer_titles +
                ", bio='" + bio + '\'' +
                ", follower_count=" + follower_count +
                ", wishlist_count=" + wishlist_count +
                ", sourcing_state='" + sourcing_state + '\'' +
                ", fb_id='" + fb_id + '\'' +
                ", fb_token='" + fb_token + '\'' +
                ", fb_token_exp=" + fb_token_exp +
                ", payment_methods=" + payment_methods +
                ", following_count=" + following_count +
                ", url='" + url + '\'' +
                ", capture_count=" + capture_count +
                ", region_count=" + region_count +
                ", account_config=" + account_config +
                ", client_state=" + client_state +
                ", tutorial_state=" + tutorial_state +
                ", shipping_addresses=" + shipping_addresses +
                ", identifiers=" + identifiers +
                ", local_notifs=" + local_notifs +
                ", current_user_relationship=" + current_user_relationship +
                ", capture_summaries=" + capture_summaries +
                ", e_tag=" + e_tag +
                "} " + super.toString();
    }

    class ClientState {

        Integer activity_feed_ts_last;
    }

    class TutorialState {

        Boolean ftue_completed;
    }
}
