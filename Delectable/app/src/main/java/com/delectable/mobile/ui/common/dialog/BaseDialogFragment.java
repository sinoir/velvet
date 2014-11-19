package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DelectableTheme_Dialog);
    }
}
