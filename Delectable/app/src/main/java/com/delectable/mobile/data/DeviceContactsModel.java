package com.delectable.mobile.data;

import com.delectable.mobile.App;
import com.delectable.mobile.api.models.TaggeeContact;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DeviceContactsModel {

    private static final String TAG = DeviceContactsModel.class.getSimpleName();

    private static final String[] CONTACTS_PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
    };

    // Filter Contacts to only show contacts that are meant to be visible
    private static final String CONTACTS_SELECTION =
            ContactsContract.Contacts.IN_VISIBLE_GROUP + " = 1";


    private static final String[] DATA_PROJECTION = new String[]{
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Email._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
    };

    // Filter selection to : mimetype = 'email' or mimetype = 'phonenumber'
    private static final String PHONE_EMAIL_SELECTION =
            ContactsContract.Data.IN_VISIBLE_GROUP + " = 1" + " AND (" +
                    ContactsContract.Data.MIMETYPE + " = " +
                    "'" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'"
                    + " OR " +
                    ContactsContract.Data.MIMETYPE + " = " +
                    "'" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'"
                    + " OR " +
                    ContactsContract.Data.MIMETYPE + " = " +
                    "'" + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "')";

    public DeviceContactsModel() {
    }

    /**
     * Load all Contacts with Phone # and/or Email Addresses
     *
     * Note: Any contact with no real contact info like email / phone # is disregarded / ignored
     */
    public List<TaggeeContact> loadDeviceContactsAsTageeContacts() {
        ArrayList<TaggeeContact> contacts = new ArrayList<TaggeeContact>();

        // Hashmap representing Contact with LookupId
        HashMap<String, TaggeeContact> contactHashMap = new HashMap<String, TaggeeContact>();

        Log.i(TAG, "Loading Contacts");

        /**
         * For Each Visible Contact, lookup Email, Phone # and Structured name
         * Add these details to the Contacts list
         */

        // URI and Query to get Visible Contacts
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = App.getInstance().getContentResolver().query(
                contactsUri,
                CONTACTS_PROJECTION,
                CONTACTS_SELECTION,
                null,
                null);

        if (contactsCursor.getCount() > 0) {
            // Loop Through all Visible Contacts to get Details
            contactsCursor.moveToFirst();
            do {
                String lookupKey = contactsCursor
                        .getString(contactsCursor
                                .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                String displayName = contactsCursor.getString(
                        contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                TaggeeContact contact = new TaggeeContact();
                contact.setEmailAddresses(new ArrayList<String>());
                contact.setPhoneNumbers(new ArrayList<String>());
                contact.setFname(displayName);
                contactHashMap.put(lookupKey, contact);
            } while (contactsCursor.moveToNext());
        }
        contactsCursor.close();

        // Populate Contacts with Data

        // Uri / Query to fetch Emails, Phone #s and Given name for specified Contact
        Uri dataUri = ContactsContract.Data.CONTENT_URI;
        Cursor dataCursor = App.getInstance().getContentResolver().query(
                dataUri,
                DATA_PROJECTION,
                PHONE_EMAIL_SELECTION,
                null,
                null);
        if (dataCursor.getCount() > 0) {
            dataCursor.moveToFirst();

            // Loop through all the results, it's 1 of 3 possible mime types (email, phone, name)
            do {
                String lookupKey = dataCursor
                        .getString(dataCursor
                                .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                String mimeType = dataCursor
                        .getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                // If it's an email, add it to the list of Emails
                if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                    String emailAddress = dataCursor.getString(
                            dataCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Email.ADDRESS));
                    if (contactHashMap.containsKey(lookupKey)) {
                        contactHashMap.get(lookupKey).getEmailAddresses().add(emailAddress);
                    }
                    // If it's a Phone #, add it to the list of Phone #s
                } else if (mimeType
                        .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                    String phoneNumber = dataCursor.getString(
                            dataCursor
                                    .getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (contactHashMap.containsKey(lookupKey)) {
                        contactHashMap.get(lookupKey).getPhoneNumbers().add(phoneNumber);
                    }
                    // Set First/last name
                } else if (mimeType
                        .equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    String givenName = dataCursor.getString(dataCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    String familyName = dataCursor.getString(dataCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    if (contactHashMap.containsKey(lookupKey)) {
                        if (givenName == null && familyName != null) {
                            contactHashMap.get(lookupKey).setFname(familyName);
                        } else if (givenName != null) {
                            contactHashMap.get(lookupKey).setFname(givenName);
                            contactHashMap.get(lookupKey).setLname(familyName);
                        }
                    }
                }
            } while (dataCursor.moveToNext());
        }
        dataCursor.close();

        // Only Add contacts that have contact info, like Email and/or Phone #
        for (TaggeeContact contact : contactHashMap.values()) {
            if (contact.getPhoneNumbers().size() > 0 || contact.getEmailAddresses().size() > 0) {
                contacts.add(contact);
            }
        }

        // Sort contacts
        Collections.sort(contacts, new TaggeeContact.FullNameComparator());
        Log.i(TAG, "Loaded Contacts: " + contacts);
        Log.i(TAG, "Loaded Contacts Size: " + contacts.size());

        return contacts;
    }
}
