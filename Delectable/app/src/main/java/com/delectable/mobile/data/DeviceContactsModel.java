package com.delectable.mobile.data;

import com.delectable.mobile.App;
import com.delectable.mobile.api.models.TaggeeContact;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
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
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
    };

    // Filter selection to : mimetype = 'email' or mimetype = 'phonenumber'
    private static final String PHONE_EMAIL_SELECTION =
            ContactsContract.Data.LOOKUP_KEY + " = ?" + " AND (" +
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

    public List<TaggeeContact> loadDeviceContactsAsTageeContacts() {
        ArrayList<TaggeeContact> contacts = new ArrayList<TaggeeContact>();

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
                TaggeeContact contact = lookupDeviceContact(lookupKey, displayName);
                contacts.add(contact);
            } while (contactsCursor.moveToNext());
        }
        contactsCursor.close();

        Log.i(TAG, "Loaded Contacts: " + contacts);

        return contacts;
    }

    /**
     * Build a TaggeeContact from Device Contact
     *
     * @param lookupKey   - Lookup key to find a contact
     * @param displayName - Default display name if first and last name doesn't exist
     */
    public TaggeeContact lookupDeviceContact(String lookupKey, String displayName) {
        TaggeeContact contact = new TaggeeContact();
        // Emails and Phone Numbers that will be linked to this Contact
        ArrayList<String> emails = new ArrayList<String>();
        ArrayList<String> phoneNumbers = new ArrayList<String>();

        // Uri / Query to fetch Emails, Phone #s and Given name for specified Contact
        Uri dataUri = ContactsContract.Data.CONTENT_URI;
        String[] selectionArgs = new String[]{lookupKey};
        Cursor dataCursor = App.getInstance().getContentResolver().query(
                dataUri,
                DATA_PROJECTION,
                PHONE_EMAIL_SELECTION,
                selectionArgs,
                null);
        if (dataCursor.getCount() > 0) {
            dataCursor.moveToFirst();

            // Loop through all the results, it's 1 of 3 possible mime types (email, phone, name)
            do {
                String mimeType = dataCursor
                        .getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                // If it's an email, add it to the list of Emails
                if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                    String emailAddress = dataCursor.getString(
                            dataCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Email.ADDRESS));
                    emails.add(emailAddress);
                    // If it's a Phone #, add it to the list of Phone #s
                } else if (mimeType
                        .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                    String phoneNumber = dataCursor.getString(
                            dataCursor
                                    .getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumbers.add(phoneNumber);
                    // Set First/last name
                } else if (mimeType
                        .equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    String givenName = dataCursor.getString(dataCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    String familyName = dataCursor.getString(dataCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    contact.setFname(givenName);
                    contact.setLname(familyName);
                }
            } while (dataCursor.moveToNext());
        }
        dataCursor.close();

        // Create the Contact
        // If First and Last names are null, set First name to display name, which is probably the email
        if (contact.getFname() == null && contact.getLname() == null) {
            contact.setFname(displayName);
        }
        contact.setEmailAddresses(emails);
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }

}
