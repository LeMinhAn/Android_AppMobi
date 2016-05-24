
package vn.appsmobi.model;

import android.text.TextUtils;

import org.json.JSONObject;

public class Tags {
    /**
     * Common tags for each api
     **/
    public static final String RESULT = "result";
    public static final String DESCRIPTION = "description";
    public static final String REASON = "reason";
    public static final String DATA = "data";
    public static final String CODE = "code";
    public static final String STATUS = "status";
    public static final String HEADER = "header";
    public static final String BODY = "body";
    public static final String DESC = "desc";

    /**
     * Common values for result
     **/
    public static final String RESULT_OK = "ok";
    public static final String RESULT_TRUE = "true";
    public static final String RESULT_CODE = "200";
    public static final String RESULT_ERROR = "error";

    public static boolean isJSONResultOK(JSONObject json) {
        return json != null && RESULT_OK.equals(json.optString(RESULT));
    }

    public static boolean isJSONReturnedOK(JSONObject json) {
        return json != null && TextUtils.equals(RESULT_CODE, json.optJSONObject(HEADER).optString(CODE));
    }

}
