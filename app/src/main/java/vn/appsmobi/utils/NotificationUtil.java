package vn.appsmobi.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import vn.appsmobi.R;


public class NotificationUtil {

    public static void cancelNotification(Context context, String tag) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(tag, 0);
    }

    public static void showNotification(Context mContext, int icon, Bitmap picture, String title, String message, Intent notificationIntent) {
        showNotification(mContext, icon, picture, title, message, notificationIntent, 0, null);
    }

    public static void showNotification(Context mContext, String title, String message, Intent notificationIntent, int id, String tag) {
        int icon = R.drawable.apps;
        showNotification(mContext, icon, null, title, message, notificationIntent, id, null);
    }

    public static void showNotification(Context mContext, int icon, Bitmap picture, String title, String message, Intent notificationIntent, int id, String tag) {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(icon)
                            .setContentTitle(title)
                            .setContentText(message);
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

            if (notificationIntent != null) {
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(GcmActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent gcmPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(gcmPendingIntent);


                //				PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //				mBuilder.setContentIntent(pendingIntent);
            }

            if (picture != null) {
                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
                style.setBigContentTitle(title);
                style.setSummaryText(message);
                style.bigPicture(picture);
                mBuilder.setStyle(style);

            } else {
                Log.i("NotificationUtil", "inboxStyle");
                NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle()
                        .bigText(message);
                mBuilder.setStyle(inboxStyle);
            }

            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;
            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(tag, id, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
