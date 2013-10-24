
package com.seekting.study;

import com.seekting.study.view.BrightValueSeekView;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VerticalSeekBarActivity extends BaseActivity implements OnClickListener {

    Button show;
    Button showCycle;
    Button hideCycle;
    BrightValueSeekView bright_value_seek_view;

    Thread showThread;
    Thread hideThread;

    public VerticalSeekBarActivity() {
        name = "×ÝÏòÐü¸¡seekbar";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_seekbar);

        show = (Button) findViewById(R.id.show);
        showCycle = (Button) findViewById(R.id.show_cycle);
        hideCycle = (Button) findViewById(R.id.hide_cycle);
        showCycle.setOnClickListener(this);
        show.setOnClickListener(this);
        hideCycle.setOnClickListener(this);
        bright_value_seek_view = (BrightValueSeekView) findViewById(R.id.bright_value_seek_view);

    }

    @Override
    public void onClick(View v) {

        if (v == show) {
            bright_value_seek_view.show(true);
        } else if (showCycle == v) {
            showThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!VerticalSeekBarActivity.this.isFinishing()) {
                        bright_value_seek_view.show(true);
                        long time = (long) (5000 * Math.random());
                        SystemClock.sleep(time);
                    }

                }
            });
            showThread.start();
        } else if (v == hideCycle) {
            hideThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!VerticalSeekBarActivity.this.isFinishing()) {
                        bright_value_seek_view.hide(true);
                        long time = (long) (5000 * Math.random());
                        SystemClock.sleep(time);
                    }

                }
            });
            hideThread.start();
        }
    }

}
