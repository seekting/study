
package com.seekting.study;

import android.content.Intent;
import android.os.Bundle;

import com.ijinshan.browser.qrcode.CaptureActivity;
import com.seekting.study.view.BlockADVView;

/**
 * @author seekting
 */
public class QRCodeActivity extends BaseActivity {

    BlockADVView blockADVView;

    boolean run = true;

    public QRCodeActivity() {
        name = "��ά��ɨ��";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
        finish();
    }
}
