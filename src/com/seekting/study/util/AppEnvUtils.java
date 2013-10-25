package com.seekting.study.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.ijinshan.browser.R;
import com.ijinshan.browser.env.AppEnv;
import com.ijinshan.browser.model.impl.manager.KDirector;
import com.ijinshan.browser.model.impl.manager.UserBehaviorLogManager;
import com.ijinshan.browser.sync.DigestUtils;
import com.ijinshan.browser.ui.smart.widget.SmartDialog;
import com.ijinshan.browser.ui.smart.widget.SmartDialog.KSmartDialogListener;
import com.ijinshan.browser.utils.KSystemUtils;
import com.ijinshan.browser.view.impl.SettingsViewNew;

public class AppEnvUtils {
    /**
   * @brief get the imei number
   * @param context
   * @return the imei number
   */
  public static String getIMEI(Context context) {
      String strIMEI = "";
      try {
          TelephonyManager tm = (TelephonyManager) context
                  .getSystemService(Context.TELEPHONY_SERVICE);
          strIMEI = tm.getDeviceId();
      } catch (Exception e) { 
      }
      return strIMEI;
  }
  
  /**
   * @brief get channel id
   * @return channel id
   */
  public static String getChannel(Context context){
      String str = getSharedPreferenceChannel(context);
      if (!TextUtils.isEmpty(str)) {
          return str;
      }
      
      str = getInstallChannel(context);
      if (TextUtils.isEmpty(str)) {
          str = AppEnv.OFFICIAL_CHANNEL;
      } else { 
          setSharedPreferenceChannel(context, str);
      }
      
      return str;
  }

  /**
   * @brief Get install channel id from asset
   * @param context Context to retrieve {@link android.content.res.AssetManager}
   * @return Install channel id
   */
  public static String getInstallChannel(Context context) {
      String str = "";
      ByteArrayOutputStream byteArrayOS = null;
      InputStream is = null;
      try{
          is = context.getResources().getAssets().open(AppEnv.CHANNEL_FILE_NAME);
          byteArrayOS = new ByteArrayOutputStream();
          int read;
          while (-1 != (read = is.read())) {
              byteArrayOS.write(read);
          }
          byte[] data = byteArrayOS.toByteArray();
          String str_tmp = new String(data);
          str = str_tmp.trim();
      } catch (Exception e){
          
      } finally {
          if(byteArrayOS != null){
              try {
                  byteArrayOS.close();
              } catch (IOException e) {}
          }
          if(is != null){
              try {
                  is.close();
              } catch (IOException e) {}
          }
      }
      return str;
  }

  /**
   * @brief Get channel id that saved previously
   * @param context Context to retrieve {@link SharedPreferences}
   * @return Channel id if exist, null otherwise
   */
  private static String getSharedPreferenceChannel(Context context) {
      SharedPreferences sp = context.getSharedPreferences(AppEnv.CHANNEL_FILE_NAME, Context.MODE_PRIVATE);
      return sp.getString(AppEnv.CHANNEL_FILE_NAME, null);
  }

  /**
   * @brief Set channel id that read from asset
   * @param context Context to retrieve {@link SharedPreferences}
   * @param str Channel id read from asset or {@link AppEnv#OFFICIAL_CHANNEL}
   */
  private static void setSharedPreferenceChannel(Context context, String str) {
      Editor editor = context.getSharedPreferences(AppEnv.CHANNEL_FILE_NAME, Context.MODE_PRIVATE).edit();
      editor.putString(AppEnv.CHANNEL_FILE_NAME, str);
      editor.commit();
  }

  /**
   * @brief get version name, for example : 1.0.0
   * @param context
   * @return string of version name
   */
  public static String getVersionName(Context context){
      if(context==null)
          return null;
      
      ComponentName cn = new ComponentName(context, context.getClass());
      String version = null;
      try {
          PackageInfo info = context.getPackageManager().getPackageInfo(cn.getPackageName(), 0);
          version = info.versionName;
      } catch (Exception e) {
          return null;
      }
      if (version == null) 
          return null;;
      return version;
  }
  
