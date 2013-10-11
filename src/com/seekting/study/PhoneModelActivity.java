
package com.seekting.study;

import android.os.Bundle;
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
                + android.os.Build.VERSION.RELEASE;
        TextView textureView = new TextView(this);
        textureView.setText(str);
        setContentView(textureView);
        
    }
}
