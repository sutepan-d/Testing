package com.example.testing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
}
