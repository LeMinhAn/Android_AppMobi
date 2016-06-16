package vn.appsmobi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.UIUtils;

/**
 * Created by tobrother on 04/01/2016.
 */
//Set adapter cho từng image cho slideImage
public class ImageAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> imageArray;
    DisplayImageOptions options;

    public ImageAdapter(Context context, ArrayList<String> imageArray, DisplayImageOptions options) {
        this.context = context;
        this.imageArray = imageArray;
        this.options = options;
    }

    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public Object getItem(int position) {
        return imageArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        UIUtils.loadImageLoader(Constants.options, imageArray.get(position), imageView);
        //ImageLoader.getInstance().displayImage(imageArray.get(position),imageView, options);
        return imageView;
    }
}
