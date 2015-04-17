package com.hitherejoe.sqlbrite.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.hitherejoe.sqlbrite.data.model.Person;

public class Db {

    public Db() { }

    public static abstract class PersonTable {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME + " TEXT NOT NULL," +
                        "UNIQUE (" + COLUMN_NAME + ")  ON CONFLICT REPLACE" +
                        " ); ";

        public static ContentValues toContentValues(Person person) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, person.name);
            return values;
        }

        public static Person parseCursor(Cursor cursor) {
            Person person = new Person();
            person.name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            return person;
        }
    }
}
