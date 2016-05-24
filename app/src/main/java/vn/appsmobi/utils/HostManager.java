
package vn.appsmobi.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class HostManager {
    private static final String TAG = "HostManager";
    // cookie form campaign webview
    public static final String DOMAIN_BASE = "com.android.amb";

    private static void setCookie(Context context, String name, String value) {
        setCookie(context, name, value, DOMAIN_BASE);
    }


    public static void setCookie(Context context, String name, String value, String domain) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager == null) {
            return;
        }
        String cookieString = name + "=" + value + "; domain=" + domain;
        cookieManager.setCookie(domain, cookieString);
        LogUtil.d(TAG, "set Cookie: " + domain);
        CookieSyncManager.getInstance().sync();
    }

    private static void removeCookie(Context context, String name, String domain) {
        LogUtil.d(TAG, "remove Cookie: " + domain + ": " + name);
        CookieManager cookieManager = CookieManager.getInstance();
        CookieSyncManager.createInstance(context);
        String cookies = cookieManager.getCookie(domain);
        if (cookies == null) {
            return;
        }
        for (String cookie : cookies.split(";")) {
            String[] cookieValues = cookie.split("=");
            if (cookieValues.length < 2) {
                return;
            }
            if (TextUtils.equals(cookieValues[0].trim(), name)) {
                StringBuilder cookieString = new StringBuilder();
                cookieString.append(name);
                cookieString.append("=;domain=");
                cookieString.append(domain);
                cookieString.append(";expires=-1");
                cookieManager.setCookie(domain, cookieString.toString());
                // cookieManager.removeExpiredCookie();
                CookieSyncManager.getInstance().sync();
            }
        }
    }


    public static void initSettingCookies(Context context) {
//        HostManager.setCookie(context,
//                HostManager.Parameters.Keys.COOKIE_NAME_PLATFORM,
//                HostManager.Parameters.Values.COOKIE_VALUE_PLATFROM);
    }
}
