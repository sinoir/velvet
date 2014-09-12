package com.delectable.mobile.events.basewines;

import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.events.BaseEvent;

public class SearchWinesEvent extends BaseEvent {

    private SearchResult<BaseWineMinimal> mResult;

    public SearchWinesEvent(String errorMessage) {
        super(errorMessage);
    }

    public SearchWinesEvent(SearchResult<BaseWineMinimal> result) {
        super(true);
        mResult = result;
    }

    public SearchResult<BaseWineMinimal> getResult() {
        return mResult;
    }
}
