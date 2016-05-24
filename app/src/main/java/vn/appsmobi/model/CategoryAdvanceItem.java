package vn.appsmobi.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tobrother on 21/01/2016.
 */
public class CategoryAdvanceItem extends DataCardItem implements Parcelable {
    public CategoryAdvanceItem() {
        super();
    }

    protected CategoryAdvanceItem(Parcel in) {
        super(in);
    }

    // get array list from json object
    public ArrayList<CategoryAdvanceItem> valueOfList(JSONObject object) throws JSONException {
        JSONArray array = object.getJSONArray("card_data");
        ArrayList<CategoryAdvanceItem> categoryAdvanceItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            CategoryAdvanceItem item = new CategoryAdvanceItem();
            item.valueOf(array.getJSONObject(i));
            categoryAdvanceItems.add(item);
        }
        return categoryAdvanceItems;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryAdvanceItem> CREATOR = new Creator<CategoryAdvanceItem>() {
        @Override
        public CategoryAdvanceItem createFromParcel(Parcel in) {
            return new CategoryAdvanceItem(in);
        }

        @Override
        public CategoryAdvanceItem[] newArray(int size) {
            return new CategoryAdvanceItem[size];
        }
    };
}
