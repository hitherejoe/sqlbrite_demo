package com.hitherejoe.sqlbrite.data.local.util;

import android.database.Cursor;

import rx.Observable;
import rx.Subscriber;

public class OnSubscribeCursor implements Observable.OnSubscribe<Cursor> {

    private final Cursor cursor;

    public OnSubscribeCursor(final Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public void call(final Subscriber<? super Cursor> subscriber) {
        try {
            while (!subscriber.isUnsubscribed() && cursor.moveToNext()) {
                subscriber.onNext(cursor);
            }
        } finally {
            if (!cursor.isClosed()) cursor.close();
        }
        subscriber.onCompleted();
    }

}
