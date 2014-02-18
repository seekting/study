
package com.seekting.study;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.seekting.study.view.SectionListView;
import com.seekting.study.view.SectionListView.City;
import com.seekting.study.view.SectionListView.Section;

public class SuperListViewActivity extends BaseActivity {
    public SuperListViewActivity() {
        name = "super ListView";
    }

    private EditText mSearchCityEditext;
    private SectionListView mSectionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.select_city_layout);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(getResources().getDrawable(R.drawable.action_bar_icon));
        actionBar.setTitle("Ñ¡Ôñ³ÇÊÐ");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.kui_address_bar_bg));
        mSearchCityEditext = (EditText) findViewById(R.id.search_city_edt);
        mSearchCityEditext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                change();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        mSectionListView = (SectionListView) findViewById(R.id.super_list_view);
        change();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void change() {
        LinkedHashMap<Section, List<City>> data = new LinkedHashMap<Section, List<City>>();
        for (char i = 'A'; i <= 'Z'; i++) {
            List<City> citys = new ArrayList<SectionListView.City>();
            Section section = new Section();
            section.setName(String.valueOf(i));
            data.put(section, citys);
            for (int j = 0; j < 5 + 60 * Math.random(); j++) {
                City city = new City();
                city.setName(String.valueOf(i) + j);
                city.setSection(String.valueOf(i));
                citys.add(city);
            }
        }
        mSectionListView.setData(data);
    }
}
