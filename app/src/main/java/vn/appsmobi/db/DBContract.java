
package vn.appsmobi.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {
    public static final String AUTHORITY = "vn.appsmobi";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Cache implements BaseColumns {
        public static final String DIRECTORY = "cache";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                AUTHORITY_URI, DIRECTORY);

        public static final String KEY = "key";
        public static final String CONTENT = "content";
        public static final String ETAG = "etag";
        public static final String ACCOUNT_ID = "user_id";

        private Cache() {
        }

        ;
    }

}
