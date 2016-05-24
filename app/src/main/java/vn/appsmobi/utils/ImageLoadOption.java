package vn.appsmobi.utils;

/**
 * Created by tobrother on 28/01/2016.
 */

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import vn.appsmobi.R;

public class ImageLoadOption {
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .displayer(new SimpleBitmapDisplayer()).build();

    public static DisplayImageOptions options_icon = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.place_holder_icon)
            .showImageOnFail(R.drawable.place_holder_icon)
            .showStubImage(R.drawable.place_holder_icon)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.NONE_SAFE)
            .displayer(new SimpleBitmapDisplayer()).build();

    public static DisplayImageOptions options_gallery = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.place_holder_screen)
            .showImageOnFail(R.drawable.place_holder_screen)
            .showStubImage(R.drawable.place_holder_screen)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .displayer(new SimpleBitmapDisplayer()).build();

}