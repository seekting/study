
package com.seekting.study.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.seekting.study.R;

public class ShareControl {

    public static final int maxWidth = 440;

    /**
     * 把原图生成带二维码的图
     */
    public static Bitmap makeBarCodeBitmap(Activity activity, Bitmap srcBitmap) {

        if (srcBitmap == null) {
            return null;
        }

        int width = DeviceUtils.getScreenWidth(activity);

        if (width > maxWidth) {
            width = maxWidth;
        }

        Bitmap newBitmap = srcBitmap;
        boolean needRecylenewBitmap = false;
        if (srcBitmap.getWidth() != width) {
            // width height
            // -----= -------
            // srcWidth srcheight
            int dstHeight = (int) ((width / srcBitmap.getWidth()) * srcBitmap.getHeight());
            newBitmap = Bitmap.createScaledBitmap(srcBitmap, width, dstHeight, true);
            needRecylenewBitmap = true;
        }
        // int height=newBitmap.getHeight();

        BitmapDrawable barCodeDrawable = (BitmapDrawable) activity.getResources().getDrawable(
                R.drawable.bar_code);
        int barCodeWidth = barCodeDrawable.getBitmap().getWidth();
        int barCodeHeight = barCodeDrawable.getBitmap().getHeight();
        // barCodeWidth/width=barCodeHeight/height
        int absloatBarCodeHeight = (int)((width*1.0f / barCodeWidth) * barCodeHeight);
        barCodeDrawable.setBounds(0, 0, width, absloatBarCodeHeight);
        BitmapDrawable barCodeBgDrawable = (BitmapDrawable) activity.getResources().getDrawable(
                R.drawable.bar_code_bg);
        barCodeBgDrawable.setBounds(0, 0, width, absloatBarCodeHeight);
        int height = newBitmap.getHeight() + absloatBarCodeHeight;
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        canvas.drawBitmap(newBitmap, 0, 0, paint);
        canvas.save();
        canvas.translate(0, newBitmap.getHeight());
        barCodeBgDrawable.draw(canvas);
        barCodeDrawable.draw(canvas);
        canvas.restore();
        if (needRecylenewBitmap && !newBitmap.isRecycled()) {
            newBitmap.recycle();
        }
        return result;

    }

}
