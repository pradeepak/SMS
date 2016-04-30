package com.example.prade.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;

/**
 * Created by prade on 4/19/2016.
 */
public class SmsListener extends BroadcastReceiver {
private onMessageReceivedListener listener= null;
    private HashSet<String> number = new HashSet<String>();
     private String contactNumber = "+14243247171";
    private static final String TAG = "SMSReceiver";
    DBHelper dbHelper;





    @Override
    public void onReceive(Context context, Intent intent) {

        dbHelper= new  DBHelper(context,null,null,1);
    Bundle bundle =intent.getExtras();
    SmsMessage smsMessages = null;
    String sender ;
    if(bundle!= null){
        Toast.makeText(context,"bundle not null",Toast.LENGTH_SHORT).show();
        number.add(contactNumber);

        try{
            Object[] pdus = (Object[]) bundle.get("pdus");
            ;
            for(int i=0;i<pdus.length;i++){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = bundle.getString("format");
                    Log.v("SMS", format);
                    smsMessages = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    Toast.makeText(context,"Message Received",Toast.LENGTH_LONG).show();

                }
                else
                {
                   smsMessages = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    Toast.makeText(context,"depricated",Toast.LENGTH_SHORT).show();

                }
                sender = smsMessages.getOriginatingAddress();
                String message = smsMessages.getMessageBody();
                Log.v("SMS", sender);
                Toast.makeText  (context,"Message From :" + sender + " content: " + message,Toast.LENGTH_LONG).show();
                Boolean num = dbHelper.checkPhoneNumber(sender);
                if(num) {
                    Toast.makeText(context, "Sender Found in DB", Toast.LENGTH_SHORT).show();



                    Log.v(TAG,number.toString());
                    Intent intent1 = new Intent();
                    intent1.setClassName("com.example.prade.sms", "com.example.prade.sms.processMessage");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    String number = "Hello";
                    intent1.putExtra("senderNumber", sender);
                    context.startActivity(intent1);
                }
                else {
                    Log.v(TAG,number.toString());
                    Toast.makeText(context,"Not Found!" +number.toString(),Toast.LENGTH_LONG).show();

                }





            }

        }
        catch (Exception e){

        }
    }
        else{
        //Toast.makeText(context,"bundle not received",Toast.LENGTH_SHORT).show();
    }
}


    public interface onMessageReceivedListener{
        public void messageReceived(SmsMessage smsMessage);
    }
}
