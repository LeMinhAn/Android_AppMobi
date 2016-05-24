
package vn.appsmobi.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import vn.appsmobi.utils.LogUtil;

public abstract class AppProvider extends ContentProvider {
    private static final UriMatcher mMatcher;
    private DatabaseHelper mHelper;
    private static final String TAG = "AppProvider";
    private static final int URI_CACHE = 3;
    private static final int URI_CACHE_ITEM = 4;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(DBContract.AUTHORITY, DBContract.Cache.DIRECTORY, URI_CACHE);
        mMatcher.addURI(DBContract.AUTHORITY, DBContract.Cache.DIRECTORY + "/#", URI_CACHE_ITEM);
    }

    @Override
    public boolean onCreate() {
        try {
            mHelper = DatabaseHelper.getInstance();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor c = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        switch (mMatcher.match(uri)) {
            case URI_CACHE:
                c = db.query(DatabaseHelper.Tables.CACHE, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            default:
                c = doQuery(uri, projection, selection, selectionArgs, sortOrder);
        }
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    private String parseSelection(String selection) {
        return (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long insertedId = 0;
        switch (mMatcher.match(uri)) {
            case URI_CACHE:
                insertedId = db.insert(DatabaseHelper.Tables.CACHE, null, values);
                break;
            default:
                insertedId = doInsert(uri, values);
                break;
        }

        if (insertedId > 0) {
            getContext().getContentResolver().notifyChange(
                    ContentUris.withAppendedId(uri, insertedId), null);
            LogUtil.d(TAG, "Insert values with uri " + uri.toString());
        }
        return ContentUris.withAppendedId(uri, insertedId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (mMatcher.match(uri)) {

            case URI_CACHE:
                count = db.delete(DatabaseHelper.Tables.CACHE, selection, selectionArgs);
                break;
            default:
                count = doDelete(uri, selection, selectionArgs);
                break;
        }
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            LogUtil.d(TAG, "Delete " + count + " rows with uri " + uri.toString());
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (mMatcher.match(uri)) {

            case URI_CACHE:
                count = db.update(DatabaseHelper.Tables.CACHE, values, selection, selectionArgs);
                break;

            default:
                count = doUpdate(uri, values, selection, selectionArgs);
                break;
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            LogUtil.d(TAG, "Update " + count + " rows with uri " + uri.toString());
        }
        return count;
    }

    public abstract Cursor doQuery(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder);

    public abstract long doInsert(Uri uri, ContentValues values);

    public abstract int doDelete(Uri uri, String selection, String[] selectionArgs);

    public abstract int doUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs);

}
