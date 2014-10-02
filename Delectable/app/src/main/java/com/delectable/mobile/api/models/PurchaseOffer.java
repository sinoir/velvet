package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class PurchaseOffer {

    private String id;

    private float expiration;

    private String vintage;

    private int min_quant;

    private int max_quant;

    private int default_quant;

    private String marketing_message;

    // TODO: What does Attributes hash look like?
    //ATTRIBUTES_HASH marketing_message_attributes;

    private ObjectNouns object_nouns;

    private ArrayList<Pricing> pricing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getExpiration() {
        return expiration;
    }

    public void setExpiration(float expiration) {
        this.expiration = expiration;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public int getMinQuant() {
        return min_quant;
    }

    public void setMinQuant(int min_quant) {
        this.min_quant = min_quant;
    }

    public int getMaxQuant() {
        return max_quant;
    }

    public void setMaxQuant(int max_quant) {
        this.max_quant = max_quant;
    }

    public int getDefaultQuant() {
        return default_quant;
    }

    public void setDefaultQuant(int default_quant) {
        this.default_quant = default_quant;
    }

    public String getMarketingMessage() {
        return marketing_message;
    }

    public void setMarketingMessage(String marketing_message) {
        this.marketing_message = marketing_message;
    }

    public String getObjectSingularNoun() {
        return object_nouns != null ? object_nouns.singular : null;
    }

    public String getObjectPluralNoun() {
        return object_nouns != null ? object_nouns.plural : null;
    }

    public ObjectNouns getObjectNouns() {
        return object_nouns;
    }

    public void setObjectNouns(ObjectNouns object_nouns) {
        this.object_nouns = object_nouns;
    }


    public ArrayList<Pricing> getPricing() {
        return pricing;
    }

    public void setPricing(ArrayList<Pricing> pricing) {
        this.pricing = pricing;
    }

    @Override
    public String toString() {
        return "PurchaseOffer{" +
                "id='" + id + '\'' +
                ", expiration=" + expiration +
                ", vintage='" + vintage + '\'' +
                ", min_quant=" + min_quant +
                ", max_quant=" + max_quant +
                ", default_quant=" + default_quant +
                ", marketing_message='" + marketing_message + '\'' +
                ", object_nouns=" + object_nouns +
                ", pricing=" + pricing +
                '}';
    }

    public static class ObjectNouns {

        private String singular;

        private String plural;

        public String getSingular() {
            return singular;
        }

        public String getPlural() {
            return plural;
        }

        @Override
        public String toString() {
            return "ObjectNouns{" +
                    "singular='" + singular + '\'' +
                    ", plural='" + plural + '\'' +
                    '}';
        }
    }
}
