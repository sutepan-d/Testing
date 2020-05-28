package com.example.testing;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PICK_RESULT = 1;

    private static final String LOG_TAG = "tag";

    String mContactId;
    String mPhoneNumber;
    String mContactName;
    TelephonyManager telephonyManager;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (bluetooth.isEnabled()) {
            PhoneStateListener phoneStateListener = new PhoneStateListener(){
                public void onCallStateChanged(int state, String phoneNumber) {
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        Intent forward = new Intent(MainActivity.this, onActivityResult.class);
                        forward.putExtra(phoneNumber, phoneNumber);
                        setResult(RESULT_OK, forward);
                        Toast.makeText(getApplicationContext(), "Tel:" + phoneNumber + mPhoneNumber, Toast.LENGTH_LONG).show();
                    }
                }
            };
            telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, RESULT_OK);
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CONTACT_PICK_RESULT) {
                Uri contactData = data.getData();
                assert contactData != null;
                Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,  ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ mContactId, null, null);
                assert c != null;
                if (c.moveToNext()) {
                    mContactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    mContactName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    Log.d(LOG_TAG, "name: " + mContactName);
                    Log.d(LOG_TAG, "hasPhone:" + hasPhone);
                    Log.d(LOG_TAG, "contactId:" + mContactId);
                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + mContactId, null, null);
                        assert phones != null;
                        while (phones.moveToNext()) {
                            mPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }
                        phones.close();
                    }
                }
            }
        }
    }
}
