package com.hitherejoe.sqlbrite.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hitherejoe.sqlbrite.R;
import com.hitherejoe.sqlbrite.SqlBriteApplication;
import com.hitherejoe.sqlbrite.data.DataManager;
import com.hitherejoe.sqlbrite.data.model.Person;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;

public class AddPersonActivity extends BaseActivity {

    @InjectView(R.id.edit_text_name)
    EditText mNameEditText;

    private static final String TAG = "AddPersonActivity";
    private DataManager mDataManager;
    private List<Subscription> mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSubscriptions = new ArrayList<>();
        mDataManager = SqlBriteApplication.get().getDataManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Subscription subscription : mSubscriptions) subscription.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                savePerson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePerson() {
        String name = mNameEditText.getText().toString();
        if (!name.isEmpty()) {
            mSubscriptions.add(AppObservable.bindActivity(this,
                    mDataManager.savePerson(new Person(name)))
                        .subscribeOn(mDataManager.getScheduler())
                        .subscribe(new Subscriber<Person>() {
                            @Override
                            public void onCompleted() {
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "There was a error saving the person " + e);
                            }

                            @Override
                            public void onNext(Person person) { }
                        }));
        } else {
            Toast.makeText(this, getString(R.string.toast_empty_name), Toast.LENGTH_SHORT).show();
        }
    }

}
