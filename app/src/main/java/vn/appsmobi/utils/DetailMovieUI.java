package vn.appsmobi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.adapter.ImageAdapter;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.model.MovieItem;
import vn.appsmobi.ui.StatusDetailButton;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;

/**
 * Created by tobrother272 on 1/26/2016.
 */
//Kế thừa từ lớp cha AppDetailActivity
public class DetailMovieUI extends DetailActivityUI   {
    TextView tvSizeDetail, tvTimeDetail, tvCategoriesDetail;
    MovieItem movieItem;
    ObservableScrollView svParentAppDetailActivity;
    RelativeLayout rlContainerImagePromo;
    Toolbar toolbarAppDetailActivity;
    LinearLayout lnSizeDetail, lnTimeDetail;

    public DetailMovieUI(Activity activity, DisplayImageOptions options, MovieItem movieItem) {
        super(activity, options, movieItem);
        this.movieItem = movieItem;
        svParentAppDetailActivity.pageScroll(View.FOCUS_UP);

    }

    @Override
    View getLayoutView() {
        LayoutInflater li = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        return li.inflate(R.layout.activity_app_detail, null);
    }

    @Override
    void setSizeCustomView(int sizeCustomView) {

    }

    @Override
    void customView() {
        tvSizeDetail = (TextView) rootView.findViewById(R.id.tvSizeDetail);
        tvTimeDetail = (TextView) rootView.findViewById(R.id.tvTimeDetail);
        tvCategoriesDetail = (TextView) rootView.findViewById(R.id.tvTimeDetail);
        lnTimeDetail = (LinearLayout) rootView.findViewById(R.id.lnTimeDetail);
        lnSizeDetail = (LinearLayout) rootView.findViewById(R.id.lnSizeDetail);
        toolbarAppDetailActivity = (Toolbar) rootView.findViewById(R.id.toolbarAppDetailActivity);
        svParentAppDetailActivity = (ObservableScrollView) rootView.findViewById(R.id.svParentAppDetailActivity);
        rlContainerImagePromo = (RelativeLayout) rootView.findViewById(R.id.rlContainerImagePromo);
        ((AppCompatActivity) activity).setSupportActionBar(toolbarAppDetailActivity);
        toolbarAppDetailActivity.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbarAppDetailActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        svParentAppDetailActivity.setScrollViewCallbacks(this);
    }

