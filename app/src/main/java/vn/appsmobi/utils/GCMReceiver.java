package vn.appsmobi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Map;

import vn.appsmobi.R;
import vn.appsmobi.activity.GcmActivity;

public class GCMReceiver {
    public static final String TYPE_UPDATE = "update";
    public static final String TYPE_UPDATE_NOTIFY = "update_notify";
    public static final String TYPE_OPENAPP = "open";
    public static final String TYPE_OPENAPP_NOTIFY = "open_notify";
    public static final String TYPE_OPENLINK = "openlink";
    // b chua dc
    public static final String TYPE_IMEDIATE_DIALOG_DOWNAPP = "immediate_dialogdownapp";
    public static final String TYPE_IMEDIATE_DIALOG_OPENLINK = "immediate_dialogopenlink";
    // e chua dc
    public static final String TYPE_DIALOG_DOWNAPP = "dialogdownapp";
    public static final String TYPE_DIALOG_OPENLINK = "dialogopenlink";
    public static final String TYPE_DETAIL = "detail";

    private final String TAG = "GCMReceiver";
    private String mType;
    private Context mContext;
    private Map<String, String> mData;

    public GCMReceiver(Context context, Map<String, String> data) {
        mData = data;
        mType = data.get(Constants.INTENT_EXTRA_NOTIFICATION_TYPE);
        mContext = context;
        Log.i(TAG, "data " + mData.get(Constants.INTENT_EXTRA_NOTIFICATION_TYPE) + " " + mData.get(Constants.INTENT_EXTRA_NOTIFICATION_MESSAGE));
    }


    @SuppressWarnings("deprecation")
    private void createNotification(Bitmap picture) {
        Log.i(TAG, "createNotification" + picture);
        int icon = R.drawable.ic_dns_black_48dp;
        String message = mData.get(Constants.INTENT_EXTRA_NOTIFICATION_MESSAGE);
        String title = mContext.getString(R.string.app_name);


        Intent notificationIntent = new Intent(mContext, GcmActivity.class);
        notificationIntent.putExtra(Constants.INTENT_EXTRA_NOTIFICATION_TYPE, mData.get(Constants.INTENT_EXTRA_NOTIFICATION_TYPE));
        notificationIntent.putExtra(Constants.INTENT_EXTRA_NOTIFICATION_URL, mData.get(Constants.INTENT_EXTRA_NOTIFICATION_URL));
        notificationIntent.putExtra(Constants.INTENT_EXTRA_NOTIFICATION_MESSAGE, mData.get(Constants.INTENT_EXTRA_NOTIFICATION_MESSAGE));
        notificationIntent.putExtra(Constants.INTENT_EXTRA_NOTIFICATION_CONTENT, mData.get(Constants.INTENT_EXTRA_NOTIFICATION_CONTENT));
        notificationIntent.putExtra(Constants.INTENT_EXTRA_NOTIFICATION_PAKAGENAME, mData.get(Constants.INTENT_EXTRA_NOTIFICATION_PAKAGENAME));
        Log.i(TAG, "getAction data receiver" + notificationIntent.getExtras());

        NotificationUtil.showNotification(mContext, icon, picture, title, message, notificationIntent);
    }

    public void onReceiver() {
        if (mType.equals(TYPE_UPDATE)) {
            // ngay lap tuc tai file ve va cai dat
            Log.i(TAG, "run action update ");
            runActionUpdate(true, null);
        } else if (mType.equals(TYPE_OPENAPP)) {
            Log.i(TAG, "run action open app ");
            openApp();
        } else {
            String image = mData.get(Constants.INTENT_EXTRA_NOTIFICATION_IMAGE);
            Log.i(TAG, "image dkm:" + image);

            if (image != null && !image.isEmpty() && !image.equals("") && !image.equals("null")) {
                ImageLoader.getInstance().loadImage(image, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                        createNotification(null);

                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                        createNotification(arg2);

                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        createNotification(null);

                    }
                });
            } else
                createNotification(null);
        }
    }

    private void openApp() {
        String packagename = mData.get(Constants.INTENT_EXTRA_NOTIFICATION_PAKAGENAME);
        try {
            PackageManager pm = mContext.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packagename);
            mContext.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public void runActionUpdate(final boolean isNotify, final Activity mActivity) {
        // download and install apk file that provided by the key "Constant.INTENT_EXTRA_NOTIFICATION_URL"
        /*
		UpdateTask task = new UpdateTask(mContext,"",isNotify) {

			@Override
			public void onUpdateComplete(JSONObject responeObject) {
				if(!isNotify && mActivity != null){
					mActivity.finish();
				}
			}
		};
		String updateURl = mData.get(Constants.INTENT_EXTRA_NOTIFICATION_URL);
		Log.i(TAG,"updateURl:" + updateURl);
		task.downloadAndUpdate(updateURl);
		*/
    }

}
