package vn.appsmobi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import vn.appsmobi.R;
import vn.appsmobi.activity.ImageDetailActivity;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.SizeCalculator;

import static vn.appsmobi.utils.UIUtils.loadImageLoader;

/**
 * Created by tobrother on 15/03/2016.
 */
public class SlideImageView {
    private ShortActionButton btDownloadImage;
    private ImageView ivImageWallPaper;
    private int imageSize;
    private RelativeLayout rlWallPaperContainer;
    private DisplayImageOptions options;
    private DataCardItem mDataCardItem;
    private int cardDatatype;
    private Handler mHandler = new Handler();
    private View rootView;

    public SlideImageView(Context context) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        rootView = li.inflate(R.layout.item_card_image, null);
        ivImageWallPaper = (ImageView) rootView.findViewById(R.id.ivImageWallPaper);
        btDownloadImage = (ShortActionButton) rootView.findViewById(R.id.btDownloadImage);
        rlWallPaperContainer = (RelativeLayout) rootView.findViewById(R.id.rlWallPaperContainer);

        ivImageWallPaper.setLayoutParams(SizeCalculator.getManager().lpSizeSlideImage);
        btDownloadImage.setLayoutParams(SizeCalculator.getManager().lpSizeSlideImage);
        btDownloadImage.rlProcessAppManage.setLayoutParams(SizeCalculator.getManager().lpRLProcessSlideAppManage);

        btDownloadImage.rlSetting.setVisibility(View.GONE);
        btDownloadImage.rbRatingCardSuggest.setVisibility(View.GONE);
        btDownloadImage.tvItemStatusAppManage.setVisibility(View.GONE);
        btDownloadImage.tvNameCardSuggest2.setVisibility(View.GONE);
        options = Constants.options;

    }

    public View getRootView() {
        return rootView;
    }

    public void setValue(JSONObject cardItem, final Context context, int vcardDataType) throws JSONException {
        cardDatatype = vcardDataType;
        mDataCardItem = DataCardItem.get(cardItem, cardDatatype);
        this.mDataCardItem.addUpdateListener(this.mUpdateListener);
        btDownloadImage.rebind(mDataCardItem);
        // load image with scale size
        // setImageViewWithScale(getIvImageWallPaper(),getImageSize(),getImageSize(), item.getIcon_image(), options);
        loadImageLoader(options, mDataCardItem.getIcon_image(), getIvImageWallPaper());
        final String[] items = {"Download", "Xem trước", "Thêm vào yêu thích"};
        rlWallPaperContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                new MaterialDialog.Builder(context)
                        .title("VUI LÒNG LỰA CHỌN !")
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        if (!Utils.Network.isNetWorkConnected(context)) {
                                            Toast.makeText(context, context.getString(R.string.network_unavaliable), Toast.LENGTH_LONG).show();
                                        } else {
                                            if (mDataCardItem == null) {
                                                LogUtil.e("ViewHolderImage null", "true");
                                            }
                                            InstallChecker.checkAndInstall(mDataCardItem, (Activity) context);
                                        }
                                        break;
                                    case 1:
                                        Utils.EventClick.CardClick(context, mDataCardItem);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        })
                        .show();
                        */

                Intent intent = new Intent(context, ImageDetailActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("selected_image", mDataCardItem);
                intent.putExtras(b);

                // Setup the transition to the detail activity
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, ivImageWallPaper, "cover");
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    public ImageView getIvImageWallPaper() {
        return ivImageWallPaper;
    }

    public void setIvImageWallPaper(ImageView ivImageWallPaper) {
        this.ivImageWallPaper = ivImageWallPaper;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    private DataCardItem.AppInfoUpdateListener mUpdateListener = new DataCardItem.AppInfoUpdateListener() {
        public void onContentUpdate(final DataCardItem appinfo) {
        }

        public void onStatusUpdate(final DataCardItem appinfo) {
            SlideImageView.this.mHandler.post(new Runnable() {
                public void run() {
                    btDownloadImage.rebind(appinfo);
                }
            });
        }
    };
}
