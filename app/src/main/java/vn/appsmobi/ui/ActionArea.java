package vn.appsmobi.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.DownloadInstallManager;
import vn.appsmobi.utils.LocalAppManager;

/**
 * Created by tobrother on 17/02/2016.
 */
public abstract class ActionArea extends LinearLayout {


    protected Context mContext;
    protected DataCardItem mAppInfo;
    protected boolean iClicked = false;

    //public abstract void clickListener();
    /*
    protected View.OnClickListener mArrangeClickListener = new View.OnClickListener()
    {
        public void onClick(View paramAnonymousView)
        {


            clickListener();


        }
    };
    */
    public boolean getIclick() {
        return iClicked;
    }

    public void setIclick(boolean iClicked) {
        this.iClicked = iClicked;
    }

    private Handler mHandler = new Handler();
    //  protected LaunchClickListener mLaunchClickListener = new LaunchClickListener();
    private DownloadInstallManager.ProgressListener mProgressListener = new DownloadInstallManager.ProgressListener() {

        @Override
        public void onProgressUpdate(String appId, final DownloadInstallManager.Progress progress) {
            ActionArea.this.mHandler.post(new Runnable() {
                public void run() {
                    if (progress == null)
                        return;
                    switch (progress.status) {
                        case 0:
                        case 2:
                            ActionArea.this.updateProgressPending(ActionArea.this.mAppInfo, progress);
                            break;
                        case 1:
                            ActionArea.this.updateProgressConnecting(ActionArea.this.mAppInfo, progress);
                            break;
                        case 5:
                            ActionArea.this.updateProgressVerifying(ActionArea.this.mAppInfo, progress);
                            break;
                        case 6:
                            ActionArea.this.updateProgressInstalling(ActionArea.this.mAppInfo, progress);
                            break;
                        case 3:
                            ActionArea.this.iClicked = true;
                            ActionArea.this.updateProgressDownloading(ActionArea.this.mAppInfo, progress);
                            break;
                        case 4:
                            ActionArea.this.updateProgressPaused(ActionArea.this.mAppInfo, progress);
                            break;
                        default:
                            break;
                    }
                }
            });
        }


        @Override
        public void onStateUpdate(String paramString, int paramInt1,
                                  int paramInt2) {


        }
    };

    public ActionArea(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO Auto-generated constructor stub
        this.mContext = context;

    }

    /**
     * Phong
     *
     * @param context
     */
    public ActionArea(Context context) {
        super(context);
        // this.setGravity(Gravity.BOTTOM);
        // TODO Auto-generated constructor stub
    }

    public void rebind(DataCardItem appInfo) {

        rebind(appInfo, true);
    }

    public void rebind(DataCardItem appInfo, boolean isActionable) {

        if (this.mAppInfo != null) {
            DownloadInstallManager.getManager().removeProgressListener(String.valueOf(this.mAppInfo.getId()), this.mProgressListener);
        }
        this.mAppInfo = appInfo;
        bindNormal(appInfo);
        // DownloadInstallManager.getManager().addProgressListener(appInfo.getId(), this.mProgressListener);
        if (!isActionable) {
            setVisibility(View.GONE);
        } else {
            if (appInfo.getCard_status() == Constants.CARD_STATUS.STATUS_INSTALLED) {

                DataCardItem localAppInfo = LocalAppManager.getManager().getLocalAppInfo(appInfo.getPackge_name());
                if (localAppInfo == null) {
                    Log.v("", "MarketActionArea status error for app " + appInfo.getPackge_name() + " : local app does not exists, but status is STATUS_INSTAILLED");
                } else if (appInfo.getVersion_code() > localAppInfo.getVersion_code()) {
                    bindUpdate(appInfo);
                    if (!isActionable)
                        setVisibility(View.GONE);
                } else {
                    bindInstalled(appInfo);
                    setVisibility(View.VISIBLE);
                }
            } else if (appInfo.getCard_status() == Constants.CARD_STATUS.STATUS_INSTALLING) {
                bindInstalling(appInfo);
                DownloadInstallManager.getManager().addProgressListener(String.valueOf(appInfo.getId()), this.mProgressListener);
                setVisibility(View.VISIBLE);
            }
        }

    }


    protected abstract void bindInstalled(DataCardItem appInfo);

    protected abstract void bindInstalling(DataCardItem appInfo);

    protected abstract void bindNormal(DataCardItem appInfo);

    protected abstract void bindPrice(DataCardItem appInfo);

    protected abstract void bindUpdate(DataCardItem appInfo);

    protected abstract void updateProgressConnecting(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected abstract void updateProgressDownloading(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected abstract void updateProgressInstalling(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected abstract void updateProgressPaused(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected abstract void updateProgressPending(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected abstract void updateProgressVerifying(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected abstract void updateProgressInstalled(DataCardItem appInfo, DownloadInstallManager.Progress progress);

    protected class LaunchClickListener implements View.OnClickListener {
        private Intent mIntent;

        protected LaunchClickListener() {
        }

        private void showNotifyBubble() {
            Toast.makeText(ActionArea.this.mContext, ActionArea.this.mContext.getString(R.string.title_downloadx, ActionArea.this.mAppInfo.getName()), Toast.LENGTH_SHORT).show();
        }

        public void onClick(View view) {
            if (this.mIntent == null)
                this.mIntent = ActionArea.this.getContext().getPackageManager().getLaunchIntentForPackage(ActionArea.this.mAppInfo.getPackge_name());
            if (this.mIntent == null)
                showNotifyBubble();
            else {
                try {
                    ActionArea.this.getContext().startActivity(this.mIntent);
                } catch (ActivityNotFoundException localActivityNotFoundException) {
                    showNotifyBubble();
                }
            }
        }

        public void setIntent(Intent paramIntent) {
            this.mIntent = paramIntent;
        }
    }
}
