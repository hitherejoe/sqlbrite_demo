package com.hitherejoe.sqlbrite.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    public String name;

    public Person() { }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    private Person(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

}
