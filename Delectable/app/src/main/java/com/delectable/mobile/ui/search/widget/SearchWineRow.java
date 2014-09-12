package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchWineRow extends RelativeLayout {

    @InjectView(R.id.wine_image)
    protected ImageView mWineImage;

    @InjectView(R.id.producer_name)
    protected FontTextView mProducerName;

    @InjectView(R.id.wine_name)
    protected FontTextView mWineName;


    public SearchWineRow(Context context) {
        this(context, null);
    }

    public SearchWineRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchWineRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_search_wine, this);
        ButterKnife.inject(this);
    }

    private void updateData(String profileImageUrl, String producerName, String wineName) {
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mWineImage);
        mProducerName.setText(producerName);
        mWineName.setText(wineName);
    }

    public void updateData(BaseWineMinimal baseWine) {
        updateData(baseWine.getPhoto().getBestThumb(),
                baseWine.getProducerName(),
                baseWine.getName());
    }
}
