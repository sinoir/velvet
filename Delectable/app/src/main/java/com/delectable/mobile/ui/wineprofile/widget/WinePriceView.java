package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;
import com.delectable.mobile.util.AnalyticsUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WinePriceView extends RelativeLayout {

    @InjectView(R.id.loading)
    protected View mLoadingView;

    @InjectView(R.id.sold_out)
    protected View mSoldOutView;

    @InjectView(R.id.check_price_button)
    protected View mCheckPrice;

    @InjectView(R.id.price_button)
    protected TextView mPriceText;

    @Inject
    protected AnalyticsUtil mAnalytics;

    private WinePriceViewActionsCallback mActionsCallback;

    private VintageWineInfo mWineInfo;

    public WinePriceView(Context context) {
        this(context, null);
    }

    public WinePriceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WinePriceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        App.injectMembers(this);

        View.inflate(context, R.layout.widget_wine_price, this);

        ButterKnife.inject(this);
    }

    public void resetUI() {
        mLoadingView.setVisibility(View.GONE);
        mSoldOutView.setVisibility(View.GONE);
        mCheckPrice.setVisibility(View.GONE);
        mPriceText.setVisibility(View.GONE);
    }

    public void updateWithPriceInfo(VintageWineInfo vintageWineInfo) {
        resetUI();

        mWineInfo = vintageWineInfo;

        // Ordering of how we check the Price States matter
        if (vintageWineInfo.isLoading()) {
            showLoading();
        } else if (vintageWineInfo.isSoldOut()) {
            mSoldOutView.setVisibility(View.VISIBLE);
        } else if (vintageWineInfo.hasPrice()) {
            mPriceText.setVisibility(View.VISIBLE);
            mPriceText.setText(getResources().getString(R.string.wine_profile_buy) + " " + vintageWineInfo.getPriceText());
        } else {
            mCheckPrice.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading() {
        resetUI();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void setSoldoutTextClicklable(boolean clickable) {
        mSoldOutView.setClickable(clickable);
    }

    public void setActionsCallback(WinePriceViewActionsCallback actionsCallback) {
        mActionsCallback = actionsCallback;
    }

    @OnClick(R.id.check_price_button)
    protected void checkPriceClicked() {
        if (mActionsCallback != null) {
            mActionsCallback.onPriceCheckClicked(mWineInfo);
        }
        mAnalytics.trackBuyButtonPressed(AnalyticsUtil.BUTTON_STATE_CHECK_PRICE);
    }

    @OnClick(R.id.price_button)
    protected void priceClicked() {
        if (mActionsCallback != null) {
            mActionsCallback.onPriceClicked(mWineInfo);
        }
        mAnalytics.trackBuyButtonPressed(AnalyticsUtil.BUTTON_STATE_PRICE_SHOWN);
    }

    @OnClick(R.id.sold_out)
    protected void soldOutClicked() {
        if (mActionsCallback != null) {
            mActionsCallback.onSoldOutClicked(mWineInfo);
        }
    }

    public static interface WinePriceViewActionsCallback {

        public void onPriceCheckClicked(VintageWineInfo wineInfo);

        public void onPriceClicked(VintageWineInfo wineInfo);

        public void onSoldOutClicked(VintageWineInfo wineInfo);
    }
}
