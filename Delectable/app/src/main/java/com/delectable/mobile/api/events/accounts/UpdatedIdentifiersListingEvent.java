package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Identifier;

import java.util.ArrayList;

public class UpdatedIdentifiersListingEvent extends BaseEvent {

    private ArrayList<Identifier> mIdentifiers;

    public UpdatedIdentifiersListingEvent(String errorMessage) {
        super(errorMessage);
    }

    public UpdatedIdentifiersListingEvent(ArrayList<Identifier> identifiers) {
        super(true);
        mIdentifiers = identifiers;
    }

    public ArrayList<Identifier> getIdentifiers() {
        return mIdentifiers;
    }
}
