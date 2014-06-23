package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.CaptureRequest;
import com.delectable.mobile.api.requests.ProvisionCaptureRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CaptureRequestTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBuildCaptureRequestMap() throws JSONException {
        CaptureRequest captureRequest = getSampleCaptureRequestWithoutTaggees();

        JSONObject actualPayloadJson = captureRequest.buildPayload();

        assertEquals(captureRequest.getLabelScanId(), actualPayloadJson.get("label_scan_id"));
        assertEquals(captureRequest.getBucket(), actualPayloadJson.get("bucket"));
        assertEquals(captureRequest.getFilename(), actualPayloadJson.get("filename"));
        assertEquals(captureRequest.getPrivate().toString(), actualPayloadJson.get("private"));
        assertEquals(captureRequest.getRating().toString(), actualPayloadJson.get("rating"));
        assertEquals(captureRequest.getFromCameraRoll().toString(),
                actualPayloadJson.get("from_camera_roll"));

        assertEquals(captureRequest.getShareTw().toString(), actualPayloadJson.get("share_tw"));
        assertEquals(captureRequest.getUserTw().toString(), actualPayloadJson.get("user_tw"));
        assertEquals(captureRequest.getShareFb().toString(), actualPayloadJson.get("share_fb"));

        assertEquals(captureRequest.getCaptureLongitude().toString(),
                actualPayloadJson.get("capture_longitude"));
        assertEquals(captureRequest.getCaptureLatitude().toString(),
                actualPayloadJson.get("capture_latitude"));

        assertEquals(captureRequest.getUserCountryCode(),
                actualPayloadJson.get("user_country_code"));
        assertEquals(captureRequest.getFoursquareLocationId(),
                actualPayloadJson.get("foursquare_location_id"));
    }

    public void testCaptureRequestFromProvision() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_provision_response);

        ProvisionCaptureRequest request = new ProvisionCaptureRequest();
        ProvisionCapture provisionCapture = request.buildResopnseFromJson(json);

        CaptureRequest actualCapture = new CaptureRequest(provisionCapture);

        assertEquals("delectable-captured-photos", actualCapture.getBucket());
        assertEquals("adam-bednarek-1403535987-cb3008064b60.jpg", actualCapture.getFilename());
    }

    private CaptureRequest getSampleCaptureRequestWithoutTaggees() {
        ProvisionCapture provisionCapture = new ProvisionCapture();
        provisionCapture.setBucket("Testbucket");
        provisionCapture.setFilename("testFileName");

        CaptureRequest sampleCapture = new CaptureRequest(provisionCapture);
        sampleCapture.setLabelScanId("scanId");
        sampleCapture.setPrivate(true);
        sampleCapture.setRating(30);
        sampleCapture.setFromCameraRoll(false);

        sampleCapture.setShareTw(true);
        sampleCapture.setShareFb(true);
        sampleCapture.setUserTw("User Twitter?");

        sampleCapture.setCaptureLongitude(74);
        sampleCapture.setCaptureLatitude(42);

        sampleCapture.setUserCountryCode("usa");
        sampleCapture.setFoursquareLocationId("foursquareID");

        return sampleCapture;
    }
}
