package vn.appsmobi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import vn.appsmobi.R;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.InstallChecker;
import vn.appsmobi.utils.SizeCalculator;
import vn.appsmobi.utils.UIUtils;
import vn.appsmobi.utils.Utils;

import static vn.appsmobi.utils.UIUtils.loadImageLoader;

/**
 * Created by tobrother on 28/12/2015.
 */
public class ViewHolderVerticalCard extends RecyclerView.ViewHolder {
    private ImageView ivImageCardSuggest, ivMenuCard3;
    private TextView tvNameCardSuggest, tvStatusCardSuggest, tvAuthorCardSuggest, tvShortDescriptionCardSuggest3;
    private RelativeLayout rlMenuCard3;
    private FullActionButton btActionVertical;
    private int imageSize;
    private DisplayImageOptions options;
    private CardItem cardItem;
    private Handler mHandler = new Handler();

    //private Button ivGetApp;
    public ViewHolderVerticalCard(View v, final Context context) {
        super(v);
        ivImageCardSuggest = (ImageView) v.findViewById(R.id.ivImageCardSuggest3);
        tvNameCardSuggest = (TextView) v.findViewById(R.id.tvNameCardSuggest3);
        tvAuthorCardSuggest = (TextView) v.findViewById(R.id.tvAuthorCardSuggest3);
        tvShortDescriptionCardSuggest3 = (TextView) v.findViewById(R.id.tvShortDescriptionCardSuggest3);
        btActionVertical = (FullActionButton) v.findViewById(R.id.btActionVertical);
        ivMenuCard3 = (ImageView) v.findViewById(R.id.ivMenuCard3);
        rlMenuCard3 = (RelativeLayout) v.findViewById(R.id.rlMenuCard3);
        // set Size for Views
        ivImageCardSuggest.setLayoutParams(SizeCalculator.getManager().lpIconImageVertical);
        //
        options = Constants.options;

    }

