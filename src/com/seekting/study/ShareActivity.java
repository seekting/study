
package com.seekting.study;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShareActivity extends BaseActivity implements OnClickListener {

    public ShareActivity() {
        name = "系统分享";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button b = new Button(this);
        b.setText(name);
        b.setOnClickListener(this);
        setContentView(b);
    }
    public static void share(Context context,String activityTitle, String msgTitle,
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
//        msgText="<a href=\"http://www.baidu.com\">百度</a>";
        
        
//        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(TextUtils.htmlEncode(msgText)));
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
//        intent.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(msgText));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }

    @Override
    public void onClick(View v) {

        share(this, "title", "message title", "message text", null);
    }

}
