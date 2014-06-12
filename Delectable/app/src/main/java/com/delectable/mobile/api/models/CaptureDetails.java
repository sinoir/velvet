package com.delectable.mobile.api.models;

import com.delectable.mobile.util.DateHelperUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CaptureDetails extends Capture {

    public static int MAX_RATING_VALUE = 40;

    String short_share_url;

    String tweet;

    HashMap<String, Integer> ratings;

    PhotoHash photo;

    BaseWine base_wine;

    WineProfile wine_profile;

    String transcription_error_message;

    String location_name;

    ArrayList<Account> liking_participants;

    ArrayList<Account> commenting_participants;

    TaggeeParticipants taggee_participants;

    ArrayList<CaptureComment> comments;

    @Override
    public BaseResponse buildFromJson(JSONObject jsonObj) {
        JSONObject payloadObj = jsonObj.optJSONObject("payload");
        CaptureDetails newResource = null;
        if (payloadObj != null && payloadObj.optJSONObject("capture") != null) {
            newResource = buildFromJson(payloadObj.optJSONObject("capture"),
                    this.getClass());
        }

        return newResource;
    }

    public CaptureComment getCommentForUserId(String id) {
        CaptureComment captureComment = null;
        if (comments != null && comments.size() > 0) {
            for (CaptureComment comment : comments) {
                if (comment.account_id.equalsIgnoreCase(id)) {
                    captureComment = comment;
                    break;
                }
            }
        }
        return captureComment;
    }

    /**
     * Get % of Rating
     *
     * @param id = User ID linked to Ratings Hash
     * @return -1.0f if no rating exists, or value between 0.0f and 1.0f
     */
    public float getRatingPercentForId(String id) {
        float ratingPercent = -1.0f;
        if (ratings != null && ratings.containsKey(id)) {
            ratingPercent = ratings.get(id).floatValue() / MAX_RATING_VALUE;
        }
        if (ratingPercent <= 0.0f) {
            ratingPercent = -1.0f;
        }
        return ratingPercent;
    }

    public Date getCreatedAtDate() {
        double createdAtTime = created_at != null ? created_at.doubleValue() : 0.0f;
        return DateHelperUtil.dateFromDouble(createdAtTime);
    }

    public int getLikesCount() {
        return liking_participants != null ? liking_participants.size() : 0;
    }

    public boolean doesUserLikeCapture(String accountId) {
        boolean likesCapture = false;
        if (liking_participants != null) {
            for (Account account : liking_participants) {
                if (account.getId().equalsIgnoreCase(accountId)) {
                    likesCapture = true;
                    break;
                }
            }
        }
        return likesCapture;
    }

    public String getShortShareUrl() {
        return short_share_url;
    }

    public void setShortShareUrl(String short_share_url) {
        this.short_share_url = short_share_url;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public HashMap<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public BaseWine getBaseWine() {
        return base_wine;
    }

    public void setBaseWine(BaseWine base_wine) {
        this.base_wine = base_wine;
    }

    public WineProfile getWineProfile() {
        return wine_profile;
    }

    public void setWineProfile(WineProfile wine_profile) {
        this.wine_profile = wine_profile;
    }

    public String getTranscriptionErrorMessage() {
        return transcription_error_message;
    }

    public void setTranscriptionErrorMessage(String transcription_error_message) {
        this.transcription_error_message = transcription_error_message;
    }

    public String getLocationName() {
        return location_name;
    }

    public void setLocationName(String location_name) {
        this.location_name = location_name;
    }

    public ArrayList<Account> getLikingParticipants() {
        return liking_participants;
    }

    public void setLikingParticipants(ArrayList<Account> liking_participants) {
        this.liking_participants = liking_participants;
    }

    public ArrayList<Account> getCommentingParticipants() {
        return commenting_participants;
    }

    public void setCommentingParticipants(ArrayList<Account> commenting_participants) {
        this.commenting_participants = commenting_participants;
    }

    public ArrayList<Account> getRegisteredParticipants() {
        return taggee_participants != null ? taggee_participants.registered : null;
    }

    public ArrayList<TaggeeContact> getFacebookParticipants() {
        return taggee_participants != null ? taggee_participants.facebook : null;
    }

    public ArrayList<TaggeeContact> getContactParticipants() {
        return taggee_participants != null ? taggee_participants.contact : null;
    }

    public TaggeeParticipants getTaggeeParticipants() {
        return taggee_participants;
    }

    public void setTaggeeParticipants(TaggeeParticipants taggee_participants) {
        this.taggee_participants = taggee_participants;
    }

    public ArrayList<CaptureComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CaptureComment> comments) {
        this.comments = comments;
    }

    class TaggeeParticipants {

        ArrayList<Account> registered;

        ArrayList<TaggeeContact> facebook;

        ArrayList<TaggeeContact> contact;

        @Override
        public String toString() {
            return "TaggeeParticipants{" +
                    "registered=" + registered +
                    ", facebook=" + facebook +
                    ", contact=" + contact +
                    '}';
        }
    }
}
