
package vn.appsmobi.loader;

import android.content.Context;

import org.json.JSONObject;

import vn.appsmobi.loader.BaseResult.ResultStatus;
import vn.appsmobi.requests.Request;
import vn.appsmobi.utils.ThreadPool;

public abstract class BasePageLoader<GenericResult extends BaseResult> extends
        BaseLoader<GenericResult> {
    // private static final String TAG = "BasePageLoader";
    private int mPage;
    private boolean mNeedNextPage;

    public BasePageLoader(Context context) {
        super(context);
        mPage = 1;
        mNeedNextPage = false;
    }

    public boolean hasNextPage() {
        if (mNeedNextPage) {
            mPage++;
        }
        setNeedDatabase(false);
        return mNeedNextPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPage() {
        return mPage;
    }

    @Override
    public void reload() {
        if (!isLoading()) {
            mPage = 1;
            mNeedNextPage = false;
            super.reload();
        }
    }

    /**
     */
    protected abstract class PageUpdateLTask extends BaseLoader<GenericResult>.UpdateTask {
        protected boolean mIsAppend;

        public PageUpdateLTask() {
            if (mPage == 1) {
                mIsAppend = false;
            } else {
                mIsAppend = true;
            }
        }

        /**
         */
        protected abstract Request getRequest(int page);

        @Override
        protected final Request getRequest() {
            return getRequest(mPage);
        }

        protected GenericResult doInBackground(Void... params) {
            Request request = getRequest(mPage);
            int status = request.getStatus();
            GenericResult result = getResultInstance();
            if (status == Request.STATUS_OK) {
                final String etag = request.getEtag();
                JSONObject mainObject = request.requestJSON();
                GenericResult newResult = parseTaskResult(mainObject);
                result = onDataLoaded(mResult, newResult);
                final String jonsString = mainObject.toString();
                if (!mIsAppend) {
                    ThreadPool.execute(new Runnable() {
                        public void run() {
                            //save cache db
                        }
                    });
                }
                return result;
            } else if (status == Request.STATUS_NETWORK_UNAVAILABLE) {
                result.setResultStatus(ResultStatus.NETWROK_ERROR);
            } else if (status == Request.STATUS_AUTH_ERROR) {
                result.setResultStatus(ResultStatus.AUTH_ERROR);
            } else {
                result.setResultStatus(ResultStatus.SERVICE_ERROR);
            }
            return result;
        }

        /**
         */
        protected GenericResult onDataLoaded(GenericResult oldResult, GenericResult newResult) {
            mNeedNextPage = newResult.getCount() >= getPageSizeValue();
            GenericResult processed = newResult;
            if (mIsAppend) {
                processed = merge(oldResult, newResult);
            }
            return processed;
        }

        protected abstract GenericResult merge(GenericResult oldResult, GenericResult newResult);

        protected int getPageSizeValue() {
            return 20;
        }
    }
}
