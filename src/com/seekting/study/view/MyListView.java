
package com.seekting.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
    }

}
