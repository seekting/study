
package com.seekting.study.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.seekting.study.R;
import com.seekting.study.util.UITools;
import com.seekting.study.view.OverScrollListView.OnOverScrollListener;

public class SuperListView extends FrameLayout {

    private OverScrollListView mListView;
    LinkedHashMap<Section, List<City>> mData;
    private static final int SECTION_TYPE = 0;
    private static final int CITY_TYPE = 1;
    private int mDataSize = 0;
    private SuperListAdapter mSuperListAdapter;

    private View mSection;
    ListViewFastScroller mFastScroller;

    public SuperListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mListView = (OverScrollListView) findViewById(R.id.list);
        mSection = findViewById(R.id.section);
        mSuperListAdapter = new SuperListAdapter();
        mListView.setAdapter(mSuperListAdapter);
        mFastScroller = (ListViewFastScroller) findViewById(R.id.fast_scroller);
        mFastScroller.setListView(mListView);
        mListView.setOnOverScrollListener(new OnOverScrollListener() {

            @Override
            public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
                if (scrollY < 0) {

                    translateSection(-scrollY);

                }
            }
        });
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                View v = mSuperListAdapter.findNextSectionView(firstVisibleItem);
                if (v != null) {
                    int top = v.getTop();
                    if (top <= 0) {
                        translateSection(0);
                        if (mSection instanceof Button && v instanceof Button) {
                            Button first = (Button) v;
                            Button button = (Button) mSection;
                            button.setText(first.getText());
                        }
                    } else {
                        if (top <= mSection.getMeasuredHeight()) {
                            translateSection(top - mSection.getMeasuredHeight());
                        } else {
                            translateSection(0);
                        }
                        if (mSection instanceof Button) {
                            Item item = mSuperListAdapter.getItem(firstVisibleItem);
                            Button button = (Button) mSection;
                            if (item instanceof City)
                            {
                                City city = (City) item;
                                button.setText(city.getSection());
                            }

                        }
                    }
                }
                if (mFastScroller != null) {
                    Item item = mSuperListAdapter.getItem(mListView
                            .getFirstVisiblePosition());
                    String section = "";
                    if (item instanceof Section) {
                        section = item.getName();
                    } else if (item instanceof City) {
                        City city = (City) item;
                        section = city.getSection();
                    }

                    mFastScroller
                            .onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount,
                                    section);
                }
            }
        });
    }

    private void translateSection(int y) {
        if (mSection != null)
            mSection.setTranslationY(y);
    }

    public void setData(LinkedHashMap<Section, List<City>> data) {
        mData = data;
        if (data != null) {
            mDataSize = data.size();
        } else {
            mDataSize = 0;
        }
        for (Iterator it = data.keySet().iterator(); it.hasNext();)
        {
            Object key = it.next();
            List<City> value = data.get(key);
            mDataSize += value.size();
        }

    }

    private class SuperListAdapter extends BaseAdapter {

        HashMap<Integer, View> mViewMap;

        public SuperListAdapter() {
            mViewMap = new HashMap<Integer, View>();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            return mDataSize;
        }

        @Override
        public Item getItem(int position) {
            int cursor = 0;
            if (mData == null) {
                return null;
            }
            for (Iterator it = mData.keySet().iterator(); it.hasNext();)
            {
                Item key = (Item) it.next();
                if (cursor == position) {
                    return key;
                }
                cursor++;
                List<City> value = mData.get(key);
                if (cursor + value.size() > position) {
                    return value.get(position - cursor);
                }
                cursor += value.size();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View findNextSectionView(int firstVisibleItem) {
            for (int i = firstVisibleItem; i < getCount(); i++) {
                Item item = getItem(i);
                if (item instanceof Section) {
                    return mViewMap.get(i);
                }
            }
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item = getItem(position);
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case CITY_TYPE:
                        TextView text = new TextView(getContext());
                        text.setTextColor(Color.BLUE);
                        convertView = text;
                        text.setTextSize(20);
                        text.setPadding(0, 20, 0, 20);
                        break;
                    case SECTION_TYPE:
                        Button section = new Button(getContext());
                        section.setTextColor(Color.RED);
                        convertView = section;
                    default:
                        break;
                }
            }
            TextView text = (TextView) convertView;
            text.setText(item.getName());
            mViewMap.put(position, convertView);
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            Object type = getItem(position);

            if (type instanceof City) {
                return CITY_TYPE;
            }
            return SECTION_TYPE;
        }

    }

    public static class Item {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Section extends Item {

    }

    public static class City extends Item {
        private String code;
        private String section;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }
    }
}
