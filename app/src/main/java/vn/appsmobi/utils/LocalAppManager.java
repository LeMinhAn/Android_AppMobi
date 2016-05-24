package vn.appsmobi.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.model.LocalDataCardItem;

/**
 * Created by tobrother on 17/02/2016.
 */

public class LocalAppManager {
    private static LocalAppManager sLocalAppManager;
    private Context mContext;
    private ConcurrentHashMap<String, LocalDataCardItem> mInstalledCards;
    private ConcurrentHashMap<String, LocalDataCardItem> mDownloadedCards;
    private ConcurrentHashMap<String, LocalDataCardItem> mInstalledNonSysApps;
    private CopyOnWriteArraySet<LocalAppInfoUpdateListener> mListeners;
    private ConcurrentHashMap<String, CopyOnWriteArraySet<LocalAppInstallRemoveListener>> mLocalAppListeners;
    private ArrayList<LocalDataCardItem> mInstalledSortList;

    /**
     * Phong
     *
     * @param mContext
     */
    public LocalAppManager(Context mContext) {
        this.mContext = mContext;
        this.mInstalledCards = new ConcurrentHashMap<String, LocalDataCardItem>();
        this.mInstalledNonSysApps = new ConcurrentHashMap<String, LocalDataCardItem>();
        this.mInstalledSortList = new ArrayList<LocalDataCardItem>();
        this.mLocalAppListeners = new ConcurrentHashMap<String, CopyOnWriteArraySet<LocalAppInstallRemoveListener>>();
        loadInstalledApps(false);
    }

    public static void init(Context context) {
        if (sLocalAppManager == null)
            sLocalAppManager = new LocalAppManager(context);
    }

    private void notifyContentChanged(DataCardItem appinfo) {
        if (this.mListeners != null) {
            Iterator<LocalAppInfoUpdateListener> localIterator = this.mListeners.iterator();
            while (localIterator.hasNext())
                ((LocalAppInfoUpdateListener) localIterator.next()).onContentChanged(appinfo);
        }
    }

    public static LocalAppManager getManager() {
        return sLocalAppManager;
    }

    /**
     * check application is installed
     *
     * @param packageName
     * @return
     */
    public boolean isInstalled(String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        else if (packageName.equals(AppMobiApplication._().getPackageName()) || this.mInstalledCards.containsKey(packageName)) {
            return true;
        } else
            return false;
    }

    /**
     * check a book , ringtone , wallpaper is downloaded
     *
     * @param
     * @return
     */
    /*
    public boolean isDownloaded(String folderName){
        if (TextUtils.isEmpty(folderName))
            return false;
        else if(Utils.checkFolderExist(folderName)) {
            return true;
        }
        else
            return false;
    }
    */
    public DataCardItem getLocalAppInfo(String packageName) {
        DataCardItem appInfo = null;
        if (TextUtils.isEmpty(packageName))
            return null;
        else
            appInfo = (DataCardItem) this.mInstalledCards.get(packageName);
        return appInfo;
    }

    public int getCount() {
        return this.mInstalledCards.size();
    }


    public ArrayList<LocalDataCardItem> getInstalledApp()

    {
        return new ArrayList<LocalDataCardItem>(mInstalledCards.values());
    }

    private void sortLocalApps() {

        //if(this.mInstalledApps.values().size() > 0)
        //this.mInstalledSortList = new ArrayList<LocalAppInfo>(this.mInstalledApps.values());
    }

