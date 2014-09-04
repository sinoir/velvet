package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.events.BaseEvent;

public class SearchAccountsEvent extends BaseEvent {

    private SearchResult<AccountSearch> mResult;

    public SearchAccountsEvent(String errorMessage) {
        super(errorMessage);
    }

    public SearchAccountsEvent(SearchResult<AccountSearch> result) {
        super(true);
        mResult = result;
    }

    public SearchResult<AccountSearch> getResult() {
        return mResult;
    }
}
