
package com.seekting.study;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class NodpiActivity extends BaseActivity {

    public NodpiActivity() {
        name = "nodpi";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nodpi_layout);
    }

   

}