    public void loadInstalledApps(boolean includeSysApps) {
        includeSysApps = true;
        mInstalledCards.clear();
        Method getPackageSizeInfo = null;
        // the package manager contains the information about all installed apps
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            getPackageSizeInfo = packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        } catch (SecurityException e1) {

            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {

            e1.printStackTrace();
        }
        List<PackageInfo> packs = packageManager.getInstalledPackages(0); //PackageManager.GET_META_DATA

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            // skip system apps if they shall not be included
            if ((!includeSysApps) && ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
                continue;
            }
            final LocalDataCardItem app = new LocalDataCardItem();
            app.setName(p.applicationInfo.loadLabel(packageManager).toString());
            //Log.v(p.applicationInfo.loadLabel(packageManager).toString() + "::" + p.packageName);
            app.setPackage_name(p.packageName);
            app.setVersion_name(p.versionName);
            app.setCard_status(Constants.CARD_STATUS.STATUS_INSTALLED);
            app.setVersion_code(p.versionCode);
            app.setSource(a.sourceDir);
            //app.setHash();
            CharSequence description = p.applicationInfo.loadDescription(packageManager);
            app.setShort_description(description != null ? description.toString() : "");
            try {
                getPackageSizeInfo.invoke(packageManager, p.packageName, new IPackageStatsObserver.Stub() {

                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        app.setSize((float) (pStats.codeSize / 1024.0 / 1024.0));
                    }
                });
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException r) {
            } catch (InvocationTargetException w) {
            }
            mInstalledCards.put(app.getPackge_name(), app);
            sortLocalApps();
        }
    }

    public void onPackageAdded(String packagename, int paramInt) {
        loadInstalledApps(false);
        if (this.mLocalAppListeners.get(packagename) != null) {
            Iterator<LocalAppInstallRemoveListener> iterator = ((CopyOnWriteArraySet<LocalAppInstallRemoveListener>) this.mLocalAppListeners.get(packagename)).iterator();
            while (iterator.hasNext())
                ((LocalAppInstallRemoveListener) iterator.next()).onAppInstalled(packagename, paramInt);
        }

        try {
            DataCardItem appinfo = DataCardItem.getAppByPackage(packagename);
            if (appinfo != null) {
                LocalAppManager.this.notifyContentChanged(appinfo);
            }

        } catch (Exception e) {
            Log.v("", "onPackageAdded Exception" + e.getMessage());
        }

    }

    public void onPackageModified(String packagename) {
        try {
            DataCardItem appinfo = DataCardItem.getAppByPackage(packagename);
            if (appinfo != null)
                LocalAppManager.this.notifyContentChanged(appinfo);
        } catch (Exception e) {
            Log.v("", "onPackageModified Exception:" + e.getMessage());
        }

    }

    public void onPackageRemoved(String packagename, int uid) {
        loadInstalledApps(false);
        Log.v("", "onPackageRemoved");
        if (this.mLocalAppListeners.get(packagename) != null) {
            Iterator<LocalAppInstallRemoveListener> iterator = ((CopyOnWriteArraySet<LocalAppInstallRemoveListener>) this.mLocalAppListeners.get(packagename)).iterator();
            while (iterator.hasNext())
                ((LocalAppInstallRemoveListener) iterator.next()).onAppRemoved(packagename, uid);
        }
        try {
            DataCardItem appinfo = DataCardItem.getAppByPackage(packagename);
            if (appinfo != null)
                LocalAppManager.this.notifyContentChanged(appinfo);
        } catch (Exception e) {
            Log.v("", "onPackageRemoved Exception:" + e.getMessage());
        }

    }


    public void addLocalAppListener(String pakagename, LocalAppInstallRemoveListener listener) {
        if (listener == null) {
            return;
        } else {
            try {
                if (this.mLocalAppListeners.get(pakagename) == null)
                    this.mLocalAppListeners.put(pakagename, new CopyOnWriteArraySet<LocalAppInstallRemoveListener>());
                ((CopyOnWriteArraySet<LocalAppInstallRemoveListener>) this.mLocalAppListeners.get(pakagename)).add(listener);
            } finally {
            }
        }
    }


    public void removeLocalAppListener(LocalAppInstallRemoveListener listener) {
        if (listener == null)
            return;
        else {
            try {
                Iterator<CopyOnWriteArraySet<LocalAppInstallRemoveListener>> iterator = this.mLocalAppListeners.values().iterator();
                CopyOnWriteArraySet<LocalAppInstallRemoveListener> arrset = null;
                do {
                    if (!iterator.hasNext())
                        break;
                    arrset = (CopyOnWriteArraySet<LocalAppInstallRemoveListener>) iterator.next();
                } while (!arrset.contains(listener));

                arrset.remove(listener);
            } finally {
            }
        }
    }

    public static abstract interface LocalAppInfoUpdateListener {
        public abstract void onContentChanged();

        public abstract void onContentChanged(DataCardItem appInfo);

        public abstract void onListChanged();

        public abstract void onListChanged(DataCardItem appInfo);

        public abstract void onLocalInstalledLoaded();
    }

    public static abstract interface LocalAppInstallRemoveListener {
        public abstract void onAppInstalled(String appid, int uid);

        public abstract void onAppRemoved(String appid, int uid);
    }

    public void addUpdateListener(LocalAppInfoUpdateListener mUpdateListener) {
        if (mUpdateListener != null) ;
        {
            if (this.mListeners == null)
                this.mListeners = new CopyOnWriteArraySet<LocalAppInfoUpdateListener>();
            this.mListeners.add(mUpdateListener);
            mUpdateListener.onListChanged();
            mUpdateListener.onContentChanged();
        }

    }


}
