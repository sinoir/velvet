package com.mienaikoe.wifimesh;

import android.os.Bundle;
import android.view.MenuItem;

public class LineActivity extends BaseActivity {

    private static final String TAG = LineActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_fragment_container);

        if (savedInstanceState == null) {
            TrainLinesFragment trainLinesFragment = new TrainLinesFragment();
            trainLinesFragment.setTrainSystem(TrainSystemModel.getTrainSystem());
            getFragmentManager().beginTransaction()
                    .add(R.id.container, trainLinesFragment)
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

    @Override
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}
