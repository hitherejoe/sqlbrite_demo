package com.hitherejoe.sqlbrite.data.local;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hitherejoe.sqlbrite.data.model.Person;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqlbrite.SqlBrite.Query;

import rx.Observable;
import rx.Subscriber;
import rx.android.content.ContentObservable;
import rx.functions.Func1;

public class DatabaseHelper {

    private SqlBrite mSqlBrite;

    public DatabaseHelper(Context context) {
        mSqlBrite = SqlBrite.create(new DbOpenHelper(context));
    }

    public Observable<Person> getPeople() {
        return mSqlBrite.createQuery(Db.PersonTable.TABLE_NAME, "SELECT * FROM " + Db.PersonTable.TABLE_NAME)
                .flatMap(new Func1<Query, Observable<Cursor>>() {
                    @Override
                    public Observable<Cursor> call(Query query) {
                        return ContentObservable.fromCursor(query.run());
                    }
                }).map(new Func1<Cursor, Person>() {
                    @Override
                    public Person call(Cursor cursor) {
                        Log.d("SIZE", cursor.getCount() + "");
                        return Db.PersonTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Person> savePerson(final Person person) {
        return Observable.create(new Observable.OnSubscribe<Person>() {
            @Override
            public void call(Subscriber<? super Person> subscriber) {
                long result = mSqlBrite.insert(Db.PersonTable.TABLE_NAME, Db.PersonTable.toContentValues(person));
                if (result >= 0) subscriber.onNext(person);
                subscriber.onCompleted();
            }
        });
    }

}
