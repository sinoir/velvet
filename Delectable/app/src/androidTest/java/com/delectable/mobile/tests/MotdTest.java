package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Motd;
import com.delectable.mobile.model.api.MotdResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class MotdTest extends BaseInstrumentationTestCase {

    public void testParseMotdResponse() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_motd);
        MotdResponse response = mGson.fromJson(json.toString(), MotdResponse.class);
        Motd.ClientConfig config = response.getPayload().getClientConfig();

        //monocle
        assertEquals(false, config.isMonocleModeEnabled());

        //sharing
        assertEquals("", config.getSharing().getFbShareCaption());
        assertEquals("Delectable for iPhone", config.getSharing().getFbShareName());
        assertEquals("Save, Share and Discover Wines.",
                config.getSharing().getFbShareDescription());

        //server settings
        assertEquals(80, config.getServerSettings().getContactListBatchSize());
        assertEquals(50, config.getServerSettings().getMaxBatchSize());
        assertEquals(10, config.getServerSettings().getRetryBatchSize());
        assertEquals(3, config.getServerSettings().getMaxRetries());

        //appirater
        assertEquals(3, config.getAppirater().getSignificantEventsUntilRateAppPrompt());
        assertEquals(5, config.getAppirater().getUsesUntilRateAppPrompt());
        assertEquals(0, config.getAppirater().getDaysUntilRateAppPrompt());
        assertEquals(3, config.getAppirater().getDaysUntilRemindingToRate());

        //sms test
        assertEquals("join me on Delectable to find cool wines! delectable.com/invite",
                config.getSmsTest().getSmsBody());
        assertEquals(2, config.getSmsTest().getMaxNumberOfGroupMessagesToSend());
        assertEquals(5, config.getSmsTest().getMaxNumberOfMessagesInGroup());
        assertEquals(0, config.getSmsTest().getMaxNumberOfOverflowSmsInvites());
        assertEquals("Join me on Delectable", config.getSmsTest().getEmailSubject());
        assertEquals(
                "Hi!<br><br>I just joined Delectable. Follow me so we can remember and discover wine together!<br><br>Here's a link to download the app to your iPhone: <a href=\"http://del.ec/invite_email\">del.ec/invite_email</a>",
                config.getSmsTest().getEmailContents());

        //capture settings
        assertEquals(
                "Identification usually takes 1-2 minutes, but we're a little swamped at the moment!\n\nYour wine will be identified shortly - thanks for your patience.",
                config.getCaptureString());

    }

}
