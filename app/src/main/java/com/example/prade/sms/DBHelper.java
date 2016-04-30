package com.example.prade.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;
import java.util.List;

/**
 * Created by prade on 4/25/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "SMSReceiver";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewHandshake.db";
    public static final String TABLE_AlERT_LIST = "AlertList";
    public static final String COLUMN_PERSON_NAME = "PersonName";
    public static final String COLUMN_PERSON_ID = "_id";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table " + TABLE_AlERT_LIST + "("
                + COLUMN_PERSON_ID + " integer primary key autoincrement, "
                + COLUMN_PERSON_NAME + " varchar(50), "
                + COLUMN_PHONE_NUMBER + " varchar(15));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP iF TABLE EXISTS " + TABLE_AlERT_LIST);
        onCreate(db);

    }

    public void addTOAllertList(Person person) {
        ContentValues contentValues = new ContentValues();
        String personName = person.getName();
        String phoneNumber = person.getnumber();
        phoneNumber = phoneNumber.replaceAll("\\s+", "");
        phoneNumber = phoneNumber.replaceAll("[-.^:,]", "");


        contentValues.put(COLUMN_PERSON_NAME, personName);
        contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_AlERT_LIST, null, contentValues);
        database.close();

    }


    public Boolean checkPhoneNumber(String pNumber) {
        Boolean Found = false;
        String Name = "";
        String PhoneNumber = "";

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("Select *  from " + TABLE_AlERT_LIST + " where  1;", null);

        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex("PhoneNumber")) != null) {
                    Name = cursor.getString(cursor.getColumnIndex("PersonName"));
                    PhoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
                    if (cursor.getString(cursor.getColumnIndex("PhoneNumber")).equals(pNumber)) {
                        Log.d("Found", Name);
                        PhoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
                        Log.d("Found", PhoneNumber);
                        Found = true;
                    }


                    //Log.d("Found",PhoneNumber);
                }
                cursor.moveToNext();
            }
        } else {
            Found = false;
            Log.d("Found", "No name");
        }
        return Found;
    }

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_AlERT_LIST + " WHERE 1";

        //Cursor point to a location in your result
        Cursor c = db.rawQuery(query, null);
        //Move to first row in your result
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("PhoneNumber")) != null) {
                dbString += c.getString(c.getColumnIndex("PhoneNumber"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public HashSet<String> getData() {
        String PhoneNumber = "";
        HashSet<String> numbers = new HashSet<String>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("Select *  from " + TABLE_AlERT_LIST + " where  1;", null);

        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex("PhoneNumber")) != null) {
                    PhoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
                    numbers.add(PhoneNumber);
                     Log.d("Found", PhoneNumber);

                }
                cursor.moveToNext();
            }

        }

        return numbers;
    }

    public List<Person> getData(List<Person> contactList) {
        String PhoneNumber = "";

        HashSet<String> numbers = new HashSet<String>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("Select *  from " + TABLE_AlERT_LIST + " where  1;", null);

        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex("PhoneNumber")) != null) {
                    Person newPerson = new Person();
                    newPerson.setpNumber(cursor.getString(cursor.getColumnIndex("PhoneNumber")));
                    newPerson.setpName(cursor.getString(cursor.getColumnIndex("PersonName")));
                    contactList.add(newPerson);
                    numbers.add(PhoneNumber);
                    Log.d("Found", newPerson.getName());

                }

                cursor.moveToNext();
            }
           // contactList.add(newPerson);
            for(Person p : contactList){
               // Log.d("List", p.getName());
            }

        }

        return contactList;
    }

    public boolean removeFromAlertList(Person p) {


            SQLiteDatabase database = getReadableDatabase();

            return database.delete(TABLE_AlERT_LIST, COLUMN_PERSON_NAME + "=" + p.getName(), null) > 0;


    }
}
