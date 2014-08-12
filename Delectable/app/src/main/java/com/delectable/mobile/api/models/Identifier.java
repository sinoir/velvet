package com.delectable.mobile.api.models;

public class Identifier {

    public static class Type {

        public static final String FACEBOOK = "facebook";

        public static final String EMAIL = "email";

        public static final String PHONE = "phone";
    }

    String id;

    Boolean primary;

    String type;

    Boolean verified;

    String string;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
