package vn.appsmobi.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.DownloadInstallManager;
import vn.appsmobi.utils.LocalAppManager;

/**
 * Created by tobrother on 20/01/2016.
 */
public class ApplicationItem extends DataCardItem {
    public static String APPLICATION_ITEM = "ApplicationItem";
    private String promo_video;
    private ArrayList<String> slide_show;
    private String version_compatible;
    private String agency_id;
    private String detail;
    private Calendar uploadDate;

    public ApplicationItem(DataCardItem dataCardItem) {
        super(dataCardItem);

    }

    public DataCardItem getDataCardItem() {
        return this;
    }

    public ApplicationItem() {
        super();
        //  setDataCardItemMethod(dataCardItemMethod);
    }

    public void valueOf(JSONObject jsonObject) throws JSONException {
        super.valueOf(jsonObject);
        if (!jsonObject.isNull(Constants.JSON_PARSE.VERSION_COMPATIBLE)) {
            this.setVersion_compatible(jsonObject.getString(Constants.JSON_PARSE.VERSION_COMPATIBLE));
        }

        if (!jsonObject.isNull(Constants.JSON_PARSE.AGENCY_ID)) {
            this.setAgency_id(jsonObject.getString(Constants.JSON_PARSE.AGENCY_ID));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.SLIDE_SHOW)) {
            JSONArray promoImages = jsonObject.getJSONArray(Constants.JSON_PARSE.SLIDE_SHOW);
            ArrayList<String> arrayListPromoImages = new ArrayList<>();
            for (int i = 0; i < promoImages.length(); i++) {
                arrayListPromoImages.add(promoImages.get(i).toString());
            }
            this.setSlide_show(arrayListPromoImages);
        }
        if (jsonObject.has(Constants.JSON_PARSE.HASH)) {
            this.setHash(jsonObject.getString(Constants.JSON_PARSE.HASH));
        }
        if (jsonObject.has(Constants.JSON_PARSE.DETAIL)) {
            this.setDetail(jsonObject.getString(Constants.JSON_PARSE.DETAIL));
        }
    }
    /*
    public static ApplicationItem get(JSONObject json,int data_type) {
        ApplicationItem item = new ApplicationItem();
        item.setData_type(data_type);
        try {
            item.valueOf(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return DataCardItem.cacheOrUpdate(item);
    }
    */


    public String getPromo_video() {
        return promo_video;
    }

    public void setPromo_video(String promo_video) {
        this.promo_video = promo_video;
    }

    public String getVersion_compatible() {
        return version_compatible;
    }

    public void setVersion_compatible(String version_compatible) {
        this.version_compatible = version_compatible;
    }

    public ArrayList<String> getSlide_show() {
        return slide_show;
    }

    public void setSlide_show(ArrayList<String> slide_show) {
        this.slide_show = slide_show;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public Calendar getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Calendar uploadDate) {
        this.uploadDate = uploadDate;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    /*
    public static ArrayList<ApplicationItem> valueOfList(JSONArray array) throws JSONException {
        ArrayList<ApplicationItem> applicationItems = new ArrayList<>();
        for (int i = 0 ; i< array.length(); i++) {
            ApplicationItem item = new ApplicationItem();
            item.valueOf(array.getJSONObject(i));
            applicationItems.add(item);
        }
        return applicationItems;
    }
    */
    /**
     * todo : override parent
     */

    // implement method
    DataCardItem.DataCardItemMethod dataCardItemMethod = new DataCardItem.DataCardItemMethod() {
        int i = 0;

        @Override
        public void mUpdateAppStatus() {

            LocalAppManager localManager = LocalAppManager.getManager();
            int currentCardStatus = getCard_status();
            setCard_status(Constants.CARD_STATUS.STATUS_NORMAL);
            if (localManager.isInstalled(getPackge_name())) {
                setCard_status(Constants.CARD_STATUS.STATUS_INSTALLED);
            }
            if (DownloadInstallManager.getManager().isDownloadingOrInstalling(getId()))
                setCard_status(Constants.CARD_STATUS.STATUS_INSTALLING);
            if (currentCardStatus != getCard_status()) {

                //Log.v("MarketAppInfo status of app " + this.packageName + " has changed from " + currentStatus + " to " + this.status);
                if (mBaseListeners != null) {
                    Iterator<DataCardItem.AppInfoUpdateListener> iterator = mBaseListeners.iterator();
                    while (iterator.hasNext()) {
                        Log.e("status_update" + String.valueOf(i++), "true");
                        ((DataCardItem.AppInfoUpdateListener) iterator.next()).onStatusUpdate(getDataCardItem());
                    }
                }

            }
        }

    };


}
