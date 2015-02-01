package com.mienaikoe.wifimesh;

import android.os.Bundle;
import android.view.MenuItem;

public class LineActivity extends BaseActivity {

    private static final String TAG = LineActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        if (savedInstanceState == null) {
            LineFragment lineFragment = new LineFragment();
            lineFragment.setTrainSystem(TrainSystemModel.getTrainSystem());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, lineFragment)
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
