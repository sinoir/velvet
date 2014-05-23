package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.registration.activity.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by abednarek on 5/22/14.
 */
public class HomeFragment extends BaseFragment {

    Button mLogoutButton;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mLogoutButton = (Button) rootView.findViewById(R.id.mLogoutButton);

        // TODO: Temp logout button (Porobably shoud put logout functionality into BaseFragment/Activity?)
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.onSignOut(getActivity());
                Intent launchIntent = new Intent();
                launchIntent.setClass(getActivity(), LoginActivity.class);
                startActivity(launchIntent);
                getActivity().finish();
            }
        });
        // TODO: Build out menu system
        return rootView;
    }
}
