package vn.appsmobi.model;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 21/01/2016.
 */
public class RingToneItem extends DataCardItem {
    private float duration_time;
    private String thumb_image;

    public RingToneItem(DataCardItem dataCardItem) {
        super(dataCardItem);
    }

    public RingToneItem() {

    }

    public RingToneItem(long duration_time, String thumb_image) {
        this.duration_time = duration_time;
        this.thumb_image = thumb_image;
    }

    public void valueOf(JSONObject jsonObject) throws JSONException {
        super.valueOf(jsonObject);
        if (!jsonObject.isNull(Constants.JSON_PARSE.DURATION_TIME)) {
            this.setDuration_time((float) jsonObject.getDouble(Constants.JSON_PARSE.DURATION_TIME));
        }
    }

    public ArrayList<RingToneItem> valueOfList(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray(Constants.JSON_PARSE.CARD_DATA);
        ArrayList<RingToneItem> ringToneItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            RingToneItem item = new RingToneItem();
            item.valueOf(array.getJSONObject(i));
            ringToneItems.add(item);
        }
        return ringToneItems;
    }


    protected RingToneItem(Parcel in) {
        super(in);
        duration_time = in.readLong();
        thumb_image = in.readString();
    }

    public float getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(float duration_time) {
        this.duration_time = duration_time;
    }
}
