package com.delectable.mobile.api.models;

/**
 * Resources that can be posted, updated, or deleted should implement this interface.
 */
public interface Transactable {

    public TransitionState getTransitionState();

    public void setTransitionState(TransitionState state);

    public boolean isTransacting();

    public void setTransacting(boolean transacting);
}
