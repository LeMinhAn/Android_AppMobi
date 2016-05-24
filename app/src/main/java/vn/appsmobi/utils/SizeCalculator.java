package vn.appsmobi.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import vn.appsmobi.R;

import static vn.appsmobi.utils.UIUtils.convertDpToPixel;
import static vn.appsmobi.utils.UIUtils.convertPixelToDp;
import static vn.appsmobi.utils.UIUtils.getScreenWidth;

/**
 * Created by tobrother on 14/03/2016.
 */
public class SizeCalculator {
    public static SizeCalculator mSizeCalculator;
    public int widthScreen;

    public int imageSizeHalf3;
    public int paddingSize;
    public int pbWidthHalf3;
    int heightActionButtonHalf3;
    int widthActionButtonHalf3;
    //
    public LinearLayout.LayoutParams lpIconImageHalf3;
    public RelativeLayout.LayoutParams lpActionButtonHalf3;
    public RelativeLayout.LayoutParams lpLayOutProgressHalf3;
    public RelativeLayout.LayoutParams lpProgressHalf3;
    public RelativeLayout.LayoutParams lpLayOutSettingHalf3;
    public RelativeLayout.LayoutParams lpCPCPlayRingToneHalf3;
    public RelativeLayout.LayoutParams lpIVPlayRingToneHalf3;
    public RelativeLayout.LayoutParams lpTVName;
    ///
    public int imageSize;
    public int pbWidth;
    int heightActionButton;
    int widthActionButton;
    float scaleSize = 3;
    int slideImageSize;
    //
    public LinearLayout.LayoutParams lpIconImage;
    public RelativeLayout.LayoutParams lpActionButton;
    public RelativeLayout.LayoutParams lpLayOutProgress;
    public RelativeLayout.LayoutParams lpProgress;
    public RelativeLayout.LayoutParams lpLayOutSetting;
    public RelativeLayout.LayoutParams lpCPCPlayRingTone;
    public RelativeLayout.LayoutParams lpIVPlayRingTone;
    public RelativeLayout.LayoutParams lpTVNameHalf3;


    // Layout for vertical card
    public LinearLayout.LayoutParams lpIconImageVertical;


    // layout for slideImage
    public RelativeLayout.LayoutParams lpButtonSlideDownload;
    public RelativeLayout.LayoutParams lpSizeSlideImage;
    public RelativeLayout.LayoutParams lpRLProcessSlideAppManage;


    public int btItemHorizontalHalf3;
    // scaleImage
    private Double scaleSizeHalf = 2.5;
    // menu icon size
    public LinearLayout.LayoutParams lpIconMenu;


    public static void init(Context context) {
        if (mSizeCalculator == null)
            mSizeCalculator = new SizeCalculator(context);
    }

    public SizeCalculator(Context context) {
        getDimen(context);
        calSize(context);
        calForItemHorizontalItemHalf();
        calForItemHorizontalRingToneHalf();
        calForItemHorizontalItem();
        calForItemHorizontalRingTone();
        calForItemVertical();
        carForSlideImage();
        carForMenuIcon();
    }

    public static SizeCalculator getManager() {
        return mSizeCalculator;
    }

    public void getDimen(Context context) {
        paddingSize = (int) context.getResources().getDimension(R.dimen.margin_small);
        widthScreen = getScreenWidth(context);
    }

    public void calSize(Context context) {
        imageSizeHalf3 = (int) ((getScreenWidth(context) - (convertDpToPixel(paddingSize * 5, context))) / scaleSizeHalf);
        imageSize = (int) ((getScreenWidth(context) - (convertDpToPixel(paddingSize * 4, context))) / scaleSize);
        //
        widthActionButtonHalf3 = imageSizeHalf3 + (paddingSize * 4);
        heightActionButtonHalf3 = widthActionButtonHalf3 + ((int) (getScreenWidth(context) / scaleSizeHalf) / 2);
        widthActionButton = imageSize + (paddingSize * 4);
        heightActionButton = widthActionButton + ((int) (getScreenWidth(context) / scaleSize) / 3);

        //
        pbWidthHalf3 = (int) convertPixelToDp((float) (((imageSizeHalf3 / 2.5) - (imageSizeHalf3 / 3))), context);
        pbWidth = (int) convertPixelToDp((float) (((imageSize / 2.5) - (imageSize / 3))), context);
        //
        slideImageSize = (getScreenWidth(context) / 3);
    }

