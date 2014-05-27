package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class PurchasedOffer {

    String id;

    String expiration;

    String vintage;

    Integer min_quant;

    Integer max_quant;

    Integer default_quant;

    String marketing_message;

    // TODO: What does Attributes hash look like?
    //ATTRIBUTES_HASH marketing_message_attributes;

    ObjectNouns object_nouns;

    ArrayList<Pricing> pricing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public Integer getMinQuant() {
        return min_quant;
    }

    public void setMinQuant(Integer min_quant) {
        this.min_quant = min_quant;
    }

    public Integer getMaxQuant() {
        return max_quant;
    }

    public void setMaxQuant(Integer max_quant) {
        this.max_quant = max_quant;
    }

    public Integer getDefaultQuant() {
        return default_quant;
    }

    public void setDefaultQuant(Integer default_quant) {
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
        return "PurchasedOffer{" +
                "id='" + id + '\'' +
                ", expiration='" + expiration + '\'' +
                ", vintage='" + vintage + '\'' +
                ", min_quant=" + min_quant +
                ", max_quant=" + max_quant +
                ", default_quant=" + default_quant +
                ", marketing_message='" + marketing_message + '\'' +
                ", object_nouns=" + object_nouns +
                ", pricing=" + pricing +
                '}';
    }

    class ObjectNouns {

        String singular;

        String plural;
    }
}
