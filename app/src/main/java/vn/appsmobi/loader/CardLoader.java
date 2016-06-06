package vn.appsmobi.loader;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.model.CardItem;
import vn.appsmobi.requests.Request;
import vn.appsmobi.utils.Constants;

public class CardLoader extends BaseLoader {

    static String TAG = "CommentLoader";
    boolean needNextPage = false;
    int page = 0;
    private int cardDataType;
    int resType;//Resource Type
    int requestType = 0;

    //	int type;// 0 = all; 1 = excellent; 2 = good; 3 = avg; 4 = bad
    public CardLoader(Context context) {
        super(context);
    }

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


    @Override
    protected BaseResult getResultInstance() {
        return new CardResult();
    }

    @Override
    protected String getCacheKey() {
        return null;
    }

    @Override
    protected UpdateTask getUpdateTask() {
        return new CardUpdateTask();
    }

    @Override
    protected BaseResult parseResult(JSONObject paramJSONObject, BaseResult result) throws Exception {
        CardResult cardResult = (CardResult) result;
        cardResult.cards = CardItem.valueOfList(paramJSONObject);
        return cardResult;
    }


    //Update Task
    private class CardUpdateTask extends BaseLoader.UpdateTask {
        protected boolean isAppend;

        public CardUpdateTask() {
            if (page == 0) {
                //comment
                isAppend = false;
            } else {
                isAppend = true;
            }
        }

        @Override
        protected Request getRequest() {//1 param: merchantId -> get list comment ;;;;; 2 param: userId -> get List comment
            if (getRequestType() == 1) {
                return new Request(Constants.getListCardForListActivity(getCardDataType(), getResType()) + "/page/" + page);
            } else if (getRequestType() == 2) {
                return new Request(Constants.getListCardForListActivity(getCardDataType(), 1) + "/page/" + page);
            } else {
                return new Request(Constants.getAllForHomeActivity() + "/page/" + page);
            }

        }

        protected BaseResult onDataLoaded(BaseResult oldResult, BaseResult newResult) {
            BaseResult localResult = null;

            if (newResult != null && (((CardResult) newResult).cards != null) && (!((CardResult) newResult).cards.isEmpty())) {
                needNextPage = true;
                localResult = newResult;
                mResult = newResult.shallowClone();
                if (this.isAppend) {
                    localResult = mergeResult((CardResult) oldResult, (CardResult) newResult);
                }
                return localResult;
            }
            return newResult;
        }
    }

    private CardResult mergeResult(CardResult oldResult, CardResult newResult) {
        CardResult cardResult = new CardResult();
        cardResult.cards = new ArrayList<>();
        if (oldResult != null && oldResult.cards != null) {
            cardResult.cards.addAll(oldResult.cards);
        }
        if (newResult != null && newResult.cards != null) {
            cardResult.cards.addAll(newResult.cards);
        }
        return cardResult;
    }


    //Result
    public static class CardResult extends BaseResult {
        public ArrayList<CardItem> cards;

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
            return cardResult;
        }
    }


    @Override
    public void reload() {
        if (!isLoading()) {
            page = -1;
            needNextPage = false;
            super.reload();
        }
    }

    public void nextPage() {
        if (needNextPage) {
            this.page = 1 + this.page;
        }
    }

    public int getCurrentPage() {
        return this.page;
    }
}