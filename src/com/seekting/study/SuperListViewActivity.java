
package com.seekting.study;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.os.Bundle;

import com.seekting.study.view.SuperListView;
import com.seekting.study.view.SuperListView.City;
import com.seekting.study.view.SuperListView.Section;

public class SuperListViewActivity extends BaseActivity {
    public SuperListViewActivity() {
        name = "super ListView";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_list_view);
        SuperListView superListView = (SuperListView) findViewById(R.id.super_list_view);
        LinkedHashMap<Section, List<City>> data = new LinkedHashMap<Section, List<City>>();

        for (char i = 'A'; i <= 'Z'; i++) {
            List<City> citys = new ArrayList<SuperListView.City>();
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

        superListView.setData(data);
    }
}
