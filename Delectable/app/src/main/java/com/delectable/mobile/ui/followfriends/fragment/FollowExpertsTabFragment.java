package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchInfluencerSuggestionsEvent;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

public class FollowExpertsTabFragment extends BaseFragment {

    @Inject
    AccountController mAccountController;

    private static final String TAG = FollowExpertsTabFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        TextView tv = new TextView(getActivity());
        tv.setText(TAG);
        return tv;

    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.fetchInfluencerSuggestions();
    }

    public void onEventMainThread(FetchInfluencerSuggestionsEvent event) {
        if (event.isSuccessful()) {
            Log.d(TAG, "fetch success! account retrieved: " + event.getAccounts().size());
            return;
        }
        Log.d(TAG, "event failed");

    }
}
