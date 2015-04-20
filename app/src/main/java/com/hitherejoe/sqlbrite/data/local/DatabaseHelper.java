package com.hitherejoe.sqlbrite.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hitherejoe.sqlbrite.data.local.util.OnSubscribeCursor;
import com.hitherejoe.sqlbrite.data.model.Person;
import com.hitherejoe.sqlbrite.ui.util.OperatorWhileDoWhile;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

public class DatabaseHelper {

    private DbOpenHelper mDatabaseOpenHelper;
    private SqlBrite mSqlBrite;

    public DatabaseHelper(Context context) {
        mDatabaseOpenHelper = new DbOpenHelper(context);
        mSqlBrite = SqlBrite.create(mDatabaseOpenHelper);
    }

    public SQLiteDatabase getReadableDatabase() {
        return mDatabaseOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDatabaseOpenHelper.getWritableDatabase();
    }

    public Observable<List<Person>> getPeople() {
        return mSqlBrite.createQuery(Db.PersonTable.TABLE_NAME, "SELECT * FROM " + Db.PersonTable.TABLE_NAME)
                .map(new Func1<SqlBrite.Query, List<Person>>() {
                    @Override
                    public List<Person> call(SqlBrite.Query query) {
                        List<Person> personList = new ArrayList<>();
                        Cursor cursor = query.run();
                        while (cursor.moveToNext()) personList.add(Db.PersonTable.parseCursor(cursor));
                        return personList;
                    }
                });
    }

    public Observable<Person> getAllPeople() {
        return mSqlBrite.createQuery(Db.PersonTable.TABLE_NAME, "SELECT * FROM " + Db.PersonTable.TABLE_NAME)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                })
                .flatMap(new Func1<Cursor, Observable<Person>>() {
                    @Override
                    public Observable<Person> call(Cursor cursor) {
                        return Observable.create(new OnSubscribeCursor(cursor))
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
