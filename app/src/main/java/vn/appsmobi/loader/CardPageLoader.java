package vn.appsmobi.loader;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.model.CardItem;
import vn.appsmobi.requests.Request;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.LogUtil;

/**
 * Created by tobrother on 29/01/2016.
 */
public class CardPageLoader extends BasePageLoader {

    static String TAG = "CommentLoader";
    boolean needNextPage = false;
    int page = 0;
    private int cardDataType;
    int resType;//Resource Type
    int requestType = 0;
    //	int type;// 0 = all; 1 = excellent; 2 = good; 3 = avg; 4 = bad

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getCardDataType() {
        return cardDataType;
    }

    public void setCardDataType(int cardDataType) {
        this.cardDataType = cardDataType;
    }

    public void nextPage() {
        if (needNextPage) {
            this.page = 1 + this.page;
        }
    }


    public CardPageLoader(Context context) {
        super(context);
    }


    /**
     * implement interface
     */

    @Override
    protected BaseResult getResultInstance() {
        // TODO Auto-generated method stub
        return new CardResult();
    }

    @Override
    protected String getCacheKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected UpdateTask getUpdateTask() {
        // TODO Auto-generated method stub
        return new CardsUpdateTask();
    }

    @Override
    protected BaseResult parseResult(JSONObject json, BaseResult baseResult) throws Exception {
        CardResult cardResult = (CardResult) baseResult;
        cardResult.cards = CardItem.valueOfList(json);
        return cardResult;
    }
    //*****************************************************************************************


    /**
     * update card class
     */
    @SuppressWarnings("unused")
    private class CardsUpdateTask extends PageUpdateLTask {

        @Override
        protected int getPageSizeValue() {
            return 10;
        }

        @Override
        protected Request getRequest(int page) {
            if (getRequestType() != 0) {
                Log.e("url", Constants.getListCardForListActivity(getCardDataType(), getResType()) + "/page/" + page);
                return new Request(Constants.getListCardForListActivity(getCardDataType(), getResType()) + "/page/" + page);
            } else {
                return new Request(Constants.getAllForHomeActivity() + "/page/" + page);
            }
        }

        @Override
        protected BaseResult merge(BaseResult oldResult, BaseResult newResult) {
            // TODO Auto-generated method stub
            CardResult cardResult = new CardResult();
            if (newResult == null && oldResult == null)
                return cardResult;
            if (newResult == null)
                return oldResult;
            if (oldResult == null)
                return newResult;
            cardResult.cards.addAll(((CardResult) oldResult).cards);
            cardResult.cards.addAll(((CardResult) newResult).cards);
            cardResult.mNextTime = ((CardResult) newResult).mNextTime;
            LogUtil.i("mNextTime:   ", cardResult.mNextTime);
            return cardResult;
        }
    }

    //***************************************************************
    // Card Result Class
    public static class CardResult extends BaseResult {
        public ArrayList<CardItem> cards;
        public String mNextTime;

        public CardResult() {
            super();
            cards = new ArrayList<>();
            mNextTime = "";
        }

        @Override
        protected int getCount() {
            if (cards != null) {
                return cards.size();

            }
            return 0;
        }

        @Override
        public BaseResult shallowClone() {
            CardResult cardResult = new CardResult();
            cardResult.cards = this.cards;
            cardResult.mNextTime = this.mNextTime;
            return cardResult;
        }
    }
    //******************************************************************
}
