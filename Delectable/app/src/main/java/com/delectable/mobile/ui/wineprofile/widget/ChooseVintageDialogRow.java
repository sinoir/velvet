package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * A representation of the {@link VintageWineInfo} View Model, this is the row for the listview that
 * appears in the Choose Vintage dialog.
 */
public class ChooseVintageDialogRow extends RelativeLayout {

    private static final int NO_AVG_RATING = -1;

    private final TextView mYear;

    private final TextView mRatingsCount;

    private final TextView mRating;

    public ChooseVintageDialogRow(Context context) {
        this(context, null);
    }

    public ChooseVintageDialogRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseVintageDialogRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_dialog_choose_vintage, this);

        mYear = (TextView) findViewById(R.id.year);
        mRatingsCount = (TextView) findViewById(R.id.ratings_count);
        mRating = (TextView) findViewById(R.id.rating);
    }

    public void updateData(String year, int ratingsCount, double rating) {
        mYear.setText(year);
        String ratingCount = getResources()
                .getQuantityString(R.plurals.choose_vintage_dialog_ratings_count, ratingsCount,
                        ratingsCount);
        mRatingsCount.setText(ratingCount);

        //rating
        if (rating == NO_AVG_RATING) { //handling a no rating case, show a dash
            mRating.setText("-");
            mRating.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            DecimalFormat format = new DecimalFormat("0.0");
            String allAvgStr = format.format(rating);
            mRating.setText(makeRatingDisplayText(allAvgStr));
            mRating.setTextColor(getResources().getColor(R.color.d_light_green));
        }
    }

    /**
     * Convenience method that calls {@link #updateData(String, int, double)}, used to update the
     * data for a normal row.
     */
    public void updateData(VintageWineInfo wineInfo) {
        String year = wineInfo.getYear();
        // TODO: Remove Rating count
        int reviewCount = wineInfo.getRatingCount();
        double rating = wineInfo.getRating();

        updateData(year, reviewCount, rating);
    }

    /**
     * Convenience method that calls {@link #updateData(String, int, double)}, used to update the
     * data for the all years row.
     */
    public void updateData(BaseWine baseWine) {
        // TODO:  Remove All years
        String year = getResources().getString(R.string.choose_vintage_dialog_all_years);
        int reviewCount = baseWine.getRatingsSummary().getAllCount();
        double rating = baseWine.getRatingsSummary().getAllAvgOfTen();

        updateData(year, reviewCount, rating);
    }

    //TODO should abstract this, copied directly from WineProfileFragment. Make textview subclass perhaps with a setter for the ratings

    /**
     * Makes the rating display text where the rating is a bit bigger than the 10.
     */
    private CharSequence makeRatingDisplayText(String rating) {
        SpannableString ss = new SpannableString(rating);
        ss.setSpan(new RelativeSizeSpan(1.3f), 0, rating.length(), 0); // set size
        CharSequence displayText = TextUtils.concat(ss, "/10");
        return displayText;
    }
}