
package com.seekting.study;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {

    SparseArray<BaseActivity> sparseArray;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        sparseArray = new SparseArray<BaseActivity>();
        list.setAdapter(new MyAdapter());
        addItems();

        list.setOnItemClickListener(this);
    }

    public void addItems() {
        sparseArray.append(0, new TestAnimatorView());
        sparseArray.append(1, new BigBitmapTestActivity());
        sparseArray.append(2, new WellComeActivity());
        sparseArray.append(3, new RouKongTextActivity());
        sparseArray.append(4, new BlockADVActivity());
        sparseArray.append(5, new PhoneModelActivity());
        sparseArray.append(6, new PopWindowActivity());
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return sparseArray.size();
        }

        @Override
        public Object getItem(int arg0) {
            return sparseArray.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            if (convertView == null) {
                convertView = new TextView(MainActivity.this);
            }
            TextView textView = (TextView) convertView;
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 20, 0, 20);
            textView.setText(sparseArray.get(position).name);
            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

        Intent intent = new Intent(MainActivity.this, sparseArray.get(position).getClass());

        startActivity(intent);
    }
}
