package com.hitherejoe.sqlbrite;

import android.app.Application;

import com.hitherejoe.sqlbrite.data.DataManager;

import rx.schedulers.Schedulers;

public class SqlBriteApplication extends Application {

    private static SqlBriteApplication sSqlBriteApplication;
    private DataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sSqlBriteApplication = this;
        mDataManager = new DataManager(this, Schedulers.io());
    }

    @Override
    public void onTerminate() {
        sSqlBriteApplication = null;
        super.onTerminate();
    }

    public static SqlBriteApplication get() {
        return sSqlBriteApplication;
    }

    public DataManager getDataManager() { return mDataManager; }

}
