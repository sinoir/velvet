package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.ProvisionCaptureRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ProvisionCaptureTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseProvisionCapture() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_provision_response);

        ProvisionCaptureRequest request = new ProvisionCaptureRequest();
        ProvisionCapture actualProvision = request.buildResopnseFromJson(json);

        assertEquals("delectable-captured-photos", actualProvision.getBucket());
        assertEquals("adam-bednarek-1403535987-cb3008064b60.jpg", actualProvision.getFilename());
        assertEquals("image/jpeg", actualProvision.getHeaders().getContentType());
        assertEquals("/delectable-captured-photos/adam-bednarek-1403535987-cb3008064b60.jpg",
                actualProvision.getHeaders().getUrl());
        assertEquals("max-age=2592000", actualProvision.getHeaders().getCacheControl());
        assertEquals("AWS AKIAJWTVYM3KHHQGQSVA:aqXqb/6j0PvqOHwGA+UCxNY2eBg=",
                actualProvision.getHeaders().getAuthorization());
        assertEquals("Mon, 23 Jun 2014 15:06:27 GMT", actualProvision.getHeaders().getDate());
    }
}
