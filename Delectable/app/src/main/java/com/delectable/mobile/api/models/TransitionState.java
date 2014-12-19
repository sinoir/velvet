package com.delectable.mobile.api.models;

public enum TransitionState {

    /**
     * Default state. Equivalent to clearing the transition state.
     */
    SYNCED,
    /**
     * For POST requests, when creating a brand new resource.
     */
    POSTING,
    /**
     * For PUT requests, when updating object properties.
     */
    UPDATING,
    /**
     * For DELETE requests
     */
    DELETING;
}
