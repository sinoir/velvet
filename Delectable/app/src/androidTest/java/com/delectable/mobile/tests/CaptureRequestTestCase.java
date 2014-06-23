package com.delectable.mobile.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.api.requests.CaptureRequest;
import com.delectable.mobile.api.requests.ProvisionCaptureRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CaptureRequestTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBuildCaptureRequestJsonWithoutTaggees() throws JSONException {
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

        // No Taggees mean empty array
        assertEquals(0, actualPayloadJson.getJSONArray("taggees").length());
    }

    public void testBuildCaptureRequestJsonWithTaggees() throws JSONException {
        CaptureRequest captureRequest = getSampleCaptureRequestWithTaggees();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();

        JSONObject actualPayloadJson = captureRequest.buildPayload();
        JSONArray taggeesJson = actualPayloadJson.getJSONArray("taggees");
        JSONObject actualFBContact = taggeesJson.getJSONObject(0);
        JSONObject actualDelectaContact = taggeesJson.getJSONObject(1);
        JSONObject actualUserContact = taggeesJson.getJSONObject(2);
        JSONObject actualUserContactWithEmailsAndNumbers = taggeesJson.getJSONObject(3);

        // Tests Facebook was parsed
        assertEquals(captureRequest.getTaggees().get(0).getFbId(), actualFBContact.get("fb_id"));

        // Tests Delectafriend
        assertEquals(captureRequest.getTaggees().get(1).getId(), actualDelectaContact.get("id"));

        // Tests User without any phone numbers or emails
        assertEquals(captureRequest.getTaggees().get(2).getFname(), actualUserContact.get("fname"));
        assertEquals(captureRequest.getTaggees().get(2).getLname(), actualUserContact.get("lname"));
        ArrayList<String> actualPhoneNumbers = gson
                .fromJson(actualUserContact.get("phone_numbers").toString(), listType);
        ArrayList<String> actualEmails = gson
                .fromJson(actualUserContact.get("email_addresses").toString(), listType);
        assertEquals(captureRequest.getTaggees().get(2).getPhoneNumbers(), actualPhoneNumbers);
        assertEquals(captureRequest.getTaggees().get(2).getEmailAddresses(), actualEmails);

        // Tests User with Email and Phone numbers
        assertEquals(captureRequest.getTaggees().get(3).getFname(),
                actualUserContactWithEmailsAndNumbers.get("fname"));
        assertEquals(captureRequest.getTaggees().get(3).getLname(),
                actualUserContactWithEmailsAndNumbers.get("lname"));
        actualPhoneNumbers = gson
                .fromJson(actualUserContactWithEmailsAndNumbers.get("phone_numbers").toString(),
                        listType);
        actualEmails = gson.fromJson(
                actualUserContactWithEmailsAndNumbers.get("email_addresses").toString(), listType);
        assertEquals(captureRequest.getTaggees().get(3).getPhoneNumbers(), actualPhoneNumbers);
        assertEquals(captureRequest.getTaggees().get(3).getEmailAddresses(), actualEmails);
    }

    public void testCaptureRequestFromProvision() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_provision_response);

        ProvisionCaptureRequest request = new ProvisionCaptureRequest();
        ProvisionCapture provisionCapture = request.buildResopnseFromJson(json);

        CaptureRequest actualCapture = new CaptureRequest(provisionCapture);

        assertEquals("delectable-captured-photos", actualCapture.getBucket());
        assertEquals("adam-bednarek-1403535987-cb3008064b60.jpg", actualCapture.getFilename());
    }

    public void testParsedCaptureDetailsFromRequest() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);
        ProvisionCapture provisionCapture = new ProvisionCapture();
        provisionCapture.setBucket("Testbucket");
        provisionCapture.setFilename("testFileName");

        CaptureRequest request = new CaptureRequest(provisionCapture);
        CaptureDetails actualCapture = request.buildResopnseFromJson(json);

        assertEquals("535be3177534906c8b0007d8", actualCapture.getId());
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

    private CaptureRequest getSampleCaptureRequestWithTaggees() {
        CaptureRequest sampleCapture = getSampleCaptureRequestWithoutTaggees();

        TaggeeContact facebookContact = new TaggeeContact();
        facebookContact.setFbId("some FB ID");

        TaggeeContact delectafriendContact = new TaggeeContact();
        delectafriendContact.setId("AccountID");

        TaggeeContact userContact = new TaggeeContact();
        userContact.setFname("John");
        userContact.setLname("Doe");

        TaggeeContact userContactWithEmailsAndPhone = new TaggeeContact();
        ArrayList<String> phoneNumbers = new ArrayList<String>();
        ArrayList<String> emails = new ArrayList<String>();
        phoneNumbers.add("718-555-5555");
        emails.add("someone@abcd.com");
        userContactWithEmailsAndPhone.setFname("Mary");
        userContactWithEmailsAndPhone.setLname("Jane");
        userContactWithEmailsAndPhone.setPhoneNumbers(phoneNumbers);
        userContactWithEmailsAndPhone.setEmailAddresses(emails);

        sampleCapture.addTaggee(facebookContact);
        sampleCapture.addTaggee(delectafriendContact);
        sampleCapture.addTaggee(userContact);
        sampleCapture.addTaggee(userContactWithEmailsAndPhone);

        return sampleCapture;
    }
}
