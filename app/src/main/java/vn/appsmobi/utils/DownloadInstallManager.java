package vn.appsmobi.utils;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import vn.appsmobi.R;
import vn.appsmobi.model.ApplicationItem;
import vn.appsmobi.model.DataCardItem;

public class DownloadInstallManager {
    public final int INSTALL_REPLACE_EXISTING = 2;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     *
     * @hide
     */
    public static final int INSTALL_SUCCEEDED = 1;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * already installed.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
    public static final String DOWNLOAD_FOLDER = "com.appsmobi/";
    public static final String DOWNLOAD_FOLDER_APPS = "apps/";
    public static final String DOWNLOAD_FOLDER_RINGTONES = "ringtones/";
    public static final String DOWNLOAD_FOLDER_BOOKS = "books/";
    public static final String DOWNLOAD_FOLDER_MOVIES = "movies/";
    public static final String DOWNLOAD_FOLDER_WALLPAPERS = "wallpapers/";
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * file is invalid.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_INVALID_APK = -2;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * is invalid.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_INVALID_URI = -3;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * service found that the device didn't have enough storage space to install the app.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * package is already installed with the same name.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the requested shared user does not exist.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_NO_SHARED_USER = -6;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * a previously installed package of the same name has a different signature
     * than the new package (and the old package's data was not removed).
     *
     * @hide
     */
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package is requested a shared user which is already installed on the
     * device and does not have matching signature.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package uses a shared library that is not available.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package uses a shared library that is not available.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package failed while optimizing and validating its dex files,
     * either because there was not enough storage or the validation failed.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_DEXOPT = -11;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package failed because the current SDK version is older than
     * that required by the package.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_OLDER_SDK = -12;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package failed because it contains a content provider with the
     * same authority as a provider already installed in the system.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * the new package failed because the current SDK version is newer than
     * that required by the package.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_NEWER_SDK = -14;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * flag.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_TEST_ONLY = -15;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * the package being installed contains native code, but none that is
     * compatible with the the device's CPU_ABI.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * the new package uses a feature that is not available.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_MISSING_FEATURE = -17;

    // ------ Errors related to sdcard
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * a secure container mount point couldn't be accessed on external media.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * the new package couldn't be installed in the specified install
     * location.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;

    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * the new package couldn't be installed in the specified install
     * location because the media is not available.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser was given a path that is not a file, or does not end with the expected
     * '.apk' extension.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser was unable to retrieve the AndroidManifest.xml file.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser encountered an unexpected exception.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser did not find any certificates in the .apk.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser found inconsistent certificates on the files in the .apk.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser encountered a CertificateEncodingException in one of the
     * files in the .apk.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser encountered a bad or missing package name in the manifest.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser encountered a bad shared user id name in the manifest.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser encountered some structural problem in the manifest.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the parser did not find any actionable tags (instrumentation or application)
     * in the manifest.
     *
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;

    /**
     * Installation failed return code: this is passed to the {@link IPackageInstallObserver} by
     * <p/>
     * if the system failed to install the package because of system issues.
     *
     * @hide
     */
    public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;

    private static DownloadInstallManager sDownloadInstallManager;
    public Context mContext;
    private HandlerThread mDownloadTaskManageThread;
    private HandlerThread mInstallThread;
    private final File mCacheDir;
    private ArrayList<String> mDownloadInstallApps;
    public final ConcurrentHashMap<String, DataCardItem> mInstallManuallyMap;
    public DownloadManager mDownloadManager;
    private InstallHandler mInstallHandler;
    private final DownloadInstallMonitor mDownloadInstallMonitor;
    public ConcurrentHashMap<Long, String> mAppDownloadMap;
    private ConcurrentHashMap<String, Progress> mCurrProgressMap;
    private ConcurrentHashMap<String, CopyOnWriteArraySet<ProgressListener>> mProgressListeners;
    private HandlerThread mProgressThread;
    private ProgressHandler mProgressHandler;
    private DownloadTaskManageHandler mDownloadTaskManageHandler;
    private HashSet<TaskListener> mTaskListeners;
    private LocalAppManager mLocalAppManager = LocalAppManager.getManager();
    private Context activityContext;
    Method method;
    private OnInstalledPackaged onInstalledPackaged;
    PackageManager pm;
    private InstallHandler.PackageInstallObserver observer;
    private ContentResolver mResolver;
    private String mPackageName;
    private DownLoadTypeInterface mDownLoadTypeInterface;

    public DownloadInstallManager(Context mContext) throws SecurityException, NoSuchMethodException {
        this.mContext = mContext;
        this.mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        this.mAppDownloadMap = new ConcurrentHashMap<Long, String>();
        this.mDownloadInstallApps = new ArrayList<String>();
        this.mInstallManuallyMap = new ConcurrentHashMap<String, DataCardItem>();
        this.mDownloadInstallMonitor = new DownloadInstallMonitor();
        this.mProgressListeners = new ConcurrentHashMap<String, CopyOnWriteArraySet<ProgressListener>>();
        this.mCurrProgressMap = new ConcurrentHashMap<String, Progress>();
        this.mTaskListeners = new HashSet<TaskListener>();
        this.mCacheDir = mContext.getDir("apks", Context.MODE_WORLD_READABLE);
        if (!this.mCacheDir.exists()) ;
        try {
            this.mCacheDir.mkdirs();
        } catch (SecurityException localSecurityException) {
            Log.v("", "cannot create apks dir");
        }
        Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
        this.pm = this.mContext.getPackageManager();
        method = pm.getClass().getMethod("installPackage", types);
    }

    public static void init(Context context, ContentResolver Resolver, String PackageName) throws SecurityException, NoSuchMethodException {
        if (sDownloadInstallManager == null) {
            sDownloadInstallManager = new DownloadInstallManager(context);
            sDownloadInstallManager.mResolver = Resolver;
            sDownloadInstallManager.mPackageName = PackageName;
        }

    }

    public static DownloadInstallManager getManager() {
        return sDownloadInstallManager;
    }


    public void setOnInstalledPackaged(OnInstalledPackaged onInstalledPackaged) {
        this.onInstalledPackaged = onInstalledPackaged;
    }

    public void installPackage(String apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        installPackage(new File(apkFile));
    }

    public void installPackage(File apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (!apkFile.exists()) throw new IllegalArgumentException();
        Uri packageURI = Uri.fromFile(apkFile);
        installPackage(packageURI);
    }

    public void installPackage(Uri apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        method.invoke(pm, new Object[]{apkFile, observer, INSTALL_REPLACE_EXISTING, null});
    }

    public void handleManualPackageInstall(String packagename) {
        if (this.mInstallManuallyMap.containsKey(packagename)) {
            DataCardItem appInfo = DataCardItem.getAppByPackage(packagename);
            if (Build.VERSION.SDK_INT < 11) {
                File f = new File(Environment.DIRECTORY_DOWNLOADS + appInfo.getPackge_name() + ".apk");
                f.delete();
            }
            Utils.createShortcutForPackage(packagename);
            DownloadInstallManager.this.mInstallHandler.installComplete(String.valueOf(appInfo.getId()), appInfo.getDownload_id(), 17);

      	 /*
           Iterator<Long> dlid = DownloadInstallManager.this.mAppDownloadMap.keySet().iterator();
	       while(dlid.hasNext()){
	            Long key = dlid.next();
	            String appid = DownloadInstallManager.this.mAppDownloadMap.get(key);
	            if(appid.equals(appInfo.getId())){
	            	 
	            }
	        }
		  */

        }

    }

    public void addProgressListener(String appid, ProgressListener listener) {
        if ((listener == null) || (TextUtils.isEmpty(appid)))
            return;
        else {
            synchronized (this.mProgressListeners) {
                CopyOnWriteArraySet<ProgressListener> localCopyOnWriteArraySet = (CopyOnWriteArraySet<ProgressListener>) this.mProgressListeners.get(appid);
                if (localCopyOnWriteArraySet == null) {
                    localCopyOnWriteArraySet = new CopyOnWriteArraySet<ProgressListener>();
                    this.mProgressListeners.put(appid, localCopyOnWriteArraySet);
                }
                localCopyOnWriteArraySet.add(listener);
                listener.onProgressUpdate(appid, (Progress) this.mCurrProgressMap.get(appid));
            }
        }
    }

    public void removeProgressListener(String appId, ProgressListener listener) {
        if ((listener == null) || (TextUtils.isEmpty(appId)))
            return;
        else
            synchronized (this.mProgressListeners) {
                CopyOnWriteArraySet<ProgressListener> localCopyOnWriteArraySet = this.mProgressListeners.get(appId);
                if (localCopyOnWriteArraySet != null)
                    localCopyOnWriteArraySet.remove(listener);
            }
    }

    public void handleDownloadComplete(long downloadid) {
        this.mDownloadTaskManageHandler.handleDownloadComplete(downloadid);
    }

    public Context getActivityContext() {
        return activityContext;
    }

    public void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }

