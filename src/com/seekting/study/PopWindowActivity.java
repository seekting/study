
package com.seekting.study;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopWindowActivity extends BaseActivity implements OnClickListener {
    public PopWindowActivity() {
        name = "PopWindow";
    }

    TextView test;
    ViewGroup parent;
    ViewGroup parent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window);
        test = (TextView) findViewById(R.id.test);
        test.setOnClickListener(this);
        parent = (ViewGroup) findViewById(R.id.parent);
        parent1 = (ViewGroup) findViewById(R.id.parent1);

    }

    @Override
    public void onClick(View v) {
        if (v == test) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View pop = inflater.inflate(R.layout.fav_icon_popup_window, null);
            PopupWindow mPopupWindow = new PopupWindow(pop, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.fav_popup_bg));

            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.showAtLocation(parent1, Gravity.LEFT | Gravity.TOP, 100, 500);
        }
    }
}
