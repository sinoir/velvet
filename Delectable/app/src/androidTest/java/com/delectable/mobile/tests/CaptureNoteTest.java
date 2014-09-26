package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.captures.CaptureNotesResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class CaptureNoteTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseListingFirstQueryCaptureNote() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_notes_listing_first_query);

        CaptureNotesResponse response = mGson.fromJson(json.toString(), CaptureNotesResponse.class);
        ListingResponse<CaptureNote> actualListing = response.getPayload();

        assertNull(actualListing.getBoundariesFromBefore());
        assertNull(actualListing.getBoundariesFromAfter());
        assertNull(actualListing.getBoundariesFromSince());

        assertEquals("52be375864264eb2690002a0", actualListing.getBoundariesToBefore());
        assertEquals("535be3177534906c8b0007d8", actualListing.getBoundariesToAfter());
        assertEquals("1401289559.3703167", actualListing.getBoundariesToSince());
        assertEquals(0, actualListing.getBefore().size());
        assertEquals(0, actualListing.getAfter().size());
        assertEquals(10, actualListing.getUpdates().size());

        assertEquals(0, actualListing.getDeletes().size());
        assertTrue(actualListing.getMore());

        assertEquals("1401289559.3703167", actualListing.getETag());
        assertEquals("note", actualListing.getContext());

        CaptureNote actualFirstUpdateNote = actualListing.getUpdates().get(0);

        assertEquals("535be3177534906c8b0007d8", actualFirstUpdateNote.getId());
        assertEquals(1398530839.6019998, actualFirstUpdateNote.getCreatedAt());
        assertEquals(false, actualFirstUpdateNote.getPrivate().booleanValue());
        assertEquals("Hard black berry. Simple acid. $25", actualFirstUpdateNote.getNote());
        assertEquals(19, actualFirstUpdateNote.getCapturerRating().intValue());
        assertEquals("2012", actualFirstUpdateNote.getVintage());
        assertEquals(0, actualFirstUpdateNote.getHelpfulingAccountIds().size());
        assertEquals("note", actualFirstUpdateNote.getContext());
        assertEquals("q1hg15NVF3vasA", actualFirstUpdateNote.getETag());

        Account actualParticipantAccount = actualFirstUpdateNote.getCapturerParticipant();

        assertEquals("52069ff93166785b5d003576", actualParticipantAccount.getId());
        assertEquals("Austin", actualParticipantAccount.getFname());
        assertEquals("Beeman", actualParticipantAccount.getLname());
        assertEquals("http://graph.facebook.com/542133246/picture?width=200",
                actualParticipantAccount.getPhoto().getUrl());
        assertEquals(false, actualParticipantAccount.isInfluencer());
        assertEquals(1, actualParticipantAccount.getInfluencerTitles().size());
        assertEquals("", actualParticipantAccount.getInfluencerTitles().get(0));
        assertEquals("Q9Jt95E_VkIKAQ", actualParticipantAccount.getETag());
    }
}
