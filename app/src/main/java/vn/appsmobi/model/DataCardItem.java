package vn.appsmobi.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.DownloadInstallManager;
import vn.appsmobi.utils.LocalAppManager;
import vn.appsmobi.utils.Utils;

/**
 * Created by tobrother on 18/01/2016.
 */
public class DataCardItem implements Parcelable {
    private String id;
    private String name; //= kendall & kylie
    private String author; //= glu
    private String resource_url; //=link tai
    private String icon_image;
    private String short_description; //=mô tả ngắn
    private String promo_image; //=hình quảng cáo
    private ArrayList<CatetoryItem> catetoryItems;
    private ArrayList<ListVersion> listVersions;
    private int status; //=trạng thái nút tải
    private float size; //
    private float rate; //
    private int downs_count; //=số lượt download
    private String date; //=ngày tải
    private String content; //=mô tả chi tiết
    private int data_type; //=là app hoặc game
    // for applications
    private String package_name;
    private int version_code;
    private int card_status;
    private String version_name;
    private long download_id;
    private String hash;
    private String source;
    private String promo_video;
    // for ringtone

    // for books

    // for wallpaper

    // listCache
    public static DataCache listCache;
    // mảng listeners
    public CopyOnWriteArraySet<AppInfoUpdateListener> mBaseListeners;


