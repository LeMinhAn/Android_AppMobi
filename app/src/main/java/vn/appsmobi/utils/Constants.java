package vn.appsmobi.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import vn.appsmobi.R;

public class Constants {
    public static String API_URL = "http://ssc-team.com/index.php/welcome";
    public static boolean DEBUG = false;

    public static String LOG_TAG = "BitmapCache";
    // global application
    // Web service url
    public static final String APPLICATION_URL = "http://cdn.allmobile.vn/cdn/service2014/";
    public static final String INTENT_EXTRA_APP_PAKAGENAME = "vn.appsmobi.intent.extra.app.pakagename";
    public static final String STRING_PREFERENCE_FIRST_TIME = "vn.appsmobi.preferences.first.time";
    public static final String STRING_PREFERENCE = "vn.appsmobi.preferences";
    public static final String INTENT_EXTRA_NOTIFICATION_MESSAGE = "message";
    public static final String INTENT_EXTRA_NOTIFICATION_TYPE = "type";
    public static final String INTENT_EXTRA_NOTIFICATION_CONTENT = "content";
    public static final String INTENT_EXTRA_NOTIFICATION_IMAGE = "image";
    public static String GET_PERMISSION_URL = APPLICATION_URL + "showpermission/id/%s";
    public static final String INTENT_EXTRA_NOTIFICATION_PAKAGENAME = "packagename";
    public static final String INTENT_EXTRA_NOTIFICATION_URL = "url";
    public static final int UNUSED_DRAWABLE_RECYCLE_DELAY_MS = 2000;

    // folder uri
    public static final String DOWNLOAD_FOLDER = "";
    public static final String RINGTONE_FOLDER = "";
    public static final String BOOK_FOLDER = "";
    public static final String WALLPAPER_FOLDER = "";


    public static class AppUpdate {
        public static final String FILE_PREFIX_FILE = "file://";
        public static final String FILE_SUFFIX_APK = ".apk";
        public static final String FILE_TYPE_APK = "application/vnd.android.package-archive";
        public static final String PREF_LAST_UPDATE_IS_OK = "pref_last_update_is_ok";
        public static final String PREF_LAST_CHECK_UPDATE = "pref_last_check_update";
        public static final int PERIOD_UPDATE_OK = 2 * 60 * 60 * 1000;
        public static final int PERIOD_CHECK_UPDATE = 10 * 60 * 1000;
        public static final String PREF_DOWNLOAD_ID = "pref_download_id";
        public static final String VALUE_TYPE_FORCE_IN_WIFI = "wifiForce";
        public static final String VALUE_TYPE_FORCE = "force";
    }

    public static class Intent {
        public static final String EXTRA_FRAGMENT_TYPE = "vn.appsmobi.extra_fragment_type";
        public static final String EXTRA_TAB_TYPE = "vn.appsmobi.extra_tab_type";
        public static final String CARD_TYPE = "vn.appsmobi.card_type";
        public static final String CARD_DATA_TYPE = "vn.appsmobi.card_data_type";
        public static final String CARD_CONTENT = "vn.appsmobi.card_content";
        public static final String CARD = "vn.appsmobi.card";
        public static final String READ_MORE_TYPE = "vn.appsmobi.read_more_type";
        public static final String CARD_NAME = "vn.appsmobi.name";
    }

    public static class RequestURL {
        public static final String HOME_URL = "vn.appsmobi.extra_fragment_type";
    }

    public static class RequestType {
        public static final int HOME_URL = 0;
        public static final int LIST_APP = 1;
        public static final int MORE_LIST = 2;
    }


    public static class Log {
        public static final String LOG = "vn.appsmobi.log.error";
    }

    public static class HOME_CARD_TYPE {
        public static final int BIG_CARD = 0;
        public static final int HORIZONTAL_CARD = 1;
        public static final int VERTICAL_CARD = 2;
        public static final int TEXT_CARD = 4;
        public static final int HEADER = -1;
    }

    public static class CARD_TYPE {
        public static final int BIG_CARD = 0;
        public static final int HORIZONTAL_CARD = 1;
        public static final int VERTICAL_CARD = 2;
        public static final int GRID_CARD = 3;
        public static final int TEXT_CARD = 4;
        public static final int HORIZONTAL_CARD_ONE = 5;
        public static final int IMAGE_CARD = 6;
    }

    public static class CARD_DATA_TYPE {
        public static final int APP = 0;
        public static final int GAME = 1;
        public static final int BOOK = 2;
        public static final int FILM = 3;
        public static final int WALLPAPER = 4;
        public static final int RINGTONE = 5;
        public static final int ALL = 6;
    }

    public static class READ_MORE_TYPE {
        public static final int APP_MORE = 0;
        public static final int COMMENT_MORE = 1;
    }

    public static class TAB_TYPE {
        public static final int CATEGORY = 0;
        public static final int HOT = 1;
        public static final int TOP = 2;
        public static final int NEW = 3;
    }

