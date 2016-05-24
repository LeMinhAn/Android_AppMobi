package vn.appsmobi.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;


public class DeviceUtils {

    public static String getEmeiInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) MainApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    public static String getVersionName() {
        String versionName = null;
        try {
            versionName = MainApplication.getInstance().getPackageManager().getPackageInfo(MainApplication.getInstance().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = MainApplication.getInstance().getPackageManager().getPackageInfo(MainApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return versionCode;
    }

    public static int getAppVersionCode() {
        PackageManager manager = MainApplication.getInstance().getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(
                    MainApplication.getInstance().getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * type:model||manufacture
     *
     * @param type
     * @return
     */
    public static String getDeviceName(String type) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (type.equals("manufacture")) {
            return manufacturer;
        } else if (type.equals("full")) {
            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            } else {
                return capitalize(manufacturer) + " " + model;
            }
        } else {
            if (model.startsWith(manufacturer)) {
                model = model.replace(manufacturer, "");
            }
            return model;
        }

    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
