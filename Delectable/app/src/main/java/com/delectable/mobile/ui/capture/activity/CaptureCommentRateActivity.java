package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.capture.fragment.CaptureCommentRateFragment;

import android.os.Bundle;

import java.util.ArrayList;

public class CaptureCommentRateActivity extends BaseActivity {

    public static final String PARAMS_COMMENT = "PARAMS_COMMENT";

    public static final String PARAMS_COMMENT_ATTRIBUTES = "PARAMS_COMMENT_ATTRIBUTES";

    public static final String PARAMS_RATING = "PARAMS_RATING";

    public static final String PARAMS_IS_RATING = "PARAMS_IS_RATING";

    private static final String TAG = CaptureCommentRateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();

        String comment = null;
        ArrayList<CaptureCommentAttributes> attributes = null;
        int rating = -1;
        boolean isRating = false;

        if (args != null) {
            comment = args.getString(PARAMS_COMMENT);
            attributes = args.getParcelableArrayList(PARAMS_COMMENT_ATTRIBUTES);
            rating = args.getInt(PARAMS_RATING);
            isRating = args.getBoolean(PARAMS_IS_RATING);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,
                            CaptureCommentRateFragment.newInstance(comment, attributes, rating,
                            isRating))
                    .commit();
        }
    }
}