    public void calForItemHorizontalItemHalf() {
        lpIconImageHalf3 = new LinearLayout.LayoutParams(imageSizeHalf3, imageSizeHalf3);
        lpIconImageHalf3.setMargins(paddingSize, paddingSize, paddingSize, paddingSize);
        lpTVNameHalf3 = new RelativeLayout.LayoutParams(imageSizeHalf3, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //
        lpActionButtonHalf3 = new RelativeLayout.LayoutParams(widthActionButtonHalf3, heightActionButtonHalf3);
        //
        lpLayOutProgressHalf3 = new RelativeLayout.LayoutParams(imageSizeHalf3, imageSizeHalf3);
        lpLayOutProgressHalf3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lpLayOutProgressHalf3.setMargins(paddingSize * 2, paddingSize * 2, paddingSize, paddingSize);
        //
        lpProgressHalf3 = new RelativeLayout.LayoutParams((int) (imageSizeHalf3 / 3)
                , (int) (imageSizeHalf3 / 3));
        lpProgressHalf3.addRule(RelativeLayout.CENTER_IN_PARENT);
        //
        lpLayOutSettingHalf3 = new RelativeLayout.LayoutParams(widthActionButtonHalf3
                , (int) (widthScreen / scaleSizeHalf) / 2);
        lpLayOutSettingHalf3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    public void calForItemHorizontalRingToneHalf() {
        lpCPCPlayRingToneHalf3 = new RelativeLayout.LayoutParams((int) (imageSizeHalf3 / 2.5)
                , (int) (imageSizeHalf3 / 2.5
        ));
        lpCPCPlayRingToneHalf3.addRule(RelativeLayout.CENTER_IN_PARENT);
        lpIVPlayRingToneHalf3 = new RelativeLayout.LayoutParams((int) (imageSizeHalf3 / 3)
                , (int) (imageSizeHalf3 / 3));
        lpIVPlayRingToneHalf3.addRule(RelativeLayout.CENTER_IN_PARENT);
    }


    public void calForItemHorizontalItem() {
        lpIconImage = new LinearLayout.LayoutParams(imageSize, imageSize);
        lpTVName = new RelativeLayout.LayoutParams(imageSize, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //
        lpActionButton = new RelativeLayout.LayoutParams(widthActionButton, heightActionButton);
        //
        lpLayOutProgress = new RelativeLayout.LayoutParams(imageSize, imageSize);
        lpLayOutProgress.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lpLayOutProgress.setMargins(paddingSize, paddingSize, paddingSize, paddingSize);
        //
        lpProgress = new RelativeLayout.LayoutParams((int) (imageSize / 3)
                , (int) (imageSize / 3));
        lpProgress.addRule(RelativeLayout.CENTER_IN_PARENT);
        //
        lpLayOutSetting = new RelativeLayout.LayoutParams(widthActionButton
                , (int) (widthScreen / scaleSize) / 2);
        lpLayOutSetting.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    public void calForItemHorizontalRingTone() {
        lpCPCPlayRingTone = new RelativeLayout.LayoutParams((int) (imageSize / 2.5)
                , (int) (imageSize / 2.5
        ));
        lpCPCPlayRingTone.addRule(RelativeLayout.CENTER_IN_PARENT);
        lpIVPlayRingTone = new RelativeLayout.LayoutParams((int) (imageSize / 3)
                , (int) (imageSize / 3));
        lpIVPlayRingTone.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    public void calForItemVertical() {
        lpIconImageVertical = new LinearLayout.LayoutParams((int) (widthScreen / 4), (int) (widthScreen / 4));
    }

    public void carForSlideImage() {
        lpButtonSlideDownload = new RelativeLayout.LayoutParams(slideImageSize, slideImageSize);
        lpSizeSlideImage = new RelativeLayout.LayoutParams(slideImageSize, slideImageSize);
        lpRLProcessSlideAppManage = new RelativeLayout.LayoutParams(slideImageSize, slideImageSize);
        lpRLProcessSlideAppManage.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        ;
    }

    public void carForMenuIcon() {
        lpIconMenu = new LinearLayout.LayoutParams(widthScreen / 5, widthScreen / 5);
    }


}
