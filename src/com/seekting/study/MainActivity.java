
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

    public static final String FRAGMENT = "fragment";

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
        sparseArray.append(7, new DeskTopActivity());
        sparseArray.append(8, new ShareActivity());
        sparseArray.append(9, new VerticalSeekBarActivity());
        sparseArray.append(10, new TranslateProgressDialogActivity());
        BaseActivity fragmentActivity = new BaseActivity();
        fragmentActivity.name = FRAGMENT;
        sparseArray.append(11, fragmentActivity);
        sparseArray.append(12, new FlowLayoutActivity());
        sparseArray.append(13, new NodpiActivity());
        sparseArray.append(14, new SuperListViewActivity());
        sparseArray.append(15, new FormatCityActivity());
        sparseArray.append(16, new MyListViewActivity());
        sparseArray.append(17, new ShortAddressActivity());
        sparseArray.append(18, new SelectAniActivity());
        sparseArray.append(19, new OtherProcessActivity());
        sparseArray.append(20, new QRCodeActivity());
        sparseArray.append(21, new QRCodeResultActivity());
        sparseArray.append(22, new ScaningAnimatorActivity());
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

        if (sparseArray.get(position).name.equals(FRAGMENT)) {
            Intent intent = new Intent(MainActivity.this, FragmentTest.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, sparseArray.get(position).getClass());
            if (sparseArray.get(position) instanceof OtherProcessActivity) {
                startActivityForResult(intent, 1);
            } else {
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode");
    }
}
