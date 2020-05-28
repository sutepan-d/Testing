package com.example.testing;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class onActivityResult extends MainActivity {

    private static final int CONTACT_PICK_RESULT = 1;

    private static final String LOG_TAG = "tag";

    String mContactId;
    String mPhoneNumber;
    String mContactName;

    protected onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CONTACT_PICK_RESULT) {
                Uri contactData = data.getData();
                assert contactData != null;
                Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,  ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ mContactId, null, null);
                assert c != null;
                if (c.moveToNext()) {
                    mContactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    mContactName = c.getString(c.getColumnIndexOrThrow(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    String hasPhone = c.getString(c.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    Log.d(LOG_TAG, "name: " + mContactName);
                    Log.d(LOG_TAG, "hasPhone:" + hasPhone);
                    Log.d(LOG_TAG, "contactId:" + mContactId);
                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + mContactId,
                                null,
                                null);

                        assert phones != null;
                        while (phones.moveToNext()) {
                            mPhoneNumber = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }
                        phones.close();
                    }
                }
            }
        }
    }
}
