package com.delectable.mobile.ui.registration.fragment;

import com.delectable.mobile.BuildConfig;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.ServerInfo;
import com.delectable.mobile.api.cache.ServerInfo.Environment;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LandingPageFragment extends BaseFragment {

    private static final String TAG = LandingPageFragment.class.getSimpleName();

    /**
     * This spinner only gets show for debug builds, allows the tester to toggle which environments
     * they're pointing to.
     */
    @InjectView(R.id.debug_spinner)
    protected Spinner mServerEnvSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_landing_page, container, false);
        ButterKnife.inject(this, rootView);

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            setupDebugSpinner();
        } else {
            mServerEnvSpinner.setVisibility(View.GONE);
        }
        return rootView;
    }

    private void setupDebugSpinner() {
        ArrayAdapter<Environment> adapter = new ArrayAdapter<Environment>(
                getActivity(), android.R.layout.simple_spinner_item, Environment.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServerEnvSpinner.setAdapter(adapter);
        mServerEnvSpinner.setOnItemSelectedListener(ServerEnvSpinnerListener);

        //grab current environment and set in UI
        Environment env = ServerInfo.getEnvironment();
        mServerEnvSpinner.setSelection(env.ordinal());
    }

    @OnClick(R.id.join_button)
    protected void goToSignup() {
        launchNextFragment(new SignUpFragment());
    }

    @OnClick(R.id.login_text_container)
    protected void goToSignin() {
        launchNextFragment(new SignInFragment());
    }

    private OnItemSelectedListener ServerEnvSpinnerListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "selected " + position);
            Environment currentEnv = ServerInfo.getEnvironment();
            if (currentEnv.ordinal() != position) {
                //means new environment was selected, cache in sharedprefs
                ServerInfo.setEnvironment(Environment.values()[position]);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
