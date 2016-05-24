package vn.appsmobi.loader;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.model.CardItem;
import vn.appsmobi.requests.Request;
import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 30/01/2016.
 */
public class CardDetailLoader extends BaseLoader {

    static String TAG = "CardDetailLoader";
    boolean needNextPage = false;
    int page = 0;
    private int cardID;

    //	int type;// 0 = all; 1 = excellent; 2 = good; 3 = avg; 4 = bad
    public CardDetailLoader(Context context) {
        super(context);
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }


    @Override
    protected BaseResult getResultInstance() {
        return new DetailCardResult();
    }

    @Override
    protected String getCacheKey() {
        return null;
    }

    @Override
    protected UpdateTask getUpdateTask() {
        return new CardInfoUpdateTask();
    }

    @Override
    protected BaseResult parseResult(JSONObject json, BaseResult result) throws Exception {
        DetailCardResult detailCardResult = (DetailCardResult) result;
        detailCardResult.infoCard = CardItem.valueOf(json.getJSONObject(Constants.JSON_OBJECT.APP_INFO));
        detailCardResult.comments = CardItem.valueOfList(json.getJSONObject(Constants.JSON_OBJECT.COMMENTS));
        detailCardResult.suggestApps = CardItem.valueOfList(json.getJSONObject(Constants.JSON_OBJECT.SUGGEST_CARDS));
        return detailCardResult;
    }


    //Update Task
    private class CardInfoUpdateTask extends BaseLoader.UpdateTask {
        public CardInfoUpdateTask() {
            super();
        }

        @Override
        protected Request getRequest() {//1 param: merchantId -> get list comment ;;;;; 2 param: userId -> get List comment
            return new Request(Constants.getCardInfo(1));
        }
    }


    // Detail Card Result
    public static class DetailCardResult extends BaseResult {
        public ArrayList<CardItem> comments;
        public ArrayList<CardItem> suggestApps;
        public CardItem infoCard;

        @Override
        protected int getCount() {
            if (comments != null && suggestApps != null && infoCard != null) {
                return 1;
            }
            return 0;
        }

        @Override
        public BaseResult shallowClone() {
            DetailCardResult cardResult = new DetailCardResult();
            cardResult.comments = this.comments;
            cardResult.suggestApps = this.suggestApps;
            cardResult.infoCard = this.infoCard;
            return cardResult;
        }


    }
    //******************************************************************

}