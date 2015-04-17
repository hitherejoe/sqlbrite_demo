package com.hitherejoe.sqlbrite;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import rx.schedulers.Schedulers;

public class BaseTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public BaseTestCase(Class<T> cls) {
        super(cls);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SqlBriteApplication.get().getDataManager().setScheduler(Schedulers.immediate());
    }

}