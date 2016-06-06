package vn.appsmobi.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.DownloadInstallManager;
import vn.appsmobi.utils.Utils;

/**
 * Created by tobrother on 18/02/2016.
 */

public class StatusDetailButton extends ActionArea {

    /**
     * init group view
     */
    View viewParent;
    // LinearLayout
    private LinearLayout llSetting;
    //Button
    private Button btOne, btTwo;
    //RelativeLayout
    private RelativeLayout rlProcessAppManage;
    // Progressbar
    private ProgressBar pbProcessAppManage;
    // ImageView
    private ImageView ivStopProcessAppManager;
    // TextView
    private TextView tvItemProcessAppManage, tvItemPercentProcessAppManage;
    public BidingData mBidingData;

    public void setmBidingData(BidingData mBidingData) {
        this.mBidingData = mBidingData;
    }

    public abstract interface BidingData {
        public abstract void bindInstalled(DataCardItem appInfo);

        public abstract void bindInstalling(DataCardItem appInfo);

        public abstract void bindNormal(DataCardItem appInfo);

        public abstract void bindPrice(DataCardItem appInfo);

        public abstract void bindUpdate(DataCardItem appInfo);

        public abstract void updateProgressConnecting(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void updateProgressDownloading(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void updateProgressInstalling(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void updateProgressPaused(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void updateProgressPending(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void updateProgressVerifying(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void updateProgressInstalled(DataCardItem appInfo, DownloadInstallManager.Progress progress);

        public abstract void eventForButtonOne(DataCardItem appInfo, Context context);

        public abstract void eventForButtonTwo(DataCardItem appInfo, DownloadInstallManager.Progress progress);


    }

    public StatusDetailButton(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewParent = li.inflate(R.layout.detail_status_button, null);
        // init view
        llSetting = (LinearLayout) viewParent.findViewById(R.id.llSetting);
        btOne = (Button) viewParent.findViewById(R.id.btOne);
        btTwo = (Button) viewParent.findViewById(R.id.btTwo);
        rlProcessAppManage = (RelativeLayout) viewParent.findViewById(R.id.rlProcessAppManage);
        pbProcessAppManage = (ProgressBar) viewParent.findViewById(R.id.pbProcessAppManage);
        ivStopProcessAppManager = (ImageView) viewParent.findViewById(R.id.ivStopProcessAppManager);
        tvItemProcessAppManage = (TextView) viewParent.findViewById(R.id.tvItemProcessAppManage);
        tvItemPercentProcessAppManage = (TextView) viewParent.findViewById(R.id.tvItemPercentProcessAppManage);
        //Add view parent vào LinearLayout (ActionArea extends LinearLayout)
        addView(viewParent);
        btOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.Network.isNetWorkConnected(mContext)) {
                    Toast.makeText(mContext, mContext.getString(R.string.network_unavaliable), Toast.LENGTH_LONG).show();
                } else {
                    if (!iClicked) {
                        post(new Runnable() {
                            public void run() {
                                mBidingData.eventForButtonOne(mAppInfo, context);
                            }
                        });
                    }
                }
            }
        });

    }

    public StatusDetailButton(Context context) {
        super(context);
    }

    @Override
    protected void bindInstalled(DataCardItem appInfo) {
        mBidingData.bindInstalled(appInfo);
    }

    @Override
    protected void bindInstalling(DataCardItem appInfo) {
        mBidingData.bindInstalling(appInfo);
    }

    @Override
    protected void bindNormal(DataCardItem appInfo) {
        mBidingData.bindNormal(appInfo);
    }

    @Override
    protected void bindPrice(DataCardItem appInfo) {
        mBidingData.bindPrice(appInfo);
    }

    @Override
    protected void bindUpdate(DataCardItem appInfo) {
        mBidingData.bindUpdate(appInfo);
    }

    @Override
    protected void updateProgressConnecting(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressConnecting(appInfo, progress);
    }

    @Override
    protected void updateProgressDownloading(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressDownloading(appInfo, progress);

    }

    @Override
    protected void updateProgressInstalling(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressInstalling(appInfo, progress);
    }

    @Override
    protected void updateProgressPaused(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressPaused(appInfo, progress);
    }

    @Override
    protected void updateProgressPending(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressPending(appInfo, progress);
    }

    @Override
    protected void updateProgressVerifying(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressVerifying(appInfo, progress);
    }

    @Override
    protected void updateProgressInstalled(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        mBidingData.updateProgressInstalled(appInfo, progress);
    }

    public LinearLayout getLlSetting() {
        return llSetting;
    }

    public void setLlSetting(LinearLayout llSetting) {
        this.llSetting = llSetting;
    }

    public Button getBtOne() {
        return btOne;
    }

    public void setBtOne(Button btOne) {
        this.btOne = btOne;
    }

    public Button getBtTwo() {
        return btTwo;
    }

    public void setBtTwo(Button btTwo) {
        this.btTwo = btTwo;
    }

    public RelativeLayout getRlProcessAppManage() {
        return rlProcessAppManage;
    }

    public void setRlProcessAppManage(RelativeLayout rlProcessAppManage) {
        this.rlProcessAppManage = rlProcessAppManage;
    }

    public ProgressBar getPbProcessAppManage() {
        return pbProcessAppManage;
    }

    public void setPbProcessAppManage(ProgressBar pbProcessAppManage) {
        this.pbProcessAppManage = pbProcessAppManage;
    }

    public ImageView getIvStopProcessAppManager() {
        return ivStopProcessAppManager;
    }

    public void setIvStopProcessAppManager(ImageView ivStopProcessAppManager) {
        this.ivStopProcessAppManager = ivStopProcessAppManager;
    }

    public TextView getTvItemProcessAppManage() {
        return tvItemProcessAppManage;
    }

    public void setTvItemProcessAppManage(TextView tvItemProcessAppManage) {
        this.tvItemProcessAppManage = tvItemProcessAppManage;
    }

    public TextView getTvItemPercentProcessAppManage() {
        return tvItemPercentProcessAppManage;
    }

    public void setTvItemPercentProcessAppManage(TextView tvItemPercentProcessAppManage) {
        this.tvItemPercentProcessAppManage = tvItemPercentProcessAppManage;
    }
}
