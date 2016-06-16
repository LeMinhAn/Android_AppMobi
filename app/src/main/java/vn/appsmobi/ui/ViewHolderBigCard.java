package vn.appsmobi.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;

import vn.appsmobi.R;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.UIUtils;
import vn.appsmobi.utils.Utils;


/**
 * Created by tobrother on 28/12/2015.
 */

//Giao diện hiện ở ngoài trang chủ sau gridview
public class ViewHolderBigCard extends RecyclerView.ViewHolder {
    private ImageView ivImageCardSuggest;
    private TextView tvNameCardSuggest, tvStatusCardSuggest, tvAuthorCardSuggest;
    private LinearLayout llContainer;
    private FullActionButton btActionVerticalBig;
    private DataCardItem myDataCardItem;
    private CardItem myCardItem;
    private Handler mHandler = new Handler();
    private Context context;
    private DisplayImageOptions options;

    public ViewHolderBigCard(View v) {
        super(v);

        ivImageCardSuggest = (ImageView) v.findViewById(R.id.ivImageCardSuggest);
        tvNameCardSuggest = (TextView) v.findViewById(R.id.tvNameCardSuggest);
        llContainer = (LinearLayout) v.findViewById(R.id.llContainer);
        tvStatusCardSuggest = (TextView) v.findViewById(R.id.tvStatusCardSuggest);
        tvAuthorCardSuggest = (TextView) v.findViewById(R.id.tvAuthorCardSuggest);
        btActionVerticalBig = (FullActionButton) v.findViewById(R.id.btActionVerticalBig);
    }

    public void setValue(final CardItem data, final Context context, DisplayImageOptions options) {
        myCardItem = data;
        this.context = context;
        this.options = options;
        try {
            myDataCardItem = DataCardItem.get(data.getCard_data().getJSONObject(0), data.getCard_data_type());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // set this big image is the first image of slide_image
        this.myDataCardItem.addUpdateListener(this.mUpdateListener);
        //ImageLoader.getInstance().displayImage(myDataCardItem.getPromo_image(), getIvImageCardSuggest(), options);
        //Lấy hình ảnh của Big_Card
        UIUtils.loadImageLoader(Constants.options, myDataCardItem.getIcon_image(), getIvImageCardSuggest());
        btActionVerticalBig.rebind(myDataCardItem);
        //Lấy rate của Big_Card
        btActionVerticalBig.rbRatingCardSuggest.setScore(myDataCardItem.getRate());
        //Lấy name của Big_Card
        getTvNameCardSuggest().setText(myDataCardItem.getName());
        //Lấy author của Big_Card
        getTvAuthorCardSuggest().setText(myDataCardItem.getAuthor());

        //Sự kiện khi click vào linnear chứa imageview
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.EventClick.CardClick(context, myDataCardItem);
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

    private DataCardItem.AppInfoUpdateListener mUpdateListener = new DataCardItem.AppInfoUpdateListener() {
        public void onContentUpdate(final DataCardItem appinfo) {
            ViewHolderBigCard.this.mHandler.post(new Runnable() {
                public void run() {
                    setValue(myCardItem, context, options);
                }
            });
        }

        public void onStatusUpdate(final DataCardItem appinfo) {
            ViewHolderBigCard.this.mHandler.post(new Runnable() {
                public void run() {
                    //Toast.makeText(ListAppListItem.this._context, "onStatusUpdate:" + appinfo.getName() , Toast.LENGTH_LONG).show();
                    btActionVerticalBig.rebind(appinfo);
                }
            });
        }
    };
}
