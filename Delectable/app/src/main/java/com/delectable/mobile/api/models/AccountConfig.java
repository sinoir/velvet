package com.delectable.mobile.api.models;

public class AccountConfig {

    public static enum Key {

        /**
         * Wine identification
         */
        PN_CAPTURE_TRANSCRIBED("pn_capture_transcribed"),
        /**
         * Comment on your win
         */
        PN_COMMENT_ON_OWN_WINE("pn_comment_on_own_wine"),
        /**
         * Respond to comment
         */
        PN_COMMENT_RESPONSE("pn_comment_response"),
        //no ui equivalent in ios v4.0 app
        PN_EXPERIMENT("pn_experiment"),
        /**
         * Friend joined delectable
         */
        PN_FRIEND_JOINED("pn_friend_joined"),
        /**
         * Like your wine
         */
        PN_LIKE_ON_OWN_WINE("pn_like_on_own_wine"),
        /**
         * Following you
         */
        PN_NEW_FOLLOWER("pn_new_follower"),
        //no ui equivalent in ios v4.0 app
        PN_PURCHASE_OFFER_MADE("pn_purchase_offer_made"),
        /**
         * Tagged on a wine
         */
        PN_TAGGED("pn_tagged");

        private String mKeyName;

        private Key(String keyName) {
            mKeyName = keyName;
        }

        public String getName() {
            return mKeyName;
        }
    }

    public void setSetting(Key key, boolean setting) {
        switch (key) {
            case PN_CAPTURE_TRANSCRIBED:
                setPnCaptureTranscribed(setting);
                break;
            case PN_COMMENT_ON_OWN_WINE:
                setPnCommentOnOwnWine(setting);
                break;
            case PN_COMMENT_RESPONSE:
                setPnCommentResponse(setting);
                break;
            case PN_EXPERIMENT:
                setPnExperiment(setting);
                break;
            case PN_FRIEND_JOINED:
                setPnFriendJoined(setting);
                break;
            case PN_LIKE_ON_OWN_WINE:
                setPnLikeOnOwnWine(setting);
                break;
            case PN_NEW_FOLLOWER:
                setPnNewFollower(setting);
                break;
            case PN_PURCHASE_OFFER_MADE:
                setPnPurchaseOfferMade(setting);
                break;
            case PN_TAGGED:
                setPnTagged(setting);
                break;
        }
    }


    private boolean pn_new_follower;

    private boolean pn_like_on_own_wine;

    private boolean pn_purchase_offer_made;

    private boolean pn_experiment;

    private int tagging_test;

    private boolean pn_comment_response;

    private boolean passive_vintank_sharing;

    private boolean pn_capture_transcribed;

    private boolean pn_tagged;

    private boolean pn_comment_on_own_wine;

    private boolean passive_og_sharing;

    private boolean pn_friend_joined;

    public boolean getPnNewFollower() {
        return pn_new_follower;
    }

    public void setPnNewFollower(boolean pn_new_follower) {
        this.pn_new_follower = pn_new_follower;
    }

    public boolean getPnLikeOnOwnWine() {
        return pn_like_on_own_wine;
    }

    public void setPnLikeOnOwnWine(boolean pn_like_on_own_wine) {
        this.pn_like_on_own_wine = pn_like_on_own_wine;
    }

    public boolean getPnPurchaseOfferMade() {
        return pn_purchase_offer_made;
    }

    public void setPnPurchaseOfferMade(boolean pn_purchase_offer_made) {
        this.pn_purchase_offer_made = pn_purchase_offer_made;
    }

    public boolean getPnExperiment() {
        return pn_experiment;
    }

    public void setPnExperiment(boolean pn_experiment) {
        this.pn_experiment = pn_experiment;
    }

    public int getTaggingTest() {
        return tagging_test;
    }

    public void setTaggingTest(int tagging_test) {
        this.tagging_test = tagging_test;
    }

    public boolean getPnCommentResponse() {
        return pn_comment_response;
    }

    public void setPnCommentResponse(boolean pn_comment_response) {
        this.pn_comment_response = pn_comment_response;
    }

    public boolean getPassiveVintankSharing() {
        return passive_vintank_sharing;
    }

    public void setPassiveVintankSharing(boolean passive_vintank_sharing) {
        this.passive_vintank_sharing = passive_vintank_sharing;
    }

    public boolean getPnCaptureTranscribed() {
        return pn_capture_transcribed;
    }

    public void setPnCaptureTranscribed(boolean pn_capture_transcribed) {
        this.pn_capture_transcribed = pn_capture_transcribed;
    }

    public boolean getPnTagged() {
        return pn_tagged;
    }

    public void setPnTagged(boolean pn_tagged) {
        this.pn_tagged = pn_tagged;
    }

    public boolean getPnCommentOnOwnWine() {
        return pn_comment_on_own_wine;
    }

    public void setPnCommentOnOwnWine(boolean pn_comment_on_own_wine) {
        this.pn_comment_on_own_wine = pn_comment_on_own_wine;
    }

    public boolean getPassiveOgSharing() {
        return passive_og_sharing;
    }

    public void setPassiveOgSharing(boolean passive_og_sharing) {
        this.passive_og_sharing = passive_og_sharing;
    }

    public boolean getPnFriendJoined() {
        return pn_friend_joined;
    }

    public void setPnFriendJoined(boolean pn_friend_joined) {
        this.pn_friend_joined = pn_friend_joined;
    }
}
