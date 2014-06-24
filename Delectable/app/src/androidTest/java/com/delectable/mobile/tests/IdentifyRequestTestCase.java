package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.requests.IdentifyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class IdentifyRequestTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testBuildIdentifyRequestJson() throws JSONException {
        IdentifyRequest expectedRequest = getSampleIdentifyRequest();

        JSONObject actualPayloadJson = expectedRequest.buildPayload();

        assertEquals(expectedRequest.getBucket(), actualPayloadJson.get("bucket"));
        assertEquals(expectedRequest.getFilename(), actualPayloadJson.get("filename"));
    }

    private IdentifyRequest getSampleIdentifyRequest() {
        ProvisionCapture provisionCapture = new ProvisionCapture();
        provisionCapture.setBucket("Testbucket");
        provisionCapture.setFilename("testFileName");

        IdentifyRequest sampleRequest = new IdentifyRequest(provisionCapture);
        return sampleRequest;
    }
}
