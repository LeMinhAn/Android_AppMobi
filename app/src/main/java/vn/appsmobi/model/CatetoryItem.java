package vn.appsmobi.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 18/01/2016.
 */
public class CatetoryItem implements Parcelable {
    private int category_id;
    private String category_name;

    public CatetoryItem(int category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public CatetoryItem() {
    }

    protected CatetoryItem(Parcel in) {
        category_id = in.readInt();
        category_name = in.readString();
    }

    public static final Creator<CatetoryItem> CREATOR = new Creator<CatetoryItem>() {
        @Override
        public CatetoryItem createFromParcel(Parcel in) {
            return new CatetoryItem(in);
        }

        @Override
        public CatetoryItem[] newArray(int size) {
            return new CatetoryItem[size];
        }
    };

    public static CatetoryItem ValueOf(JSONObject jsonObject) throws JSONException {
        CatetoryItem catetoryItem = new CatetoryItem();
        if (jsonObject.isNull(Constants.JSON_PARSE.CATEGORY_ID)) {
            catetoryItem.setCategory_id(jsonObject.getInt(Constants.JSON_PARSE.CATEGORY_ID));
        }
        if (jsonObject.isNull(Constants.JSON_PARSE.CATEGORY_NAME)) {
            catetoryItem.setCategory_name(jsonObject.getString(Constants.JSON_PARSE.CATEGORY_NAME));
        }
        return catetoryItem;
    }

    public static ArrayList<CatetoryItem> ValueOfList(JSONArray jsonArray) throws JSONException {
        JSONArray array = jsonArray;
        ArrayList<CatetoryItem> catetoryItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            catetoryItems.add(ValueOf(array.getJSONObject(i)));
        }
        return catetoryItems;
    }


    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category_id);
        dest.writeString(category_name);
    }
}
