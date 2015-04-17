package com.hitherejoe.sqlbrite.ui.util;

import com.hitherejoe.sqlbrite.data.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MockModelUtil {

    public static List<Person> createMockPeopleList() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Joe Birch"));
        personList.add(new Person("Ivan Carballo"));
        personList.add(new Person("Jason Fry"));
        personList.add(new Person("Jemma Slater"));
        personList.add(new Person("Stefan Pearson"));
        personList.add(new Person("Antony Ribot"));
        personList.add(new Person("Jerome Ribot"));
        return personList;
    }
}
