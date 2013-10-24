
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
 * 得加权限 <uses-permission
 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * 
 * @author seekting
 */
public class DeskTopActivity extends BaseActivity implements OnClickListener {

    public DeskTopActivity() {  
        name = "网页快捷方式";
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
     * 创建桌面快捷方式模拟uc
     */
    public void addShortcut(final Parcelable icon, final String name, final Uri uri) {

        Intent intentAddShortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // 添加名称

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 添加图标

        /* 设置不允许重复创建 */
        intentAddShortcut.putExtra("duplicate", false);

        // 设置Launcher的Uri数据

        Intent intentLauncher = new Intent();

        // intentLauncher.setData(uri);

        // 添加快捷方式的启动方法

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
     * 创建桌面快捷方式
     */
    public void addShortcut1(final Parcelable icon, final String name, final Uri uri) {

        Intent intentAddShortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // 添加名称

        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 添加图标

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        // intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        // icon);
        // 这个方法不好,会导致有些手机显示图标不全
        // intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, b);
        // 建议用这个方法生成图标
        intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        /* 设置不允许重复创建 */
        intentAddShortcut.putExtra("duplicate", false);

        // 设置Launcher的Uri数据

        Intent intentLauncher = new Intent();

        intentLauncher.setData(uri);
        // 加上这个可以避免有些手机还是会图标重复
//        intentLauncher.setAction(Intent.ACTION_VIEW);
        intentLauncher.setAction("com.ijinshan.intent.action.HOMEPAGE");

        // 添加快捷方式的启动方法

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
     * 获取正在运行桌面包名（注：存在多个桌面时且未指定默认桌面时，该方法返回Null,使用时需处理这个情况）
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
            // 有多个桌面程序存在，且未指定默认项时；
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
    // "名称"
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
        // {"title", "iconResource" }, "title=?", new String[] {"名称"}, null);
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
                // System.out.println("已创建");
                Toast.makeText(this, "已经有了", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // 有可能会没有访问contentprovider权限,会抛权限异常
            return false;
        }
        return isInstallShortcut;
    }

    private void deleteShortCut() {
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra("duplicate", false);
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // String appClass = this.getPackageName() + "." +
        // this.getLocalClassName();
        Intent intentLauncher = new Intent();

        intentLauncher.setData(uri);
        // 加上这个可以避免有些手机还是会图标重复
//        intentLauncher.setAction(Intent.ACTION_VIEW);
        intentLauncher.setAction("com.ijinshan.intent.action.HOMEPAGE");

        // 添加快捷方式的启动方法

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
         * 主要是2.2.0删除2.2.0的快捷方式
         */
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

            // 快捷方式的名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "我的视频");
            Intent intentLauncher = new Intent();
            intentLauncher.setAction(Intent.ACTION_VIEW);
            intentLauncher.setData(Uri.parse("http://v.m.liebao.cn/?f=android9"));
            intentLauncher.putExtra("from_shortcut", true);
            // 添加快捷方式的启动方法
            intentLauncher.setClassName("com.ijinshan.browser", "com.ijinshan.browser.screen.BrowserActivity");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentLauncher);
            sendBroadcast(shortcut);
    }
}
