
package vn.appsmobi.requests;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import vn.appsmobi.utils.LogUtil;
import vn.appsmobi.utils.MainApplication;
import vn.appsmobi.utils.Utils;

public class Request {
    private static final String TAG = "Request";
    /**
     * The following variables started with STATUS_ are request response status
     * code to indicate callers what happens.
     */
    // Status OK
    public static final int STATUS_OK = 0;
    // Server error, related to HTTPStatus 40X and 50X
    public static final int STATUS_SERVER_ERROR = 1;
    // Client error, include io
    public static final int STATUS_CLIENT_ERROR = 2;
    // Connection parameters error
    public static final int STATUS_PARAM_ERROR = 3;
    // Status network unavailable
    public static final int STATUS_NETWORK_UNAVAILABLE = 4;
    // Status service unavailable
    public static final int STATUS_SERVICE_UNAVAILABLE = 5;
    // Status unknown error
    public static final int STATUS_UNKNOWN_ERROR = 6;
    // NOT MODIFIED
    public static final int STATUS_NOT_MODIFIED = 7;
    // Auth error
    public static final int STATUS_AUTH_ERROR = 8;

    // Timeout (in ms) we specify for each http request
    protected static final int HTTP_REQUEST_TIMEOUT_MS = 10 * 1000;
    protected static final int HTTP_REQUEST_DELAY_MS = 5 * 1000;

    private static final CookieManager sCookieManager = new CookieManager(null,
            CookiePolicy.ACCEPT_ALL);

    // The responsed json result and the user id and Auth token
    private JSONObject mJSONResult;
    private String mUserId;
    private static String sUserAgent;
    private static String sCookie;
    private String mEtag;
    private String mResponse = "";
    // The parameters list
    private Map<String, String> mParamsList;
    protected String mRequestUrl;
    private HTTP_METHOD mRequestMethod;
    private boolean mNeedSignature;
    private HashMap<String, String> mRequestHeaders;
    private int statusCode;

    public Request(String url) {
        mRequestUrl = url;
        mRequestMethod = HTTP_METHOD.POST;
        mNeedSignature = false;
        mRequestHeaders = new HashMap<String, String>();
    }

    public Request addHeader(String name, String value) {
        mRequestHeaders.put(name, value);
        return this;
    }

    public Request setHttpMethod(HTTP_METHOD method) {
        mRequestMethod = method;
        return this;
    }

    // Get the parameters which will be appended to the requested url
    public void addParam(String key, String value) {
        if (mParamsList == null) {
            mParamsList = new HashMap<String, String>();
        }

        mParamsList.put(key, value);
    }

    // Clear all parameters
    public void clearParams() {
        if (mParamsList != null) {
            mParamsList.clear();
        }
    }

    /**
     * Weather the parameters required to be signed
     *
     * @param needed
     * @return this
     */
    public Request setNeedSignature(boolean needed) {
        mNeedSignature = needed;
        return this;
    }

    protected void getConn() {
        final String url = getRequestUrl();
        if (TextUtils.isEmpty(url)) {
//            return null;
        }

        LogUtil.d(TAG, "getConn:The connection url is " + url);
//        HttpURLConnection conn = null;
//        try {
//            final URL req = new URL(url);
//            conn = (HttpURLConnection) req.openConnection();
//            conn.setReadTimeout(HTTP_REQUEST_TIMEOUT_MS);
//            conn.setConnectTimeout(HTTP_REQUEST_TIMEOUT_MS);
//            conn.setRequestMethod(mRequestMethod);
//            if (TextUtils.equals(mRequestMethod, HttpPost.METHOD_NAME)) {
//                conn.setDoOutput(true);
//                conn.setUseCaches(false);
//            }
//            String cookie = getCookies();
//            if (!TextUtils.isEmpty(cookie)) {
//                conn.setRequestProperty("Cookie", getCookies());
//            }
//            conn.setRequestProperty("User-Agent", getUserAgent());
//            if (mRequestHeaders != null && mRequestHeaders.size() > 0) {
//                Iterator iter = mRequestHeaders.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
//                    conn.setRequestProperty(entry.getKey(), entry.getValue());
//                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return conn;
    }

