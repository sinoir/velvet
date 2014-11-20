package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class ConfirmationDialogFragment extends BaseDialogFragment {

    public static ConfirmationDialogFragment newInstance() {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, null, false);
        ButterKnife.inject(this, view);

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(R.string.order_confirmation_title);
            getActivity().getActionBar().setHomeButtonEnabled(false);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        }

        return view;
    }
}
