
package vn.appsmobi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import vn.appsmobi.R;
import vn.appsmobi.utils.MainApplication;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DB_NAME = "";
    private static DatabaseHelper sInstance;
    private static Context mContext;

    public interface Tables {
        public static final String CACHE = "cache";
    }

    public final class Index {

    }

    public static synchronized void init(Context context, int dbVersion, String dbName) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
            mContext = context;
            DB_VERSION = dbVersion;
            DB_NAME = dbName;
        }
    }

    public static synchronized DatabaseHelper getInstance() throws Exception {
        if (sInstance == null) {
            throw new Exception(MainApplication.getContext().getString(R.string.error_db_require_init));
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.CACHE + "(" +
                DBContract.Cache.KEY + " TEXT PRIMARY KEY," +
                DBContract.Cache.CONTENT + " TEXT NOT NULL," +
                DBContract.Cache.ETAG + " TEXT," +
                DBContract.Cache.ACCOUNT_ID + " TEXT);"
        );


    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
