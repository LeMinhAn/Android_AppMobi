package vn.appsmobi.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import vn.appsmobi.R;
import vn.appsmobi.model.WallPaperItem;

/**
 * Created by tobrother on 27/01/2016.
 */
//Kế thừa từ lớp cha DetailMediaActivityUI
public class DetailWallPaperUI extends DetailMediaActivityUI {

    WallPaperItem wallPaperItem;

    public DetailWallPaperUI(Activity activity, DisplayImageOptions options, WallPaperItem wallPaperItem) {
        super(activity, options, wallPaperItem);
        this.wallPaperItem = wallPaperItem;
    }

    @Override
    View getLayoutView() {
        LayoutInflater li = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        return li.inflate(R.layout.activity_image_detail, null);
    }

    @Override
    void setSizeCustomView() {

    }

    @Override
    void customView() {

    }

    @Override
    void initCustomValue() {

    }

    @Override
    void initCustomAction() {

    }
}