    @Override
    void initCustomValue() {
        tvCategoriesDetail.setVisibility(View.VISIBLE);
        lnSizeDetail.setVisibility(View.VISIBLE);
        lnTimeDetail.setVisibility(View.VISIBLE);
//        if(movieItem==null){
//            Log.e("tag", "nulllllllllllll");
//        }
//        Log.e("tag", "getSize" + movieItem.getSize());
//        Log.e("tag", "getDuration_time" + movieItem.getDuration_time());
          tvSizeDetail.setText(Double.toString(dataCardItem.getSize())+" MB");
//        tvTimeDetail.setText(Double.toString(movieItem.getDuration_time())+" Phút");
//        tvCategoriesDetail.setText(Utils.getCategory(movieItem.getCatetoryItems()));

        toolbarAppDetailActivity.setTitle(dataCardItem.getName());
        super.mBidingData = new StatusDetailButton.BidingData() {
            @Override
            public void bindInstalled(final DataCardItem appInfo) {

            }

            @Override
            public void bindInstalling(DataCardItem appInfo) {

            }

            @Override
            public void bindNormal(final DataCardItem appInfo) {
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
                btStatusDetailButton.getBtOne().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setText(activity.getString(R.string.watch));
                btStatusDetailButton.getBtTwo().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtTwo().setText(activity.getString(R.string.get));
                btStatusDetailButton.getBtTwo().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                btStatusDetailButton.getBtOne().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @Override
            public void bindPrice(DataCardItem appInfo) {

            }

            @Override
            public void bindUpdate(final DataCardItem appInfo) {


            }

            @Override
            public void updateProgressConnecting(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setText(activity.getString(R.string.title_connecting));
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
            }

            @Override
            public void updateProgressDownloading(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                btStatusDetailButton.setEnabled(false);
                String str = "0";
                String percent = "0";
                if ((progress.totalBytes > 0) && (progress.currBytes > 0)) {
                    percent = String.valueOf(Math.round(100.0D * progress.currBytes / progress.totalBytes));
                    str = Utils.NumberFormat.valueOf((double) progress.currBytes) + "/" + Utils.NumberFormat.valueOf((double) progress.totalBytes) + " (MB) ";
                }
                btStatusDetailButton.getLlSetting().setVisibility(View.GONE);
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.VISIBLE);
                btStatusDetailButton.getPbProcessAppManage().setProgress(Integer.parseInt(percent));
                btStatusDetailButton.getTvItemProcessAppManage().setText(str);
                btStatusDetailButton.getTvItemPercentProcessAppManage().setText(percent + " %");
            }

            @Override
            public void updateProgressInstalling(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                if (DownloadInstallManager.getManager().mInstallManuallyMap.containsKey(appInfo.getPackge_name())) {
                    btStatusDetailButton.setEnabled(true);
                    btStatusDetailButton.getBtOne().setVisibility(View.VISIBLE);
                    btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                    btStatusDetailButton.getBtOne().setText(activity.getString(R.string.menu_item_card_install));
                    btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);

                } else {
                    btStatusDetailButton.setEnabled(false);
                    btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                    btStatusDetailButton.getBtOne().setVisibility(View.VISIBLE);
                    btStatusDetailButton.getBtOne().setText(activity.getString(R.string.title_installing));
                    btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
                }
            }

            @Override
            public void updateProgressPaused(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setText(activity.getString(R.string.title_pause));
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
            }

            @Override
            public void updateProgressPending(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setText(activity.getString(R.string.title_pending));
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
            }

            @Override
            public void updateProgressVerifying(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setText(activity.getString(R.string.title_verifying));
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
            }

            @Override
            public void updateProgressInstalled(DataCardItem appInfo, DownloadInstallManager.Progress progress) {
                btStatusDetailButton.getLlSetting().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtOne().setText(activity.getString(R.string.open_app));
                btStatusDetailButton.getBtTwo().setVisibility(View.VISIBLE);
                btStatusDetailButton.getBtTwo().setText(activity.getString(R.string.uninstall));
                btStatusDetailButton.getRlProcessAppManage().setVisibility(View.GONE);
                DataCardItem appInfo1 = DataCardItem.get(appInfo.getId());
                appInfo1.setCard_status(Constants.CARD_STATUS.STATUS_INSTALLED);
                this.bindInstalled(appInfo1);
            }

            @Override
            public void eventForButtonOne(DataCardItem appInfo, Context context) {
                InstallChecker.checkAndInstall(appInfo, (Activity) context);
            }

            @Override
            public void eventForButtonTwo(DataCardItem appInfo, DownloadInstallManager.Progress progress) {

            }

        };
    }


    @Override
    void updateCustomView(DataCardItem dataCardItem) {
        movieItem = (MovieItem) dataCardItem;
//        imageAdapter = new ImageAdapter(activity, movieItem.getSlide_show(), options);
//        gSlideShow.setAdapter(imageAdapter);
    }

    @Override
    ArrayList<String> getSlideImage(DataCardItem dataCardItem) {
        return null;
    }

    //Set kích thước cho image trong detail film
    @Override
    LinearLayout.LayoutParams getLayoutParamsIcon() {
        int widthIcon = getScreenWidth(activity) / 5;
        int heightIcon = widthIcon * 2;
        return new LinearLayout.LayoutParams(widthIcon, heightIcon);
    }

    @Override
    public void updateContent() {

    }

    @Override
    public void updateStatus() {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = activity.getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / rlContainerImagePromo.getHeight());
        toolbarAppDetailActivity.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(rlContainerImagePromo, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
