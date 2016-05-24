package vn.appsmobi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import vn.appsmobi.model.DataCardItem;

/**
 * Created by tobrother on 21/01/2016.
 */
public class AppMobiApplication extends MainApplication {
    private static AppMobiApplication _application;

    public AppMobiApplication() {
        _application = this;
    }

    @Override
    public String getGAPropertyID() {
        return null;
    }

    public static AppMobiApplication _() {
        return _application;
    }

    public int getAppVersionCode() {
        PackageManager manager = getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public void onCreate() {
        super.onCreate();
        LocalAppManager.init(this);
        try {
            DownloadInstallManager.init(this, getContentResolver(), getPackageName());
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DataCardItem.init(this);
        DownloadInstallManager.getManager().initData();
        SizeCalculator.init(this);
    }

    /**
     * @param set :
     * @return : kiểm tra xem đây có phải lần đầu tiên mở app hay ko
     * dựa vào preferences check
     * đồng thời sau khi check thì set lại giá trị cho nó
     */
    public boolean isFirstTime(boolean set) {
        SharedPreferences prefs = this.getSharedPreferences(Constants.STRING_PREFERENCE, Context.MODE_PRIVATE);
        boolean is_first_time = prefs.getBoolean(Constants.STRING_PREFERENCE_FIRST_TIME, false);
        if (is_first_time) {
            return false;
        }
        if (set) {
            SharedPreferences.Editor e = prefs.edit();
            e.putBoolean(Constants.STRING_PREFERENCE_FIRST_TIME, true);
            e.commit();
        }
        return true;
    }
}
