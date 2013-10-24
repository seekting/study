package com.seekting.study.util;

import android.os.Build;
import android.text.TextUtils;

public class DeviceHelper {
    public static String Model = android.os.Build.MODEL.toLowerCase();
    public static String FingerPrint = android.os.Build.FINGERPRINT.toLowerCase();
    
    public static boolean isMeizu() {
        return isManufacturerEqualTo("meizu");
    }

    public static boolean isMX1() {
        return isManufacturerEqualTo("meizu") && Build.HARDWARE.equalsIgnoreCase("mx");
    }
    
    public static boolean isSumsang() {
        return isManufacturerEqualTo("sumsang");
    }
    
    public static boolean isHTC() {
        return isManufacturerEqualTo("htc");
    }
    
    public static boolean isMIUI() {
        return FingerPrint.contains("miui") || isXiaoMi();
    }
    
    public static boolean isXiaoMi() {
        return Model.startsWith("mi") && FingerPrint.contains("xiaomi");
    }
    
    public static boolean isManufacturerEqualTo(String manuf) {
        if (TextUtils.isEmpty(manuf)) {
            return false;
        }

        if (TextUtils.isEmpty(Build.MANUFACTURER)) {
            return false;
        }

        return Build.MANUFACTURER.equalsIgnoreCase(manuf);
    }
}
