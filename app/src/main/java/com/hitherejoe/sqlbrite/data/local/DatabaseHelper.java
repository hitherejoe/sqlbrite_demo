package com.hitherejoe.sqlbrite.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hitherejoe.sqlbrite.data.model.Person;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqlbrite.SqlBrite.Query;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.content.ContentObservable;
import rx.functions.Func1;

public class DatabaseHelper {

    private DbOpenHelper mDatabaseOpenHelper;
    private SqlBrite mSqlBrite;

    public DatabaseHelper(Context context) {
        mDatabaseOpenHelper = new DbOpenHelper(context);
        mSqlBrite = SqlBrite.create(mDatabaseOpenHelper);
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDatabaseOpenHelper.getWritableDatabase();
    }

    public Observable<Person> getPeople() {
        return mSqlBrite.createQuery(Db.PersonTable.TABLE_NAME, "SELECT * FROM " + Db.PersonTable.TABLE_NAME)
                .flatMap(new Func1<Query, Observable<Person>>() {
                    @Override
                    public Observable<Person> call(Query query) {
                        return ContentObservable.fromCursor(query.run())
                                .map(new Func1<Cursor, Person>() {
                                    @Override
                                    public Person call(Cursor cursor) {
                                        return Db.PersonTable.parseCursor(cursor);
                                    }
                                });
                    }
                });
    }

    public Observable<Void> savePeople(final List<Person> personList) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                SQLiteDatabase db = getWritableDatabase();
                db.beginTransaction();
                try {
                    for (Person person : personList) {
                        db.insertOrThrow(Db.PersonTable.TABLE_NAME, null, Db.PersonTable.toContentValues(person));
                    }
                    db.setTransactionSuccessful();
                    subscriber.onCompleted();
                } finally {
                    db.endTransaction();
                }
            }
        });
    }

    public Observable<Long> savePerson(final Person person) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long rowsUpdated = mSqlBrite.insert(Db.PersonTable.TABLE_NAME, Db.PersonTable.toContentValues(person));
                subscriber.onNext(rowsUpdated);
                subscriber.onCompleted();
            }
        });
    }

}
