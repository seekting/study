
package com.seekting.study;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * �ü�Ȩ�� <uses-permission
 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * 
 * @author seekting
 */
public class DeskTopActivity extends BaseActivity implements OnClickListener {

    public DeskTopActivity() {  
        name = "��ҳ��ݷ�ʽ";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desk_top_activity);
        Button add=(Button) findViewById(R.id.add);
        Button delete=(Button) findViewById(R.id.delete);
        Button myVideo=(Button)findViewById(R.id.my_video);
        myVideo.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        Myb m = new Myb();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.launcher.action.INSTALL_SHORTCUT");
        registerReceiver(m, intentFilter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add){
        Uri uri = Uri.parse("http://www.baidu.com");
        Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
        if (isShortcutInstalled(name)) {

        } else {
            addShortcut1(icon, name, uri);
        }}
        else if(v.getId()==R.id.delete){
            deleteShortCut();
        }else if(v.getId()==R.id.my_video){
            createOldJinshanShortcut();
        }
    }

    /**
     * ���������ݷ�ʽģ��uc
     */
    public void addShortcut(final Parcelable icon, final String name, final Uri uri) {

        Intent intentAddShortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // �������

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // ���ͼ��

        /* ���ò������ظ����� */
        intentAddShortcut.putExtra("duplicate", false);

        // ����Launcher��Uri����

        Intent intentLauncher = new Intent();

        // intentLauncher.setData(uri);

        // ��ӿ�ݷ�ʽ����������

        // intentLauncher.setClassName("com.ijinshan.browser",
        // "com.ijinshan.browser.screen.BrowserActivity");
        intentLauncher.setAction("com.ijinshan.intent.action.Browser");
        intentLauncher.putExtra("openurl", "www.baidu.com");
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentLauncher);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, b);
        // intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        // icon);
        sendBroadcast(intentAddShortcut);

    }

    /**
     * ���������ݷ�ʽ
     */
    public void addShortcut1(final Parcelable icon, final String name, final Uri uri) {

        Intent intentAddShortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // �������

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // ���ͼ��

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        // intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        // icon);
        // �����������,�ᵼ����Щ�ֻ���ʾͼ�겻ȫ
        // intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, b);
        // �����������������ͼ��
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        /* ���ò������ظ����� */
        intentAddShortcut.putExtra("duplicate", false);

        // ����Launcher��Uri����

        Intent intentLauncher = new Intent();

        intentLauncher.setData(uri);
        // ����������Ա�����Щ�ֻ����ǻ�ͼ���ظ�
//        intentLauncher.setAction(Intent.ACTION_VIEW);
        intentLauncher.setAction("com.ijinshan.intent.action.HOMEPAGE");

        // ��ӿ�ݷ�ʽ����������

        intentLauncher.setClassName("com.ijinshan.browser",
                "com.ijinshan.browser.screen.BrowserActivity");
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentLauncher);
        sendBroadcast(intentAddShortcut);

    }

    public class Myb extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean du = intent.getBooleanExtra("duplicate", true);
            Intent shortCutIntent = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
            String s = shortCutIntent.getStringExtra("??");

            System.out.println("du" + du);

        }

    }

    /**
     * http://blog.csdn.net/cailingyun0129/article/details/8442885
     * ��ȡ�����������������ע�����ڶ������ʱ��δָ��Ĭ������ʱ���÷�������Null,ʹ��ʱ�账����������
     */
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // �ж�����������ڣ���δָ��Ĭ����ʱ��
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }

    // private boolean hasShortcut() {
    //
    // final String AUTHORITY = "com.android.launcher.settings";
    // final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY +
    // "/favorites?notify=true");
    // Cursor c = getContentResolver().query(
    // CONTENT_URI,
    // new String[] {
    // "title"
    // },
    // "title=?",
    // new String[] {
    // "����"
    // },
    // null);
    //
    // Cursor c = getContentResolver().query(CONTENT_URI, new String[] {"title",
    // "iconResource" }, "title=?", new String[]
    // {getString(R.string.app_name).trim()}, null);
    //
    // if (c != null && c.moveToNext()) {
    // return true;
    // }
    // return false;
    // }
    private boolean hasShortcut()
    {
        boolean isInstallShortcut = false;
        final String AUTHORITY = "com.android.launcher.settings";
        // final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY +
        // "/favorites?notify=true");
        final Uri CONTENT_URI = Uri
                .parse("content://com.android.launcher2.settings/favorites?notify=true");
        // Cursor c = getContentResolver().query(CONTENT_URI, new String[]
        // {"title", "iconResource" }, "title=?", new String[] {"����"}, null);
        Cursor c = getContentResolver().query(CONTENT_URI, new String[] {
                "title", "iconResource"
        }, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String title = c.getString(c.getColumnIndex("title"));
                System.out.println("title" + title);

            }
        }

        return isInstallShortcut;
    }

    public boolean isShortcutInstalled(String name) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = this
                .getContentResolver();
        String AUTHORITY = null;
        String packageName = getLauncherPackageName(this);
        if (packageName == null) {
            return false;
        }
        try {

            AUTHORITY = packageName + ".settings";
            Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                    + "/favorites?notify=true");
            Cursor c = cr.query(CONTENT_URI,
                    new String[] {
                            "title", "iconResource"
                    }, "title=?",
                    new String[] {
                        name
                    }, null);

            if (c != null && c.getCount() > 0) {
                isInstallShortcut = true;
                // System.out.println("�Ѵ���");
                Toast.makeText(this, "�Ѿ�����", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // �п��ܻ�û�з���contentproviderȨ��,����Ȩ���쳣
            return false;
        }
        return isInstallShortcut;
    }

    private void deleteShortCut() {
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra("duplicate", false);
        // ��ݷ�ʽ������
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // String appClass = this.getPackageName() + "." +
        // this.getLocalClassName();
        Intent intentLauncher = new Intent();

        intentLauncher.setData(uri);
        // ����������Ա�����Щ�ֻ����ǻ�ͼ���ظ�
//        intentLauncher.setAction(Intent.ACTION_VIEW);
        intentLauncher.setAction("com.ijinshan.intent.action.HOMEPAGE");

        // ��ӿ�ݷ�ʽ����������

        intentLauncher.setClassName("com.ijinshan.browser",
                "com.ijinshan.browser.screen.BrowserActivity");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                intentLauncher);

        sendBroadcast(shortcut);
    }

    private int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }

    public void createOldJinshanShortcut(){
        /**
         * ��Ҫ��2.2.0ɾ��2.2.0�Ŀ�ݷ�ʽ
         */
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

            // ��ݷ�ʽ������
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�ҵ���Ƶ");
            Intent intentLauncher = new Intent();
            intentLauncher.setAction(Intent.ACTION_VIEW);
            intentLauncher.setData(Uri.parse("http://v.m.liebao.cn/?f=android9"));
            intentLauncher.putExtra("from_shortcut", true);
            // ��ӿ�ݷ�ʽ����������
            intentLauncher.setClassName("com.ijinshan.browser", "com.ijinshan.browser.screen.BrowserActivity");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentLauncher);
            sendBroadcast(shortcut);
    }
}
