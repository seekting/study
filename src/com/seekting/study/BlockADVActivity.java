
package com.seekting.study;

import com.seekting.study.view.BlockADVView;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * @author seekting
 */
public class BlockADVActivity extends BaseActivity {

    BlockADVView blockADVView;

    public BlockADVActivity() {
        name = "¹ã¸æÀ¹½ØÐü¸¡´°";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_adv_layout);
        blockADVView = (BlockADVView) findViewById(R.id.block_adv_view);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    int n = (int) (Math.random() * 20);
                    for (int i = 0; i < n; i++) {
                        blockADVView.setAdvCount(i, false);
                        int time = (int) (500 * Math.random());
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // blockADVView.setAdvCount(20, true);
                    blockADVView.finish();
                    SystemClock.sleep(BlockADVView.SHOW_TIME + BlockADVView.ANIMATOR_TIME);
                }

            }
        });
        thread.start();
    }
}