    private class DownloadTaskManageHandler extends Handler {
        private volatile int mDownloadingCount = 0;
        private ConcurrentLinkedQueue<DataCardItem> mWaitingQueue = new ConcurrentLinkedQueue<DataCardItem>();

        public DownloadTaskManageHandler(Looper arg2) {
            super();
            initialize();
        }

        private void initialize() {
            post(new Runnable() {
                public void run() {
                    //DownloadInstallManager.DownloadTaskManageHandler.this.reloadDownloadingTasks();
                }
            });
        }

        public void handleDownloadComplete(final long id) {
            post(new Runnable() {
                public void run() {
                    String appid = (String) DownloadInstallManager.this.mAppDownloadMap.get(Long.valueOf(id));
                    if (TextUtils.isEmpty(appid)) {
                        return;
                    } else {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(id);
                        Cursor cursor = DownloadInstallManager.this.mDownloadManager.query(query);
                        if (cursor != null)
                            try {
                                if (cursor.moveToFirst()) {
                                    String uri = "";

                                    if (Build.VERSION.SDK_INT >= 11)
                                        uri = cursor.getString(cursor.getColumnIndexOrThrow("local_filename"));
                                    //	File mFile = new File(Uri.parse(uri).getPath());
                                    int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                                    if (status != -1) {
                                        if (status == DownloadManager.STATUS_FAILED) {
                                            Log.e("DownloadInstallManager -- download status", "PAUSE");
                                            DownloadInstallManager.DownloadTaskManageHandler.this.downloadFail(id, DownloadManager.PAUSED_UNKNOWN);
                                        } else if (TextUtils.isEmpty(uri)) {
                                            Log.e("DownloadInstallManager -- download status", " empty");
                                            DownloadInstallManager.DownloadTaskManageHandler.this.downloadFail(id, DownloadManager.PAUSED_UNKNOWN);
                                        } else {
                                            Log.e("DownloadInstallManager -- download status", "Install now");
                                            DownloadInstallManager.DownloadTaskManageHandler.this.downloadSuccess(id); // remove download map
                                            if (DataCardItem.get(appid).getData_type() == Constants.CARD_DATA_TYPE.APP || DataCardItem.get(appid).getData_type() == Constants.CARD_DATA_TYPE.GAME) {
                                                DownloadInstallManager.this.mInstallHandler.install(appid, id, uri); // install file apk from download folder
                                            } else {
                                                DownloadInstallManager.this.mProgressHandler.updateProgress(appid, 5);
                                                DownloadInstallManager.this.mProgressHandler.updateProgress(appid, 6);

                                            }
                                        }
                                    } else {
                                        DownloadInstallManager.DownloadTaskManageHandler.this.downloadFail(id, status);
                                    }
                                    if (cursor != null)
                                        cursor.close();
                                } else {
                                    DownloadInstallManager.DownloadTaskManageHandler.this.downloadFail(id, DownloadManager.STATUS_FAILED);
                                }
                            } catch (Exception e) {
                                Log.v("", "handleDownloadComplete exception:" + e.getMessage());
                            } finally {
                                if (cursor != null)
                                    cursor.close();
                            }
                    }
                }
            });
        }

        protected void downloadSuccess(long downloadId) {
            DownloadInstallManager.this.mAppDownloadMap.remove(Long.valueOf(downloadId));
        }

        protected void downloadFail(long downloadId, final int error) {
            Log.v("", "download fail status:" + error);

            final String appid = DownloadInstallManager.this.mAppDownloadMap.remove(Long.valueOf(downloadId));
            DownloadInstallManager.this.mInstallHandler.post(new Runnable() {
                public void run() {
                    DownloadInstallManager.this.mDownloadInstallMonitor.onFail(appid, error);
                }
            });
            synchronized (DownloadInstallManager.this.mInstallManuallyMap) {
                DownloadInstallManager.this.mInstallManuallyMap.remove(ApplicationItem.get(appid).getPackge_name());
            }
            synchronized (DownloadInstallManager.this.mDownloadInstallApps) {
                DownloadInstallManager.this.mDownloadInstallApps.remove(appid);
                DownloadInstallManager.this.mCurrProgressMap.remove(appid);
            }
            synchronized (DownloadInstallManager.this.mProgressListeners) {
                DownloadInstallManager.this.mProgressListeners.remove(appid);
                Iterator<TaskListener> iterator = DownloadInstallManager.this.mTaskListeners.iterator();
                while (iterator.hasNext()) {
                    ((DownloadInstallManager.TaskListener) iterator.next()).onTaskFail(appid, error);
                }
            }
            checkAndArrangeNext();
        }

        // todo pause a download
        public void pauseDownload(long... ids) {
            Cursor cursor = query(new Query().setFilterById(ids));
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    int status = cursor
                            .getInt(cursor.getColumnIndex(COLUMN_STATUS));
                    if (status != STATUS_RUNNING && status != STATUS_PENDING) {
                        throw new IllegalArgumentException(
                                "Can only pause a running download: "
                                        + cursor.getLong(cursor
                                        .getColumnIndex(COLUMN_ID)));
                    }
                }
            } finally {
                cursor.close();
            }

            ContentValues values1 = new ContentValues();
            values1.put(Downloads.COLUMN_CONTROL, Downloads.CONTROL_PAUSED);
            values1.put(Downloads.COLUMN_NO_INTEGRITY, 1);

            mContext.getContentResolver().update(mBaseUri, values1, getWhereClauseForIds(ids),
                    getWhereArgsForIds(ids));

            cursor = query(new Query().setFilterById(ids));

            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    //if (cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == STATUS_PAUSED) {
                    Log.e("DownLoadInstallManager --- Status Paused ", String.valueOf(cursor
                            .getInt(cursor.getColumnIndex(COLUMN_STATUS))));
                    //  Log.e("DownLoadInstallManager --- Control Paused 1", String.valueOf( cursor
                    //         .getInt(cursor.getColumnIndex(Downloads.COLUMN_CONTROL))));

