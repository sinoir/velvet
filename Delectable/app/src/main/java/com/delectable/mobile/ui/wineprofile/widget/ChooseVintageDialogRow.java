package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A representation of the {@link VintageWineInfo} View Model, this is the row for the listview that
 * appears in the Choose Vintage dialog.
 */
public class ChooseVintageDialogRow extends RelativeLayout {

    private static final int NO_AVG_RATING = -1;

    @InjectView(R.id.year)
    protected TextView mYear;

    @InjectView(R.id.rating)
    protected TextView mRating;

    @InjectView(R.id.price_view)
    protected WinePriceView mWinePriceView;

    public ChooseVintageDialogRow(Context context) {
        this(context, null);
    }

    public ChooseVintageDialogRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseVintageDialogRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_dialog_choose_vintage, this);

        ButterKnife.inject(this);
    }

    private void updateData(String year, double rating) {
        mYear.setText(year);

        //rating
        if (rating == NO_AVG_RATING) { //handling a no rating case, show a dash
            mRating.setText("-");
            mRating.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            DecimalFormat format = new DecimalFormat("0.0");
            String allAvgStr = format.format(rating);
            mRating.setText(allAvgStr);
            mRating.setTextColor(getResources().getColor(R.color.d_light_green));
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
}