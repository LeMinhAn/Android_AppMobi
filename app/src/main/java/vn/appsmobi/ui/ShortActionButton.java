package vn.appsmobi.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.DownloadInstallManager;
import vn.appsmobi.utils.InstallChecker;
import vn.appsmobi.utils.Utils;

import static vn.appsmobi.utils.UIUtils.setRatingSize;

/**
 * Created by tobrother on 19/02/2016.
 */
public class ShortActionButton extends ActionArea {

    /**
     * init group view
     */
    View viewParent;
    TextView tvItemStatusAppManage;
    TextView tvItemPercentProcessAppManage;
    RelativeLayout rlProcessAppManage, rlSetting;
    TextView tvNameCardSuggest2;
    CircularProgressBar cpcProcessAppManage;
    CustomRatingBarListItem rbRatingCardSuggest;
    boolean isPause = false;


    public ShortActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewParent = li.inflate(R.layout.short_action_button, null);
        tvItemStatusAppManage = (TextView) viewParent.findViewById(R.id.tvItemStatusAppManage);
        tvNameCardSuggest2 = (TextView) viewParent.findViewById(R.id.tvNameCardSuggest2);
        tvItemPercentProcessAppManage = (TextView) viewParent.findViewById(R.id.tvItemPercentProcessAppManage);
        rlProcessAppManage = (RelativeLayout) viewParent.findViewById(R.id.rlProcessAppManage);
        rlSetting = (RelativeLayout) viewParent.findViewById(R.id.rlSetting);
        rbRatingCardSuggest = (CustomRatingBarListItem) viewParent.findViewById(R.id.rbRatingCardSuggest3);
        setRatingSize(rbRatingCardSuggest, context, 5);
        cpcProcessAppManage = (CircularProgressBar) viewParent.findViewById(R.id.cpcProcessAppManage);
        addView(viewParent);
        tvItemStatusAppManage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // check network connection
                if (!Utils.Network.isNetWorkConnected(mContext)) {
                    Toast.makeText(mContext, mContext.getString(R.string.network_unavaliable), Toast.LENGTH_LONG).show();
                } else {
                    if (!iClicked) {
                        post(new Runnable() {
                            public void run() {
                                InstallChecker.checkAndInstall(mAppInfo, (Activity) getContext());
                            }
                        });
                        //
                    }
                }
            }
        });
        cpcProcessAppManage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
// event stop download
                if (isPause) {
                    DownloadInstallManager.getManager().resumeDownload(mAppInfo);
                    isPause = false;
                } else {
                    DownloadInstallManager.getManager().pauseDownload(mAppInfo);
                    isPause = true;
                }
            }
        });
    }

    public ShortActionButton(Context context) {
        super(context);
    }

    @Override
    protected void bindInstalled(DataCardItem appInfo) {
        rlProcessAppManage.setVisibility(View.GONE);
        rlSetting.setVisibility(View.VISIBLE);
        tvItemStatusAppManage.setText(getContext().getString(R.string.installed));
    }

    @Override
    protected void bindInstalling(DataCardItem appInfo) {
        if (DownloadInstallManager.getManager().mInstallManuallyMap.containsKey(appInfo.getPackge_name())) {
            setEnabled(true);
        } else {
            setEnabled(false);
            rlProcessAppManage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void bindNormal(DataCardItem appInfo) {
        this.iClicked = false;
        //this.tvItemStatusAppManage.setText(getContext().getString(R.string.menu_item_card_install));
        this.rlProcessAppManage.setVisibility(View.GONE);
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.notif_title_download));
        // setOnClickListener(this.mArrangeClickListener);
    }

    @Override
    protected void bindPrice(DataCardItem appInfo) {

    }

    @Override
    protected void bindUpdate(DataCardItem appInfo) {
        this.iClicked = false;
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_update));
        this.rlProcessAppManage.setVisibility(View.GONE);

        // setOnClickListener(this.mArrangeClickListener);
    }

    @Override
    protected void updateProgressConnecting(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        this.rlSetting.setVisibility(View.VISIBLE);
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_connecting));
        this.rlProcessAppManage.setVisibility(View.GONE);
    }

    @Override
    protected void updateProgressDownloading(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        setEnabled(false);
        String percent = "0";
        if ((progress.totalBytes > 0) && (progress.currBytes > 0)) {
            percent = String.valueOf(Math.round(100.0D * progress.currBytes / progress.totalBytes));
        }
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_downloading));
        this.rlProcessAppManage.setVisibility(View.VISIBLE);
        this.cpcProcessAppManage.setProgress(Integer.parseInt(percent));
        this.tvItemPercentProcessAppManage.setText(percent + " %");
    }

    @Override
    protected void updateProgressInstalling(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        if (DownloadInstallManager.getManager().mInstallManuallyMap.containsKey(appInfo.getPackge_name())) {
            setEnabled(true);
            this.tvItemStatusAppManage.setVisibility(View.VISIBLE);
            this.rlSetting.setVisibility(VISIBLE);
            this.tvItemStatusAppManage.setText(getContext().getString(R.string.menu_item_card_install));
            this.rlProcessAppManage.setVisibility(View.GONE);
        } else {
            setEnabled(false);
            this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_installing));
            this.rlProcessAppManage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void updateProgressPaused(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        this.rlSetting.setVisibility(View.VISIBLE);
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_pause));
        this.rlProcessAppManage.setVisibility(View.GONE);
    }

    @Override
    protected void updateProgressPending(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_pending));
        this.rlProcessAppManage.setVisibility(View.GONE);
    }

    @Override
    protected void updateProgressVerifying(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.title_verifying));
        this.rlProcessAppManage.setVisibility(View.GONE);
    }

    @Override
    protected void updateProgressInstalled(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
        this.tvItemStatusAppManage.setText(getContext().getString(R.string.installed));
        this.rlProcessAppManage.setVisibility(View.GONE);
        DataCardItem appInfo1 = DataCardItem.get(appInfo.getId());
        appInfo1.setCard_status(Constants.CARD_STATUS.STATUS_INSTALLED);
        this.bindInstalled(appInfo1);
    }
}
