package com.hitherejoe.sqlbrite.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.hitherejoe.sqlbrite.R;
import com.hitherejoe.sqlbrite.data.model.Person;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

@LayoutId(R.layout.item_person)
public class PersonHolder extends ItemViewHolder<Person> {

    @ViewId(R.id.text_name)
    TextView mNameText;

    public PersonHolder(View view) {
        super(view);
    }

    @Override
    public void onSetValues(Person person, PositionInfo positionInfo) {
        mNameText.setText(person.name);
    }

}