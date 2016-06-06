package vn.appsmobi.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.ui.TouchImageView;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;
import static vn.appsmobi.utils.UIUtils.setViewSize;

/**
 * Created by tobrother on 27/01/2016.
 */
//Lớp cha của lớp DetailWallPaperUI, DetailRingToneUI
public abstract class DetailMediaActivityUI {
    // Activity
    Activity activity;
    // ImageView
    ImageView ivShareMedia, ivLikeMedia, ivInfoMedia, ivBackGroundMedia;
    View rootView;
    // image loader
    DisplayImageOptions options;
    // DataCardItem
    DataCardItem dataCardItem;
    // zoom image configure
    private static final TouchImageView.ScaleType[] scaleTypes = {TouchImageView.ScaleType.CENTER, TouchImageView.ScaleType.CENTER_CROP, TouchImageView.ScaleType.CENTER_INSIDE, TouchImageView.ScaleType.FIT_XY, TouchImageView.ScaleType.FIT_CENTER};

    public DetailMediaActivityUI(Activity activity, DisplayImageOptions options, DataCardItem dataCardItem) {
        this.activity = activity;
        this.options = options;
        this.dataCardItem = dataCardItem;
        initView();
        activity.setContentView(rootView);
        initValue();
        initAction();
    }

    public void initView() {
        rootView = getLayoutView();
        ivBackGroundMedia = (TouchImageView) rootView.findViewById(R.id.ivImageDetailImage);
        ivBackGroundMedia.setMinimumWidth(1000);
        ivShareMedia = (ImageView) rootView.findViewById(R.id.ivShareImage);
        ivLikeMedia = (ImageView) rootView.findViewById(R.id.ivLikeImage);
        ivInfoMedia = (ImageView) rootView.findViewById(R.id.ivInfoImage);
        customView();
        setSizeView();
    }

    public void setSizeView() {
        int sizeIcon = getScreenWidth(activity) / 8;
        setViewSize(ivShareMedia, sizeIcon, sizeIcon);
        setViewSize(ivInfoMedia, sizeIcon, sizeIcon);
        setViewSize(ivLikeMedia, sizeIcon, sizeIcon);
        setSizeCustomView();
    }

    public void initAction() {
        ivShareMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivLikeMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivInfoMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] infos = {"Name : " + dataCardItem.getName(), "Description : " + dataCardItem.getShort_description(), "Size : " + Float.toString(dataCardItem.getSize()), "Download : " + Integer.toString(dataCardItem.getDowns_count())};
                new MaterialDialog.Builder(activity)
                        .title("WALLPAPER INFO")
                        .items(infos)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            }
                        })
                        .show();
            }
        });
        initCustomAction();
    }

    public void initValue() {
        ImageLoader.getInstance().displayImage(dataCardItem.getIcon_image(), ivBackGroundMedia, options);
        initCustomValue();
    }

    // abstract function
    abstract View getLayoutView();

    // set size for custom view in child
    abstract void setSizeCustomView();

    // init custom views in child
    abstract void customView();

    // init custom values for views of child
    abstract void initCustomValue();

    // init action for custom views in child
    abstract void initCustomAction();
}
