package com.delectable.mobile.api.models;

import java.util.List;

public class Account extends AccountProfile {

    private AccountConfig account_config;

    private List<Identifier> identifiers;

    private String sourcing_state;

    private List<ShippingAddress> shipping_addresses;

    private List<PaymentMethod> payment_methods;

    private String email;

    private ClientState client_state;

    private TutorialState tutorial_state;

    private LocalNotifications local_notifs;

    private String fb_id;

    private String fb_token;

    private float fb_token_exp;

    private float tw_id;

    private String tw_screen_name;

    private String tw_token;

    private String tw_token_secret;

    public AccountConfig getAccountConfig() {
        return account_config;
    }

    public void setAccountConfig(AccountConfig account_config) {
        this.account_config = account_config;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
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

    public String getSourcingState() {
        return sourcing_state;
    }

    public void setSourcingState(String sourcing_state) {
        this.sourcing_state = sourcing_state;
    }

    public List<ShippingAddress> getShippingAddresses() {
        return shipping_addresses;
    }

    public void setShippingAddresses(List<ShippingAddress> shipping_addresses) {
        this.shipping_addresses = shipping_addresses;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return payment_methods;
    }

    public void setPaymentMethods(List<PaymentMethod> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActivityFeedTsLast() {
        return client_state != null ? client_state.activity_feed_ts_last : 0;
    }

    public void setActivityFeedTsLast(int activity_feed_ts_last) {
        if (this.client_state == null) {
            this.client_state = new ClientState();
        }
        this.client_state.activity_feed_ts_last = activity_feed_ts_last;
    }

    public boolean getFtueCompleted() {
        return tutorial_state != null ? tutorial_state.ftue_completed : false;
    }

    public void setFtueCompleted(boolean ftue_completed) {
        if (this.tutorial_state == null) {
            this.tutorial_state = new TutorialState();
        }
        this.tutorial_state.ftue_completed = ftue_completed;
    }


    public LocalNotifications getLocalNotifs() {
        return local_notifs;
    }

    public void setLocalNotifs(LocalNotifications local_notifs) {
        this.local_notifs = local_notifs;
    }

    public String getFbId() {
        return fb_id;
    }

    public boolean isFacebookConnected() {
        return fb_id == null ? false : true;
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

    public float getFbTokenExp() {
        return fb_token_exp;
    }

    public void setFbTokenExp(float fb_token_exp) {
        this.fb_token_exp = fb_token_exp;
    }

    public float getTwId() {
        return tw_id;
    }

    public void setTwId(float tw_id) {
        this.tw_id = tw_id;
    }

    public String getTwScreenName() {
        return tw_screen_name;
    }

    public void setTwScreenName(String tw_screen_name) {
        this.tw_screen_name = tw_screen_name;
    }

    public String getTwToken() {
        return tw_token;
    }

    public void setTwToken(String tw_token) {
        this.tw_token = tw_token;
    }

    public String getTwTokenSecret() {
        return tw_token_secret;
    }

    public void setTwTokenSecret(String tw_token_secret) {
        this.tw_token_secret = tw_token_secret;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + getId() + '\'' +
                ", fname='" + getFname() + '\'' +
                ", lname='" + getLname() + '\'' +
                ", bio='" + getBio() + '\'' +
                ", photo=" + getPhoto() +
                ", influencer=" + isInfluencer() +
                ", influencer_titles=" + getInfluencerTitles() +
                ", context='" + getContext() + '\'' +
                ", current_user_relationship=" + getCurrentUserRelationship() +
                ", shadowbanned=" + isShadowbanned() +
                ", e_tag='" + getETag() + '\'' +
                ", follower_count=" + getFollowerCount() +
                ", following_count=" + getFollowingCount() +
                ", capture_count=" + getCaptureCount() +
                ", public_capture_count=" + getPublicCaptureCount() +
                ", region_count=" + getRegionCount() +
                ", wishlist_count=" + getWishlistCount() +
                ", url='" + getUrl() + '\'' +
                ", capture_summaries=" + getCaptureSummaries() +
                ", email='" + email + '\'' +
                ", sourcing_state='" + sourcing_state + '\'' +
                ", fb_id='" + fb_id + '\'' +
                ", fb_token='" + fb_token + '\'' +
                ", fb_token_exp=" + fb_token_exp +
                ", payment_methods=" + payment_methods +
                ", account_config=" + account_config +
                ", client_state=" + client_state +
                ", tutorial_state=" + tutorial_state +
                ", shipping_addresses=" + shipping_addresses +
                ", identifiers=" + identifiers +
                ", local_notifs=" + local_notifs +
                '}';
    }

    class ClientState {

        private int activity_feed_ts_last;
    }

    class TutorialState {

        private boolean ftue_completed;
    }

}
