package com.delectable.mobile.events.basewines;

import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.events.BaseEvent;

public class SearchWinesEvent extends BaseEvent {

    private SearchResult<BaseWine> mResult;

    public SearchWinesEvent(String errorMessage) {
        super(errorMessage);
    }

    public SearchWinesEvent(SearchResult<BaseWine> result) {
        super(true);
        mResult = result;
    }

    public SearchResult<BaseWine> getResult() {
        return mResult;
    }
}
