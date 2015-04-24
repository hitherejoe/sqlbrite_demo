package com.hitherejoe.sqlbrite.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hitherejoe.sqlbrite.SqlBriteApplication;
import com.hitherejoe.sqlbrite.R;
import com.hitherejoe.sqlbrite.data.DataManager;
import com.hitherejoe.sqlbrite.data.model.Person;
import com.hitherejoe.sqlbrite.ui.adapter.PersonHolder;
import com.hitherejoe.sqlbrite.ui.util.MockModelUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.recycler_people)
    RecyclerView mPersonRecycler;

    private static final String TAG = "MainActivity";
    private DataManager mDataManager;
    private List<Subscription> mSubscriptions;
    private ArrayList<Person> mCurrentPeople;
    private EasyRecyclerAdapter<Person> mEasyRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mCurrentPeople = new ArrayList<>();
        mSubscriptions = new ArrayList<>();
        mDataManager = SqlBriteApplication.get().getDataManager();
        setupRecyclerView();
        getPeople();
        savePeople();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Subscription subscription : mSubscriptions) subscription.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(this, AddPersonActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        mPersonRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEasyRecyclerAdapter = new EasyRecyclerAdapter<>(this, PersonHolder.class, mCurrentPeople);
        mPersonRecycler.setAdapter(mEasyRecyclerAdapter);
    }

    private void savePeople() {
        List<Person> personList = MockModelUtil.createMockPeopleList();
        mSubscriptions.add(AppObservable.bindActivity(this, mDataManager.savePeople(personList))
                .subscribeOn(mDataManager.getScheduler())
                .subscribe(new Subscriber<Person>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "There was an error saving the people..." + e);
                        Toast.makeText(MainActivity.this, "Sorry, there was an error saving the person!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Person person) { }
                }));
    }

    private void getPeople() {
        mSubscriptions.add(AppObservable.bindActivity(this, mDataManager.getPeople())
                .subscribeOn(mDataManager.getScheduler())
                .subscribe(new Subscriber<Person>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "There was an error getting the people " + e);
                    }

                    @Override
                    public void onNext(Person person) {
                        if (!mCurrentPeople.contains(person)) {
                            mCurrentPeople.add(person);
                            mEasyRecyclerAdapter.notifyItemInserted(mCurrentPeople.size());
                        }
                    }
                }));
    }

}
