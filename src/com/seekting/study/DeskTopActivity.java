
package com.seekting.study;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 得加权限
 * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * @author seekting
 *
 */
public class DeskTopActivity extends BaseActivity implements OnClickListener {

    public DeskTopActivity() {
        name = "网页快捷方式";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button b = new Button(this);
        b.setText(name);
        b.setOnClickListener(this);
        setContentView(b);
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse("http://www.baidu.com");
        Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
        addShortcut(icon, name, uri);
    }

    /**
     * 创建桌面快捷方式
     */
    public void addShortcut(final Parcelable icon, final String name, final Uri uri) {

        Intent intentAddShortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // 添加名称

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "名称");

        // 添加图标

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        /* 设置不允许重复创建 */
        intentAddShortcut.putExtra("duplicate", false);

        // 设置Launcher的Uri数据

        Intent intentLauncher = new Intent();

        intentLauncher.setData(uri);

        // 添加快捷方式的启动方法

        intentLauncher.setClassName("com.ijinshan.browser",
                "com.ijinshan.browser.screen.BrowserActivity");
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentLauncher);
        sendBroadcast(intentAddShortcut);

    }
}
