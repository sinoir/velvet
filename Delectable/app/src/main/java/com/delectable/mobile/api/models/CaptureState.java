package com.delectable.mobile.api.models;

public enum CaptureState {
    /**
     * no basewine, no wineprofile, no transcription error message (when instant match fails)
     */
    UNIDENTIFIED,
    /**
     * transcription error message exists (when people tried to transcribed but failed)
     */
    IMPOSSIBLED,
    /**
     * basewine exists
     */
    UNVERIFIED,
    /**
     * wineprofile exists
     */
    IDENTIFIED;

    public static CaptureState getState(PendingCapture capture) {
        if (capture.getWineProfile() != null) {
            return IDENTIFIED;
        }
        if (capture.getBaseWine() != null) {
            return UNVERIFIED;
        }
        if (capture.getTranscriptionErrorMessage() != null) {
            return IMPOSSIBLED;
        }
        return UNIDENTIFIED;
    }

    public static CaptureState getState(CaptureDetails capture) {
        if (capture.getWineProfile() != null) {
            return IDENTIFIED;
        }
        if (capture.getBaseWine() != null) {
            return UNVERIFIED;
        }
        if (capture.getTranscriptionErrorMessage() != null) {
            return IMPOSSIBLED;
        }
        return UNIDENTIFIED;
    }
}
