package com.example.prade.sms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class processMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_message);

        Intent intent =  getIntent();
        Bundle bundle = intent.getExtras();
        SmsMessage smsMessages = null;
        String sender;
        if (bundle != null) {
            Toast.makeText(getBaseContext(), "bundle not null in processMessage", Toast.LENGTH_SHORT).show();
            String number = bundle.getString("senderNumber");
            Toast.makeText(getBaseContext(), number, Toast.LENGTH_SHORT).show();
            TextView mesageNumber = (TextView) findViewById(R.id.sms_details);
            mesageNumber.setText(number);





        }
    }
}
