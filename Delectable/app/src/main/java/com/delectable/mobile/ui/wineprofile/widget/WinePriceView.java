package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WinePriceView extends RelativeLayout {

    @InjectView(R.id.loading)
    protected View mLoadingView;

    @InjectView(R.id.sold_out)
    protected View mSoldOutView;

    @InjectView(R.id.check_price_button)
    protected View mCheckPrice;

    @InjectView(R.id.price_button)
    protected TextView mPriceText;

    public WinePriceView(Context context) {
        this(context, null);
    }

    public WinePriceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WinePriceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.widget_wine_price, this);

        ButterKnife.inject(this);
    }
}
