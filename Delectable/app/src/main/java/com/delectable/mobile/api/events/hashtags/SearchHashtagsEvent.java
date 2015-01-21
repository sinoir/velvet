package com.delectable.mobile.api.events.hashtags;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.api.util.ErrorUtil;

public class SearchHashtagsEvent extends BaseEvent {

    private SearchResult<HashtagResult> mResult;

    public SearchHashtagsEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }

    public SearchHashtagsEvent(SearchResult<HashtagResult> result) {
        super(true);
        mResult = result;
    }

    public SearchResult<HashtagResult> getResult() {
        return mResult;
    }
}
