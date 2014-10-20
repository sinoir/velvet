package com.delectable.mobile.api.events.basewines;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.api.util.ErrorUtil;

public class SearchWinesEvent extends BaseEvent {

    private SearchResult<BaseWineMinimal> mResult;

    public SearchWinesEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }

    public SearchWinesEvent(SearchResult<BaseWineMinimal> result) {
        super(true);
        mResult = result;
    }

    public SearchResult<BaseWineMinimal> getResult() {
        return mResult;
    }
}
