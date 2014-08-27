package com.delectable.mobile.api.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class TaggeeContact implements Parcelable, Serializable {

    public static final Parcelable.Creator<TaggeeContact> CREATOR
            = new Parcelable.Creator<TaggeeContact>() {
        public TaggeeContact createFromParcel(Parcel source) {
            return new TaggeeContact(source);
        }

        public TaggeeContact[] newArray(int size) {
            return new TaggeeContact[size];
        }
    };

    private static final long serialVersionUID = -248001638605165056L;

    String id;

    String fb_id;

    String fname;

    String lname;

    PhotoHash photo;

    ArrayList<String> phone_numbers;

    ArrayList<String> email_addresses;

    public TaggeeContact() {
    }

    /**
     * Build Taggee from Account object
     */
    public TaggeeContact(AccountMinimal account) {
        this.id = account.getId();
        this.fname = account.getFname();
        this.lname = account.getLname();
        this.photo = account.getPhoto();
        // We don't need phone #s or email addresses, those are for contacts from the device Contacts
    }

    public TaggeeContact(String id, String fb_id, String fname, String lname,
            PhotoHash photo, ArrayList<String> phone_numbers,
            ArrayList<String> email_addresses) {
        this.id = id;
        this.fb_id = fb_id;
        this.fname = fname;
        this.lname = lname;
        this.photo = photo;
        this.phone_numbers = phone_numbers;
        this.email_addresses = email_addresses;
    }

    private TaggeeContact(Parcel in) {
        this.id = in.readString();
        this.fb_id = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
        this.phone_numbers = (ArrayList<String>) in.readSerializable();
        this.email_addresses = (ArrayList<String>) in.readSerializable();
    }

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
        return getFname() != null && !isFacebookContact() && !isDelectaFriendContact();
    }

    public JSONObject buildJsonObject() throws JSONException {
        // TODO: Remove once we use new Request Objects for new Capture Flow bits.
        // This will be handled by GSON ..
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

    public String getFullName() {
        return fname + " " + lname;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fb_id);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeParcelable(this.photo, 0);
        dest.writeSerializable(this.phone_numbers);
        dest.writeSerializable(this.email_addresses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaggeeContact that = (TaggeeContact) o;

        if (email_addresses != null ? !email_addresses.equals(that.email_addresses)
                : that.email_addresses != null) {
            return false;
        }
        if (fb_id != null ? !fb_id.equals(that.fb_id) : that.fb_id != null) {
            return false;
        }
        if (fname != null ? !fname.equals(that.fname) : that.fname != null) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (lname != null ? !lname.equals(that.lname) : that.lname != null) {
            return false;
        }
        if (phone_numbers != null ? !phone_numbers.equals(that.phone_numbers)
                : that.phone_numbers != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fb_id != null ? fb_id.hashCode() : 0);
        result = 31 * result + (fname != null ? fname.hashCode() : 0);
        result = 31 * result + (lname != null ? lname.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (phone_numbers != null ? phone_numbers.hashCode() : 0);
        result = 31 * result + (email_addresses != null ? email_addresses.hashCode() : 0);
        return result;
    }
}
