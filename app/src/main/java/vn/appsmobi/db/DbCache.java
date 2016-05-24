
package vn.appsmobi.db;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;

import java.util.ArrayList;

import vn.appsmobi.utils.LogUtil;

public class DbCache {

    private static final String TAG = "DbCache";
    private Context mContext;

    private static final int COLUMNS_KEY = 0;
    private static final int COLUMNS_CONTENT = 1;
    private static final int COLUMNS_ETAG = 2;

    public DbCache(Context context) {
        mContext = context;
    }

    // TODO  

    /**
     * @param key
     * @param content
     * @param etag
     */
    public void setItem(String key, String content, String etag) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(ContentProviderOperation.newDelete(DBContract.Cache.CONTENT_URI)
                .withSelection("/?", new String[]{
                        key
                })
                .build());

        ContentValues values = new ContentValues();
        values.put(DBContract.Cache.KEY, key);
        values.put(DBContract.Cache.CONTENT, content);
        values.put(DBContract.Cache.ETAG, etag);
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newInsert(DBContract.Cache.CONTENT_URI);
        builder.withValues(values);
        operations.add(builder.build());

        try {
            mContext.getContentResolver().applyBatch(DBContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "saveCacheToDb: failed");
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            LogUtil.e(TAG, "saveCacheToDb: failed");
            e.printStackTrace();
        }

    }

    public void deleteItem(String key) {
        mContext.getContentResolver().delete(DBContract.Cache.CONTENT_URI, DBContract.Cache.KEY + " = ?",
                new String[]{
                        key
                });
    }

    public void deleteUserRelatedItem() {
        mContext.getContentResolver().delete(DBContract.Cache.CONTENT_URI, DBContract.Cache.ACCOUNT_ID + " IS NOT NULL",
                null);
    }

    /**
     * @param key
     * @return
     */
    public DbCacheItem getItem(String key) {
        DbCacheItem item = null;
        Cursor cursor = mContext.getContentResolver().query(
                DBContract.Cache.CONTENT_URI, null, DBContract.Cache.KEY + "=?", new String[]{
                        key
                },
                null);
        if (cursor == null) {
            return null;
        }
        try {
            if (!cursor.moveToFirst()) {
                return null;
            }
            item = new DbCacheItem();
            item.mKey = cursor.getString(COLUMNS_KEY);
            item.mContent = cursor.getString(COLUMNS_CONTENT);
            item.mEtag = cursor.getString(COLUMNS_ETAG);
        } finally {
            cursor.close();
        }
        return item;
    }

    public final class DbCacheItem {
        public String mKey;
        public String mContent;
        public String mEtag;
    }
}
