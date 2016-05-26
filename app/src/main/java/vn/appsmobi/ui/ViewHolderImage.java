package vn.appsmobi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.InstallChecker;
import vn.appsmobi.utils.LogUtil;
import vn.appsmobi.utils.UIUtils;
import vn.appsmobi.utils.Utils;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;
import static vn.appsmobi.utils.UIUtils.setViewSize;

/**
 * Created by tobrother on 08/01/2016.
 */
public class ViewHolderImage extends RecyclerView.ViewHolder {
    private ShortActionButton btDownloadImage;
    private ImageView ivImageWallPaper;
    private int imageSize;
    private RelativeLayout rlWallPaperContainer;
    private JSONObject jsonObject;
    private DisplayImageOptions options;
    private Context context;
    private DataCardItem mDataCardItem;

    private int cardDatatype;
    private Handler mHandler = new Handler();

    public ViewHolderImage(View itemView, Context vcontext, int scaleImage) {
        super(itemView);
        context = vcontext;
        ivImageWallPaper = (ImageView) itemView.findViewById(R.id.ivImageWallPaper);
        btDownloadImage = (ShortActionButton) itemView.findViewById(R.id.btDownloadImage);
        rlWallPaperContainer = (RelativeLayout) itemView.findViewById(R.id.rlWallPaperContainer);
        int sizeImage = getScreenWidth(context) / scaleImage;
        setImageSize(sizeImage);
        setViewSize(sizeImage, sizeImage, ivImageWallPaper);

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(sizeImage, sizeImage);
        btDownloadImage.setLayoutParams(layoutParams2);
        btDownloadImage.setPadding(0, 0, 0, 0);

        RelativeLayout.LayoutParams layoutParams11 = new RelativeLayout.LayoutParams(sizeImage, sizeImage);
        layoutParams11.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        btDownloadImage.rlProcessAppManage.setLayoutParams(layoutParams11);

        btDownloadImage.rlSetting.setVisibility(View.GONE);
        btDownloadImage.rbRatingCardSuggest.setVisibility(View.GONE);
        btDownloadImage.tvItemStatusAppManage.setVisibility(View.GONE);
        btDownloadImage.tvNameCardSuggest2.setVisibility(View.GONE);
    }

    public void setValue(JSONObject cardItem, DisplayImageOptions voptions, Context vcontext, int vcardDataType) throws JSONException {
        context = vcontext;
        jsonObject = cardItem;
        options = voptions;
        cardDatatype = vcardDataType;
        mDataCardItem = DataCardItem.get(cardItem, cardDatatype);
        this.mDataCardItem.addUpdateListener(this.mUpdateListener);
        btDownloadImage.rebind(mDataCardItem);
        // load image with scale size
        // setImageViewWithScale(getIvImageWallPaper(),getImageSize(),getImageSize(), item.getIcon_image(), options);
        //loadImageLoader(options, mDataCardItem.getIcon_image(), getIvImageWallPaper());

        //Khi click vào hình ảnh trong wallpaper
        UIUtils.loadImageLoader(Constants.options, mDataCardItem.getIcon_image(), getIvImageWallPaper());
        final String[] items = {"Download", "Xem trước", "Thêm vào yêu thích"};
        rlWallPaperContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        })
                        .show();
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
            ViewHolderImage.this.mHandler.post(new Runnable() {
                public void run() {
                    btDownloadImage.rebind(appinfo);
                }
            });
        }
    };
}
