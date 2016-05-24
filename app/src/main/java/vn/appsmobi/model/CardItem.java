package vn.appsmobi.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tobrother272 on 12/24/2015.
 */
public class CardItem {
    public static String CARD_ITEM_TAG = "CARD_ITEM";
    //Trang chủ
    private String card_title;  // More = tiltle
    private String card_more_action; // Xem thêm = more action
    private int card_data_type; //
    private int card_type; // loại card
    private JSONArray card_data;

    public CardItem(String card_title, int card_type) {
        this.card_title = card_title;
        this.card_type = card_type;
    }

    public CardItem() {

    }

    // get child Item Card Data from Json Object
    //Lấy dữ liệu từng items từ Json
    public static CardItem valueOf(JSONObject jsonObject) throws JSONException {
        CardItem itemCard = new CardItem();
        if (!jsonObject.isNull("card_title")) {
            itemCard.setCard_title(jsonObject.getString("card_title"));
        }
        if (!jsonObject.isNull("card_more_action")) {
            itemCard.setCard_more_action(jsonObject.getString("card_more_action"));
        }
        if (!jsonObject.isNull("card_type")) {
            itemCard.setCard_type(jsonObject.getInt("card_type"));
        }
        if (!jsonObject.isNull("card_data_type")) {
            itemCard.setCard_data_type(jsonObject.getInt("card_data_type"));
        }
        if (!jsonObject.isNull("card_data")) {
            JSONArray arrayCardData = jsonObject.getJSONArray("card_data");
            itemCard.setCard_data(arrayCardData);

        }
        return itemCard;
    }

    // get list card from json
    public static ArrayList<CardItem> valueOfList(JSONObject jsonObject) throws JSONException {
        JSONArray arr = jsonObject.getJSONArray("cards");
        ArrayList<CardItem> itemCards = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            itemCards.add(valueOf(arr.getJSONObject(i)));
        }
        return itemCards;
    }

    public String getCard_title() {
        return card_title;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public String getCard_more_action() {
        return card_more_action;
    }

    public void setCard_more_action(String card_more_action) {
        this.card_more_action = card_more_action;
    }

    public int getCard_type() {
        return card_type;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public JSONArray getCard_data() {
        return card_data;
    }

    public void setCard_data(JSONArray card_data) {
        this.card_data = card_data;
    }

    public int getCard_data_type() {
        return card_data_type;
    }

    public void setCard_data_type(int card_data_type) {
        this.card_data_type = card_data_type;
    }
}
