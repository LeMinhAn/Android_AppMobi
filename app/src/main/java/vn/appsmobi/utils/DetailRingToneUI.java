package vn.appsmobi.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import vn.appsmobi.R;
import vn.appsmobi.model.RingToneItem;

import static vn.appsmobi.utils.UIUtils.convertDpToPixel;
import static vn.appsmobi.utils.UIUtils.convertPixelToDp;
import static vn.appsmobi.utils.UIUtils.getScreenWidth;
import static vn.appsmobi.utils.UIUtils.setImageSize;

/**
 * Created by tobrother on 27/01/2016.
 */
//Kế thừa từ lớp cha DetailMediaActivityUI
public class DetailRingToneUI extends DetailMediaActivityUI {
    RingToneItem ringToneItem;
    ImageView ivPlayDetailImage;
    CircularProgressBar cpcPlayDetailImage;

    public DetailRingToneUI(Activity activity, DisplayImageOptions options, RingToneItem ringToneItem) {
        super(activity, options, ringToneItem);
        this.ringToneItem = ringToneItem;
    }

    @Override
    View getLayoutView() {
        LayoutInflater li = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        return li.inflate(R.layout.activity_test_image_detail, null);
    }

    @Override
    void setSizeCustomView() {
        float paddingSize = activity.getResources().getDimension(R.dimen.margin_small);
        int imageWidth = (int) ((getScreenWidth(activity) - (convertDpToPixel(paddingSize * 3, activity))));
        setImageSize(ivPlayDetailImage, imageWidth / 5);
        setImageSize(cpcPlayDetailImage, (int) (imageWidth / 4.5));
        int cal = (int) convertPixelToDp((float) (((imageWidth / 4.5) - (imageWidth / 5))), activity);
        cpcPlayDetailImage.setProgressBarWidth(cal);
        cpcPlayDetailImage.setBackgroundProgressBarWidth(cal);
    }

    @Override
    void customView() {
        ivPlayDetailImage = (ImageView) rootView.findViewById(R.id.ivPlayDetailImage);
        cpcPlayDetailImage = (CircularProgressBar) rootView.findViewById(R.id.cpcPlayDetailImage);
    }

    @Override
    void initCustomValue() {
        ivPlayDetailImage.setVisibility(View.VISIBLE);
        cpcPlayDetailImage.setVisibility(View.VISIBLE);
    }

    @Override
    void initCustomAction() {
        ivPlayDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
