package vn.appsmobi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.DownloadInstallManager;
import vn.appsmobi.utils.LocalAppManager;
import vn.appsmobi.utils.Utils;

/**
 * Created by tobrother on 21/01/2016.
 */
public class BookItem extends DataCardItem {
    private String file_type;
    private ArrayList<String> slide_show;

    public BookItem getDataCardItem() {
        return this;
    }

    public BookItem() {
        super();
    }

    public BookItem(DataCardItem dataCardItem) {
        super(dataCardItem);
    }

    public void valueOf(JSONObject jsonObject) throws JSONException {
        super.valueOf(jsonObject);
        if (!jsonObject.isNull(Constants.JSON_PARSE.FILE_TYPE)) {
            this.setFile_type(jsonObject.getString(Constants.JSON_PARSE.FILE_TYPE));
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.SLIDE_SHOW)) {
            JSONArray promoImages = jsonObject.getJSONArray(Constants.JSON_PARSE.SLIDE_SHOW);
            ArrayList<String> arrayListPromoImages = new ArrayList<>();
            for (int i = 0; i < promoImages.length(); i++) {
                arrayListPromoImages.add(promoImages.get(i).toString());
            }
            this.setSlide_show(arrayListPromoImages);
        }
    }

    public ArrayList<BookItem> valueOfList(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray(Constants.JSON_PARSE.CARD_DATA);
        ArrayList<BookItem> bookItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            BookItem item = new BookItem();
            item.valueOf(array.getJSONObject(i));
            bookItems.add(item);
        }
        return bookItems;
    }

    public ArrayList<String> getSlide_show() {
        return slide_show;
    }

    public void setSlide_show(ArrayList<String> slide_show) {
        this.slide_show = slide_show;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    /**
     * todo : override parent
     */
    // implement method
    DataCardItemMethod dataCardItemMethod = new DataCardItemMethod() {
        @Override
        public void mUpdateAppStatus() {
            LocalAppManager localManager = LocalAppManager.getManager();
            int currentCardStatus = getCard_status();
            setCard_status(Constants.CARD_STATUS.STATUS_NORMAL);
            if (Utils.checkFolderExist(Constants.DOWNLOAD_FOLDER + Constants.BOOK_FOLDER + "/" + getName()))
                setCard_status(Constants.CARD_STATUS.STATUS_DOWNLOADED);
            if (DownloadInstallManager.getManager().isDownloadingOrInstalling(getId()))
                setCard_status(Constants.CARD_STATUS.STATUS_INSTALLING);
            if (currentCardStatus != getCard_status()) {
                //Log.v("MarketAppInfo status of app " + this.packageName + " has changed from " + currentStatus + " to " + this.status);
                if (mBaseListeners != null) {
                    Iterator<AppInfoUpdateListener> iterator = mBaseListeners.iterator();
                    while (iterator.hasNext()) {
                        ((AppInfoUpdateListener) iterator.next()).onStatusUpdate(getDataCardItem());
                    }
                }

            }
        }

    };
}
