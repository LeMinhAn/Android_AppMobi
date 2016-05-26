
package vn.appsmobi.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.appsmobi.R;
import vn.appsmobi.activity.AppDetailActivity;
import vn.appsmobi.activity.HomeActivity;
import vn.appsmobi.model.DataCardItem;

public class Utils {
    public static final class Network {
        public static boolean isNetWorkConnected(Context context) {
            ConnectivityManager connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }

        public static int getActiveNetworkType(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo info = cm.getActiveNetworkInfo();
                if (info != null) {
                    return info.getType();
                }
            }
            return -1;
        }

        public static boolean isWifiConnected(Context context) {
            ConnectivityManager connManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            return networkInfo != null &&
                    networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }

        public static String getWifiSSID(Context context) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getSSID();
        }

        public static boolean isMobileConnected(Context context) {
            ConnectivityManager connManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            return networkInfo != null &&
                    networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
    }

    public static final class Preference {
        public static void setLongPref(Context context, String key, Long value) {
            if (context == null) {
                return;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                Editor editor = pref.edit();
                if (editor != null) {
                    editor.putLong(key, value);
                    editor.commit();
                }
            }
        }

        public static long getLongPref(Context context, String key, long defaultValue) {
            if (context == null) {
                return defaultValue;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                return pref.getLong(key, defaultValue);
            }
            return defaultValue;
        }

        public static void setStringPref(Context context, String key, String value) {
            if (context == null) {
                return;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                Editor editor = pref.edit();
                if (editor != null) {
                    editor.putString(key, value);
                    editor.commit();
                }
            }
        }

        public static String getStringPref(Context context, String key, String defaultValue) {
            if (context == null) {
                return defaultValue;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                return pref.getString(key, defaultValue);
            }
            return defaultValue;
        }

        public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
            if (context == null) {
                return defaultValue;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                return pref.getBoolean(key, defaultValue);
            }
            return defaultValue;
        }

        public static void setBooleanPref(Context context, String key, boolean value) {
            if (context == null) {
                return;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                Editor editor = pref.edit();
                if (editor != null) {
                    editor.putBoolean(key, value);
                    editor.commit();
                }
            }
        }

        public static void removePref(Context context, String key) {
            if (context == null) {
                return;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                Editor editor = pref.edit();
                if (editor != null) {
                    editor.remove(key);
                    editor.commit();
                }
            }
        }

        public static ArrayList<String> getAllPreferenceKey(Context context) {
            if (context == null) {
                return null;
            }
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if (pref != null) {
                Map<String, ?> map = pref.getAll();
                Iterator it = map.keySet().iterator();
                ArrayList<String> list = new ArrayList<String>();
                while (it.hasNext()) {
                    list.add(it.next().toString());
                }
                return list;
            }
            return null;
        }
    }

    /**
     * show or hide keyboard
     */
    public static void logHeap() {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        Log.d("tag", "debug. =================================");
        Log.d("tag", "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        Log.d("tag", "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory() / 1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory() / 1048576)) + "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
    }

    public static final class SoftInput {
        public static void hide(Context context, IBinder windowToken) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(windowToken, 0);
            }
        }

        public static void show(Context context, View view) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, 0);
            }
        }
    }

    /**
     * Date Time format to String
     * get full time , month , day of month , date of week  ,  year
     */
    public static final class DateTime {
        private static final int MILLSECONDS = 1000;

        public static String formatTime(Context context, String timeSeconds) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    context.getString(R.string.repair_progress_date_format_deafault));
            return sdf.format(new Date(Long.parseLong(timeSeconds) * MILLSECONDS));
        }

        /**
         * return a day string of today, using functin "formatDate"
         *
         * @param context
         * @return
         */
        public static String formatToday(Context context) {
            return formatDate(context, String.valueOf((new Date()).getTime() / MILLSECONDS));
        }

        /**
         * return a string of timeSeconds, as the format "YYYY-MM-DD"
         *
         * @param context
         * @param timeSeconds
         * @return string of date
         */
        public static String formatDate(Context context, String timeSeconds) {
            Time time = new Time();
            time.set(Long.parseLong(timeSeconds) * MILLSECONDS);
            return time.format(context.getString(R.string.order_date_format));
        }

        public static String getMonth(String timeSeconds) {
            Time time = new Time();
            time.set(Long.parseLong(timeSeconds) * MILLSECONDS);
            return String.valueOf(time.month + 1);
        }

        public static String getDayOfMonth(String timeSeconds) {
            Time time = new Time();
            time.set(Long.parseLong(timeSeconds) * MILLSECONDS);
            return String.valueOf(time.monthDay);
        }


        public static String formatDateString(Context context, String dateStr) {
            Date date = formatStringToDate(
                    context.getString(R.string.repair_progress_date_format_deafault), dateStr);
            String newDateStr = dateStr;
            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(
                        context.getString(R.string.repair_progress_date_format));
                newDateStr = formatter.format(date);
            }

            return newDateStr;
        }


        public static Date formatStringToDate(String formatStr, String dateStr) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
            Date newDateStr = null;
            try {
                newDateStr = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return newDateStr;
        }

    }

    /**
     * Money currency
     */
    public static final class Money {
        public static String valueOf(double value) {
            int valueInt = (int) value;
            if (valueInt == value) {
                return String.valueOf(valueInt);
            } else {
                return String.valueOf(value);
            }
        }
    }

    /**
     * Phone format
     */
    public static final class PhoneFormat {

        public static String valueOf(String phone) {
            if (!TextUtils.isEmpty(phone)) {
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                Matcher m = p.matcher(phone);
                if (m.matches()) {
                    phone = phone.substring(0, 3) + "****" + phone.substring(7);
                }
                return phone;
            }
            return phone;
        }
    }

    public static final class NumberFormat {
        public static String valueOf(double inputNumber) {
            double newKB = Double.parseDouble(String.valueOf(inputNumber)) / Double.parseDouble(String.valueOf(1024 * 1024));
            DecimalFormat df = new DecimalFormat("###.##");
            return String.valueOf(df.format(newKB));
        }
    }

    /**
     * Play video with URL
     */
    public static final class Video {

        /**
         * Invoke player to play video.
         *
         * @param context   context used to start player activity
         * @param uriString video uri, like
         *                  "http://forum.ea3w.com/coll_ea3w/attach/2008_10/12237832415.3gp"
         * @return true if success, false otherwise
         */
        public static boolean playVideo(Context context, String uriString) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "video/*";
            try {
                Uri uri = Uri.parse(uriString);
                intent.setDataAndType(uri, type);
                context.startActivity(intent);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    /**
     * Sử lý nhạc
     * Chuyển đỗi Milliseconds to thời gian
     */
    public static final class Music {
        public static final String milliSecondsToTimer(long milliseconds) {
            String finalTimerString = "";
            String secondsString = "";

            //Chuyển đổi thời gian sang định dạng Hours:Minutes:Seconds
            int hours = (int) (milliseconds / (1000 * 60 * 60));
            int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
            //Thêm giờ nếu có
            if (hours > 0) {
                finalTimerString = hours + ":";
            }

            //Thêm vào số 0 nếu có 1 chữ số
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }
            finalTimerString = finalTimerString + minutes + ":" + secondsString;

            //kết quả trả về
            return finalTimerString;
        }

        /**
         * @param currentDuration : thời gian đã chạy được
         * @param totalDuration   : tổng thời gian
         * @return % thời gian đã chạy được
         */
        public static final int getProgressPercentage(long currentDuration, long totalDuration) {
            Double percentage = (double) 0;
            long currentSeconds = (int) (currentDuration / 1000);
            long totalSeconds = (int) (totalDuration / 1000);
            //Tính toán tỉ lệ %
            percentage = (((double) currentSeconds) / totalSeconds) * 100;
            //trả về giá trị %
            return percentage.intValue();
        }

        /**
         * @param progress      : phần trăm hiện tại
         * @param totalDuration : tổng time
         * @return : thời gian đã chạy được
         */
        public static final int progressToTimer(int progress, int totalDuration) {
            int currentDuration = 0;
            totalDuration = (int) (totalDuration / 1000);
            currentDuration = (int) ((((double) progress) / 100) * totalDuration);

            //trả về thời gian hiện tại trong mili giây
            return currentDuration * 1000;
        }
    }

    public static void showInstallSuccessNotification(DataCardItem appinfo) {
        Intent intent = AppMobiApplication._().getPackageManager().getLaunchIntentForPackage(appinfo.getPackge_name());
        cancelNotification(AppMobiApplication._(), String.valueOf(appinfo.getId()));
        showNotification(AppMobiApplication._(), intent, appinfo.getName(), AppMobiApplication._().getString(R.string.notif_install_successful, appinfo.getName()), R.drawable.stat_notify_install_success, appinfo.getId());
    }

    public static void showInstallingNotification(DataCardItem appinfo) {

        Context context = AppMobiApplication._();

        Intent intent = new Intent(context, AppDetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_APP_PAKAGENAME, appinfo.getPackge_name());

        showNotification(context, intent, appinfo.getName(), context.getString(R.string.notif_installing, appinfo.getName()), R.drawable.stat_notify_installing, appinfo.getId());

    }

    public static void cancelNotification(Context context, String tag) {
        /**
         * Context.NOTIFICATION_SERVICE <== "notification"
         */
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(tag, 0);
    }

    public static void showNotification(Context context, Intent intent, String title, String message, int iconId, String id) {
        showNotification(context, intent, title, message, iconId, id, true);
    }

    /**
     * Show Notification on status bar // if click this notification open HomeActivity
     *
     * @param context
     * @param intent
     * @param title
     * @param message
     * @param iconId
     * @param id
     * @param ispending
     */
    public static void showNotification(Context context, Intent intent, String title, String message, int iconId, String id, boolean ispending) {
        NotificationManager localNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification();

        notify.icon = iconId;

        notify.tickerText = message;

        notify.when = System.currentTimeMillis();

        notify.flags = (0x10 | notify.flags);


        if (intent == null)
            if (!ispending)
                intent = new Intent(context, HomeActivity.class); // open home activity
        //  notify.setLatestEventInfo(context, title, message, PendingIntent.getActivity(context, 0, intent, Notification.FLAG_AUTO_CANCEL));
        // loi tai day
        Log.e("ERROR_UTILS", "NOTIFICATION");

        try {
            localNotificationManager.notify("dasdsd", 0, notify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createShortcutForPackage(String packageName) {
        Intent mIntent = AppMobiApplication._().getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            PackageManager pm = AppMobiApplication._().getPackageManager();
            ResolveInfo ri = pm.resolveActivity(mIntent, 0);
            String shortcutName = ri.loadLabel(pm).toString();
            Drawable ico = null;
            try {
                ico = pm.getActivityIcon(mIntent);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mIntent.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            addIntent
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, mIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, drawableToBitmapIcon(ico));

            addIntent
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            addIntent
                    .putExtra("duplicate", false);
            AppMobiApplication._().sendBroadcast(addIntent);
        }
    }


    /**
     * @param drawable drawable in drawable folder
     * @return bitmap of it
     */
    public static Bitmap drawableToBitmapIcon(Drawable drawable) {
        float scale = AppMobiApplication._().getResources().getDisplayMetrics().density;
        int size = 0;
        if (scale == 0.75) {
            size = 36;
        } else if (scale == 1.0) {
            size = 48;
        } else if (scale == 1.5) {
            size = 72;
        } else {
            size = 96;
        }

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bm = Bitmap.createScaledBitmap(bitmap, size, size, true);
            return bm;
        }


        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/AppsMobile.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }


    public static void installShortcut(Context context, String packageName, String componentName, String shortcutName, Parcelable icon) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        ComponentName cn = new ComponentName(packageName, componentName);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(cn));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcut.putExtra("duplicate", false);
        context.sendBroadcast(shortcut);
    }

    public static Context createPackageContext(Context context, String pkgName) {
        Context result = null;
        try {
            result = context.createPackageContext(pkgName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
        } catch (PackageManager.NameNotFoundException e) {
            Log.v("", "createPackageContext(): " + e.getStackTrace());
        }
        return result;
    }

    /**
     * check a folder exist
     *
     * @param folder_uri
     * @return
     */
    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
        if (file.exists() && file.isDirectory())
            ret = false;
        else
            file.mkdirs();
        return ret;
    }

    public static boolean checkFolderExist(String folder_uri) {
        File dir = new File(Environment.getExternalStorageDirectory() + folder_uri);
        if (dir.exists() && dir.isDirectory()) {
            return true;
        }
        return false;
    }

    public static class EventClick {
        public static void CardClick(Context context, DataCardItem dataCardItem) {

            Intent intent = new Intent(context, AppDetailActivity.class);
            Bundle bundle = new Bundle();
            Log.e("tag", "-------------------");
            bundle.putParcelable(Constants.Intent.CARD, dataCardItem);

            bundle.putInt(Constants.Intent.CARD_DATA_TYPE, dataCardItem.getData_type());
            intent.putExtras(bundle);
            ((Activity) context).startActivity(intent);
        }
    }

    public static File getFileFrom(DataCardItem dataCardItem) {
        String sub_folder = "";
        String file_type = "";
        switch (dataCardItem.getData_type()) {
            case Constants.CARD_DATA_TYPE.APP:
            case Constants.CARD_DATA_TYPE.GAME:
                sub_folder = DownloadInstallManager.DOWNLOAD_FOLDER_APPS;
                file_type = "apk";
                break;
            case Constants.CARD_DATA_TYPE.RINGTONE:
                sub_folder = DownloadInstallManager.DOWNLOAD_FOLDER_RINGTONES;
                file_type = "mp3";
                break;
            case Constants.CARD_DATA_TYPE.WALLPAPER:
                sub_folder = DownloadInstallManager.DOWNLOAD_FOLDER_WALLPAPERS;
                file_type = "jpg";
                break;
            case Constants.CARD_DATA_TYPE.BOOK:
                sub_folder = DownloadInstallManager.DOWNLOAD_FOLDER_BOOKS;
                file_type = "pdf";
                break;
            case Constants.CARD_DATA_TYPE.FILM:
                sub_folder = DownloadInstallManager.DOWNLOAD_FOLDER_MOVIES;
                file_type = "mp4";
                break;
        }
        String folder_string = DownloadInstallManager.DOWNLOAD_FOLDER + "/" + sub_folder;
        Utils.createDirIfNotExists(folder_string);
        return new File(Environment.getExternalStorageDirectory(), folder_string + dataCardItem.getName() + "." + file_type);
    }
}
