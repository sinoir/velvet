package com.delectable.mobile.ui.registration.dialog;

import com.delectable.mobile.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This Dialog isn't a floating window dialog, but provides a way to mask the entire view with white
 * opacity and show a loading circle.
 */
public class LoadingCircleDialog extends DialogFragment {

    public static final String TAG = LoadingCircleDialog.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DelectableTheme_Dialog_LoadingCircle);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading_circle, container, false);
        return view;
    }

}