  /**
   * @brief get version code, for example : 10000100
   * @param context
   * @return integer of version code
   */
  public static String getVersionCode(Context context){
      if(context==null)
          return "0";
      
      ComponentName cn = new ComponentName(context, context.getClass());
      try {
          PackageInfo info = context.getPackageManager().getPackageInfo(cn.getPackageName(), 0);
          StringBuilder sb = new StringBuilder();
          return sb.append(info.versionCode).toString();
      } catch (Exception e) {
          return "0";
      }
  }
  
  /**
   * 读取手机的model号
   * @author caisenchuan
   * */
  public static String getPhoneType() {
      return android.os.Build.MODEL;
  }
  
  static private String getStoragePath() {
      String path = null;
      Context context = KDirector.getApplicationContext();
      //优先使用内部存储卡
      path = KSystemUtils.getInternalSdcardPath(context);
      if(null == path) {
          path = KSystemUtils.getExternalSdcardPath(context);
      }
      return path;
  }
  /**
   * check system requirements
   */
  public static final int MIN_CORES = 2;
  public static final int SEP_CORES = 4;
  public static final String SYSTEM_NOT_CHECK = "kbrowser.system_not_check";
  public static final boolean SYSTEM_CHECK_FLAG = false;
  public static final int MIN_MEM_IN_kB = 512000; // 700M
  public static boolean isSystemQualified(Context cxt) {
      if (!SYSTEM_CHECK_FLAG)
          return true;
      String path = getStoragePath();
      path += File.separator;
      path += SYSTEM_NOT_CHECK;
      //path = UpdateUtil.getExternalDir(cxt) + SYSTEM_NOT_CHECK;
      boolean bNotCheck = new File(path).exists();
      if (bNotCheck) 
          return true;
      int cores = getNumCores();
      if (cores >= SEP_CORES)
          return true;
      if (cores < MIN_CORES)
          return false;
      long mem_in_kB = getTotalRAM();     
      if (mem_in_kB != -1 && mem_in_kB < MIN_MEM_IN_kB)
          return false;
      return true;
  }   
  
  
  public static String getNumCoresStr() {
      int c = getNumCores();
      String r = "" + c;
      return r;
  }
  
  /**
   * Gets total RAM
   * return -1 if error
   */
  public static final String MEM_TOTAL = "MemTotal:";
  public static final String MEM_UNIT = " kB";    
  public static long getTotalRAM() {
      RandomAccessFile reader = null;
      String load = null;
      try {
          reader = new RandomAccessFile("/proc/meminfo", "r");
          load = reader.readLine();
          reader.close();
      } catch (IOException ex) {
          ex.printStackTrace();
      } finally {         
      }
      
      if (TextUtils.isEmpty(load))
          return -1;
      
      int off = load.indexOf(MEM_TOTAL);
      if (off == -1) {
          Log.e("CHECK", "Can't get memory total:" + load);
          return -1;
      }
      
      off = off + MEM_TOTAL.length();
      
      int end = load.indexOf(MEM_UNIT);
      if (end == -1) {
          Log.e("CHECK", "Can't get memory total:" + load);
          return -1;
      }
      
      String num = load.substring(off, end).trim();
      Log.d("CHECK", "RAM:" + num);
      long size = Long.valueOf(num);
      return size;
  }
  /**
   * Gets the number of cores available in this device, across all processors.
   * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
   * @return The number of cores, or 1 if failed to get result
   */
  private static int getNumCores() {
      //Private Class to display only CPU devices in the directory listing
      class CpuFilter implements FileFilter {
          @Override
          public boolean accept(File pathname) {
              //Check if filename is "cpu", followed by a single digit number
              if(Pattern.matches("cpu[0-9]", pathname.getName())) {
                  return true;
              }
              return false;
          }      
      }

      try {
          //Get directory containing CPU info
          File dir = new File("/sys/devices/system/cpu/");
          //Filter to only list the devices we care about
          File[] files = dir.listFiles(new CpuFilter());
          //Return the number of cores (virtual CPU devices)
          return files.length;
      } catch(Exception e) {
          //Default to return 1 core
          return 1;
      }
  }
  
  private static byte[] g_CHECKSIGN = new byte[] { (byte)0xd7, (byte)0xfc, (byte)0x8d, (byte)0x1e };
  
  
}