    public int getStatus() {
        if (!Utils.Network.isNetWorkConnected(MainApplication.getContext())) {
            return STATUS_NETWORK_UNAVAILABLE;
        }


        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(mRequestMethod.toValue(), mRequestUrl, future, future) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParamsList;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                HTTP_REQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue mRequestQueue = Volley.newRequestQueue(MainApplication.getContext());
        mRequestQueue.add(request);

        try {
            mResponse = future.get();
            mJSONResult = new JSONObject(mResponse);
            // do something with response
        } catch (InterruptedException e) {
            // handle the error
        } catch (ExecutionException e) {
            VolleyError error = new VolleyError(e);
            if (error instanceof NetworkError) {

            } else if (error instanceof ServerError) {
                statusCode = STATUS_SERVER_ERROR;
            } else if (error instanceof AuthFailureError) {
                statusCode = STATUS_SERVER_ERROR;
            } else if (error instanceof ParseError) {

            } else if (error instanceof NoConnectionError) {
                if (!Utils.Network.isNetWorkConnected(MainApplication.getContext())) {
                    statusCode = STATUS_NETWORK_UNAVAILABLE;
                } else {
                    statusCode = STATUS_SERVICE_UNAVAILABLE;
                }
            } else if (error instanceof TimeoutError) {
                statusCode = STATUS_SERVICE_UNAVAILABLE;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        StringRequest postRequest = new StringRequest(mRequestMethod.toValue(), mRequestUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        mResponse = response;
//                        try {
//                            mJSONResult = new JSONObject(response);
//                        }catch (JSONException e){
//
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        if( error instanceof NetworkError) {
//
//                        }else if( error instanceof ServerError) {
//                            statusCode = STATUS_SERVER_ERROR;
//                        } else if( error instanceof AuthFailureError) {
//                            statusCode = STATUS_SERVER_ERROR;
//                        } else if( error instanceof ParseError) {
//
//                        } else if( error instanceof NoConnectionError) {
//                            if (!Utils.Network.isNetWorkConnected(MainApplication.getContext())) {
//                                statusCode = STATUS_NETWORK_UNAVAILABLE;
//                            } else {
//                                statusCode = STATUS_SERVICE_UNAVAILABLE;
//                            }
//                        } else if( error instanceof TimeoutError) {
//                            statusCode = STATUS_SERVICE_UNAVAILABLE;
//                        }
//
//
//
//
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                return mParamsList;
//            }
//        };
//        Volley.newRequestQueue(MainApplication.getContext()).add(postRequest);


//        HttpURLConnection conn = null;
//        BufferedReader br = null;
//        int statusCode = STATUS_CLIENT_ERROR;
//
//        try {
//            conn = getConn();
//            // Connect to the server
//            conn.connect();
//
//            if (TextUtils.equals(mRequestMethod, HttpPost.METHOD_NAME)) {
//                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//                out.writeBytes(getParams());
//                out.flush();
//            }
//            // Check the response code
//            int responseCode = conn.getResponseCode();
//            LogUtil.d(TAG, "The response code is:" + responseCode);
//            if (responseCode == HttpStatus.SC_OK) {
//            	String compressed = conn
//                        .getHeaderField("Compressed");
//
//                String etag = conn.getHeaderField("etag");
//                if (!TextUtils.isEmpty(etag)) {
//                    mEtag = etag;
//                }
//
//                // Read cookie
//                putCookies(conn.getHeaderFields());
//
//                // Read response from the connection to after posting info
//                InputStream in = conn.getInputStream();
//                if (compressed != null && compressed.equals("Compressed")) {
//                    Inflater inflater = new Inflater(true);
//                    in = new InflaterInputStream(in, inflater);
//                }
//                br = new BufferedReader(new InputStreamReader(in));
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                LogUtil.d(TAG, "The response is:" + sb.toString());
//                mResponse = sb.toString();
//                mJSONResult = new JSONObject(mResponse);
//                statusCode = STATUS_OK;
//
//            } else if (responseCode == HttpStatus.SC_NOT_MODIFIED) {
//                statusCode = STATUS_NOT_MODIFIED;
//            } else if (isServerError(responseCode)) {
//                if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
//                    statusCode = STATUS_AUTH_ERROR;
//                } else {
//                    statusCode = STATUS_SERVER_ERROR;
//                }
//            } else {
//                statusCode = STATUS_UNKNOWN_ERROR;
//            }
//        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
//            statusCode = STATUS_SERVICE_UNAVAILABLE;
//        } catch (JSONException e) {
//            statusCode = STATUS_SERVICE_UNAVAILABLE;
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//            if (!Utils.Network.isNetWorkConnected(MainApplication.getContext())) {
//                statusCode = STATUS_NETWORK_UNAVAILABLE;
//            } else {
//                statusCode = STATUS_SERVICE_UNAVAILABLE;
//            }
//        } finally {
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//        LogUtil.d(TAG, "getStatus:The status code is " + statusCode);
//        LogUtil.d(TAG, "etag is " + conn.getHeaderField("etag"));
        return statusCode;
    }

    public JSONObject requestJSON() {
        return mJSONResult;
    }

    public String getRespone() {


        return mResponse;
    }
//    protected boolean isServerError(int statusCode) {
//        return statusCode == HttpStatus.SC_BAD_REQUEST
//                || statusCode == STATUS_AUTH_ERROR
//                || statusCode == HttpStatus.SC_FORBIDDEN
//                || statusCode == HttpStatus.SC_NOT_ACCEPTABLE
//                || statusCode / 100 == 5;
//    }


    private String getParams() {
//        List<NameValuePair> paramList = null;
//        if (mNeedSignature) {
//            paramList = signParamters();
//        } else {
//            paramList = mParamsList;
//        }
//
//        if (paramList == null) {
//            paramList = new ArrayList<NameValuePair>();
//        }
//
//        return URLEncodedUtils.format(paramList, HTTP.UTF_8);
        return "";
    }

    // The constructed url to requested
    public String getRequestUrl() {
        if (mRequestMethod == HTTP_METHOD.POST) {
            return mRequestUrl + "?random=" + UUID.randomUUID().toString().replaceAll("-", "");
        }
        final String params = getParams();
        return TextUtils.isEmpty(params) ? mRequestUrl : mRequestUrl.contains("?") ? String.format("%s&%s",
                mRequestUrl,
                params) : String.format("%s?%s", mRequestUrl,
                params);
    }

    private String getUserAgent() {
        if (sUserAgent == null) {

            sUserAgent = "";
        }
        return sUserAgent;
    }

    public void setUserAgent(String userAgent) {
        sUserAgent = userAgent;
    }

    private void putCookies(Map<String, List<String>> map) {
        try {
            sCookieManager.put(URI.create(mRequestUrl), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCookies() {

        List<HttpCookie> cookieList = sCookieManager.getCookieStore().getCookies();
        if (cookieList == null || cookieList.size() == 0) {
            return sCookie;
        }

        StringBuilder sbCookie = new StringBuilder();
        for (HttpCookie cookie : cookieList) {
            if (TextUtils.indexOf(URI.create(mRequestUrl).getHost(), cookie.getDomain()) > 0) {
                sbCookie.append(cookie.getName());
                sbCookie.append("=");
                sbCookie.append(cookie.getValue());
                sbCookie.append("; ");
            }
        }

        return sbCookie.toString() + sCookie;
    }

    public String getEtag() {
        return mEtag;
    }


    public enum HTTP_METHOD {
        POST(1),
        GET(0);
        int value;

        private HTTP_METHOD(int value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public int toValue() {
            return this.value;
        }

    }


}
