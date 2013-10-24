
package com.seekting.study;

import com.seekting.study.util.DeviceHelper;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PhoneModelActivity extends BaseActivity {

    public PhoneModelActivity() {
        name = "手机机型";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String str = "Product Model: " + android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK + "," + android.os.Build.VERSION.SDK
                + android.os.Build.VERSION.RELEASE + Build.MANUFACTURER + Build.HARDWARE;
        TextView textureView = new TextView(this);
        textureView.setText(str);
        setContentView(textureView);
        System.out.println("小米"+DeviceHelper.isXiaoMi());
        System.out.println("MIUI"+DeviceHelper.isMIUI());

        
    }
}
