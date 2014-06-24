package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.LabelScan;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.IdentifyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LabelScanTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseCaptureDetailsCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_label_scan_identify_match);
        ProvisionCapture provisionCapture = new ProvisionCapture();
        provisionCapture.setBucket("Testbucket");
        provisionCapture.setFilename("testFileName");

        IdentifyRequest request = new IdentifyRequest(provisionCapture);

        LabelScan actualScan = request.buildResopnseFromJson(json);

        assertEquals("53a9fbbe31a01582e5000232", actualScan.getId());
        assertEquals(
                "https://s3.amazonaws.com/delectable-label-scan-photos/adam-bednarek-1-1403648955-44f2b83726b3.jpg",
                actualScan.getPhoto());
        // Wine Profile
        assertEquals(1, actualScan.getBaseWine().getWineProfiles().size());
        assertEquals("impossible",
                actualScan.getBaseWine().getWineProfiles().get(0).getPriceStatus());
        assertEquals("526b19e4dde641ac00000005",
                actualScan.getBaseWine().getWineProfiles().get(0).getId());
        assertEquals(35.285714285714285,
                actualScan.getBaseWine().getWineProfiles().get(0).getRatingsSummary().getProAvg());
        assertEquals(7, actualScan.getBaseWine().getWineProfiles().get(0).getRatingsSummary()
                .getProCount());
        assertEquals(35.57142857142857,
                actualScan.getBaseWine().getWineProfiles().get(0).getRatingsSummary().getAllAvg());
        assertEquals(14, actualScan.getBaseWine().getWineProfiles().get(0).getRatingsSummary()
                .getAllCount());
        assertEquals("3Ld8OSljXfJZAg", actualScan.getBaseWine().getWineProfiles().get(0).getETag());
        assertEquals(-1.0, actualScan.getBaseWine().getWineProfiles().get(0).getPrice());
        assertEquals("5305bacb8049af4f3a00a405",
                actualScan.getBaseWine().getWineProfiles().get(0).getBaseWineId());
        assertEquals("2010", actualScan.getBaseWine().getWineProfiles().get(0).getVintage());
        assertEquals("", actualScan.getBaseWine().getWineProfiles().get(0).getDescription());
        assertEquals("subprofile", actualScan.getBaseWine().getWineProfiles().get(0).getContext());
        assertEquals(null, actualScan.getBaseWine().getWineProfiles().get(0).getPriceText());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_nano.jpg",
                actualScan.getBaseWine().getWineProfiles().get(0).getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_micro.jpg",
                actualScan.getBaseWine().getWineProfiles().get(0).getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_thumb.jpg",
                actualScan.getBaseWine().getWineProfiles().get(0).getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_medium.jpg",
                actualScan.getBaseWine().getWineProfiles().get(0).getPhoto().getMediumUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af.jpg",
                actualScan.getBaseWine().getWineProfiles().get(0).getPhoto().getUrl());

        assertEquals(1, actualScan.getBaseWine().getVarietalComposition().size());
        assertEquals("20", actualScan.getBaseWine().getVarietalComposition().get(0).getId());
        assertEquals("#e5bc64",
                actualScan.getBaseWine().getVarietalComposition().get(0).getColor());
        assertEquals("Chardonnay",
                actualScan.getBaseWine().getVarietalComposition().get(0).getName());

        assertEquals(35.285714285714285, actualScan.getBaseWine().getRatingsSummary().getProAvg());
        assertEquals(7, actualScan.getBaseWine().getRatingsSummary().getProCount());
        assertEquals(35.57142857142857, actualScan.getBaseWine().getRatingsSummary().getAllAvg());
        assertEquals(14, actualScan.getBaseWine().getRatingsSummary().getAllCount());
        assertEquals("526b19e4dde641ac00000005",
                actualScan.getBaseWine().getDefaultWineProfileId());
        assertEquals("1603408", actualScan.getBaseWine().getRegionId());
        assertEquals(false, actualScan.getBaseWine().getCarbonation());
        assertEquals("not_desert", actualScan.getBaseWine().getSweetness());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_nano.jpg",
                actualScan.getBaseWine().getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_micro.jpg",
                actualScan.getBaseWine().getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_thumb.jpg",
                actualScan.getBaseWine().getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af_medium.jpg",
                actualScan.getBaseWine().getPhoto().getMediumUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/petra-and-dade-thieriot-1382750543-3224602277af.jpg",
                actualScan.getBaseWine().getPhoto().getUrl());

        assertEquals("5305bacb8049af4f3a00a405", actualScan.getBaseWine().getId());
        assertEquals("5305baca8049af4f3a00a309",
                actualScan.getBaseWine().getProducerId());
        assertEquals(4, actualScan.getBaseWine().getRegionPath().size());
        assertEquals("2973141", actualScan.getBaseWine().getRegionPath().get(0).getId());
        assertEquals("France", actualScan.getBaseWine().getRegionPath().get(0).getName());

        assertEquals("Y7F9vOAgapaDSg", actualScan.getBaseWine().getETag());
        assertEquals("Domaine Roulot", actualScan.getBaseWine().getProducerName());
        assertEquals("white", actualScan.getBaseWine().getColor());
        assertEquals("", actualScan.getBaseWine().getDescription());
        assertEquals("Meursault 1er Cru Chardonnay", actualScan.getBaseWine().getName());
        assertEquals("profile", actualScan.getBaseWine().getContext());
        assertEquals(null, actualScan.getBaseWine().getForwardId());
    }
}
