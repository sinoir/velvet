package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.capture.fragment.LikingPeopleFragment;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class LikingPeopleActivity extends BaseActivity {

    private static final String TAG = LikingPeopleActivity.class.getSimpleName();

    private ArrayList<AccountMinimal> mLikingPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mLikingPeople = args.getParcelableArrayList(LikingPeopleFragment.PARAMS_LIKING_PEOPLE);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, LikingPeopleFragment.newInstance(mLikingPeople))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishDeepLinkActivity();
                break;
        }
        return true;
    }
}
