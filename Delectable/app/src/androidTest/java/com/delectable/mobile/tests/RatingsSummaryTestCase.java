package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.RatingsSummaryHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.api.requests.WineProfilesContext;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

/**
 * Created by wayne on 6/30/14.
 */
public class RatingsSummaryTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRaringsParcelable() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_profiles_subprofile_ctx);
        WineProfilesContext request = new WineProfilesContext();
        WineProfile wine = (WineProfile) request.buildResopnseFromJson(json);
        RatingsSummaryHash expectedRatings = wine.getRatingsSummary();

        Parcel testParcel = Parcel.obtain();

        int originalPos = testParcel.dataPosition();
        expectedRatings.writeToParcel(testParcel, 0);

        // Must reset Data position!!
        testParcel.setDataPosition(originalPos);

        RatingsSummaryHash actualRatings = RatingsSummaryHash.CREATOR.createFromParcel(testParcel);

        assertEquals(expectedRatings, actualRatings);
    }

}
