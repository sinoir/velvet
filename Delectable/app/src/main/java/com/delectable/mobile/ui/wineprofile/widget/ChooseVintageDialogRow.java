package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.WineProfileSubProfile;
import com.delectable.mobile.ui.common.widget.Rating;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A representation of {@link WineProfileSubProfile}, this is the row for the listview that appears
 * in the {@link com.delectable.mobile.ui.wineprofile.dialog.ChooseVintageDialog ChooseVintageDialog}.
 */
public class ChooseVintageDialogRow extends RelativeLayout {

    @InjectView(R.id.year)
    protected TextView mYear;

    @InjectView(R.id.ratings_count)
    protected TextView mRatingsCount;

    @InjectView(R.id.rating)
    protected TextView mRating;

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

    public void updateData(String year, int ratingsCount, double rating) {
        mYear.setText(year);
        String ratingCount = getResources()
                .getQuantityString(R.plurals.choose_vintage_dialog_ratings_count, ratingsCount,
                        ratingsCount);
        mRatingsCount.setText(ratingCount);
        mRating.setText(Rating.forDisplay(getContext(), rating));
    }

    /**
     * Convenience method that calls {@link #updateData(String, int, double)}, used to update the
     * data for a normal row.
     */
    public void updateData(WineProfileSubProfile wineProfile) {
        String year = wineProfile.getVintage();
        int reviewCount = wineProfile.getRatingsSummary().getAllCount();
        double rating = wineProfile.getRatingsSummary().getAllAvg();

        updateData(year, reviewCount, rating);
    }

    /**
     * Convenience method that calls {@link #updateData(String, int, double)}, used to update the
     * data for the all years row.
     */
    public void updateData(BaseWine baseWine) {
        String year = getResources().getString(R.string.choose_vintage_dialog_all_years);
        int reviewCount = baseWine.getRatingsSummary().getAllCount();
        double rating = baseWine.getRatingsSummary().getAllAvg();

        updateData(year, reviewCount, rating);
    }
}