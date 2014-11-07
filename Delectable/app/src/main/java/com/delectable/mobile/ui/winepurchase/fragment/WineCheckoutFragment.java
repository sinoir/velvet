package com.delectable.mobile.ui.winepurchase.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class WineCheckoutFragment extends BaseFragment {

    private static final String ARGS_VINTAGE_ID = "ARGS_VINTAGE_ID";

    private String mVintageId;

    public static WineCheckoutFragment newInstance(String vintageId) {
        WineCheckoutFragment fragment = new WineCheckoutFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_VINTAGE_ID, vintageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        Bundle args = getArguments();
        if (args != null) {
            mVintageId = args.getString(ARGS_VINTAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_checkout, null, false);
        ButterKnife.inject(this, view);
        return view;
    }
}
