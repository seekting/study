
package com.seekting.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seekting.study.view.ExpandAniLinearLayout;
import com.seekting.study.view.ExpandAniLinearLayout.onItemSelectListener;

public class SelectAniActivity extends BaseActivity implements OnClickListener,
        onItemSelectListener {
    public SelectAniActivity() {
        name = "一个选择动画";
    }

    Button button;
    ExpandAniLinearLayout mSelectLayout = null;
    ViewGroup mRoot;

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_ani_layout);
        mRoot = (ViewGroup) findViewById(R.id.layout);

        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(this);
        inflater = LayoutInflater.from(this);
    }

    @Override
    public void onClick(View v) {

        if (button == v) {
            if (mSelectLayout == null || mRoot.indexOfChild(mSelectLayout) <= -1) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0);
                params.weight = 1;
                params.topMargin = 20;
                mSelectLayout = (ExpandAniLinearLayout) inflater.inflate(
                        R.layout.search_engine_ani_container,
                        null);

                mRoot.addView(mSelectLayout, params);

                mSelectLayout.setItemBgRes(R.drawable.search_engine_item_selector);
                mSelectLayout.addItem(R.drawable.search_engine_baidu, 0);
                mSelectLayout.addItem(R.drawable.search_engine_google, 0);
                mSelectLayout.addItem(R.drawable.search_engine_novel, 0);
                mSelectLayout.addItem(R.drawable.search_engine_video, 0);
                mSelectLayout.addItem(R.drawable.search_engine_taobao, 0);
                mSelectLayout.setSelectPosition(3);
                mSelectLayout.startExpand();
                mSelectLayout.setOnItemSelectListener(this);
            }
            else {
                mRoot.removeView(mSelectLayout);
            }
        }
    }

    @Override
    public void onSelect(int position) {

        mRoot.removeView(mSelectLayout);
        Toast.makeText(this, "position:" + position, 500).show();
    }
}
