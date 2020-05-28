package com.example.testing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "tag";

    Dialog dialog;
    ListView listView;
    String mContactName;
    ArrayAdapter adapter;
    TelephonyManager telephonyManager;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> num = new ArrayList();
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listView = findViewById(R.id.listview);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        num.add("89025162826");
        if (bluetooth.isEnabled()) {
            PhoneStateListener phoneStateListener = new PhoneStateListener() {
                public void onCallStateChanged(int state, String incomingNumber) {
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            dialog = new Dialog(getApplicationContext());
                            dialog.setTitle("Вам звонил:" + mContactName + "Настройте кодовую фразу");
                            dialog.show();
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            Toast.makeText(getApplicationContext(), "Tel:" + incomingNumber, Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };
            telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            contactResult();
        }
        else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, RESULT_CANCELED);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tel = num.get(position);
                adapter = new ArrayAdapter(getApplicationContext(), R.layout.list);
                listView.setAdapter(adapter);
            }
        });
    }

    public void contactResult() {
        Log.i("info", "CR is started");
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String mPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        Log.d(LOG_TAG, "name: " + mContactName);
        Log.d(LOG_TAG, "hasPhone:" + mPhoneNumber);
        while (phones.moveToNext()) {
            Intent forw = new Intent(this, MainActivity.class);
            forw.putExtra(mPhoneNumber, mContactName);
            setResult(RESULT_OK, forw);
        }
        phones.close();
    }

}