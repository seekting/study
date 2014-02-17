
package com.seekting.study.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.seekting.study.R;
import com.seekting.study.view.OverScrollListView.OnOverScrollListener;

public class SectionListView extends FrameLayout {

    private OverScrollListView mListView;
    LinkedHashMap<Section, List<City>> mData;
    private static final int SECTION_TYPE = 0;
    private static final int CITY_TYPE = 1;
    private int mDataSize = 0;
    private SectionListAdapter mSectionListAdapter;

    private TextView mSection;
    private View mSectionLayout;
    SectionFastScroller mFastScroller;

    public SectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mListView = (OverScrollListView) findViewById(R.id.list);
        mSectionLayout = findViewById(R.id.section);
        mSection = (TextView) mSectionLayout.findViewById(R.id.text_item);
        mSectionListAdapter = new SectionListAdapter();
        mListView.setAdapter(mSectionListAdapter);
        mFastScroller = (SectionFastScroller) findViewById(R.id.fast_scroller);
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
                if (totalItemCount == 0) {
                    mSection.setVisibility(View.GONE);
                } else {
                    mSection.setVisibility(View.VISIBLE);
                }
                View v = mSectionListAdapter.findNextSectionView(firstVisibleItem);
                if (v != null) {
                    int top = v.getTop();
                    if (top <= 0) {
                        translateSection(0);
                        TextView first = (TextView) v.findViewById(R.id.text_item);
                        mSection.setText(first.getText());
                    } else {
                        if (top <= mSection.getMeasuredHeight()) {
                            translateSection(top - mSection.getMeasuredHeight());
                        } else {
                            translateSection(0);
                        }
                        Item item = mSectionListAdapter.getItem(firstVisibleItem);
                        if (item instanceof City)
                        {
                            City city = (City) item;
                            mSection.setText(city.getSection());
                        }

                    }
                }
                if (mFastScroller != null) {
                    Item item = mSectionListAdapter.getItem(mListView
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
        if (mSectionLayout != null)
            mSectionLayout.setTranslationY(y);
    }

    public void setData(LinkedHashMap<Section, List<City>> data) {
        if (data != null) {
            mDataSize = data.size();
        } else {
            mDataSize = 0;
        }
        for (Iterator<Section> it = data.keySet().iterator(); it.hasNext();)
        {
            Section key = it.next();
            List<City> value = data.get(key);
            mDataSize += value.size();
        }
        mData = data;
        mSectionListAdapter.notifyDataSetChanged();
        mListView.setSelection(0);

    }

    private class SectionListAdapter extends BaseAdapter {

        HashMap<Integer, View> mViewMap;
        LayoutInflater mLayoutInflater;

        public SectionListAdapter() {
            mViewMap = new HashMap<Integer, View>();
            mLayoutInflater = LayoutInflater.from(getContext());
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
            for (Iterator<Section> it = mData.keySet().iterator(); it.hasNext();)
            {
                Section key = it.next();
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
                        View view = mLayoutInflater.inflate(
                                R.layout.city_select_item, null);
                        convertView = view;

                        break;
                    case SECTION_TYPE:
                        View section = mLayoutInflater.inflate(
                                R.layout.city_select_section_item, null);
                        convertView = section;
                    default:
                        break;
                }
            }
            TextView text = (TextView) convertView.findViewById(R.id.text_item);
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
