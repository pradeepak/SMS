package com.example.prade.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity   {
    private static final String TAG = "SMSReceiver";
    SmsListener smsListener=null;
    static final int Request_Contact = 1;
    private RecyclerView recyclerView;
    AlertList myAlertListAdaptor;

    HashSet<String> number = new HashSet<String>();
    private ArrayList<Person> friends = new ArrayList <Person>();
    List<Person> personData = new ArrayList<>();

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsListener = new SmsListener();
        dbHelper= new  DBHelper(getBaseContext(),null,null,1);
        number = dbHelper.getData();
        for(String n : number){
            Log.d(TAG,n);
        }
        recyclerView = (RecyclerView) findViewById(R.id.alertList);
        personData = dbHelper.getData(personData);
        loadRecycler();

        for(Person q : personData){
           // Log.d(TAG,q.getnumber());
            //Log.d(TAG,q.getName());

        }

        TextView prindDB = (TextView) findViewById(R.id.printDB);
        Button AddContact = (Button) findViewById(R.id.AddContact);
        AddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
                //loadRecycler();
            }
        });
        final Button deleteContact = (Button) findViewById(R.id.RemoveContact);
        deleteContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                excludeContact();
            }
        });



    }
public void loadRecycler(){

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    myAlertListAdaptor = new AlertList(this,personData);

    recyclerView.setAdapter(myAlertListAdaptor);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
}



    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }



    private void pickContact(){

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);//ContactsContract.Contacts.CONTENT_URI
        startActivityForResult(pickContactIntent, Request_Contact);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "request code is: " + requestCode);
        Log.d(TAG, "result code is: " + resultCode);
        if ((resultCode != Activity.RESULT_OK) && (resultCode != Activity.RESULT_FIRST_USER))
            return;

        Log.d(TAG, "request code is: " + requestCode);

        if (requestCode == Request_Contact) {
            Uri contactUri = data.getData(); // gets Uri from Intent object
            Log.d(TAG, contactUri.toString());
            // now you query the ContentProvider with the data you want
            String[] queryFields = new String[]{
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1};    //ContactsContract.Contacts.DISPLAY_NAME

            Cursor c = this.getContentResolver().query(contactUri, queryFields, null, null, null);

            // check to make sure you got the results
            if (c.getCount() == 0) {
                c.close();
                return;
            }

            Person newPerson = new Person();

            final TextView contactList = (TextView) findViewById(R.id.contactList);
            c.moveToFirst();

            String[] temp = c.getColumnNames();


            String person = c.getString(0);
            String pNumber = c.getString(1);

            //checks for duplicate entries......
            if (!number.contains(pNumber)) {
                number.add(pNumber);
                newPerson.setpName(person);
                newPerson.setpNumber(pNumber);
                friends.add(newPerson);
                Log.d(TAG, "The Friend is: " + friends.get(0));
               //contactList.setText(contactList.getText() + person + ", ");
                contactList.setText(newPerson.getName() + " " + newPerson.getnumber());


                dbHelper.addTOAllertList(newPerson);
                personData.add(newPerson);
                myAlertListAdaptor.notifyItemInserted(personData.size() - 1);


            } else {
                Toast.makeText(getBaseContext(), "You have already selected " + person, Toast.LENGTH_LONG).show();
            }

            c.close();
        }
    }
    public void excludeContact(){
        final ArrayList<Integer> check = new ArrayList<Integer>();
        String[] existinglist = new String[personData.size()];
        int i = 0;
        while ( i <personData.size() ){
            existinglist[i] = personData.get(i).getName();
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Friends");

        builder.setMultiChoiceItems(existinglist, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int obj, boolean b) {
                        if (b) {

                            check.add(obj);
                        } else if (!b) {
                            for (int i = 0; i < check.size(); i++) {
                                if (check.get(i) == obj) {
                                    check.remove(i);
                                }
                            }

                        }

                    }
                }

        );

        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

                Collections.sort(check);

                for (int i = check.size() - 1; i >= 0; i--) {
                    Log.d(TAG, "In OKKKKKKKKKKKKKKKKKK " + check.get(i));
                    int exclude = check.get(i);
                    number.remove(personData.get(exclude).getnumber().toString());
                   // personData.remove(exclude);
                    //dbHelper.removeFromAlertList(personData.get(exclude));
                }

                final TextView findFriends = (TextView) findViewById(R.id.contactList);
                String show = "";
                for (int i = 0; i < personData.size(); i++) {
                    show = show + personData.get(i).getName() + ", ";
                }
                findFriends.setText(show);
            }
        });
        builder.show();

    }

    public void printDatabase() {
        String dbString = dbHelper.databaseToString();
        TextView prindDB = (TextView) findViewById(R.id.printDB);
        prindDB.setText(dbString);

    }


}
