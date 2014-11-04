package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.RatingsSummaryHash;
import com.delectable.mobile.api.models.WineProfileSubProfile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

public class RatingsSummaryTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRatingsParcelable() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_profiles_subprofile_ctx);
        JSONObject payload = json.optJSONObject("payload");
        JSONObject wineProfile = payload.optJSONObject("wine_profile");
        WineProfileSubProfile wine = mGson.fromJson(wineProfile.toString(), WineProfileSubProfile.class);
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
