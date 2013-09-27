
package com.seekting.study;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BigBitmapTestActivity extends BaseActivity {

    public BigBitmapTestActivity() {
        name = "大图变小图的技巧";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.big_bitmap_layout);
    }

    public void notUseOptions(View v) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bm, 100, 100, true);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(newBitmap);
        setContentView(imageView);
    }

    public void useOptions(View v) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.test, options);
        int height = options.outHeight;
        int width = options.outWidth;
        String imageType = options.outMimeType;

        options.inSampleSize = 8;
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test, options);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(bm);
        setContentView(imageView);
    }

}
