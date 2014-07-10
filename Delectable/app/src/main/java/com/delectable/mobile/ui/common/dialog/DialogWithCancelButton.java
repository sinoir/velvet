package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DialogWithCancelButton extends AlertDialog {

    private TextView mTitleTextView;

    protected DialogWithCancelButton(Context context) {
        super(context);
        customizeTitleView();
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customizeTitleDivider();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

    private void customizeTitleView() {
        View titleView = getLayoutInflater().inflate(R.layout.dialog_title_with_close_button, null);
        View cancelButton = titleView.findViewById(R.id.dialog_cancel_button);
        setCustomTitle(titleView);
        mTitleTextView = (TextView) titleView.findViewById(R.id.dialog_title);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void customizeTitleDivider() {
        // Need to hack the title bar divider, this is hard coded on the android platform...
        Resources res = getContext().getResources();
        int dividerGrayColor = res.getColor(R.color.d_light_gray);
        View titleDivider = findViewById(res.getIdentifier("titleDivider", "id", "android"));
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(dividerGrayColor);
        }
    }
}
