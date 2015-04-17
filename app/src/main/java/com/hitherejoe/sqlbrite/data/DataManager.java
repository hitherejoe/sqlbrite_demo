package com.hitherejoe.sqlbrite.data;

import android.content.Context;

import com.hitherejoe.sqlbrite.data.local.DatabaseHelper;
import com.hitherejoe.sqlbrite.data.model.Person;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

public class DataManager {

    private DatabaseHelper mDatabaseHelper;
    private Scheduler mScheduler;

    public DataManager(Context context, Scheduler scheduler) {
        mDatabaseHelper = new DatabaseHelper(context);
        mScheduler = scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        mScheduler = scheduler;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public Scheduler getScheduler() {
        return mScheduler;
    }

    public Observable<Void> savePeople(final List<Person> peopleList) {
        return mDatabaseHelper.savePeople(peopleList);
    }

    public Observable<Long> savePerson(Person person) {
        return mDatabaseHelper.savePerson(person);
    }

    public Observable<List<Person>> getPeople() {
        return mDatabaseHelper.getPeople();
    }
}
