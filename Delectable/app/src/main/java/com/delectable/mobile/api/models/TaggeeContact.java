package com.delectable.mobile.api.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaggeeContact {

    String id;

    String fb_id;

    String fname;

    String lname;

    PhotoHash photo;

    ArrayList<String> phone_numbers;

    ArrayList<String> email_addresses;

    public static JSONArray buildJsonArray(ArrayList<TaggeeContact> contacts) throws JSONException {
        JSONArray taggeeArray = new JSONArray();
        if (contacts != null) {
            for (TaggeeContact contact : contacts) {
                taggeeArray.put(contact.buildJsonObject());
            }
        }
        return taggeeArray;
    }

    public boolean isFacebookContact() {
        return getFbId() != null;
    }

    public boolean isDelectaFriendContact() {
        return getId() != null;
    }

    public boolean isContact() {
        return getFname() != null;
    }

    public JSONObject buildJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (isFacebookContact()) {
            jsonObject.put("fb_id", getFbId());
        } else if (isDelectaFriendContact()) {
            jsonObject.put("id", getId());
        } else if (isContact()) {
            jsonObject.put("fname", getFname());
            jsonObject.put("lname", getLname());
            if (getPhoneNumbers() == null) {
                setPhoneNumbers(new ArrayList<String>());
            }
            if (getEmailAddresses() == null) {
                setEmailAddresses(new ArrayList<String>());
            }
            jsonObject.put("phone_numbers", getPhoneNumbers());
            jsonObject.put("email_addresses", getEmailAddresses());
        }
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbId() {
        return fb_id;
    }

    public void setFbId(String fb_id) {
        this.fb_id = fb_id;
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

    public ArrayList<String> getPhoneNumbers() {
        return phone_numbers;
    }

    public void setPhoneNumbers(ArrayList<String> phone_numbers) {
        this.phone_numbers = phone_numbers;
    }

    public ArrayList<String> getEmailAddresses() {
        return email_addresses;
    }

    public void setEmailAddresses(ArrayList<String> email_addresses) {
        this.email_addresses = email_addresses;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", fb_id='" + fb_id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", photo=" + photo +
                ", phone_numbers=" + phone_numbers +
                ", email_addresses=" + email_addresses +
                '}';
    }
}
