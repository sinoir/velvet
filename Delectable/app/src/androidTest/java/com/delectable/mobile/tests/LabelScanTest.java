package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.model.api.scanwinelabels.LabelScanResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LabelScanTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseLabelScanResults() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_label_scan_identify_match);

        LabelScanResponse responseObject = mGson.fromJson(json.toString(), LabelScanResponse.class);
        LabelScan actualLabelScan = responseObject.getLabelScan();

        assertEquals("5407947dfe38d40dbf000014", actualLabelScan.getId());
        assertEquals(1, actualLabelScan.getBaseWineMatches().size());

        BaseWine actualBaseWine = actualLabelScan.getBaseWineMatches().get(0);
        assertEquals("53e90b0076930605000acf80", actualBaseWine.getId());
        assertEquals("POE", actualBaseWine.getName());
        assertEquals("53e90aff76930605000acf7f", actualBaseWine.getProducerId());
        assertEquals("Angel Camp Vinyard", actualBaseWine.getProducerName());
        assertEquals("2970273", actualBaseWine.getRegionId());
        assertEquals("53e90b00f265ba04004da251", actualBaseWine.getDefaultWineProfileId());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotosStaging/dima-vartanian-1402353941-89496f7715c8.jpg",
                actualBaseWine.getPhoto().getUrl());
        assertNotNull(actualBaseWine.getDescription());
        assertEquals("53e90b00f265ba04004da251", actualBaseWine.getWineProfiles().get(0).getId());
        assertEquals("profile", actualBaseWine.getContext());
        assertEquals("Wu-dKsH-8dEh0g", actualBaseWine.getETag());
    }
}
