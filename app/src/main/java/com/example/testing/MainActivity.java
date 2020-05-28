package com.example.testing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TelephonyManager telephonyManager;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    String mPhoneNumber;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (bluetooth.isEnabled()) {
            final Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            PhoneStateListener phoneStateListener = new PhoneStateListener(){
                public void onCallStateChanged(int state, String phoneNumber) {
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            mPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Toast.makeText(getApplicationContext(), "Tel:" + phoneNumber + mPhoneNumber, Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };
            try {
                assert telephonyManager != null;
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }catch (Exception e){
                System.out.println("Not yet implemented");
        }
        }
        else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, RESULT_OK);
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