    private DataCardItemMethod dataCardItemMethod;

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }


    // interface todo : update view for this data card view
    public static abstract interface AppInfoUpdateListener {
        public abstract void onContentUpdate(DataCardItem appInfo);

        public abstract void onStatusUpdate(DataCardItem appInfo);
    }

    // remove interface
    public void removeUpdateListener(AppInfoUpdateListener listener) {
        if (listener != null) {
            try {
                if (this.mBaseListeners != null)
                    this.mBaseListeners.remove(listener);
            } finally {
            }
        }
    }

    // add interface into list interface object
    public void addUpdateListener(AppInfoUpdateListener listener) {
        //Log.e("listener id add "+this.getName(),String.valueOf(listener));
        // Log.e("listener card type "+this.getName(),this.getId());
        if (listener != null) {
            if (this.mBaseListeners == null)
                this.mBaseListeners = new CopyOnWriteArraySet<AppInfoUpdateListener>();
            this.mBaseListeners.add(listener);
        }
    }

    /**
     * interface todo : interface for child implement
     */
    public static abstract interface DataCardItemMethod {
        public abstract void mUpdateAppStatus();
    }

    public DataCardItemMethod getDataCardItemMethod() {
        return dataCardItemMethod;
    }

    public void setDataCardItemMethod(DataCardItemMethod dataCardItemMethod) {
        this.dataCardItemMethod = dataCardItemMethod;
    }

    /**
     * Update app status :
     * update status
     */

    //Dựa vào CARD_STATUS để biết được trạng thái của app đó
    public void updateAppStatus() {
        LocalAppManager localManager = LocalAppManager.getManager();
        int currentCardStatus = getCard_status();
        setCard_status(Constants.CARD_STATUS.STATUS_NORMAL);
        if (localManager.isInstalled(getPackge_name())) {
            setCard_status(Constants.CARD_STATUS.STATUS_INSTALLED);
        }
        if (DownloadInstallManager.getManager().isDownloadingOrInstalling(getId()))
            setCard_status(Constants.CARD_STATUS.STATUS_INSTALLING);
        if (currentCardStatus != getCard_status()) {
            if (mBaseListeners != null) {
                //Log.e("DataCardItem --- mBaseListeners size 2"+this.getName()+" -- ",String.valueOf(mBaseListeners.size()));
                Iterator<DataCardItem.AppInfoUpdateListener> iterator = mBaseListeners.iterator();
                while (iterator.hasNext()) {
                    //Log.e("DataCardItem --- update iterator "+this.getName()+" -- ",String.valueOf(mBaseListeners.size()));
                    // Log.e("listener id get "+this.getName(),String.valueOf((DataCardItem.AppInfoUpdateListener) iterator.next()));
                    ((DataCardItem.AppInfoUpdateListener) iterator.next()).onStatusUpdate(this);
                }
            }
        }
    }

    /**
     * get DataCardItem from it's ID
     *
     * @param id
     * @return
     */
    public static DataCardItem get(String id) {
        return listCache.get(id);
    }

    /**
     * get DataCardItem and insert into ListCache
     *
     * @param dataCardItem
     * @return
     */
    public static DataCardItem cacheOrUpdate(DataCardItem dataCardItem) {
        return listCache.cache(dataCardItem);
    }


    /**
     * get DatacardItem from Json of service API
     *
     * @param json
     * @return
     */
    public static DataCardItem get(JSONObject json, int data_type) {
        DataCardItem item = new DataCardItem();
        item.setData_type(data_type);
        try {
            item.valueOf(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return DataCardItem.cacheOrUpdate(item);
    }

    /**
     * get DataCardItem from package name
     *
     * @param packagename
     * @return DataCardItem with package name
     */
    public static DataCardItem getAppByPackage(String packagename) {
        return listCache.getAppByPackage(packagename);
    }

    /**
     * check can install or update
     *
     * @return
     */
    public boolean canInstallOrUpdate() {
        DataCardItem localApp = LocalAppManager.getManager().getLocalAppInfo(this.package_name); // lấy app này đã cài trên máy so sánh với version code nhận được trên server
        // if have not app in this device => can install
        if (localApp == null) {
            return true;
        } else {

        }
        // else if have app in this device and new version code > old version code
        if (localApp != null && localApp.getVersion_code() < this.getVersion_code()) {
            return true;
        }
        // with ringtone , wallpaper , book , movie
        /*
        if(appinfo != null ){
            return true;
        }
        */
        return false;
    }

    public boolean isDownloaded() {
        if (Utils.getFileFrom(this).exists()) {
            return true;
        }
        return false;
    }
    /**
     * todo Class DataCache
     */
    public static class DataCache {
        private ConcurrentHashMap<String, DataCardItem> listCache; // load all card data và lưu vao 1 danh sách
        private LocalAppManager mAppManager;
        private ConcurrentHashMap<String, CopyOnWriteArraySet<String>> mPackageToAppIdMap;
        private ConcurrentHashMap<String, CopyOnWriteArraySet<String>> mFolderToAppIdMap;

        public DataCache(Context context) {
            this.listCache = new ConcurrentHashMap<String, DataCardItem>();
            this.mPackageToAppIdMap = new ConcurrentHashMap<String, CopyOnWriteArraySet<String>>();
            this.mAppManager = LocalAppManager.getManager();
            this.mAppManager.addUpdateListener(this.mUpdateListener);
            DownloadInstallManager.getManager().addTaskListener(this.mTaskListener);
        }

        /**
         * add data card Id of card này vào danh sách id cache ID
         *
         * @param dataCardItem
         */
        private void addToPackageIdMap(DataCardItem dataCardItem) {

            CopyOnWriteArraySet<String> arraySet = (CopyOnWriteArraySet<String>) this.mPackageToAppIdMap.get(dataCardItem.getPackge_name());
            if (arraySet == null) {
                arraySet = new CopyOnWriteArraySet<String>();
                this.mPackageToAppIdMap.put(dataCardItem.getPackge_name(), arraySet);
            }
            if (!arraySet.contains(dataCardItem.getId()))
                arraySet.add(dataCardItem.getId());

        }

        /**
         * add to folder book , ringtone , image downloaded follow name
         *
         * @param dataCardItem
         */
        private void addToFolderSourceIdMap(DataCardItem dataCardItem) {
            CopyOnWriteArraySet<String> arraySet = (CopyOnWriteArraySet<String>) this.mFolderToAppIdMap.get(dataCardItem.getName());
            if (arraySet == null) {
                arraySet = new CopyOnWriteArraySet<String>();
                this.mFolderToAppIdMap.put(dataCardItem.getName(), arraySet);
            }
            if (!arraySet.contains(dataCardItem.getId()))
                arraySet.add(dataCardItem.getId());
        }

        /**
         * Đưa data card này vào danh sách cache nếu chưa đưa vào còn nếu đã đưa vào thì update lại thông tin
         *
         * @param appinfo
         * @return
         */
        public DataCardItem cache(DataCardItem appinfo) {
            if (appinfo != null) {
                addToPackageIdMap(appinfo);
                if (!this.listCache.containsKey(appinfo.getId())) {
                    this.listCache.put(appinfo.getId(), appinfo);
                    appinfo.updateAppStatus();

                } else {
                    DataCardItem localApp = (DataCardItem) this.listCache.get(appinfo.getId());
                    if (appinfo.getShort_description() != null) {
                        localApp.setShort_description(appinfo.getShort_description());
                        localApp.setDowns_count(appinfo.getDowns_count());
                        localApp.setVersion_name(appinfo.getVersion_name());
                        localApp.setVersion_code(appinfo.getVersion_code());
                        localApp.setHash(appinfo.getHash());
                        localApp.setData_type(appinfo.getData_type());
                        localApp.setIcon_image(appinfo.getIcon_image());
                        // loi loop vo han
                        //localApp.setCard_status(appinfo.getCard_status());
                        //localApp.setUploadDate(appinfo.getUploadDate());
                        //if(localApp.getAgencyId() != null)
                        //   localApp.setAgencyid(appinfo.getAgencyId());
                        this.listCache.remove(appinfo.getId());
                        this.listCache.put(appinfo.getId(), localApp);

                    }
                    localApp.updateAppStatus();
                    return localApp;
                }
            }
            return appinfo;
        }

        public ArrayList<DataCardItem> getAppDownloading() {
            ArrayList<DataCardItem> listDownloading = new ArrayList<>();
            for (String id : this.listCache.keySet()) {
                if (get(id).getCard_status() == Constants.CARD_STATUS.STATUS_INSTALLING)
                    listDownloading.add(get(id));
            }
            return listDownloading;
        }

        /**
         * get data card theo ID
         *
         * @param appid
         * @return
         */
        public DataCardItem get(String appid) {
            DataCardItem appInfo;
            if (TextUtils.isEmpty(appid))
                appInfo = null;
            else {
                appInfo = (DataCardItem) this.listCache.get(appid);
            }
            return appInfo;
        }

        /**
         * get data card theo package name
         *
         * @param package_name
         * @return
         */
        // get package name with application
        public DataCardItem getAppByPackage(String package_name) {
            Iterator<String> iterator = null;
            if ((this.mPackageToAppIdMap != null) && (!this.mPackageToAppIdMap.isEmpty())) {
                CopyOnWriteArraySet<String> localCopyOnWriteArraySet = (CopyOnWriteArraySet<String>) this.mPackageToAppIdMap.get(package_name);
                if ((localCopyOnWriteArraySet != null) && (!localCopyOnWriteArraySet.isEmpty())) {
                    iterator = localCopyOnWriteArraySet.iterator();
                }
                while (iterator.hasNext()) {
                    DataCardItem appinfo = get((String) iterator.next());
                    if (appinfo != null) {
                        return appinfo;
                    }
                }
            }
            return null;
        }

        // get package name with application
        public DataCardItem getAppByFolder(String folder_uri) {
            Iterator<String> iterator = null;
            if ((this.mPackageToAppIdMap != null) && (!this.mPackageToAppIdMap.isEmpty())) {
                CopyOnWriteArraySet<String> localCopyOnWriteArraySet = (CopyOnWriteArraySet<String>) this.mFolderToAppIdMap.get(folder_uri);
                if ((localCopyOnWriteArraySet != null) && (!localCopyOnWriteArraySet.isEmpty()))
                    iterator = localCopyOnWriteArraySet.iterator();
                while (iterator.hasNext()) {
                    DataCardItem appinfo = get((String) iterator.next());
                    if (appinfo != null) {
                        return appinfo;
                    }
                }
            }
            return null;
        }
        // get resource folder with ringtone , wallpaper , book
        //public DataCardItem getResource(){
        //}
        /**
         * Interface todo : thực hiện download task
         */
        private DownloadInstallManager.TaskListener mTaskListener = new DownloadInstallManager.TaskListener() {
            public void onTaskFail(String appid, int error) {
                DataCardItem appInfo = DataCardItem.DataCache.this.get(appid);
                appInfo.updateAppStatus();
            }

            public void onTaskStart(String appid) {
                DataCardItem appInfo = DataCardItem.DataCache.this.get(appid);
                appInfo.updateAppStatus();
            }

            public void onTaskSuccess(String appid) {
                DataCardItem appInfo = DataCardItem.DataCache.this.get(appid);
                appInfo.updateAppStatus();
            }
        };
        /**
         * Interface todo : update local app : update status download or installed
         */
        private LocalAppManager.LocalAppInfoUpdateListener mUpdateListener = new LocalAppManager.LocalAppInfoUpdateListener() {
            private void updateInstalledStatus(DataCardItem appinfo) {
                DataCardItem app = DataCardItem.get(appinfo.getId());
                app.updateAppStatus();
            }

            @Override
            public void onContentChanged() {
                Iterator<LocalDataCardItem> iterator = DataCardItem.DataCache.this.mAppManager.getInstalledApp().iterator();
                while (iterator.hasNext()) {
                    try {
                        DataCardItem app = DataCardItem.getAppByPackage(((iterator.next()).getPackge_name()));
                        if (app != null)
                            updateInstalledStatus(app);
                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onContentChanged(DataCardItem appInfo) {
                // TODO Auto-generated method stub
                updateInstalledStatus(appInfo);
            }

            @Override
            public void onListChanged() {
                // TODO Auto-generated method stub
                Iterator<LocalDataCardItem> iterator = DataCardItem.DataCache.this.mAppManager.getInstalledApp().iterator();
                while (iterator.hasNext()) {
                    try {
                        DataCardItem app = DataCardItem.getAppByPackage(((iterator.next()).getPackge_name()));
                        if (app != null)
                            updateInstalledStatus(app);
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onListChanged(DataCardItem appInfo) {
                updateInstalledStatus(appInfo);
            }

            @Override
            public void onLocalInstalledLoaded() {
                Iterator<LocalDataCardItem> iterator = DataCardItem.DataCache.this.mAppManager.getInstalledApp().iterator();
                while (iterator.hasNext()) {
                    try {
                        DataCardItem app = DataCardItem.getAppByPackage(((iterator.next()).getPackge_name()));
                        if (app != null)
                            updateInstalledStatus(app);
                    } catch (Exception e) {
                    }
                }
            }

        };
    }

    /**
     * todo init function
     */
    // constructor for app
    public DataCardItem(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public DataCardItem(DataCardItem dataCardItem) {
        this.setName(dataCardItem.getName());
        this.setId(dataCardItem.getId());
        this.setAuthor(dataCardItem.getAuthor());
        this.setCatetoryItems(dataCardItem.getCatetoryItems());
        this.setListVersions(dataCardItem.getListVersions());
        this.setDowns_count(dataCardItem.getDowns_count());
        this.setIcon_image(dataCardItem.getIcon_image());
        this.setShort_description(dataCardItem.getShort_description());
        this.setSize(dataCardItem.getSize());
        this.setStatus(dataCardItem.getStatus());
        this.setRate(dataCardItem.getRate());
        this.setPromo_image(dataCardItem.getPromo_image());
        this.setPromo_video(dataCardItem.getPromo_video());
        this.setResource_url(dataCardItem.getResource_url());
        this.setDate(dataCardItem.getDate());
        //
        this.setHash(dataCardItem.getHash());
        this.setPackage_name(dataCardItem.getPackge_name());
        this.setVersion_code(dataCardItem.getVersion_code());
        this.setVersion_name(dataCardItem.getVersion_name());
        this.setListVersions(dataCardItem.getListVersions());
        this.setDownload_id(dataCardItem.getDownload_id());
        this.setCard_status(dataCardItem.getCard_status());
        this.setData_type(dataCardItem.getData_type());
    }

    public DataCardItem() {
    }

    protected DataCardItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        author = in.readString();
        resource_url = in.readString();
        icon_image = in.readString();
        short_description = in.readString();
        promo_image = in.readString();
        promo_video = in.readString();
        catetoryItems = in.createTypedArrayList(CatetoryItem.CREATOR);
        listVersions = in.createTypedArrayList(ListVersion.CREATOR);
        status = in.readInt();
        size = in.readFloat();
        rate = in.readFloat();
        downs_count = in.readInt();
        content = in.readString();
        date = in.readString();
        package_name = in.readString();
        version_name = in.readString();
        version_code = in.readInt();
        download_id = in.readLong();
        hash = in.readString();
        card_status = in.readInt();
        data_type = in.readInt();
    }

    // init listCache in Singleton
    public static void init(Context context) {
        if (listCache == null)
            listCache = new DataCache(context);
    }

    public static final Creator<DataCardItem> CREATOR = new Creator<DataCardItem>() {
        @Override
        public DataCardItem createFromParcel(Parcel in) {

            return new DataCardItem(in);
        }

        @Override
        public DataCardItem[] newArray(int size) {
            return new DataCardItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(resource_url);
        dest.writeString(icon_image);
        dest.writeString(short_description);
        dest.writeString(promo_image);
        dest.writeString(promo_video);
        dest.writeTypedList(catetoryItems);
        dest.writeTypedList(listVersions);
        dest.writeInt(status);
        dest.writeFloat(size);
        dest.writeFloat(rate);
        dest.writeInt(downs_count);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(package_name);
        dest.writeString(version_name);
        dest.writeInt(version_code);
        dest.writeLong(download_id);
        dest.writeString(hash);
        dest.writeInt(card_status);
        dest.writeInt(data_type);
    }

    // get data for child item card data from Json Object
    public void valueOf(JSONObject jsonObject) throws JSONException {
        if (!jsonObject.isNull(Constants.JSON_PARSE.ID)) {
            this.setId(jsonObject.getString(Constants.JSON_PARSE.ID));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.NAME)) {
            this.setName(jsonObject.getString(Constants.JSON_PARSE.NAME));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.RESOURCE_URL)) {
            this.setResource_url(jsonObject.getString(Constants.JSON_PARSE.RESOURCE_URL));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.CATEGORIES)) {
            this.setCatetoryItems(CatetoryItem.ValueOfList(jsonObject.getJSONArray(Constants.JSON_PARSE.CATEGORIES)));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.LIST_VERSION)) {
            this.setCatetoryItems(CatetoryItem.ValueOfList(jsonObject.getJSONArray(Constants.JSON_PARSE.LIST_VERSION)));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.STATUS)) {
            this.setStatus(jsonObject.getInt(Constants.JSON_PARSE.STATUS));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.DOWNS_COUNT)) {
            this.setDowns_count(jsonObject.getInt(Constants.JSON_PARSE.DOWNS_COUNT));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.SIZE)) {
            this.setSize((float) jsonObject.getDouble(Constants.JSON_PARSE.SIZE));
        }else {
            this.setSize(0);
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.RATE)) {
            this.setRate((float) jsonObject.getDouble(Constants.JSON_PARSE.RATE));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.ICON_IMAGE)) {
            this.setIcon_image(jsonObject.getString(Constants.JSON_PARSE.ICON_IMAGE));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.SHORT_DESCRIPTION)) {
            this.setShort_description(jsonObject.getString(Constants.JSON_PARSE.SHORT_DESCRIPTION));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.AUTHOR)) {
            this.setAuthor(jsonObject.getString(Constants.JSON_PARSE.AUTHOR));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.PROMO_IMAGE)) {
            this.setPromo_image(jsonObject.getString(Constants.JSON_PARSE.PROMO_IMAGE));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.PROMO_VIDEO)) {
            this.setPromo_video(jsonObject.getString(Constants.JSON_PARSE.PROMO_VIDEO));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.CONTENT)) {
            this.setContent(jsonObject.getString(Constants.JSON_PARSE.CONTENT));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.DATE)) {
            this.setDate(jsonObject.getString(Constants.JSON_PARSE.DATE));
        }
        // this is for app cache
        if (!jsonObject.isNull(Constants.JSON_PARSE.PACKAGE_NAME)) {
            this.setPackage_name(jsonObject.getString(Constants.JSON_PARSE.PACKAGE_NAME));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.VERSION_CODE)) {
            this.setVersion_code(jsonObject.getInt(Constants.JSON_PARSE.VERSION_CODE));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.VERSION_NAME)) {
            this.setVersion_name(jsonObject.getString(Constants.JSON_PARSE.VERSION_NAME));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.HASH)) {
            this.setHash(jsonObject.getString(Constants.JSON_PARSE.HASH));
        }
    }


    public static ArrayList<DataCardItem> valueOfList(JSONArray array) throws JSONException {
        ArrayList<DataCardItem> dataCardItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            DataCardItem item = new DataCardItem();
            item.valueOf(array.getJSONObject(i));
            dataCardItems.add(item);
        }
        return dataCardItems;
    }


    /**
     * todo : get set function
     */
    public int getCard_status() {
        return card_status;
    }

    public void setCard_status(int card_status) {
        this.card_status = card_status;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public long getDownload_id() {
        return download_id;
    }

    public void setDownload_id(long download_id) {
        this.download_id = download_id;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackge_name() {
        return this.package_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getPromo_image() {
        return promo_image;
    }

    public void setPromo_image(String promo_image) {
        this.promo_image = promo_image;
    }


    public String getPromo_video() {
        return promo_video;
    }

    public void setPromo_video(String promo_video) {
        this.promo_video = promo_video;
    }


    public ArrayList<CatetoryItem> getCatetoryItems() {
        return catetoryItems;
    }

    public void setCatetoryItems(ArrayList<CatetoryItem> catetoryItems) {
        this.catetoryItems = catetoryItems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getDowns_count() {
        return downs_count;
    }

    public void setDowns_count(int downs_count) {
        this.downs_count = downs_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ListVersion> getListVersions() {
        return listVersions;
    }

    public void setListVersions(ArrayList<ListVersion> listVersions) {
        this.listVersions = listVersions;
    }


}
