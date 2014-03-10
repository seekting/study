
package com.seekting.study;

import android.os.Bundle;

public class OtherProcessActivity extends BaseActivity {

    public OtherProcessActivity() {
        name = "不在本进程里执行的activity";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.flowlayout_activity);

    }
    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
