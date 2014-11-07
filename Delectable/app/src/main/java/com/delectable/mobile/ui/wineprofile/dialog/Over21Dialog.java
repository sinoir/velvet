package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Over21Dialog extends DialogFragment {

    private static final String TAG = Over21Dialog.class.getSimpleName();

    public static Over21Dialog newInstance() {
        Over21Dialog f = new Over21Dialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_over_21, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.over21_button)
    public void onOver21Clicked() {
        UserInfo.setIsOver21(true);
        dismiss();
    }

    @OnClick(R.id.under21_button)
    public void onUnder21Clicked() {
        UserInfo.setIsOver21(false);
        dismiss();
    }
}
