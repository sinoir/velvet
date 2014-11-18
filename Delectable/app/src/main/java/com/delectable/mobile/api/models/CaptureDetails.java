package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class CaptureDetails extends CaptureMinimal {

    private String transcription_error_message;

    private String location_name;

    private AccountMinimal capturer_participant;

    private ArrayList<AccountMinimal> liking_participants;

    private ArrayList<AccountMinimal> commenting_participants;

    private TaggeeParticipants taggee_participants;

    private ArrayList<CaptureComment> comments;

    /**
     * Updates existing capture with updated capture
     *
     * @param newCapture - Capture must be the same "object" / ID as this, and context must be
     *                   detailed
     */
    public void updateWithNewCapture(CaptureDetails newCapture) {
        setPrivate(newCapture.getPrivate());
        setShortShareUrl(newCapture.getShortShareUrl());
        setTweet(newCapture.getTweet());
        setRatings(newCapture.getRatings());
        setPhoto(newCapture.getPhoto());
        setBaseWine(newCapture.getBaseWine());
        setWineProfile(newCapture.getWineProfile());
        transcription_error_message = newCapture.getTranscriptionErrorMessage();
        location_name = newCapture.getLocationName();
        liking_participants = newCapture.getLikingParticipants();
        commenting_participants = newCapture.getCommentingParticipants();
        setCapturerParticipant(newCapture.getCapturerParticipant());
        taggee_participants = newCapture.getTaggeeParticipants();
        comments = newCapture.getComments();
        setETag(newCapture.getETag());
    }

    public ArrayList<CaptureComment> getCommentsForUserId(String id) {
        ArrayList<CaptureComment> captureComments = new ArrayList<CaptureComment>();
        if (comments != null && comments.size() > 0) {
            for (CaptureComment comment : comments) {
                if (comment.account_id.equalsIgnoreCase(id)) {
                    captureComments.add(comment);
                }
            }
        }
        return captureComments;
    }

    public CaptureComment getComment(String commentId) {
        CaptureComment captureComment = null;
        if (comments != null && comments.size() > 0) {
            for (CaptureComment comment : comments) {
                if (comment.id.equalsIgnoreCase(commentId)) {
                    captureComment = comment;
                    break;
                }
            }
        }
        return captureComment;
    }

    public boolean isUserTagged(String id) {
        if (taggee_participants != null && taggee_participants.registered != null) {
            for (AccountMinimal user : taggee_participants.registered) {
                if (user.getId().equalsIgnoreCase(id)) {
                    return true;
                }
            }
        }
        return false;
    }


    public int getLikesCount() {
        return liking_participants != null ? liking_participants.size() : 0;
    }

    public boolean doesUserLikeCapture(String accountId) {
        boolean likesCapture = false;
        if (liking_participants != null) {
            for (AccountMinimal account : liking_participants) {
                if (account.getId().equals(accountId)) {
                    likesCapture = true;
                    break;
                }
            }
        }
        return likesCapture;
    }

    //TODO this method should take the isLiked value as well bc wierd syncing issues may occur
    public void toggleUserLikesCapture(AccountMinimal userAccount) {
        boolean userLikedCapture = false;
        if (liking_participants != null) {
            for (AccountMinimal account : liking_participants) {
                if (account.getId().equals(userAccount.getId())) {
                    userLikedCapture = true;
                    liking_participants.remove(account);
                    break;
                }
            }
        }
        if (!userLikedCapture) {
            if (liking_participants == null) {
                liking_participants = new ArrayList<AccountMinimal>();
            }
            liking_participants.add(userAccount);
        }
    }

    public int getNumberTaggedParticipants() {
        int numParticipants = 0;
        if (getRegisteredParticipants() != null) {
            numParticipants += getRegisteredParticipants().size();
        }
        if (getFacebookParticipants() != null) {
            numParticipants += getFacebookParticipants().size();
        }
        if (getContactParticipants() != null) {
            numParticipants += getContactParticipants().size();
        }
        return numParticipants;
    }

    /**
     * Helper to get Display Title
     *
     * @return Wine Producer Name if it was a match, otherwise "UNIDENTIFIED"
     */
    public String getDisplayTitle() {
        String title = "";
        if (getWineProfile() != null) {
            title = getWineProfile().getProducerName();
            // Else if the capture went through that had no wine
        } else if (getTranscriptionErrorMessage() == null) {
            title = "UNIDENTIFIED";
        }
        return title;
    }

    /**
     * Helper to get Display Desciption
     *
     * @return Wine Name if it was a match, otherwise error / currently identifying wine text
     */
    public String getDisplayDescription() {
        String desc = "";
        if (getWineProfile() != null) {
            desc = getWineProfile().getName();
            // Else if the capture went through that had no wine
        } else if (getTranscriptionErrorMessage() != null) {
            desc = getTranscriptionErrorMessage();
        } else {
            desc = "We are identifying this wine.";
        }
        return desc;
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

    public AccountMinimal getCapturerParticipant() {
        return capturer_participant;
    }

    public void setCapturerParticipant(AccountMinimal capturer_participant) {
        this.capturer_participant = capturer_participant;
    }

    public ArrayList<AccountMinimal> getLikingParticipants() {
        return liking_participants;
    }

    public void setLikingParticipants(ArrayList<AccountMinimal> liking_participants) {
        this.liking_participants = liking_participants;
    }

    public ArrayList<AccountMinimal> getCommentingParticipants() {
        return commenting_participants;
    }

    public void setCommentingParticipants(ArrayList<AccountMinimal> commenting_participants) {
        this.commenting_participants = commenting_participants;
    }

    public ArrayList<AccountMinimal> getRegisteredParticipants() {
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

    public int getCommentsCount() {
        if (comments == null || comments.isEmpty()) {
            return 0;
        }
        // if first comment is from capturer it is not considered a regular comment
        return (comments.get(0).getAccountId().equals(capturer_participant.getId()))
                ? comments.size() - 1
                : comments.size();
    }

    @Override
    public String toString() {
        return "CaptureDetails{" +
                "id='" + getId() + '\'' +
                ", created_at=" + getCreatedAt() +
                ", private_=" + getPrivate() +
                ", context='" + getContext() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", short_share_url='" + getShortShareUrl() + '\'' +
                ", tweet='" + getTweet() + '\'' +
                ", ratings=" + getRatings() +
                ", photo=" + getPhoto() +
                ", base_wine=" + getBaseWine() +
                ", wine_profile=" + getWineProfile() +
                ", capturer_participant=" + capturer_participant +
                ", transcription_error_message='" + transcription_error_message + '\'' +
                ", location_name='" + location_name + '\'' +
                ", liking_participants=" + liking_participants +
                ", commenting_participants=" + commenting_participants +
                ", taggee_participants=" + taggee_participants +
                ", comments=" + comments +
                '}';
    }

    public static class TaggeeParticipants {

        ArrayList<AccountMinimal> registered;

        ArrayList<TaggeeContact> facebook;

        ArrayList<TaggeeContact> contact;

        public ArrayList<AccountMinimal> getRegistered() {
            return registered;
        }

        public ArrayList<TaggeeContact> getFacebook() {
            return facebook;
        }

        public ArrayList<TaggeeContact> getContact() {
            return contact;
        }

        /**
         * @return Returns {@code null} if userId wasn't found.
         */
        public AccountMinimal checkForRegisteredUser(String userId) {
            for (AccountMinimal account : registered) {
                if (account.getId().equals(userId)) {
                    return account;
                }
            }
            return null;
        }

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
