package com.example.prade.sms;

import android.os.Parcel;
import android.os.Parcelable;


public class Person implements Parcelable {
    private String personName;
    private String personNumber;

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel p) {
            return new Person(p);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    /**
     * Create a Person model object from a Parcel. This
     * function is called via the Parcelable creator.
     *
     * @param p The Parcel used to populate the
     * Model fields.
     */
    public Person(Parcel p) {
        this.personName= p.readString();

        // TODO - fill in here

    }


    public Person(){

    }
    public Person(String name) {
        this.personName = name;

        // TODO - fill in here, please note you must have more arguments here
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(personName);


        // TODO - fill in here
    }

    public String getName(){
        return personName;
    }

    public String getnumber() {
        return personNumber;
    }

    public void setpName(String pName) {
        this.personName = pName;
    }

    public void setpNumber(String pNumber) {
        this.personNumber= pNumber;
    }




    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
