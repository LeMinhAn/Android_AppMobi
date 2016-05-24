package vn.appsmobi.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import vn.appsmobi.R;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.GCMReceiver;


public class GcmActivity extends Activity {
    private static final String TAG = GcmActivity.class.getSimpleName();
    private String mType;
    private String murl;
    private String mPacketName;
    private String mContent;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getExtras();
        Log.i(TAG, "onCreate:" + bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(false);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }

        if (bundle != null) {
            mType = bundle.getString(Constants.INTENT_EXTRA_NOTIFICATION_TYPE);
            murl = bundle.getString(Constants.INTENT_EXTRA_NOTIFICATION_URL);
            mPacketName = bundle.getString(Constants.INTENT_EXTRA_NOTIFICATION_PAKAGENAME);
            mContent = bundle.getString(Constants.INTENT_EXTRA_NOTIFICATION_CONTENT);

            Log.i(TAG, mType);
            if (mType.equals(GCMReceiver.TYPE_OPENAPP_NOTIFY)) {
                runActionOpenApp();
            } else if (mType.equals(GCMReceiver.TYPE_DETAIL)) {
                runActionGotoDetail();
            } else if (mType.equals(GCMReceiver.TYPE_OPENLINK)) {
                runActionOpenLink();
            } else if (mType.equals(GCMReceiver.TYPE_DIALOG_DOWNAPP) || mType.equals(GCMReceiver.TYPE_DIALOG_OPENLINK)) {
                runActionDialog();
            } else if (mType.equals(GCMReceiver.TYPE_UPDATE_NOTIFY)) {
                runActionUpdateDownload();
            } else {
                startActivityDefault();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //		super.onBackPressed();
    }

    private void startActivityDefault() {
        String packageName = getPackageName();
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    private void startAppByPackageName(String packagename) {
        try {
            PackageManager pm = getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packagename);
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void openLink(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void runActionDialog() {
        setContentView(R.layout.activity_gcm);
        WebView wv = (WebView) findViewById(R.id.wv);
        TextView btnOk = (TextView) findViewById(R.id.btnDialogOk);
        TextView btnCancel = (TextView) findViewById(R.id.btnDialogCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GcmActivity.this.finish();
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        wv.loadUrl(mContent);

        if (mType.equals(GCMReceiver.TYPE_DIALOG_DOWNAPP)) {
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    GcmActivity.this.runActionUpdateDownload();
                }
            });

        } else if (mType.equals(GCMReceiver.TYPE_DIALOG_OPENLINK)) {
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    GcmActivity.this.runActionOpenLink();
                    GcmActivity.this.finish();
                }
            });

        }
    }

    private void runActionUpdateDownload() {
        Map<String, String> data = new HashMap<String, String>();
        data.put(Constants.INTENT_EXTRA_NOTIFICATION_MESSAGE, mContent);
        data.put(Constants.INTENT_EXTRA_NOTIFICATION_TYPE, mType);
        data.put(Constants.INTENT_EXTRA_NOTIFICATION_PAKAGENAME, mPacketName);
        data.put(Constants.INTENT_EXTRA_NOTIFICATION_URL, murl);
        GCMReceiver receiver = new GCMReceiver(this, data);
        receiver.runActionUpdate(false, this);
    }

    private void runActionOpenLink() {
        Log.i(TAG, "runActionOpenLink " + murl);
        openLink(murl);
        finish();
    }

    private void runActionOpenApp() {
        Log.i(TAG, "runActionOpenApp " + mPacketName);
        startAppByPackageName(mPacketName);
        finish();
    }

    private void runActionGotoDetail() {
        Intent goToMarket = null;
        goToMarket = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://detail?id=" + mPacketName));
        startActivity(goToMarket);
    }
}
