
package com.seekting.study;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * �ü�Ȩ��
 * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * @author seekting
 *
 */
public class DeskTopActivity extends BaseActivity implements OnClickListener {

    public DeskTopActivity() {
        name = "��ҳ��ݷ�ʽ";
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
     * ���������ݷ�ʽ
     */
    public void addShortcut(final Parcelable icon, final String name, final Uri uri) {

        Intent intentAddShortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // �������

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "����");

        // ���ͼ��

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        /* ���ò������ظ����� */
        intentAddShortcut.putExtra("duplicate", false);

        // ����Launcher��Uri����

        Intent intentLauncher = new Intent();

        intentLauncher.setData(uri);

        // ��ӿ�ݷ�ʽ����������

        intentLauncher.setClassName("com.ijinshan.browser",
                "com.ijinshan.browser.screen.BrowserActivity");
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentLauncher);
        sendBroadcast(intentAddShortcut);

    }
}
