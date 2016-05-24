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
import vn.appsmobi.model.BookItem;
import vn.appsmobi.model.DataCardItem;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;

/**
 * Created by tobrother272 on 1/26/2016.
 */
public class DetailBookUI extends DetailActivityUI {
    BookItem bookItem;
    TextView tvFileTypeDetail, tvSizeDetail;

    public DetailBookUI(Activity activity, DisplayImageOptions options, BookItem bookItem) {
        super(activity, options, bookItem);
        this.bookItem = bookItem;
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
        tvFileTypeDetail = (TextView) rootView.findViewById(R.id.tvFileTypeDetail);
        tvSizeDetail = (TextView) rootView.findViewById(R.id.tvSizeDetail);
    }

    @Override
    void initCustomValue() {
        ivPromoImage.setVisibility(View.INVISIBLE);
        tvFileTypeDetail.setVisibility(View.VISIBLE);
        tvSizeDetail.setVisibility(View.VISIBLE);

    }

    @Override
    void updateCustomView(DataCardItem dataCardItem) {
        bookItem = (BookItem) dataCardItem;
        imageAdapter = new ImageAdapter(activity, bookItem.getSlide_show(), options);
        gSlideShow.setAdapter(imageAdapter);
    }

    @Override
    ArrayList<String> getSlideImage(DataCardItem dataCardItem) {
        return null;
    }
    //Set kích thước cho image trong detail book
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
