
package com.seekting.study;

import java.io.File;

import com.seekting.study.util.ShareControl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ShareActivity extends BaseActivity implements OnClickListener {

    public ShareActivity() {
        name = "系统分享";
    }

    Button textShare;
    Button imgShare;

    ImageView share_imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share);
        textShare = (Button) findViewById(R.id.share_text);
        imgShare = (Button) findViewById(R.id.share_img);
        textShare.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        share_imgview = (ImageView) findViewById(R.id.share_imgview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_img);
        byte[] data=ShareControl.Bitmap2Bytes(bitmap);
        
//        Bitmap bm = ShareControl.makeBarCodeBitmap(this, bitmap);
        Bitmap bm = ShareControl.makeBarCodeBitmap(this, data);

        share_imgview.setImageBitmap(bm);
    }

    public static void share(Context context, String activityTitle, String msgTitle,
            String msgText,
            String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpeg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        // msgText="<a href=\"http://www.baidu.com\">百度</a>";

        // intent.putExtra(Intent.EXTRA_TEXT,
        // Html.fromHtml(TextUtils.htmlEncode(msgText)));
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        // intent.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(msgText));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.share_text:
                share(this, "title", "message title", "message text", null);
                break;

            case R.id.share_img: {
                String path = "/sdcard/share_img.jpg";
                share(this, "title", "message title", "message text", path);
                // getResources().getDrawable()

                break;
            }
            default:
                break;
        }

    }

}
