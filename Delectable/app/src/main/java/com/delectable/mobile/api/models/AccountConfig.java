package com.delectable.mobile.api.models;

public class AccountConfig {

    public static enum Key {

        /**
         * Wine identification
         */
        PN_CAPTURE_TRANSCRIBED("pn_capture_transcribed"),
        /**
         * Comment on your wine
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

    /**
     * returns null if setting doesn't exist.
     */
    public Boolean getSetting(Key key) {
        switch (key) {
            case PN_CAPTURE_TRANSCRIBED:
                return pn_capture_transcribed;
            case PN_COMMENT_ON_OWN_WINE:
                return pn_comment_on_own_wine;
            case PN_COMMENT_RESPONSE:
                return pn_comment_response;
            case PN_EXPERIMENT:
                return pn_experiment;
            case PN_FRIEND_JOINED:
                return pn_friend_joined;
            case PN_LIKE_ON_OWN_WINE:
                return pn_like_on_own_wine;
            case PN_NEW_FOLLOWER:
                return pn_new_follower;
            case PN_PURCHASE_OFFER_MADE:
                return pn_purchase_offer_made;
            case PN_TAGGED:
                return pn_tagged;
            default:
                return null;
        }
    }

    public void setSetting(Key key, boolean setting) {
        switch (key) {
            case PN_CAPTURE_TRANSCRIBED:
                pn_capture_transcribed = setting;
                break;
            case PN_COMMENT_ON_OWN_WINE:
                pn_comment_on_own_wine = setting;
                break;
            case PN_COMMENT_RESPONSE:
                pn_comment_response = setting;
                break;
            case PN_EXPERIMENT:
                pn_experiment = setting;
                break;
            case PN_FRIEND_JOINED:
                pn_friend_joined = setting;
                break;
            case PN_LIKE_ON_OWN_WINE:
                pn_like_on_own_wine = setting;
                break;
            case PN_NEW_FOLLOWER:
                pn_new_follower = setting;
                break;
            case PN_PURCHASE_OFFER_MADE:
                pn_purchase_offer_made = setting;
                break;
            case PN_TAGGED:
                pn_tagged = setting;
                break;
            default:
                //do nothing
                break;
        }
    }
}
