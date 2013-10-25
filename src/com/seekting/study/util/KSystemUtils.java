/**
 * @brief     Package com.ijinshan.browser.utils
 * @author    zhouchenguang
 * @since     1.0.0.0
 * @version   1.0.0.0
 * @date      2012-12-23
 */
package com.seekting.study.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ijinshan.browser.R;
import com.ijinshan.browser.entity.Info;
import com.ijinshan.browser.env.AppEnv;
import com.ijinshan.browser.screen.BrowserActivity;
import com.ijinshan.browser.sync.Base64;
import com.ijinshan.browser.sync.Base64Encoder;

import android.os.Build.VERSION;
import android.os.storage.StorageManager;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;

/** 
 * @file      KSystemUtils.java
 * @brief     This file is part of the Utils module of KBrowser project. \n
 *            This file serves as "java" source file that presents global 
 *            system definitions that would be required by all of
 *            the modules. \n
 *
 * @author    zhouchenguang
 * @since     1.0.0.0
 * @version   1.0.0.0
 * @date      2012-12-23
 *
 * \if TOSPLATFORM_CONFIDENTIAL_PROPRIETARY
 * ============================================================================\n
 *\n
 *           Copyright (c) 2012 zhouchenguang.  All Rights Reserved.\n
 *\n
 * ============================================================================\n
 *\n
 *                              Update History\n
 *\n
 * Author (Name[WorkID]) | Modification | Tracked Id | Description\n
 * --------------------- | ------------ | ---------- | ------------------------\n
 * zhouchenguang[7897]   |  2012-12-23  | <xxxxxxxx> | Initial Created.\n
 *\n
 * \endif
 *
 * <tt>
 *\n
 * Release History:\n
 *\n
 * Author (Name[WorkID]) | ModifyDate | Version | Description \n
 * --------------------- | ---------- | ------- | -----------------------------\n
 * zhouchenguang[7897]   | 2012-12-23 | 1.0.0.0 | Initial created. \n
 *\n
 * </tt>
 */
//=============================================================================
//                                  IMPORT PACKAGES
//=============================================================================

//=============================================================================
//                                 CLASS DEFINITIONS
//=============================================================================
/**
 * @class     KSystemUtils.java
 * @brief     Class that defines all system utils. \n
 *
 * @author    zhouchenguang
 * @since     1.0.0.0
 * @version   1.0.0.0
 * @date      2012-12-23
 * @par       Applied:  External
 */
public class KSystemUtils {

	//-------------------------------------------------------------------------
    // Public static members
    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    // Private static members
    //-------------------------------------------------------------------------
    /**
     * TAG
     * */
    private static String TAG = "KSystemUtils";
	/**
	 * @brief      Screen width in pixels. \n
	 */
	private static int SCREEN_WIDTH = 480;
	/**
	 * @brief      Screen height in pixels. \n
	 */
	private static int SCREEN_HEIGHT = 800;
	/**
	 * @brief      Smallest screen width in DP. \n
	 */
	private static int SMALLEST_SCREEN_WIDTHWDP = 320;
	/**
	 * @brief      Font scale. \n
	 */
	private static float FONT_SCALE = 1.0F;
	
    //-------------------------------------------------------------------------
    // Public static member methods
    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    /**
     * @brief      Initialize system settings.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     None
     *
     * \par Important Notes:
     * - Notes:  It will initialize the global variable SCREEN_WIDTH and
     *           SCREEN_HEIGHT. The values are assigned as device in vertical
     *           orientation. \n    
     * - Notes:  It should be performed when the first activity is running. \n  
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static void initSysSettings(Activity activity) {
        DisplayMetrics  dm = new DisplayMetrics();  
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
		if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        SCREEN_WIDTH = dm.heightPixels;
	        SCREEN_HEIGHT = dm.widthPixels;
		} else {
	        SCREEN_WIDTH = dm.widthPixels;
	        SCREEN_HEIGHT = dm.heightPixels;
		}
		SMALLEST_SCREEN_WIDTHWDP = activity.getResources().getConfiguration().smallestScreenWidthDp;
		FONT_SCALE = activity.getResources().getConfiguration().fontScale;
    }
    
    /**
     * 
     */
    
