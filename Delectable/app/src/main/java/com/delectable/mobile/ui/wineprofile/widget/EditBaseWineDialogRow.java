package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditBaseWineDialogRow extends RelativeLayout {

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    public EditBaseWineDialogRow(Context context) {
        this(context, null);
    }

    public EditBaseWineDialogRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditBaseWineDialogRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_dialog_edit_base_wine, this);
        ButterKnife.inject(this);
    }

    public void updateData(String producerName, String wineName) {
        if (producerName != null) {
            mProducerName.setText(producerName.toLowerCase());
        }
        mWineName.setText(wineName);
    }

}