    public static class SCALE_IMAGE_CARD {
        public static final int AVATAR_COMMENT = 12;
        public static final int SIMPLE_HORIZONTAL = 3;
        public static final int IMAGE_CARD = 2;
        public static final int VERTICAL_CARD = 4;
        public static final int RING_STONE = 3;
    }

    public static class APP_STATE {
        public static final int LOADING = 0;
        public static final int NEW = 1;
        public static final int UPDATE = 2;
        public static final int INSTALLED = 3;
    }
    // constant for  API URL ger data
    // get all card for home activity


    public static class JSON_PARSE {
        //
        public static final String CARD_DATA = "card_data";
        //
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String RESOURCE_URL = "resource_url";
        public static final String CATEGORIES = "categories";
        public static final String STATUS = "status";
        public static final String DOWNS_COUNT = "downs_count";
        public static final String SIZE = "size";
        public static final String RATE = "rate";
        public static final String ICON_IMAGE = "icon_image";
        public static final String SHORT_DESCRIPTION = "short_description";
        public static final String AUTHOR = "author";
        public static final String PROMO_IMAGE = "promo_image";
        public static final String CONTENT = "content";
        public static final String DATE = "date";
        public static final String FILE_TYPE = "file_type";
        public static final String SLIDE_SHOW = "slide_show";
        public static final String PROMO_VIDEO = "promo_video";
        public static final String DURATION_TIME = "time";
        public static final String VERSION_COMPATIBLE = "version_compatible";
        public static final String HASH = "hash";
        public static final String DETAIL = "detail";
        public static final String AGENCY_ID = "agency_id";
        public static final String PACKAGE_NAME = "package_name";
        public static final String VERSION_CODE = "version_code";
        public static final String VERSION_NAME = "version_name";
        // Category
        public static final String CATEGORY_ID = "category_id";
        public static final String CATEGORY_NAME = "category_name";
        // link version
        public static final String LIST_VERSION = "LIST_VERSION";
        public static final String LINK_TITLE = "link_title";
        public static final String LINK_DOWN = "link_down";

    }

    public static class CARD_STATUS {
        public static final int STATUS_NORMAL = 0;
        public static final int STATUS_INSTALLED = 1;
        public static final int STATUS_INSTALLING = 2;
        public static final int STATUS_UPDATE = 3;
        public static final int STATUS_SAW = 4;
        public static final int STATUS_DOWNLOADED = 5;
    }

    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_image)
            .showImageForEmptyUri(R.drawable.ic_image)
            .showImageOnFail(R.drawable.ic_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .resetViewBeforeLoading()
            .bitmapConfig(Bitmap.Config.RGB_565)
//            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//            .considerExifParams(true)
//            .displayer(new FadeInBitmapDisplayer(50))
            .build();

    public static class JSON_OBJECT {
        public static final String COMMENTS = "comments";
        public static final String APP_INFO = "card_info";
        public static final String SUGGEST_CARDS = "suggest_cards";
    }

    public static String getAllForHomeActivity() {
        return API_URL + "/get_all_card";
    }

    // get all card for list activity
    public static String getListCardForListActivity(int cardType, int resType) {
        return API_URL + "/get_list_card/card_data_type/" + cardType + "/res_type/" + resType;
        //  API_URL + "/get_list_card/card-type/0/res-type/0    : get category of application ;
        //  API_URL + "/get_list_card/card-type/0/res-type/1    : get hot of application ;
        //  API_URL + "/get_list_card/card-type/0/res-type/2    : get top download of application ;
        //  API_URL + "/get_list_card/card-type/0/res-type/3    : get new of application ;
    }

    // get all card of category
    public static String getAllCardsOfCategory(int cardType, int categoryId) {
        return API_URL + "/get_cards_of_category/card_type/" + cardType + "/category_id/" + categoryId;
        //  API_URL + "/get-cards-of-category/0/category-id/0    : get category of application ;

    }

    // get all card of category
    public static String getCardInfo(int cardID) {
        return API_URL + "/get_card_info/cardID/" + cardID;
    }

    /*
    // get all card of category
    public static String getCardInfo(int cardType, int cardId) {
        return API_URL + "/get_card_info/card_type/" + cardType + "/card_id/" + cardId;
        //  API_URL + "/get-card-info/card/0/card-id/111    : get info of application which have id = 111

    }
    */
    // get suggest card of card
    public static String getSuggestCard(int cardType, int cardId) {
        return API_URL + "/get_card_suggest/card_type/" + cardType + "/card_id/" + cardId;
        //  API_URL + "/get-card-suggest/card/0/card-id/111    : get suggest application of application which have id = 111
    }

    // get comment of card
    public static String getCommentCard(int cardId) {
        return API_URL + "/get_comment/card_id/" + cardId;
        //  API_URL + "comment/card-id/111    : get all comment of application which have id = 111
    }


}