    public static int getCompositorWidth(){
    	return (int)(SCREEN_WIDTH * 0.87);
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Get screen width.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     Screen width in pixels. \n
     *
     * \par Important Notes:
     * - Notes:  It will return current screen width in pixels with assuming 
     *           current device is placed in vertical orientation. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static int getScreenWidth() {
	    return SCREEN_WIDTH;
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Get screen height.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     Screen height in pixels. \n
     *
     * \par Important Notes:
     * - Notes:  It will return current screen height in pixels with assuming 
     *           current device is placed in vertical orientation. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static int getScreenHeight() {
	    return SCREEN_HEIGHT;
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Get Smallest screen width in DP.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     Smallest screen width in DP. \n
     *
     * \par Important Notes:
     * - Notes:     \n
     *
     * @author     huangzongming
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static int getSmallestScreenWidthDP() {
	    return SMALLEST_SCREEN_WIDTHWDP;
    }
 
    //-------------------------------------------------------------------------
    /**
     * @brief      Get font scale.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     font scale. \n
     *
     * \par Important Notes:
     * - Notes:     \n
     *
     * @author     huangzongming
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static float getFontScale() {
	    return FONT_SCALE;
    }

    //-------------------------------------------------------------------------
    /**
     * @brief      Get android sdk version of current mobile.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     sdk version in integer. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Get if android sdk of current mobile is newer than 4.1.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     true for current sdk is newer than 4.0. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static boolean isSdkAfterJellyBean() {
      return (getSdkVersion() > 15);
    }
  
    //-------------------------------------------------------------------------
    /**
     * @brief      Get internal sdcard path.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     path for internal sdcard. \n
     *
     * @author     huangzongming
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static String getInternalSdcardPath(Context context) {
    	String path = null;
    	if(getSdkVersion() >= 14) {
    		StorageManager mStorageManager = null; 
    		mStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
            String[] storagePathList = mStorageManager.getVolumePaths();
            if (storagePathList != null) {
            	if (storagePathList.length >= 1) {
            		if(checkSDCardMount(context,storagePathList[0]))
            			path = storagePathList[0];
            	}                    
            }
         }else {
         //for lower than android 4.0 , still using /mnt/sdcard
         path = Environment.getExternalStorageDirectory().getAbsolutePath();
      }
      return path;
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Get external sdcard path.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     path for external sdcard. \n
     *
     * @author     huangzongming
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static String getExternalSdcardPath(Context context) {
    	String path = null;
    	if(getSdkVersion() >= 14) {
    		StorageManager mStorageManager = null; 
            mStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
            String[] storagePathList = mStorageManager.getVolumePaths();
            if (storagePathList != null) {
            	if (storagePathList.length >= 2) {
            		if(checkSDCardMount(context,storagePathList[1]))
            			path = storagePathList[1];            	 
            	}
            } 
        }else {
        	//for lower than android 4.0 , still using /mnt/sdcard
        	path = Environment.getExternalStorageDirectory().getAbsolutePath();
      }
      return path;
    }

    //-------------------------------------------------------------------------
    /**
     * @brief      Check sdcard whether mounted.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     true if sdcard been mounted \n
     *
     * @author     huangzongming
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static boolean checkSDCardMount(Context context,String mountPoint) {
      if(mountPoint == null){ 
          return false; 
      } 
      if(getSdkVersion() >= 14) {
    	  String state = null;
    	  StorageManager mStorageManager = null; 
    	  mStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
    	  try {
    		  state = mStorageManager.getVolumeState(mountPoint); 
    	  } catch(IllegalArgumentException e) {
    		  return false;
    	  }
    	  return Environment.MEDIA_MOUNTED.equals(state); 
      }else {
    	  return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
      }
      
    }

    //-------------------------------------------------------------------------
    /**
     * @brief      Get if android sdk of current mobile is newer than 4.1.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @return     None. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static void setTextToClipBoard(String text, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService("clipboard");
        cmb.setText(text);
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Send string to others. Such as share page Url with others 
     *             via Message or Email, etc.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] context             Context to launch chooser activity. \n
     * @param [IN] str                 The string which user want to share. \n 
     * 
     * @return     None. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static void sendString(Context context, String str) {
        Intent send = new Intent("android.intent.action.SEND_STRING");
        send.putExtra(Intent.EXTRA_TEXT, str);
        
        try {
            context.startActivity(Intent.createChooser(send,
                    context.getText(R.string.sendText)));
        } catch(android.content.ActivityNotFoundException e) {
            KLog.e("sendString", "not activity handle android.intent.action.SEND_STRING", e);
        }
    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for add telephone number to contacts.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] telNumber           telephone number to be handled. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doAddTelContact(String telNumber) {
//        Intent newIntent = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
//        newIntent.putExtra(Intents.Insert.PHONE, telNumber);
//        return newIntent;
//    }

    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for add telephone number to contacts.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] telNumber           telephone number to be handled. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doAddEmaliToExistContact(String email) {
//        Intent newIntent = new Intent(Intent.ACTION_INSERT_OR_EDIT, Contacts.CONTENT_URI);
//        newIntent.setType(Contacts.CONTENT_ITEM_TYPE);
//        newIntent.putExtra(Intents.Insert.EMAIL, email);
//        return newIntent;
//    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for add email to contacts.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] email               email address to be handled. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doAddEmailToContact(String email) {
//        Intent newIntent = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
//        newIntent.putExtra(Intents.Insert.EMAIL, email);
//        return newIntent;
//    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for add telephone number to exist contact.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] telNumber           telephone number to be handled. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doAddTelToExistContact(String telNumber) {
//        Intent newIntent = new Intent(Intent.ACTION_INSERT_OR_EDIT, Contacts.CONTENT_URI);
//        newIntent.setType(Contacts.CONTENT_ITEM_TYPE);
//        newIntent.putExtra(Intents.Insert.PHONE, telNumber);
//        return newIntent;
//    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for sent email.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] email               email address to be sent to. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doEmail(String email) {
//        Uri uri = Uri.parse("mailto:" + email);
//        return new Intent(Intent.ACTION_SENDTO, uri);
//    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for make a call.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] telNumber           telephone number to be called. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doCall(String telNumber) {
//        Uri uri = Uri.parse("tel:" + telNumber);
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(uri);
//        return intent;
//    }
    
    //-------------------------------------------------------------------------
    /**
     * @brief      Create intent for send sms.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] telNumber           telephone number to be sent to. \n
     * 
     * @return     Intent. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
//    public static Intent doSendSms(String telNumber) {
//        Uri uri = Uri.parse("smsto:" + telNumber);
//        return new Intent(Intent.ACTION_SENDTO, uri);
//    }

    
    //-------------------------------------------------------------------------
    /**
     * @brief      Handle utf8 string that has been invalid handled by android.
     *
     * @par        Sync (or) Async:
     * This is a Synchronous function.
     * 
     * @param [IN] String              source string. \n
     * 
     * @return     String. \n
     *
     * @author     zhouchenguang
     * @since      1.0.0.0
     * @version    1.0.0.0
     * @par        Prospective Clients:
     * External Classes
    */
    public static String recoverString(String srcString,boolean Gb2312) {
    	if (srcString == null) {
    		return null;
    	}
    	
    	if(srcString.toLowerCase().indexOf("=?utf8?b?")>0 && srcString.indexOf("?=") > 0)
    	{
    		int pos = srcString.toLowerCase().indexOf("=?utf8?b?");
    		int pos2= srcString.indexOf("?=");
    		if(pos + 9 >= pos2)
    			return srcString;
    		
    		String str=srcString.substring(pos + 9,pos2);
    		byte[] tmp=Base64.decode(str.getBytes());
    		try{
	    		str=new String(tmp,"UTF-8");
	    		str=srcString.substring(0,pos)+str+srcString.substring(pos2+2);
	    		return str;
    		}catch(Exception ex){
    			return srcString;
    		}		
    	}
    	else if(srcString.toLowerCase().indexOf("=?utf8?q?")>0 && srcString.indexOf("?=") > 0)     //add by caisenchuan
        {
            int pos = srcString.toLowerCase().indexOf("=?utf8?q?");
            int pos2= srcString.indexOf("?=");
            if(pos + 9 >= pos2)
                return srcString;
            
            String str=srcString.substring(pos + 9,pos2);
            //byte[] tmp=Base64.decode(str.getBytes());
            try{
                //str=new String(tmp,"UTF-8");
                str = QuotedPrintable.decode(str.getBytes(), "UTF-8");
                str = srcString.substring(0,pos)+str+srcString.substring(pos2+2);
                return str;
            }catch(Exception ex){
                return srcString;
            }
        }
    	else if(srcString.toLowerCase().indexOf("=?gb2312?b?")>0 && srcString.indexOf("?=") > 0)
    	{
    		int pos = srcString.toLowerCase().indexOf("=?gb2312?b?");
    		int pos2= srcString.indexOf("?=");
    		if(pos + 11 >= pos2)
    			return srcString;
    		
    		String str=srcString.substring(pos + 11,pos2);
    		byte[] tmp=Base64.decode(str.getBytes());
    		try{
	    		str=new String(tmp,"GB2312");
	    		str=srcString.substring(0,pos)+str+srcString.substring(pos2+2);
	    		return str;
    		}catch(Exception ex){
    			return srcString;
    		}		
    	}
    	else if(srcString.toLowerCase().indexOf("=?gb2312?q?")>0 && srcString.indexOf("?=") > 0)       //从来没有测试�?�?, add by caisenchuan
        {
            int pos = srcString.toLowerCase().indexOf("=?gb2312?q?");
            int pos2= srcString.indexOf("?=");
            if(pos + 11 >= pos2)
                return srcString;
            
            String str=srcString.substring(pos + 11,pos2);
            //byte[] tmp=Base64.decode(str.getBytes());
            try{
                //str=new String(tmp,"GB2312");
                str = QuotedPrintable.decode(str.getBytes(), "GB2312");
                str=srcString.substring(0,pos)+str+srcString.substring(pos2+2);
                return str;
            }catch(Exception ex){
                return srcString;
            }       
        }
    	
        byte[] b = srcString.getBytes();
        
        if (b == null || b.length < 1) {
        	return srcString;
        }
        
        byte[] c = new byte[b.length];
        int pos = 0;
        for (int i = 0; i < b.length; i++) {
        	if (b[i] >= 0) {
        		c[pos] = b[i];
        		pos++;
        	} else if ((i + 1) < b.length) {
        		c[pos] = (byte)((b[i] & 0x03) << 6 | (b[i + 1] & 0x3f));
        		pos++;
        		i++;
        	}
        }
        byte[] d = new byte[pos];
        for (int i = 0; i < pos; i++) {
        	d[i] = c[i];
        }
        String result;
        try {
        	if(Gb2312)
        		result = new String(d, "GB2312");
        	else
        		result = new String(d, "UTF-8");
        }
        catch (Exception e) {
        	KLog.e(TAG, "Exception", e);
        	result = "";
        }
        return result;
    }

    public static ArrayList<ResolveInfo> getHasDefValueBrowser(Context context){
        ArrayList<ResolveInfo> list_browsers = new ArrayList<ResolveInfo>();
        PackageManager packageManager = context.getPackageManager();
        
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(AppEnv.LIEBAO_OFFICIAL_URL));
        
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        List<ComponentName> list_comp = new ArrayList<ComponentName>();
        List<IntentFilter> list_filter = new ArrayList<IntentFilter>();
        
        Iterator<ResolveInfo> activity_iter = activities.iterator();
        while(activity_iter.hasNext()){
            
            ResolveInfo resolveInfo = activity_iter.next();
            packageManager.getPreferredActivities(list_filter, list_comp, resolveInfo.activityInfo.packageName);
            
            Iterator<IntentFilter> filter_iter = list_filter.iterator();
            while(filter_iter.hasNext()){
                IntentFilter fil = filter_iter.next();
                if((fil.hasCategory(Intent.CATEGORY_BROWSABLE)
                        || fil.hasCategory(Intent.CATEGORY_DEFAULT))
                        && fil.hasDataScheme("http")){
                    list_browsers.add(resolveInfo);
                }
            }
        }
        
        return list_browsers;
    }
    
    public static ArrayList<ResolveInfo> getHasDefValueBrowserExceptSelf(Context context){
        ArrayList<ResolveInfo> arr = getHasDefValueBrowser(context);
        
        Iterator<ResolveInfo> iter = arr.iterator();
        while(iter.hasNext()){
            ResolveInfo resolve = iter.next();
            if(resolve.activityInfo.packageName.equals(AppEnv.PACKAGE_NAME)){
                arr.remove(resolve);
            }
        }
        
        return arr;
    }
    
    public static boolean isSelfDefaultBrowser(Context context){
        boolean flag = false;
        
        ArrayList<ResolveInfo> arr = getHasDefValueBrowser(context);
        Iterator<ResolveInfo> iter = arr.iterator();
        while(iter.hasNext()){
            ResolveInfo resolve = iter.next();
            if(resolve.activityInfo.packageName.equals(AppEnv.PACKAGE_NAME)){
                return true;
            }
        }
        
        return flag;
    }
    
    private static final String SCHEME = "package";
    /**
     * 调用系统InstalledAppDetails界面�?��的Extra名称(用于Android 2.1及之前版�?
     */
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    /**
     * 调用系统InstalledAppDetails界面�?��的Extra名称(用于Android 2.2)
     */
    private static final String APP_PKG_NAME_22 = "pkg";

    private static final String ACTION_APPLICATION_DETAILS_SETTINGS_23 = "android.settings.APPLICATION_DETAILS_SETTINGS";
    /**
     * InstalledAppDetails�?��包名
     */
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    /**
     * InstalledAppDetails类名
     */
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    /**
     * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息�?对于Android 2.3（Api Level
     * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）�?
     * @param packageName
     *            应用程序的包�?
     */
    public static Intent getPackageDetailsIntent(String packageName) {
        Intent intent = new Intent();
        int apiLevel = 0;
        try {
            apiLevel = VERSION.SDK_INT;
        } catch (Exception ex) {
        }
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接�?
            intent.setAction(ACTION_APPLICATION_DETAILS_SETTINGS_23);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码�?
            // 2.2�?.1中，InstalledAppDetails使用的APP_PKG_NAME不同�?
            final String appPkgName = (apiLevel >7 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        return intent;
    }
    
    /**
     * 在新窗口中打�?��览器
     * @param context 上下�?
     * @param url 地址
     * @param from 会附带在Extra的Info.LOADURL_FROM_INTERNAL字段�?
     */
    public static void openNewWindowInKBrowser(Context context, String url, String from) {
        openNewWindowInKBrowser(context, url, from, null, 0);
    }
    
    /**
     * 在新窗口中打�?��览器
     * @param context 上下�?
     * @param url 地址
     * @param from 会附带在Extra的Info.LOADURL_FROM_INTERNAL字段�?
     * @param extra 要附带的其他参数，by caisenchuan
     */
    public static void openNewWindowInKBrowser(Context context, String url, String from, Bundle extra, int flags) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_uri_browsers = Uri.parse(url);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(Info.LOADURL_FROM_INTERNAL, from);        
        intent.setData(content_uri_browsers);
        intent.setPackage(context.getPackageName());
        intent.setClassName("com.ijinshan.browser", BrowserActivity.class.getName());
        
        //添加Bundle中的�?
        if(extra != null) {
            intent.putExtras(extra);
        }
        if (flags != 0)
        	intent.putExtra(Info.LOADURL_FLAGS, flags);
        
        context.startActivity(intent);
    }
}
