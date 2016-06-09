package vn.appsmobi.loader;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import vn.appsmobi.R;
import vn.appsmobi.activity.BaseActivity;
import vn.appsmobi.db.DBContract;
import vn.appsmobi.db.DBContract.Cache;
import vn.appsmobi.db.DbCache;
import vn.appsmobi.model.Tags;
import vn.appsmobi.requests.Request;
import vn.appsmobi.utils.LogUtil;
import vn.appsmobi.utils.ThreadPool;
import vn.appsmobi.utils.UIUtils;

public abstract class BaseLoader<GenericResult extends BaseResult> extends
        Loader<GenericResult> {
    private static final String TAG = "BaseLoader";
    protected GenericResult mResult;
    protected volatile boolean mIsLoading;
    protected boolean mNeedDatabase;
    protected boolean mNeedServer;
    protected boolean mNeedDeliverResult;
    private boolean mHasDeliverdResult;
    protected List<AsyncTask<Void, Void, GenericResult>> mTaskList;
    private int mNextExecuteTask;
    private ProgressNotifiable mProgressNotifiable;
    protected String mEtag;
    protected DbCache mCache;
    private WeakReference<BaseActivity> mBaseActivityRef;
    private Context activityContext;

    public BaseLoader(Context context) {
        super(context);
        activityContext = context;
        if (context instanceof BaseActivity) {
            mBaseActivityRef = new WeakReference<BaseActivity>(
                    (BaseActivity) context);
        }
        mIsLoading = false;
        mNeedDatabase = true;
        mNeedServer = true;
        mNeedDeliverResult = true;
        mHasDeliverdResult = false;

        mTaskList = new ArrayList<AsyncTask<Void, Void, GenericResult>>();
        mNextExecuteTask = 0;
        mCache = new DbCache(context);
        mResult = getResultInstance();
    }

    /**
     */
    public void reload() {
        if (!this.isLoading()) {
            this.mNeedDatabase = false;
            this.forceLoad();
        }
    }

    public void setNeedDatabase(boolean needDatabase) {
        this.mNeedDatabase = needDatabase;
    }

    public void setNeedServer(boolean needServer) {
        this.mNeedServer = needServer;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setProgressNotifiable(ProgressNotifiable progressNotifiable) {
        this.mProgressNotifiable = progressNotifiable;
        if (progressNotifiable != null) {
            progressNotifiable.init(dataExists(), mIsLoading);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onStartLoading() {
        if (mResult.getCount() > 0) {
            deliverResult((GenericResult) mResult.shallowClone());
        }

        if (!mIsLoading && (mResult.getCount() == 0 || takeContentChanged())) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        mTaskList.clear();
        mNextExecuteTask = 0;
        initTaskList(mTaskList);
        executeNextTask();
    }

    /**
     */
    protected void initTaskList(List<AsyncTask<Void, Void, GenericResult>> tasks) {
        if (mNeedDatabase) {
            DatabaseTask task = getDatabaseTask();
            if (task != null) {
                tasks.add(task);
            }
        }
        if (mNeedServer) {
            UpdateTask task = getUpdateTask();
            if (task != null) {
                tasks.add(task);
            }
        }
    }

    /**
     */
    protected void executeNextTask() {
        if (hasNextTask()) {
            AsyncTask<Void, Void, GenericResult> task = null;
            while (task == null && hasNextTask()) {
                task = mTaskList.get(mNextExecuteTask);
                mNextExecuteTask++;
            }
            if (task != null) {
                task.execute();
            }
        }
    }

    protected boolean hasNextTask() {
        return mNextExecuteTask < mTaskList.size();
    }

    protected DatabaseTask getDatabaseTask() {
        return new DatabaseTask();
    }

    protected UpdateTask getUpdateTask() {
        return null;
    }

    protected boolean dataExists() {
        return mResult.getCount() > 0 && mHasDeliverdResult;
    }

    protected abstract GenericResult parseResult(JSONObject json,
                                                 GenericResult baseResult) throws Exception;

    protected abstract GenericResult getResultInstance();

    /**
     */
    protected abstract class BaseTask extends
            AsyncTask<Void, Void, GenericResult> {
        @Override
        protected void onPreExecute() {
            mIsLoading = true;
            if (mProgressNotifiable != null) {
                mProgressNotifiable.startLoading(dataExists());
            }
        }

        @Override
        protected void onPostExecute(GenericResult result) {
            mIsLoading = false;
            final BaseResult.ResultStatus status = result.getResultStatus();
            if (status != BaseResult.ResultStatus.OK) {
                if (mProgressNotifiable != null) {
                    mProgressNotifiable.onError(dataExists(), status,
                            new Callback() {
                                @Override
                                public boolean handleMessage(Message message) {
                                    if (status == BaseResult.ResultStatus.NETWROK_ERROR) {
                                        Intent intent = new Intent(
                                                Settings.ACTION_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getContext().startActivity(intent);
                                    } else if (status == BaseResult.ResultStatus.AUTH_ERROR) {
                                        if (mBaseActivityRef != null) {
                                            BaseActivity ba = mBaseActivityRef
                                                    .get();
                                            if (ba != null) {
                                                // login require
                                            }
                                        }
                                    } else {
                                        reload();
                                    }
                                    return true;
                                }
                            });
                }
            } else {
                mResult = result;
                if (mNeedDeliverResult) {
                    deliverResult(result);
                    mHasDeliverdResult = true;
                } else {
                    mNeedDeliverResult = true;
                }
                if (mProgressNotifiable != null && !hasNextTask()) {
                    mProgressNotifiable.stopLoading(dataExists(), hasNextTask(), result.getResultStatus());
                }
            }
            executeNextTask();
        }

        protected GenericResult parseTaskResult(JSONObject json) {
            GenericResult result = getResultInstance();
            if (result == null) {
                throw new IllegalStateException(
                        "The parsed result should not be null, you must construct"
                                + "a result to indicate the task state");
            }
            try {
                if (json.has(Tags.HEADER)) {
                    if (json.optJSONObject(Tags.HEADER).has(Tags.CODE)) {
                        if (json.optJSONObject(Tags.HEADER).optInt(Tags.CODE) == 205) {
                            result.setResultStatus(BaseResult.ResultStatus.IP_ERROR);
                            return result;
                        }
                    }
                }
                result = parseResult(json, result);
            } catch (Exception e) {
                result.setResultStatus(BaseResult.ResultStatus.DATA_ERROR);
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     */
    protected abstract String getCacheKey();

    /**
     */
    protected boolean isUserRelated() {
        return false;
    }

    public void saveCacheToDB(String key, String content, String etag) {

        if (TextUtils.isEmpty(content)) {
            return;
        }

        ContentValues values = new ContentValues();
        String cacheKey = getCacheKey();

        values.put(Cache.KEY, cacheKey);
        values.put(Cache.CONTENT, content);
        values.put(Cache.ETAG, etag);
        if (isUserRelated() /* && LoginManager.getInstance().hasLogin() */) {
            // values.put(Cache.ACCOUNT_ID,
            // LoginManager.getInstance().getUserId());
        }

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newDelete(Cache.CONTENT_URI);
        builder.withSelection(Cache.KEY + "=?", new String[]{key});
        operations.add(builder.build());

        builder = ContentProviderOperation.newInsert(Cache.CONTENT_URI);
        builder.withValues(values);
        operations.add(builder.build());

        try {
            getContext().getContentResolver().applyBatch(DBContract.AUTHORITY,
                    operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    protected class DatabaseTask extends BaseTask {

        @Override
        protected GenericResult doInBackground(Void... params) {
            GenericResult result = getResultInstance();
            DbCache.DbCacheItem item = mCache.getItem(getCacheKey());
            if (item != null && item.mEtag != null) {
                mEtag = item.mEtag;
            }
            if (item != null) {
                try {
                    JSONObject json = new JSONObject(item.mContent);
                    GenericResult newResult = parseTaskResult(json);
                    result = onDataLoaded(mResult, newResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        /**
         */
        protected GenericResult onDataLoaded(GenericResult oldResult,
                                             GenericResult newResult) {
            return newResult;
        }
    }

    /**
     */
    protected abstract class UpdateTask extends BaseTask {
        private boolean needSaveToDb;
        String jonsString = "";

        public UpdateTask() {
            super();
            this.needSaveToDb = true;
        }

        public UpdateTask(boolean needSaveToDb) {
            super();
            this.needSaveToDb = needSaveToDb;
        }

        /**
         * @return the update task result. Null to indicate our service error.
         */
        @Override
        protected GenericResult doInBackground(Void... params) {

            Request request = getRequest();
            if (mEtag != null) {
                request.addHeader("If-None-Match", mEtag);
            }
            int status = request.getStatus();
            GenericResult result = getResultInstance();
            if (status == Request.STATUS_OK) {

                final String etag = request.getEtag();
                JSONObject mainObject = request.requestJSON();
                GenericResult newResult = parseTaskResult(mainObject);
                result = onDataLoaded(mResult, newResult);

                jonsString = "";

                jonsString = mainObject.toString();

                if (needSaveToDb && mNeedDatabase
                        && !TextUtils.isEmpty(getCacheKey())) {
                    ThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCacheToDB(getCacheKey(), jonsString, etag);
                        }
                    });
                }
                return result;
            } else if (status == Request.STATUS_NOT_MODIFIED) {
                mNeedDeliverResult = false;
                result = mResult;
            } else if (status == Request.STATUS_NETWORK_UNAVAILABLE) {
                result.setResultStatus(BaseResult.ResultStatus.NETWROK_ERROR);
            } else if (status == Request.STATUS_AUTH_ERROR) {
                result.setResultStatus(BaseResult.ResultStatus.AUTH_ERROR);
            } else {
                result.setResultStatus(BaseResult.ResultStatus.SERVICE_ERROR);
            }
            return result;
        }

        /**
         */
        protected abstract Request getRequest();

        /**
         */
        protected GenericResult onDataLoaded(GenericResult oldResult,
                                             GenericResult newResult) {
            return newResult;
        }
    }
}

