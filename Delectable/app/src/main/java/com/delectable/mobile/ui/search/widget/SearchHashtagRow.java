package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchHashtagRow extends RelativeLayout {

    @InjectView(R.id.hashtag_name)
    protected FontTextView mHashtagName;

    @InjectView(R.id.occurence_count)
    protected FontTextView mOccurenceCount;


    public SearchHashtagRow(Context context) {
        this(context, null);
    }

    public SearchHashtagRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchHashtagRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_search_hashtag, this);
        ButterKnife.inject(this);
    }

    private void updateData(String hashtagName, Long occurrenceCount) {
        mHashtagName.setText(hashtagName);
        mOccurenceCount.setText(String.valueOf(occurrenceCount));
    }

    public void updateData(HashtagResult hashtag) {
        updateData("#"+hashtag.getTag(), hashtag.getCaptureCount());
    }
}