                    //}
                }
            } finally {
                cursor.close();
            }
        }

        // todo pause a download
        public void resumeDownload(long... ids) {
            Cursor cursor = query(new Query().setFilterById(ids));
            try {

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    int status = cursor
                            .getInt(cursor.getColumnIndex(COLUMN_STATUS));
                    if (status != STATUS_PAUSED) {
                        throw new IllegalArgumentException(
                                "Cann only resume a paused download: "
                                        + cursor.getLong(cursor
                                        .getColumnIndex(COLUMN_ID)));
                    }
                }
            } finally {
                cursor.close();
            }

            ContentValues values = new ContentValues();
            values.put(Downloads.COLUMN_STATUS, Downloads.STATUS_PENDING);
            values.put(Downloads.COLUMN_CONTROL, 0);
            mContext.getContentResolver().update(mBaseUri, values, getWhereClauseForIds(ids),
                    getWhereArgsForIds(ids));
            //   Log.e("DownLoadInstallManager --- resume step ", "5");
            cursor = query(new Query().setFilterById(ids));
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == STATUS_PAUSED) {
                        Log.e("DownLoadInstallManager --- Status resume ", String.valueOf(cursor
                                .getInt(cursor.getColumnIndex(COLUMN_STATUS))));
                        //  Log.e("DownLoadInstallManager --- Control resume 1", String.valueOf( cursor
                        //         .getInt(cursor.getColumnIndex(Downloads.COLUMN_CONTROL))));
                    }

                }
            } finally {
                cursor.close();
            }

        }

        public void restartDownload(long... ids) {
            Cursor cursor = query(new Query().setFilterById(ids));
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    int status = cursor
                            .getInt(cursor.getColumnIndex(COLUMN_STATUS));
                    if (status != STATUS_SUCCESSFUL && status != STATUS_FAILED) {
                        throw new IllegalArgumentException(
                                "Cannot restart incomplete download: "
                                        + cursor.getLong(cursor
                                        .getColumnIndex(COLUMN_ID)));
                    }
                }
            } finally {
                cursor.close();
            }

            ContentValues values = new ContentValues();
            values.put(Downloads.COLUMN_CURRENT_BYTES, 0);
            values.put(Downloads.COLUMN_TOTAL_BYTES, -1);
            values.putNull(Downloads._DATA);
            values.put(Downloads.COLUMN_STATUS, Downloads.STATUS_PENDING);
            //values.put(Downloads.COLUMN_FAILED_CONNECTIONS, 0);
            /*
            values.put(Downloads.COLUMN_CURRENT_BYTES, 0);
            values.put(Downloads.COLUMN_TOTAL_BYTES, -1);
            values.putNull(Downloads._DATA);
            values.put(Downloads.COLUMN_STATUS, Downloads.STATUS_PENDING);
            */
            mResolver.update(mBaseUri, values, getWhereClauseForIds(ids),
                    getWhereArgsForIds(ids));
        }

        private long downloadInternal(String uri, DataCardItem appInfo) throws IllegalArgumentException {
            File f = Utils.getFileFrom(appInfo);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
            request.setTitle(appInfo.getName());
            request.setMimeType("application/vnd.android.package-archive");
            Uri dst_uri = Uri.fromFile(f);
            request.setDestinationUri(dst_uri);
            //Restrict the types of networks over which this download may proceed.
            request.setShowRunningNotification(true);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            if (Build.VERSION.SDK_INT < 11) {
                f = Utils.getFileFrom(appInfo);
                dst_uri = Uri.fromFile(f);
                request.setDestinationUri(dst_uri);
            }
            //Set whether this download may proceed over a roaming connection.
            request.setAllowedOverRoaming(false);
            return DownloadInstallManager.getManager().mDownloadManager.enqueue(request);
        }

        private void startConnectAndDownload(final DataCardItem appInfo, final boolean isReloaded) {
            new Thread(new Runnable() {
                public void run() {
                    LogUtil.e(TAG + " --- startConnectAndDownload ", "true");
                    DataCardItem localappInfo = appInfo;
                    DownloadInstallManager.this.mProgressHandler.updateProgress(String.valueOf(localappInfo.getId()), 1);
                    DownloadInstallManager.DownloadTaskManageHandler downloadTaskManager = DownloadInstallManager.DownloadTaskManageHandler.this;
                    long downloadId = downloadTaskManager.downloadInternal(appInfo.getResource_url(), localappInfo);
                    DownloadInstallManager.this.mAppDownloadMap.put(Long.valueOf(downloadId), String.valueOf(localappInfo.getId()));
                    Log.e("DownloadInstallManager --- DownloadID --", String.valueOf(downloadId));
                    ApplicationItem.get(String.valueOf(localappInfo.getId())).setDownload_id(downloadId);
                }
            }).start();
        }

        public void arrange(final DataCardItem appInfo) {
            post(new Runnable() {
                public void run() {
                    try {
                        if (appInfo != null) {
                            synchronized (DownloadInstallManager.this.mDownloadInstallApps) {
                                if (!DownloadInstallManager.this.mDownloadInstallApps.contains(appInfo.getId()))
                                    DownloadInstallManager.this.mDownloadInstallApps.add(appInfo.getId());
                                DownloadInstallManager.this.mInstallHandler.post(new Runnable() {
                                    public void run() {
                                        DownloadInstallManager.this.mDownloadInstallMonitor.onStart(appInfo.getId());
                                    }
                                });
                                Iterator<TaskListener> iterator = DownloadInstallManager.this.mTaskListeners.iterator();
                                if (iterator.hasNext())
                                    ((DownloadInstallManager.TaskListener) iterator.next()).onTaskStart(appInfo.getId());
                            }
                            synchronized (DownloadInstallManager.DownloadTaskManageHandler.this.mWaitingQueue) {
                                if ((DownloadInstallManager.DownloadTaskManageHandler.this.mWaitingQueue.isEmpty()) && (DownloadInstallManager.DownloadTaskManageHandler.this.mDownloadingCount < 2)) {
                                    DownloadInstallManager.DownloadTaskManageHandler.this.startConnectAndDownload(appInfo, false);
                                    return;
                                }
                                DownloadInstallManager.DownloadTaskManageHandler.this.mWaitingQueue.add(appInfo);
                                DownloadInstallManager.this.mProgressHandler.updateProgress(String.valueOf(appInfo.getId()), 0);
                            }
                        }
                    } catch (Exception e) {
                        Log.v("", "DownloadInstall.arrange Exception:" + e.getMessage());
                    }

                }
            });
        }

        public void checkAndArrangeNext() {
            post(new Runnable() {
                public void run() {
                    synchronized (DownloadInstallManager.DownloadTaskManageHandler.this.mWaitingQueue) {
                        ApplicationItem appinfo = (ApplicationItem) DownloadInstallManager.DownloadTaskManageHandler.this.mWaitingQueue.poll();
                        if (appinfo != null) {
                            DownloadInstallManager.DownloadTaskManageHandler.this.startConnectAndDownload(appinfo, false);
                        }
                        return;
                    }
                }
            });

        }
    }

    public void install() {

    }

    public class InstallHandler extends Handler {
        public InstallHandler(Looper arg2) {
            super();
        }

        private String getPatchedApkPath(String appName) {
            return DownloadInstallManager.this.mCacheDir.getAbsolutePath() + "/" + appName + ".apk";
        }

        /**
         * call function install file .apk
         *
         * @param appid
         * @throws IOException
         */

        public void install(String appid) throws IOException {
            //DownloadInstallManager.this.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", null);
            // DownloadInstallManager.this.mContext.getPackageManager().installPackage(Uri.parse(uri), new DownloadInstallManager.InstallHandler.PackageInstallObserver               (DownloadInstallManager.InstallHandler.this,  id, uri), 2, DownloadInstallManager.this.mContext.getPackageName());
            DataCardItem appinfo = DataCardItem.get(appid);
            Utils.showInstallingNotification(appinfo);
            File file = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_FOLDER + "/" + DOWNLOAD_FOLDER_APPS + appinfo.getName() + ".apk");
            Uri downloadFile = Uri.fromFile(file);
            try {
                observer = new InstallHandler.PackageInstallObserver(appinfo, downloadFile.getPath());
                method.invoke(pm, new Object[]{downloadFile, observer, INSTALL_REPLACE_EXISTING, null});
            } catch (Exception e) {
                e.printStackTrace();
                DownloadInstallManager.this.mInstallManuallyMap.put(appinfo.getPackge_name(), appinfo);
                Intent localIntent = new Intent(Intent.ACTION_VIEW);
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setDataAndType(downloadFile, "application/vnd.android.package-archive");
                DownloadInstallManager.this.mContext.startActivity(localIntent);
                //
            }
        }

        public void install(String appid, long id, String uri) throws IOException {
            DownloadInstallManager.this.mProgressHandler.updateProgress(appid, 5);
            DownloadInstallManager.this.mProgressHandler.updateProgress(appid, 6);
            //DownloadInstallManager.this.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", null);
            // DownloadInstallManager.this.mContext.getPackageManager().installPackage(Uri.parse(uri), new DownloadInstallManager.InstallHandler.PackageInstallObserver               (DownloadInstallManager.InstallHandler.this,  id, uri), 2, DownloadInstallManager.this.mContext.getPackageName());
            DataCardItem appinfo = DataCardItem.get(appid);
            Utils.showInstallingNotification(appinfo);
            File file = null;
            if (Build.VERSION.SDK_INT >= 11) {
                file = new File(uri);
            } else {
                file = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_FOLDER + "/" + DOWNLOAD_FOLDER_APPS + appinfo.getName() + ".apk");
            }
            Uri downloadFile = Uri.fromFile(file);
            try {
                observer = new InstallHandler.PackageInstallObserver(appinfo, id, uri);
                method.invoke(pm, new Object[]{downloadFile, observer, INSTALL_REPLACE_EXISTING, null});
            } catch (Exception e) {
                e.printStackTrace();
                DownloadInstallManager.this.mInstallManuallyMap.put(appinfo.getPackge_name(), appinfo);
                Intent localIntent = new Intent(Intent.ACTION_VIEW);
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setDataAndType(downloadFile, "application/vnd.android.package-archive");
                DownloadInstallManager.this.mContext.startActivity(localIntent);
                //
            }
        }

        private void installComplete(String appId, long downloadId, int error) {
            Iterator<TaskListener> iterator = DownloadInstallManager.this.mTaskListeners.iterator();
            DownloadInstallManager.this.mDownloadInstallApps.remove(appId);
            if (!iterator.hasNext()) {
                DownloadInstallManager.this.mDownloadTaskManageHandler.checkAndArrangeNext();
            } else {
                ((DownloadInstallManager.TaskListener) iterator.next()).onTaskSuccess(appId);
            }
            synchronized (DownloadInstallManager.this.mProgressListeners) {
                DownloadInstallManager.this.mProgressListeners.remove(appId);
                DownloadInstallManager.this.mDownloadInstallMonitor.onSuccess(appId);
            }
            DownloadInstallManager.this.mAppDownloadMap.remove(downloadId);
        }


        public class PackageDeleteObserver implements LocalAppManager.LocalAppInstallRemoveListener {
            private String mApkPath;
            private String mAppId;
            private long mDownloadId;
            private Timer mTimer;

            public PackageDeleteObserver(String appid, long dlid) {
                this.mDownloadId = dlid;
                this.mAppId = appid;
                this.mTimer = new Timer();
                this.mTimer.schedule(new TimerTask() {
                                         public void run() {
                                             //DownloadInstallManager.this.mInstallHandler.(DownloadInstallManager.InstallHandler.PackageDeleteObserver.this.mAppId, DownloadInstallManager.InstallHandler.PackageDeleteObserver.this.mDownloadId, 15);
                                             DownloadInstallManager.this.mLocalAppManager.removeLocalAppListener(DownloadInstallManager.InstallHandler.PackageDeleteObserver.this);
                                         }
                                     }
                        , 10000L);
            }

            public void onAppInstalled(String appid, int dlid) {
            }

            public void onAppRemoved(String appid, int downloadid) {
                this.mTimer.cancel();
                DownloadInstallManager.InstallHandler.this.post(new Runnable() {
                    public void run() {
                        //DownloadInstallManager.this.mInstallHandler.install(DownloadInstallManager.InstallHandler.PackageDeleteObserver.this.mAppId, DownloadInstallManager.InstallHandler.PackageDeleteObserver.this.mDownloadId, DownloadInstallManager.InstallHandler.PackageDeleteObserver.this.mApkPath);
                        DownloadInstallManager.this.mLocalAppManager.removeLocalAppListener(DownloadInstallManager.InstallHandler.PackageDeleteObserver.this);
                    }

                });
            }
        }


        class PackageInstallObserver extends IPackageInstallObserver.Stub {
            private final String mApkPath;
            private final DataCardItem mApp;
            private long mDownloadId;

            public PackageInstallObserver(DataCardItem appinfo, long downloadId, String apkPath) {
                this.mApp = appinfo;
                this.mDownloadId = downloadId;
                this.mApkPath = apkPath;
            }

            public PackageInstallObserver(DataCardItem appinfo, String apkPath) {
                this.mApp = appinfo;
                this.mApkPath = apkPath;
            }

            @Override
            public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                DownloadInstallManager.this.mDownloadManager.remove(DownloadInstallManager.InstallHandler.PackageInstallObserver.this.mDownloadId);
                DownloadInstallManager.this.mAppDownloadMap.remove(Long.valueOf(DownloadInstallManager.InstallHandler.PackageInstallObserver.this.mDownloadId));
                DownloadInstallManager.InstallHandler.this.installComplete(String.valueOf(DownloadInstallManager.InstallHandler.PackageInstallObserver.this.mApp.getId()), DownloadInstallManager.InstallHandler.PackageInstallObserver.this.mDownloadId, 10);
                InstallHandler localInstallHandler = DownloadInstallManager.this.mInstallHandler;
                localInstallHandler.getClass();
                LocalAppManager.getManager().addLocalAppListener(packageName, new DownloadInstallManager.InstallHandler.PackageDeleteObserver(packageName, this.mDownloadId));
                Utils.createShortcutForPackage(this.mApp.getPackge_name());
            }
        }
    }

    private class ProgressHandler extends Handler {
        private Cursor mDownloadCursor;
        private ContentObserver mDownloadObserver = new ContentObserver(this) {
            public void onChange(boolean paramAnonymousBoolean) {
                DownloadInstallManager.ProgressHandler.this.checkProgress();
            }
        };

        public ProgressHandler(Looper arg2) {
            super();
            initialize();
        }

        private void checkProgress() {
            post(new Runnable() {
                public void run() {
                    long dlid = 0;
                    if (DownloadInstallManager.ProgressHandler.this.mDownloadCursor == null) {
                        DownloadInstallManager.ProgressHandler.this.setupAndEnsureDownloadObserver();
                    } else {
                        DownloadInstallManager.ProgressHandler.this.mDownloadCursor.requery();
                    }
                    if (DownloadInstallManager.ProgressHandler.this.mDownloadCursor != null) {
                        int id = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
                        int status = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
                        int reason = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON);
                        int currBytes = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int totalBytes = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                        HashSet<Long> hashset = new HashSet<Long>();
                        DownloadInstallManager.ProgressHandler.this.mDownloadCursor.moveToPosition(-1);
                        while (DownloadInstallManager.ProgressHandler.this.mDownloadCursor.moveToNext()) {
                            dlid = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getLong(id);
                            hashset.add(Long.valueOf(dlid));
                            int i1 = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getInt(status);
                            int i2 = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getInt(reason);
                            int i3 = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getInt(currBytes);
                            int i4 = DownloadInstallManager.ProgressHandler.this.mDownloadCursor.getInt(totalBytes);
                            String str = (String) DownloadInstallManager.this.mAppDownloadMap.get(Long.valueOf(dlid));
                            if (!TextUtils.isEmpty(str))
                                DownloadInstallManager.ProgressHandler.this.updateProgress(str, DownloadInstallManager.ProgressHandler.this.translateStatus(i1), i2, i3, i4);
                        }
                        Iterator<Long> localIterator = DownloadInstallManager.this.mAppDownloadMap.keySet().iterator();
                        while (localIterator.hasNext()) {
                            Long localLong = (Long) localIterator.next();
                            String appid = (String) DownloadInstallManager.this.mAppDownloadMap.get(Long.valueOf(localLong));
                            DataCardItem appinfo = DataCardItem.get(appid);
                            if (!hashset.contains(localLong)) {
                                if (DownloadInstallManager.this.mAppDownloadMap.contains(localLong)) {

                                } else {

                                }


                                if (DownloadInstallManager.this.mInstallManuallyMap.contains(appinfo.getPackge_name())) {

                                } else {

                                }
                                //  if(dlid == localLong  && !DownloadInstallManager.this.mInstallManuallyMap.contains(appinfo.getPackageName()))
                                DownloadInstallManager.this.handleDownloadComplete(localLong.longValue());
                            }
                        }
                    }
                }
            });
        }


        private void initialize() {
            post(new Runnable() {
                public void run() {
                    DownloadInstallManager.ProgressHandler.this.setupAndEnsureDownloadObserver();
                }
            });
        }


        private void setupAndEnsureDownloadObserver() {
            try {
                if (this.mDownloadCursor == null) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterByStatus(7);
                    this.mDownloadCursor = DownloadInstallManager.this.mDownloadManager.query(query);
                    if (this.mDownloadCursor != null)
                        this.mDownloadCursor.registerContentObserver(this.mDownloadObserver);
                }
                return;
            } finally {
            }
        }

        private int translateStatus(int status) {
            int i;
            switch (status) {
                case 1:
                    i = 1;
                    break;
                case 4:
                    i = 4;
                    break;
                case 2:
                    i = 3;
                    break;
                case 3:
                default:
                    i = -1;
            }
            return i;
        }

        private void updateProgress(String appId, int status, int reason, int currBytes, int totalBytes) {
            DownloadInstallManager.Progress progress = (DownloadInstallManager.Progress) DownloadInstallManager.this.mCurrProgressMap.get(appId);
            if (progress == null) {
                progress = new DownloadInstallManager.Progress();
                progress.status = -1;
                DownloadInstallManager.this.mCurrProgressMap.put(appId, progress);
            }
            int oldStatus = progress.status;
            progress.status = status;
            progress.reason = reason;
            progress.currBytes = currBytes;
            progress.totalBytes = totalBytes;
            CopyOnWriteArraySet<ProgressListener> localCopyOnWriteArraySet = (CopyOnWriteArraySet<ProgressListener>) DownloadInstallManager.this.mProgressListeners.get(appId);
            if (localCopyOnWriteArraySet != null) {
                Iterator<ProgressListener> localIterator = localCopyOnWriteArraySet.iterator();
                while (localIterator.hasNext()) {
                    DownloadInstallManager.ProgressListener localProgressListener = (DownloadInstallManager.ProgressListener) localIterator.next();
                    localProgressListener.onProgressUpdate(appId, progress);
                    if (oldStatus != status)
                        localProgressListener.onStateUpdate(appId, status, oldStatus);
                }
            }
        }

        public void updateProgress(final String appid, final int status) {
            post(new Runnable() {
                public void run() {
                    DownloadInstallManager.ProgressHandler.this.updateProgress(appid, status, 0, 0, 0);
                }
            });
        }
    }

    public void initData() {
        this.mProgressThread = new HandlerThread("ProgressThread");
        this.mProgressThread.start();
        this.mProgressHandler = new ProgressHandler(this.mProgressThread.getLooper());
        this.mDownloadTaskManageThread = new HandlerThread("DownloadTaskManageThread");
        this.mDownloadTaskManageThread.start();
        this.mDownloadTaskManageHandler = new DownloadTaskManageHandler(this.mDownloadTaskManageThread.getLooper());
        this.mInstallThread = new HandlerThread("InstallThread");
        this.mInstallThread.start();
        this.mInstallHandler = new InstallHandler(this.mInstallThread.getLooper());
    }

    private boolean isInstalledAndNotUpdate(DataCardItem paramAppInfo) {
        if (paramAppInfo.canInstallOrUpdate())
            return false;
        return true;
    }

    public void pauseDownload(DataCardItem appInfo) {
        Log.e("DownloadInstallManager --- id downloader", String.valueOf(appInfo.getDownload_id()));
        this.mDownloadTaskManageHandler.pauseDownload(appInfo.getDownload_id());
    }

    public void resumeDownload(DataCardItem appInfo) {
        Log.e("DownloadInstallManager --- id downloader", String.valueOf(appInfo.getDownload_id()));
        this.mDownloadTaskManageHandler.resumeDownload(appInfo.getDownload_id());
    }

    /**
     * get list downloading for app manager activity
     *
     * @return
     */
    /*
    public ArrayList<DataCardItem> getListDownLoad(){
        ArrayList<DataCardItem> arrayList = new ArrayList<>();
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
        Cursor c = YOUR_DM.query(q);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // process download
                title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                // get other required data by changing the constant passed to getColumnIndex
            }
        }
    }
    */

    public abstract interface DownLoadTypeInterface {
        public abstract long downloadInternal(String uri, DataCardItem appInfo);
    }

    public void setDownLoadTypeInterface(DownLoadTypeInterface mDownLoadTypeInterface) {
        this.mDownLoadTypeInterface = mDownLoadTypeInterface;
    }

    public boolean arrange(DataCardItem appInfo) {
        boolean bool = false;
        this.mProgressHandler.setupAndEnsureDownloadObserver();
        // if downloading or installing
        if (isDownloadingOrInstalling(String.valueOf(appInfo.getId()))) {
            if (DownloadInstallManager.this.mInstallManuallyMap.containsKey(appInfo.getPackge_name())) {
                Log.e("DownloadInstallManager---Status", "Downloading or installing ");
                //DownloadInstallManager.this.handleDownloadComplete(appInfo.getDownload_id());
                long dlid = appInfo.getDownload_id();
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(dlid);
                Cursor cursor = DownloadInstallManager.this.mDownloadManager.query(query);
                if (cursor != null)
                    try {
                        if (cursor.moveToFirst()) {
                            String uri = "";
                            if (Build.VERSION.SDK_INT >= 11)
                                uri = cursor.getString(cursor.getColumnIndexOrThrow("local_filename"));
                            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                            if (status != -1) {
                                DownloadInstallManager.this.mInstallHandler.install(String.valueOf(appInfo.getId()), dlid, uri);
                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
            }
            return bool;
        }
        // if installed and not have update
        if (isInstalledAndNotUpdate(appInfo)) {
            Log.e("DownloadInstallManager---Status", "Installed and Not have update");
        } else if (appInfo.isDownloaded()) {
            final String appid = appInfo.getId();

            final String[] items = {"BẠN ĐÃ DOWNLOAD , BẠN CÓ MUỐN MỞ NGAY"};
            new MaterialDialog.Builder(activityContext)
                    .title("THÔNG BÁO")
                    .items(items)
                    .positiveText("MỞ")
                    .negativeText("THOÁT")
                    .positiveColor(activityContext.getResources().getColor(R.color.main_color))
                    .negativeColor(activityContext.getResources().getColor(R.color.main_color))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            try {
                                DownloadInstallManager.this.mInstallHandler.install(appid); // install file apk from download folder
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();
            Log.e("DownloadInstallManager---Status", "Downloaded");
        } else {
            Log.e("DownloadInstallManager---Status", "have update or not install");
            this.mDownloadTaskManageHandler.arrange(appInfo);
            bool = true;
        }
        return bool;
    }

    public boolean isDownloadingOrInstalling(String appId) {
        synchronized (this.mDownloadInstallApps) {
            boolean bool = this.mDownloadInstallApps.contains(appId);
            return bool;
        }
    }

    public static abstract interface TaskListener {
        public abstract void onTaskFail(String appid, int errorcode);

        public abstract void onTaskStart(String appid);

        public abstract void onTaskSuccess(String appid);
    }


    public static abstract interface ProgressListener {
        public abstract void onProgressUpdate(String value, DownloadInstallManager.Progress paramProgress);

        public abstract void onStateUpdate(String paramString, int paramInt1, int paramInt2);
    }

    public class Progress {
        public int currBytes;
        public int reason;
        public int status;
        public int totalBytes;

        public Progress() {
        }
    }

    private class DownloadInstallMonitor {
        private SparseArray mAutoUpdateResultArray = new SparseArray();
        private int mAutoUpdateTotalCount = 0;

        public DownloadInstallMonitor() {
        }

        public void onFail(String appId, int error) {
            if (error == DownloadManager.STATUS_PAUSED) {
                DownloadInstallManager.this.mDownloadTaskManageHandler.post(new Runnable() {
                    public void run() {
                        // InstallChecker.showNoEnoughSpaceDialog(AppMobiApplication._());
                        Toast.makeText(AppMobiApplication._(), "Tải xuống không thành công , Vui lòng thử lại", Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Toast.makeText(AppMobiApplication._(), "Tải xuống không thành công", Toast.LENGTH_LONG).show();
        }

        public void onStart(String paramString) {

        }

        public void onSuccess(String appid) {
            DataCardItem appinfo = DataCardItem.get(appid);
            if (LocalAppManager.getManager().isInstalled(appinfo.getPackge_name())) {
                Utils.showInstallSuccessNotification(appinfo);
            }

        }
    }

    public void addTaskListener(TaskListener listenner) {
        if (listenner != null) {
            synchronized (this.mTaskListeners) {
                this.mTaskListeners.add(listenner);
            }
        }
    }

    public void removeTaskListener(TaskListener listenner) {
        if (listenner != null) {
            synchronized (this.mTaskListeners) {
                this.mTaskListeners.remove(listenner);
            }
        }
    }

    // new function for pause and resume and restart download

    /**
     * This class may be used to filter download manager queries.
     */
    public Cursor query(Query query) {
        Cursor underlyingCursor = query.runQuery(mResolver, UNDERLYING_COLUMNS,
                mBaseUri);

        if (underlyingCursor == null) {

            return null;
        }
        return new CursorTranslator(underlyingCursor, mBaseUri);
    }

    public class Query {
        /**
         * Constant for use with {@link #orderBy}
         *
         * @hide
         */
        public static final int ORDER_ASCENDING = 1;

        /**
         * Constant for use with {@link #orderBy}
         *
         * @hide
         */
        public static final int ORDER_DESCENDING = 2;

        private long[] mIds = null;
        private Integer mStatusFlags = null;
        private String mOrderByColumn = Downloads.COLUMN_LAST_MODIFICATION;
        private int mOrderDirection = ORDER_DESCENDING;
        private boolean mOnlyIncludeVisibleInDownloadsUi = false;

        /**
         * Include only the downloads with the given IDs.
         *
         * @return this object
         */
        public Query setFilterById(long... ids) {
            mIds = ids;
            return this;
        }

        /**
         * Include only downloads with status matching any the given status
         * flags.
         *
         * @param flags any combination of the STATUS_* bit flags
         * @return this object
         */
        public Query setFilterByStatus(int flags) {
            mStatusFlags = flags;
            return this;
        }

        /**
         * Controls whether this query includes downloads not visible in the
         * system's Downloads UI.
         *
         * @param value if true, this query will only include downloads that
         *              should be displayed in the system's Downloads UI; if false
         *              (the default), this query will include both visible and
         *              invisible downloads.
         * @return this object
         * @hide
         */
        public Query setOnlyIncludeVisibleInDownloadsUi(boolean value) {
            mOnlyIncludeVisibleInDownloadsUi = value;
            return this;
        }

        /**
         * Change the sort order of the returned Cursor.
         *
         * @param column    one of the COLUMN_* constants; currently, only
         *                  {@link #COLUMN_LAST_MODIFIED_TIMESTAMP} and
         *                  {@link #COLUMN_TOTAL_SIZE_BYTES} are supported.
         * @param direction either {@link #ORDER_ASCENDING} or
         *                  {@link #ORDER_DESCENDING}
         * @return this object
         * @hide
         */
        public Query orderBy(String column, int direction) {
            if (direction != ORDER_ASCENDING && direction != ORDER_DESCENDING) {
                throw new IllegalArgumentException("Invalid direction: "
                        + direction);
            }

            if (column.equals(COLUMN_LAST_MODIFIED_TIMESTAMP)) {
                mOrderByColumn = Downloads.COLUMN_LAST_MODIFICATION;
            } else if (column.equals(COLUMN_TOTAL_SIZE_BYTES)) {
                mOrderByColumn = Downloads.COLUMN_TOTAL_BYTES;
            } else {
                throw new IllegalArgumentException("Cannot order by " + column);
            }
            mOrderDirection = direction;
            return this;
        }

        /**
         * Run this query using the given ContentResolver.
         *
         * @param projection the projection to pass to ContentResolver.query()
         * @return the Cursor returned by ContentResolver.query()
         */
        Cursor runQuery(ContentResolver resolver, String[] projection,
                        Uri baseUri) {
            Uri uri = baseUri;
            List<String> selectionParts = new ArrayList<String>();
            String[] selectionArgs = null;

            if (mIds != null) {
                selectionParts.add(getWhereClauseForIds(mIds));
                selectionArgs = getWhereArgsForIds(mIds);
            }

            if (mStatusFlags != null) {
                List<String> parts = new ArrayList<String>();
                if ((mStatusFlags & STATUS_PENDING) != 0) {
                    parts.add(statusClause("=", Downloads.STATUS_PENDING));
                }
                if ((mStatusFlags & STATUS_RUNNING) != 0) {
                    parts.add(statusClause("=", Downloads.STATUS_RUNNING));
                }
                if ((mStatusFlags & STATUS_PAUSED) != 0) {
                    parts.add(statusClause("=", Downloads.STATUS_PAUSED_BY_APP));
                    parts.add(statusClause("=",
                            Downloads.STATUS_WAITING_TO_RETRY));
                    parts.add(statusClause("=",
                            Downloads.STATUS_WAITING_FOR_NETWORK));
                    parts.add(statusClause("=",
                            Downloads.STATUS_QUEUED_FOR_WIFI));
                }
                if ((mStatusFlags & STATUS_SUCCESSFUL) != 0) {
                    parts.add(statusClause("=", Downloads.STATUS_SUCCESS));
                }
                if ((mStatusFlags & STATUS_FAILED) != 0) {
                    parts.add("(" + statusClause(">=", 400) + " AND "
                            + statusClause("<", 600) + ")");
                }
                selectionParts.add(joinStrings(" OR ", parts));
            }
            if (mOnlyIncludeVisibleInDownloadsUi) {
                selectionParts.add(Downloads.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI
                        + " != '0'");
            }
            // only return rows which are not marked 'deleted = 1'
            selectionParts.add(Downloads.COLUMN_DELETED + " != '1'");

            String selection = joinStrings(" AND ", selectionParts);
            String orderDirection = (mOrderDirection == ORDER_ASCENDING ? "ASC"
                    : "DESC");
            String orderBy = mOrderByColumn + " " + orderDirection;
            return resolver.query(uri, projection, selection, selectionArgs,
                    orderBy);
        }

        private String joinStrings(String joiner, Iterable<String> parts) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (String part : parts) {
                if (!first) {
                    builder.append(joiner);
                }
                builder.append(part);
                first = false;
            }
            return builder.toString();
        }

        private String statusClause(String operator, int value) {
            return Downloads.COLUMN_STATUS + operator + "'" + value + "'";
        }
    }


    private Uri mBaseUri = Downloads.CONTENT_URI;

    /**
     * Get a parameterized SQL WHERE clause to select a bunch of IDs.
     */
    static String getWhereClauseForIds(long[] ids) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("(");
        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                whereClause.append("OR ");
            }
            whereClause.append(Downloads._ID);
            whereClause.append(" = ? ");
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    /**
     * Get the selection args for a clause returned by
     * {@link #getWhereClauseForIds(long[])}.
     */
    static String[] getWhereArgsForIds(long[] ids) {
        String[] whereArgs = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            whereArgs[i] = Long.toString(ids[i]);
        }
        return whereArgs;
    }

    /////
    private static final String TAG = "DownloadInstallManager";

    /**
     * An identifier for a particular download, unique across the system.
     * Clients use this ID to make subsequent calls related to the download.
     */
    public final static String COLUMN_ID = BaseColumns._ID;

    /**
     * The client-supplied title for this download. This will be displayed in
     * system notifications. Defaults to the empty string.
     */
    public final static String COLUMN_TITLE = "title";

    /**
     * The client-supplied description of this download. This will be displayed
     * in system notifications. Defaults to the empty string.
     */
    public final static String COLUMN_DESCRIPTION = "description";

    /**
     * URI to be downloaded.
     */
    public final static String COLUMN_URI = "uri";

    /**
     * Internet Media Type of the downloaded file. If no value is provided upon
     * creation, this will initially be null and will be filled in based on the
     * server's response once the download has started.
     *
     * @see <a href="http://www.ietf.org/rfc/rfc1590.txt">RFC 1590, defining
     * Media Types</a>
     */
    public final static String COLUMN_MEDIA_TYPE = "media_type";

    /**
     * Total size of the download in bytes. This will initially be -1 and will
     * be filled in once the download starts.
     */
    public final static String COLUMN_TOTAL_SIZE_BYTES = "total_size";

    /**
     * Uri where downloaded file will be stored. If a destination is supplied by
     * client, that URI will be used here. Otherwise, the value will initially
     * be null and will be filled in with a generated URI once the download has
     * started.
     */
    public final static String COLUMN_LOCAL_URI = "local_uri";

    /**
     * Current status of the download, as one of the STATUS_* constants.
     */
    public final static String COLUMN_STATUS = "status";

    /**
     * Provides more detail on the status of the download. Its meaning depends
     * on the value of {@link #COLUMN_STATUS}.
     * <p/>
     * When {@link #COLUMN_STATUS} is {@link #STATUS_FAILED}, this indicates the
     * type of error that occurred. If an HTTP error occurred, this will hold
     * the HTTP status code as defined in RFC 2616. Otherwise, it will hold one
     * of the ERROR_* constants.
     * <p/>
     * When {@link #COLUMN_STATUS} is {@link #STATUS_PAUSED}, this indicates why
     * the download is paused. It will hold one of the PAUSED_* constants.
     * <p/>
     * If {@link #COLUMN_STATUS} is neither {@link #STATUS_FAILED} nor
     * {@link #STATUS_PAUSED}, this column's value is undefined.
     *
     * @see <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6.1.1">RFC
     * 2616 status codes</a>
     */
    public final static String COLUMN_REASON = "reason";

    /**
     * Number of bytes download so far.
     */
    public final static String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";

    /**
     * Timestamp when the download was last modified, in
     * {@link System#currentTimeMillis System.currentTimeMillis()} (wall clock
     * time in UTC).
     */
    public final static String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";

    /**
     * Value of {@link #COLUMN_STATUS} when the download is waiting to start.
     */
    public final static int STATUS_PENDING = 1 << 0;

    /**
     * Value of {@link #COLUMN_STATUS} when the download is currently running.
     */
    public final static int STATUS_RUNNING = 1 << 1;

    /**
     * Value of {@link #COLUMN_STATUS} when the download is waiting to retry or
     * resume.
     */
    public final static int STATUS_PAUSED = 1 << 2;

    /**
     * Value of {@link #COLUMN_STATUS} when the download has successfully
     * completed.
     */
    public final static int STATUS_SUCCESSFUL = 1 << 3;

    /**
     * Value of {@link #COLUMN_STATUS} when the download has failed (and will
     * not be retried).
     */
    public final static int STATUS_FAILED = 1 << 4;

    /**
     * Value of COLUMN_ERROR_CODE when the download has completed with an error
     * that doesn't fit under any other error code.
     */
    public final static int ERROR_UNKNOWN = 1000;

    /**
     * Value of {@link #COLUMN_REASON} when a storage issue arises which doesn't
     * fit under any other error code. Use the more specific
     * {@link #ERROR_INSUFFICIENT_SPACE} and {@link #ERROR_DEVICE_NOT_FOUND}
     * when appropriate.
     */
    public final static int ERROR_FILE_ERROR = 1001;

    /**
     * Value of {@link #COLUMN_REASON} when an HTTP code was received that
     * download manager can't handle.
     */
    public final static int ERROR_UNHANDLED_HTTP_CODE = 1002;

    /**
     * Value of {@link #COLUMN_REASON} when an error receiving or processing
     * data occurred at the HTTP level.
     */
    public final static int ERROR_HTTP_DATA_ERROR = 1004;

    /**
     * Value of {@link #COLUMN_REASON} when there were too many redirects.
     */
    public final static int ERROR_TOO_MANY_REDIRECTS = 1005;

    /**
     * Value of {@link #COLUMN_REASON} when there was insufficient storage
     * space. Typically, this is because the SD card is full.
     */
    public final static int ERROR_INSUFFICIENT_SPACE = 1006;

    /**
     * Value of {@link #COLUMN_REASON} when no external storage device was
     * found. Typically, this is because the SD card is not mounted.
     */
    public final static int ERROR_DEVICE_NOT_FOUND = 1007;

    /**
     * Value of {@link #COLUMN_REASON} when some possibly transient error
     * occurred but we can't resume the download.
     */
    public final static int ERROR_CANNOT_RESUME = 1008;

    /**
     * Value of {@link #COLUMN_REASON} when the requested destination file
     * already exists (the download manager will not overwrite an existing
     * file).
     */
    public final static int ERROR_FILE_ALREADY_EXISTS = 1009;

    /**
     * Value of {@link #COLUMN_REASON} when the download is paused because some
     * network error occurred and the download manager is waiting before
     * retrying the request.
     */
    public final static int PAUSED_WAITING_TO_RETRY = 1;

    /**
     * Value of {@link #COLUMN_REASON} when the download is waiting for network
     * connectivity to proceed.
     */
    public final static int PAUSED_WAITING_FOR_NETWORK = 2;

    /**
     * Value of {@link #COLUMN_REASON} when the download exceeds a size limit
     * for downloads over the mobile network and the download manager is waiting
     * for a Wi-Fi connection to proceed.
     */
    public final static int PAUSED_QUEUED_FOR_WIFI = 3;

    /**
     * Value of {@link #COLUMN_REASON} when the download is paused for some
     * other reason.
     */
    public final static int PAUSED_UNKNOWN = 4;

    /**
     * Broadcast intent action sent by the download manager when a download
     * completes.
     */
    public final static String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";

    /**
     * Broadcast intent action sent by the download manager when the user clicks
     * on a running download, either from a system notification or from the
     * downloads UI.
     */
    public final static String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";

    /**
     * Intent action to launch an activity to display all downloads.
     */
    public final static String ACTION_VIEW_DOWNLOADS = "android.intent.action.VIEW_DOWNLOADS";

    /**
     * Intent extra included with {@link #ACTION_DOWNLOAD_COMPLETE} intents,
     * indicating the ID (as a long) of the download that just completed.
     */
    public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";

    // this array must contain all public columns
    private static final String[] COLUMNS = new String[]{COLUMN_ID,
            COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_URI, COLUMN_MEDIA_TYPE,
            COLUMN_TOTAL_SIZE_BYTES, COLUMN_LOCAL_URI, COLUMN_STATUS,
            COLUMN_REASON, COLUMN_BYTES_DOWNLOADED_SO_FAR,
            COLUMN_LAST_MODIFIED_TIMESTAMP};
    // columns to request from DownloadProvider
    private static final String[] UNDERLYING_COLUMNS = new String[]{
            Downloads._ID, Downloads.COLUMN_TITLE,
            Downloads.COLUMN_DESCRIPTION, Downloads.COLUMN_URI,
            Downloads.COLUMN_MIME_TYPE, Downloads.COLUMN_TOTAL_BYTES,
            Downloads.COLUMN_STATUS, Downloads.COLUMN_CURRENT_BYTES,
            Downloads.COLUMN_LAST_MODIFICATION, Downloads.COLUMN_DESTINATION,
            Downloads.COLUMN_FILE_NAME_HINT, Downloads._DATA,};

    private static final Set<String> LONG_COLUMNS = new HashSet<String>(
            Arrays.asList(COLUMN_ID, COLUMN_TOTAL_SIZE_BYTES, COLUMN_STATUS,
                    COLUMN_REASON, COLUMN_BYTES_DOWNLOADED_SO_FAR,
                    COLUMN_LAST_MODIFIED_TIMESTAMP));


    private static class CursorTranslator extends CursorWrapper {
        public CursorTranslator(Cursor cursor, Uri baseUri) {
            super(cursor);
        }

        @Override
        public int getColumnIndex(String columnName) {
            return Arrays.asList(COLUMNS).indexOf(columnName);
        }

        @Override
        public int getColumnIndexOrThrow(String columnName)
                throws IllegalArgumentException {
            int index = getColumnIndex(columnName);
            if (index == -1) {
                throw new IllegalArgumentException("No such column: "
                        + columnName);
            }
            return index;
        }

        @Override
        public String getColumnName(int columnIndex) {
            int numColumns = COLUMNS.length;
            if (columnIndex < 0 || columnIndex >= numColumns) {
                throw new IllegalArgumentException("Invalid column index "
                        + columnIndex + ", " + numColumns + " columns exist");
            }
            return COLUMNS[columnIndex];
        }

        @Override
        public String[] getColumnNames() {
            String[] returnColumns = new String[COLUMNS.length];
            System.arraycopy(COLUMNS, 0, returnColumns, 0, COLUMNS.length);
            return returnColumns;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getDouble(int columnIndex) {
            return getLong(columnIndex);
        }

        private boolean isLongColumn(String column) {
            return LONG_COLUMNS.contains(column);
        }

        @Override
        public float getFloat(int columnIndex) {
            return (float) getDouble(columnIndex);
        }

        @Override
        public int getInt(int columnIndex) {
            return (int) getLong(columnIndex);
        }

        @Override
        public long getLong(int columnIndex) {
            return translateLong(getColumnName(columnIndex));
        }

        @Override
        public short getShort(int columnIndex) {
            return (short) getLong(columnIndex);
        }

        @Override
        public String getString(int columnIndex) {
            return translateString(getColumnName(columnIndex));
        }

        private String translateString(String column) {
            if (isLongColumn(column)) {
                return Long.toString(translateLong(column));
            }
            if (column.equals(COLUMN_TITLE)) {
                return getUnderlyingString(Downloads.COLUMN_TITLE);
            }
            if (column.equals(COLUMN_DESCRIPTION)) {
                return getUnderlyingString(Downloads.COLUMN_DESCRIPTION);
            }
            if (column.equals(COLUMN_URI)) {
                return getUnderlyingString(Downloads.COLUMN_URI);
            }
            if (column.equals(COLUMN_MEDIA_TYPE)) {
                return getUnderlyingString(Downloads.COLUMN_MIME_TYPE);
            }

            assert column.equals(COLUMN_LOCAL_URI);
            return getLocalUri();
        }

        private String getLocalUri() {
            String localPath = getUnderlyingString(Downloads._DATA);
            if (localPath == null) {
                return null;
            }
            return Uri.fromFile(new File(localPath)).toString();
        }

        private long translateLong(String column) {
            if (!isLongColumn(column)) {
                // mimic behavior of underlying cursor -- most likely, throw
                // NumberFormatException
                return Long.valueOf(translateString(column));
            }

            if (column.equals(COLUMN_ID)) {
                return getUnderlyingLong(Downloads._ID);
            }
            if (column.equals(COLUMN_TOTAL_SIZE_BYTES)) {
                return getUnderlyingLong(Downloads.COLUMN_TOTAL_BYTES);
            }
            if (column.equals(COLUMN_STATUS)) {
                return translateStatus((int) getUnderlyingLong(Downloads.COLUMN_STATUS));
            }
            if (column.equals(COLUMN_REASON)) {
                return getReason((int) getUnderlyingLong(Downloads.COLUMN_STATUS));
            }
            if (column.equals(COLUMN_BYTES_DOWNLOADED_SO_FAR)) {
                return getUnderlyingLong(Downloads.COLUMN_CURRENT_BYTES);
            }
            assert column.equals(COLUMN_LAST_MODIFIED_TIMESTAMP);
            return getUnderlyingLong(Downloads.COLUMN_LAST_MODIFICATION);
        }

        private long getReason(int status) {
            switch (translateStatus(status)) {
                case STATUS_FAILED:
                    return getErrorCode(status);

                case STATUS_PAUSED:
                    return getPausedReason(status);

                default:
                    return 0; // arbitrary value when status is not an error
            }
        }

        private long getPausedReason(int status) {
            switch (status) {
                case Downloads.STATUS_WAITING_TO_RETRY:
                    return PAUSED_WAITING_TO_RETRY;

                case Downloads.STATUS_WAITING_FOR_NETWORK:
                    return PAUSED_WAITING_FOR_NETWORK;

                case Downloads.STATUS_QUEUED_FOR_WIFI:
                    return PAUSED_QUEUED_FOR_WIFI;

                default:
                    return PAUSED_UNKNOWN;
            }
        }

        private long getErrorCode(int status) {
            if ((400 <= status && status < Downloads.MIN_ARTIFICIAL_ERROR_STATUS)
                    || (500 <= status && status < 600)) {
                // HTTP status code
                return status;
            }

            switch (status) {
                case Downloads.STATUS_FILE_ERROR:
                    return ERROR_FILE_ERROR;

                case Downloads.STATUS_UNHANDLED_HTTP_CODE:
                case Downloads.STATUS_UNHANDLED_REDIRECT:
                    return ERROR_UNHANDLED_HTTP_CODE;

                case Downloads.STATUS_HTTP_DATA_ERROR:
                    return ERROR_HTTP_DATA_ERROR;

                case Downloads.STATUS_TOO_MANY_REDIRECTS:
                    return ERROR_TOO_MANY_REDIRECTS;

                case Downloads.STATUS_INSUFFICIENT_SPACE_ERROR:
                    return ERROR_INSUFFICIENT_SPACE;

                case Downloads.STATUS_DEVICE_NOT_FOUND_ERROR:
                    return ERROR_DEVICE_NOT_FOUND;

                case Downloads.STATUS_CANNOT_RESUME:
                    return ERROR_CANNOT_RESUME;

                case Downloads.STATUS_FILE_ALREADY_EXISTS_ERROR:
                    return ERROR_FILE_ALREADY_EXISTS;

                default:
                    return ERROR_UNKNOWN;
            }
        }

        private long getUnderlyingLong(String column) {
            return super.getLong(super.getColumnIndex(column));
        }

        private String getUnderlyingString(String column) {
            return super.getString(super.getColumnIndex(column));
        }

        private int translateStatus(int status) {
            switch (status) {
                case Downloads.STATUS_PENDING:
                    return STATUS_PENDING;

                case Downloads.STATUS_RUNNING:
                    return STATUS_RUNNING;

                case Downloads.STATUS_PAUSED_BY_APP:
                case Downloads.STATUS_WAITING_TO_RETRY:
                case Downloads.STATUS_WAITING_FOR_NETWORK:
                case Downloads.STATUS_QUEUED_FOR_WIFI:
                    return STATUS_PAUSED;

                case Downloads.STATUS_SUCCESS:
                    return STATUS_SUCCESSFUL;

                default:
                    assert Downloads.isStatusError(status);
                    return STATUS_FAILED;
            }
        }
    }
}
