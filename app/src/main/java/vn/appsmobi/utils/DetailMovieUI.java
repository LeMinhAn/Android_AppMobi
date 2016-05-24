package vn.appsmobi.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.adapter.ImageAdapter;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.model.MovieItem;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;

/**
 * Created by tobrother272 on 1/26/2016.
 */
public class DetailMovieUI extends DetailActivityUI {
    TextView tvSizeDetail, tvTimeDetail, tvCategoriesDetail;
    MovieItem movieItem;

    public DetailMovieUI(Activity activity, DisplayImageOptions options, MovieItem movieItem) {
        super(activity, options, movieItem);
        this.movieItem = movieItem;
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
    }

    @Override
    void initCustomValue() {
        tvSizeDetail.setVisibility(View.VISIBLE);
        tvTimeDetail.setVisibility(View.VISIBLE);
        tvCategoriesDetail.setVisibility(View.VISIBLE);
        //tvSizeDetail.setText(Float.toString(movieItem.getSize())+" MB");
        //tvTimeDetail.setText(Float.toString(movieItem.getTime())+" Phút");
    }


    @Override
    void updateCustomView(DataCardItem dataCardItem) {
        movieItem = (MovieItem) dataCardItem;
        imageAdapter = new ImageAdapter(activity, movieItem.getSlide_show(), options);
        gSlideShow.setAdapter(imageAdapter);
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
}
