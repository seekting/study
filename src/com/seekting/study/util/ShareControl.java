
package com.seekting.study.util;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.seekting.study.R;

public class ShareControl {

    public static final int maxWidth = 440;
    public static final int barcode_height = 278;

    public static Bitmap makeBarCodeBitmap(Activity activity, byte[] data) {

        Bitmap srcBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        return makeBarCodeBitmap(activity, srcBitmap);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();

    }

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

        int srcBitmapHeight = srcBitmap.getHeight();
        if (srcBitmap.getWidth() != width) {
            // width height
            // -----= -------
            // srcWidth srcheight
            srcBitmapHeight = (int) ((width*1.0 / srcBitmap.getWidth()) * srcBitmap.getHeight());
        }
        BitmapDrawable barcodeRepatDrawable = (BitmapDrawable) activity.getResources().getDrawable(
                R.drawable.bar_code_repeat_bg);
        int barcodeBgHeight = barcodeRepatDrawable.getBitmap().getHeight();
        if (width < maxWidth) {
            barcodeBgHeight = (int) ((width * 1.0f / maxWidth) * barcodeBgHeight);
        }
        // 左背景
        barcodeRepatDrawable.setBounds(0, 0, width, barcodeBgHeight);

        int height = srcBitmapHeight + barcodeBgHeight;
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        Rect dst=new Rect(0, 0, width, srcBitmapHeight);
        canvas.drawBitmap(srcBitmap, null, dst, paint);;
        canvas.save();
        canvas.translate(0, srcBitmapHeight);
        barcodeRepatDrawable.draw(canvas);
        // 剪刀
        BitmapDrawable scissors = (BitmapDrawable) activity.getResources().getDrawable(
                R.drawable.scissors);
        scissors.setBounds(0, 0, scissors.getBitmap().getWidth(), scissors.getBitmap().getHeight());
        canvas.translate(0, 2 - scissors.getBitmap().getHeight() >> 1);
        scissors.draw(canvas);
        canvas.restore();

        // 二维码
        BitmapDrawable barCodeDrawable = (BitmapDrawable) activity.getResources().getDrawable(
                R.drawable.bar_code);
        int barCodeWidth = barCodeDrawable.getBitmap().getWidth();
        int barCodeHeight = barCodeDrawable.getBitmap().getHeight();
        // barCodeWidth/width=barCodeHeight/height
        int absloatBarCodeHeight = (int) ((width * 1.0f / barCodeWidth) * barCodeHeight);
        barCodeDrawable.setBounds(0, 0, width, absloatBarCodeHeight);

        int dy = ((barcodeBgHeight - absloatBarCodeHeight) >> 1) + srcBitmapHeight;
        canvas.save();
        canvas.translate(0, dy);
        barCodeDrawable.draw(canvas);
        canvas.restore();
        return result;

    }

}
