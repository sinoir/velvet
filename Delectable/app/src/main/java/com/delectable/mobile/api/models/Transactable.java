package com.delectable.mobile.api.models;

/**
 * Resources that can be posted, updated, or deleted should implement this interface.
 */
public interface Transactable {

    public TransitionState getTransitionState();

    public void setTransitionState(TransitionState state);

    public boolean isTransacting();

    public void setTransacting(boolean transacting);

    public String getTransactionKey();

    /**
     * Used to identify what is changing inside the object that implements Transactable, for if
     * there are multiple properties of that object that can be acted upon.
     */
    public void setTransactionKey(String key);
}