    public void setValue(CardItem cardItem, final Context context, DisplayImageOptions options) {
        this.options = options;
        DataCardItem dataCardItem = new DataCardItem();
        try {
            dataCardItem = DataCardItem.get(cardItem.getCard_data().getJSONObject(0), cardItem.getCard_data_type());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataCardItem.addUpdateListener(this.mUpdateListener);
        // set image url from json for this image --------------------------
        //setImageViewWithScale(getIvImageCardSuggest(), getImageSize(), getImageSize(), dataCardItem.getIcon_image());
        UIUtils.loadImageLoader(Constants.options, dataCardItem.getIcon_image(), getIvImageCardSuggest());
        //-----------------------------------------------------//
        tvShortDescriptionCardSuggest3.setText(dataCardItem.getShort_description());
        getTvNameCardSuggest().setText(dataCardItem.getName());
        getTvAuthorCardSuggest().setText(dataCardItem.getAuthor());
        getBtActionVertical().rebind(dataCardItem);
        getBtActionVertical().rbRatingCardSuggest.setScore(dataCardItem.getRate());

        // get id to download or add to favorite
        final String id = dataCardItem.getId();
        final DataCardItem appItem = dataCardItem;
        getRlMenuCard3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.menu_card, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(final MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.install:
                                if (!Utils.Network.isNetWorkConnected(context)) {
                                    Toast.makeText(context, context.getString(R.string.network_unavaliable), Toast.LENGTH_LONG).show();
                                } else {
                                    InstallChecker.checkAndInstall(appItem, (Activity) context);
                                }
                                break;
                            case R.id.add_favorite:
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public void setValue(final Context context, JSONObject jsonObject, int cardDataType) {
        DataCardItem dataCardItem = DataCardItem.get(jsonObject, cardDataType);
        dataCardItem.addUpdateListener(this.mUpdateListener);
        btActionVertical.rebind(dataCardItem);
        // set value
        // setImageViewWithScale(ivImageCardSuggest, SizeCalculator.getManager().imageSizeHalf3, SizeCalculator.getManager().imageSizeHalf3, dataCardItem.getIcon_image());
        // ImageLoader.getInstance().displayImage(dataCardItem.getIcon_image(), ivImageCardSuggest, options, new UIUtils.AnimateFirstDisplayListener());
        UIUtils.loadImageLoader(Constants.options, dataCardItem.getIcon_image(), ivImageCardSuggest);
        btActionVertical.rbRatingCardSuggest.setScore(dataCardItem.getRate());
        getTvNameCardSuggest().setText(dataCardItem.getName());
        getTvAuthorCardSuggest().setText(dataCardItem.getAuthor());
        tvShortDescriptionCardSuggest3.setText(dataCardItem.getShort_description());
        //
        final DataCardItem dataItem = dataCardItem;
        getRlMenuCard3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.menu_card, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.install:
                                if (!Utils.Network.isNetWorkConnected(context)) {
                                    Toast.makeText(context, context.getString(R.string.network_unavaliable), Toast.LENGTH_LONG).show();
                                } else {
                                    InstallChecker.checkAndInstall(dataItem, (Activity) context);
                                }
                                break;
                            case R.id.add_favorite:
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public void setValue(final Context context, DataCardItem vdataCardItem) {
        DataCardItem dataCardItem = vdataCardItem;
        dataCardItem.addUpdateListener(this.mUpdateListener);
        if (dataCardItem.getCard_status() == Constants.CARD_STATUS.STATUS_INSTALLED) {
            Drawable icon = null;
            try {
                icon = context.getPackageManager().getApplicationIcon(dataCardItem.getPackge_name());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            ivImageCardSuggest.setImageDrawable(icon);
            getTvAuthorCardSuggest().setText(dataCardItem.getPackge_name());
        } else {
            loadImageLoader(options, dataCardItem.getIcon_image(), ivImageCardSuggest);
            getTvAuthorCardSuggest().setText(dataCardItem.getAuthor());
        }
        btActionVertical.rebind(dataCardItem);
        btActionVertical.rbRatingCardSuggest.setVisibility(View.GONE);
        getTvNameCardSuggest().setText(dataCardItem.getName());

        tvShortDescriptionCardSuggest3.setText(dataCardItem.getShort_description());
        final DataCardItem dataItem = dataCardItem;
        getRlMenuCard3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.menu_card2, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.uninstall:
                                if (!Utils.Network.isNetWorkConnected(context)) {
                                    Toast.makeText(context, context.getString(R.string.network_unavaliable), Toast.LENGTH_LONG).show();
                                } else {
                                    InstallChecker.checkAndInstall(dataItem, (Activity) context);
                                }
                                break;
                            case R.id.add_favorite:
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public ImageView getIvImageCardSuggest() {
        return ivImageCardSuggest;
    }

    public void setIvImageCardSuggest(ImageView ivImageCardSuggest) {
        this.ivImageCardSuggest = ivImageCardSuggest;
    }

    public TextView getTvNameCardSuggest() {
        return tvNameCardSuggest;
    }

    public void setTvNameCardSuggest(TextView tvNameCardSuggest) {
        this.tvNameCardSuggest = tvNameCardSuggest;
    }

    public TextView getTvStatusCardSuggest() {
        return tvStatusCardSuggest;
    }

    public void setTvStatusCardSuggest(TextView tvStatusCardSuggest) {
        this.tvStatusCardSuggest = tvStatusCardSuggest;
    }

    public TextView getTvAuthorCardSuggest() {
        return tvAuthorCardSuggest;
    }

    public void setTvAuthorCardSuggest(TextView tvAuthorCardSuggest) {
        this.tvAuthorCardSuggest = tvAuthorCardSuggest;
    }

    /*
    public RatingBar getRbRatingCardSuggest() {
        return rbRatingCardSuggest;
    }

    public void setRbRatingCardSuggest(RatingBar rbRatingCardSuggest) {
        this.rbRatingCardSuggest = rbRatingCardSuggest;
    }
    */
    public RelativeLayout getRlMenuCard3() {
        return rlMenuCard3;
    }

    public void setRlMenuCard3(RelativeLayout rlMenuCard3) {
        this.rlMenuCard3 = rlMenuCard3;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public FullActionButton getBtActionVertical() {
        return btActionVertical;
    }

    public void setBtActionVertical(FullActionButton btActionVertical) {
        this.btActionVertical = btActionVertical;
    }

    private DataCardItem.AppInfoUpdateListener mUpdateListener = new DataCardItem.AppInfoUpdateListener() {
        public void onContentUpdate(final DataCardItem appinfo) {
            ViewHolderVerticalCard.this.mHandler.post(new Runnable() {
                public void run() {
                    //   setValue(cardItem,context,options);
                }
            });
        }

        public void onStatusUpdate(final DataCardItem appinfo) {
            ViewHolderVerticalCard.this.mHandler.post(new Runnable() {
                public void run() {
                    getBtActionVertical().rebind(appinfo);
                }
            });
        }
    };
}
