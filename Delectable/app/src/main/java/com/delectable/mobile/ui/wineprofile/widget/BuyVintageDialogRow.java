package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;
import com.delectable.mobile.util.TextUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A representation of the {@link VintageWineInfo} View Model, this is the row for the listview that
 * appears in the Choose Vintage dialog.
 */
public class BuyVintageDialogRow extends RelativeLayout {

    private static final int NO_AVG_RATING = -1;

    @InjectView(R.id.year)
    protected TextView mYear;

    @InjectView(R.id.rating)
    protected TextView mRating;

    @InjectView(R.id.price_view)
    protected WinePriceView mWinePriceView;

    public BuyVintageDialogRow(Context context) {
        this(context, null);
    }

    public BuyVintageDialogRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyVintageDialogRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_dialog_buy_vintage, this);

        ButterKnife.inject(this);
    }

    private void updateData(String year, double rating) {
        mYear.setText(year);

        //rating
        if (rating == NO_AVG_RATING) { //handling a no rating case, show a dash
            mRating.setText("-");
            mRating.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            mRating.setText(TextUtil.makeRatingDisplayText(getContext(), rating));
        }
    }

    /**
     * Convenience method that calls {@link #updateData(String, double)}, used to update the data
     * for a normal row.
     */
    public void updateData(VintageWineInfo wineInfo) {
        String year = wineInfo.getYear();
        double rating = wineInfo.getRating();

        updateData(year, rating);
        mWinePriceView.updateWithPriceInfo(wineInfo);
    }

    public void setWinePriceActionCallback(WinePriceView.WinePriceViewActionsCallback callback) {
        mWinePriceView.setActionsCallback(callback);
    }
}