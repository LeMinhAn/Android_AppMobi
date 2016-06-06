package vn.appsmobi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 21/01/2016.
 */
public class MovieItem extends DataCardItem {
    private float duration_time;
    private String promo_video;
    private ArrayList<String> slide_show;

    public MovieItem() {
        super();
    }

    public MovieItem(DataCardItem dataCardItem) {
        super(dataCardItem);
    }

    public void valueOf(JSONObject jsonObject) throws JSONException {
        super.valueOf(jsonObject);
        if (!jsonObject.isNull(Constants.JSON_PARSE.DURATION_TIME)) {
            this.setDuration_time((float) jsonObject.getDouble(Constants.JSON_PARSE.DURATION_TIME));
        }
        else {
            this.setDuration_time(0);
        }
        if (!jsonObject.isNull(Constants.JSON_PARSE.PROMO_VIDEO)) {
            this.setPromo_video(jsonObject.getString(Constants.JSON_PARSE.PROMO_VIDEO));
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

    public ArrayList<MovieItem> valueOfList(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray(Constants.JSON_PARSE.CARD_DATA);
        ArrayList<MovieItem> movieItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            MovieItem item = new MovieItem();
            item.valueOf(array.getJSONObject(i));
            movieItems.add(item);
        }
        return movieItems;
    }


    public String getPromo_video() {
        return promo_video;
    }

    public void setPromo_video(String promo_video) {
        this.promo_video = promo_video;
    }

    public ArrayList<String> getSlide_show() {
        return slide_show;
    }

    public void setSlide_show(ArrayList<String> slide_show) {
        this.slide_show = slide_show;
    }

    public float getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(float duration_time) {
        this.duration_time = duration_time;
    }
}
