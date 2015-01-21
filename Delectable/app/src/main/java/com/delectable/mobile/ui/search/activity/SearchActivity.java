package com.delectable.mobile.ui.search.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.search.fragment.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class SearchActivity extends BaseActivity {

    private static final String DEFAULT_TAB = "DEFAULT_TAB";

    public static Intent newIntent(Context c, int defaultTab) {
        Intent intent = new Intent(c, SearchActivity.class);
        intent.putExtra(DEFAULT_TAB, defaultTab);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_toolbar_fragment_container);

        int showTab = getIntent().getIntExtra(DEFAULT_TAB, SearchFragment.WINES);

        if (savedInstanceState == null) {
            SearchFragment fragment = SearchFragment.newInstance(showTab);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

}
