package vn.appsmobi.utils;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public abstract class MainApplication extends Application {

    public static boolean DEBUG = false;
    private static Context sContext;
    private static MainApplication sInstance;
    public static final String PREF_USER_DEBUG = "pref_user_debug";

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sInstance = this;
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(120);
        tracker = analytics.newTracker(getGAPropertyID());
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    // could be overwrite on child

    public abstract String getGAPropertyID();

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }

    public static boolean isUserDebug() {
        return MainApplication.DEBUG && Utils.Preference.getBooleanPref(sContext,
                PREF_USER_DEBUG,
                true);
    }


}
