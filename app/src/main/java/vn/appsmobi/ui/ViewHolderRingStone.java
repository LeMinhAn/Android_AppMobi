package vn.appsmobi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.InstallChecker;
import vn.appsmobi.utils.SizeCalculator;
import vn.appsmobi.utils.Utils;

import static vn.appsmobi.utils.UIUtils.loadImageLoader;

/**
 * Created by tobrother on 08/01/2016.
 */


public class ViewHolderRingStone extends RecyclerView.ViewHolder {
    private ImageView ivThumbRingStone, ivPlayRingStone;
    private FrameLayout flGetRingStone;
    private TextView tvNameOfRingTone;
    private ShortActionButton btActionRingTone;
    private CircularProgressBar cpcPlayRingStone;
    private int myImageSize;
    private DataCardItem mDataCardItem;
    private Handler mHandler = new Handler();
    private JSONObject jsonObject;
    private int cardDataType;
    private DisplayImageOptions options;
    private Context context;
    // size calculator
    int paddingSize;
    int pbWidth;
    LinearLayout.LayoutParams lpIconImage;
    RelativeLayout.LayoutParams lpActionButton, lpLayOutProgress, lpProgress, lpSetting, lpCPCPlayRingToneHalf3, lpIVPlayRingToneHalf3, lpTVName;

    public ViewHolderRingStone(View itemView) {
        super(itemView);
        ivThumbRingStone = (ImageView) itemView.findViewById(R.id.ivThumbRingStone);
        ivPlayRingStone = (ImageView) itemView.findViewById(R.id.ivPlayRingStone);
        flGetRingStone = (FrameLayout) itemView.findViewById(R.id.flGetRingStone);
        btActionRingTone = (ShortActionButton) itemView.findViewById(R.id.btActionRingTone);
        cpcPlayRingStone = (CircularProgressBar) itemView.findViewById(R.id.cpcPlayRingStone);
        tvNameOfRingTone = (TextView) itemView.findViewById(R.id.tvNameOfRingTone);
        paddingSize = SizeCalculator.getManager().paddingSize;
        // get calculator size
        // get calculator size

        pbWidth = SizeCalculator.getManager().pbWidth;
        lpIconImage = SizeCalculator.getManager().lpIconImage;
        lpActionButton = SizeCalculator.getManager().lpActionButton;
        lpLayOutProgress = SizeCalculator.getManager().lpLayOutProgress;
        lpProgress = SizeCalculator.getManager().lpProgress;
        lpSetting = SizeCalculator.getManager().lpLayOutSetting;
        lpCPCPlayRingToneHalf3 = SizeCalculator.getManager().lpCPCPlayRingTone;
        lpIVPlayRingToneHalf3 = SizeCalculator.getManager().lpIVPlayRingTone;
        lpTVName = SizeCalculator.getManager().lpTVName;

    }

    public void getSizeHalf() {
        pbWidth = SizeCalculator.getManager().pbWidthHalf3;
        lpIconImage = SizeCalculator.getManager().lpIconImageHalf3;
        lpActionButton = SizeCalculator.getManager().lpActionButtonHalf3;
        lpLayOutProgress = SizeCalculator.getManager().lpLayOutProgressHalf3;
        lpProgress = SizeCalculator.getManager().lpProgressHalf3;
        lpSetting = SizeCalculator.getManager().lpLayOutSettingHalf3;
        lpCPCPlayRingToneHalf3 = SizeCalculator.getManager().lpCPCPlayRingToneHalf3;
        lpIVPlayRingToneHalf3 = SizeCalculator.getManager().lpIVPlayRingToneHalf3;
        lpTVName = SizeCalculator.getManager().lpTVNameHalf3;

    }

    public void getSizeFull() {
        pbWidth = SizeCalculator.getManager().pbWidth;
        lpIconImage = SizeCalculator.getManager().lpIconImage;
        lpActionButton = SizeCalculator.getManager().lpActionButton;
        lpLayOutProgress = SizeCalculator.getManager().lpLayOutProgress;
        lpProgress = SizeCalculator.getManager().lpProgress;
        lpSetting = SizeCalculator.getManager().lpLayOutSetting;
        lpCPCPlayRingToneHalf3 = SizeCalculator.getManager().lpCPCPlayRingTone;
        lpIVPlayRingToneHalf3 = SizeCalculator.getManager().lpIVPlayRingTone;
        lpTVName = SizeCalculator.getManager().lpTVName;
    }

    public void setSize() {
        ivThumbRingStone.setLayoutParams(lpIconImage);
        ivPlayRingStone.setLayoutParams(lpIVPlayRingToneHalf3);
        cpcPlayRingStone.setLayoutParams(lpCPCPlayRingToneHalf3);
        cpcPlayRingStone.setProgressBarWidth(pbWidth);
        cpcPlayRingStone.setBackgroundProgressBarWidth(pbWidth);
        // calculate width and height for image
        btActionRingTone.setLayoutParams(lpActionButton);
        //
        btActionRingTone.rlProcessAppManage.setLayoutParams(lpLayOutProgress);
        btActionRingTone.rlProcessAppManage.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        //
        btActionRingTone.cpcProcessAppManage.setLayoutParams(lpProgress);
        btActionRingTone.cpcProcessAppManage.setProgressBarWidth(pbWidth);
        btActionRingTone.cpcProcessAppManage.setBackgroundProgressBarWidth(pbWidth);
        //

        btActionRingTone.rlSetting.setLayoutParams(lpSetting);
        btActionRingTone.rlSetting.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        btActionRingTone.tvItemStatusAppManage.setVisibility(View.GONE);
        btActionRingTone.tvNameCardSuggest2.setVisibility(View.GONE);
        tvNameOfRingTone.setLayoutParams(lpTVName);
        //  http://stream2.r17s101.vcdn.vn/fsfsdfdsfdserwrwq3/6056f80edda7cd1b3b02f63dcbd69a2b/56970f8c/2013/01/28/5/6/56552a07660d3447211b25d42a82e1dd.mp3
    }


    public void setValue(final JSONObject vjsonObject, DisplayImageOptions vOptions, Context vContext, int vcardDataType) {
        options = vOptions;
        context = vContext;
        jsonObject = vjsonObject;
        cardDataType = vcardDataType;
        mDataCardItem = DataCardItem.get(jsonObject, cardDataType);
        btActionRingTone.rbRatingCardSuggest.setScore(mDataCardItem.getRate());
        this.mDataCardItem.addUpdateListener(this.mUpdateListener);
        tvNameOfRingTone.setText(mDataCardItem.getName());
        //   setImageViewWithScale(getIvThumbRingStone(), getMyImageSize(), getMyImageSize(), item.getIcon_image(), options);// set image with scale
        loadImageLoader(options, mDataCardItem.getIcon_image(), getIvThumbRingStone());
        getIvPlayRingStone().setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_circle));
        btActionRingTone.rebind(mDataCardItem);
        flGetRingStone.setOnClickListener(new View.OnClickListener() {
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
                                    InstallChecker.checkAndInstall(mDataCardItem, (Activity) context);
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

    public ImageView getIvThumbRingStone() {
        return ivThumbRingStone;
    }

    public void setIvThumbRingStone(ImageView ivThumbRingStone) {
        this.ivThumbRingStone = ivThumbRingStone;
    }

    public ImageView getIvPlayRingStone() {
        return ivPlayRingStone;
    }

    public void setIvPlayRingStone(ImageView ivPlayRingStone) {
        this.ivPlayRingStone = ivPlayRingStone;
    }


    public ShortActionButton getBTActionRingTone() {
        return btActionRingTone;
    }

    public void setBTActionRingTone(ShortActionButton btActionRingTone) {
        this.btActionRingTone = btActionRingTone;
    }


    public TextView getTvNameRingStone() {
        return tvNameOfRingTone;
    }

    public void setTvNameRingStone(TextView tvNameRingStone) {
        this.tvNameOfRingTone = tvNameRingStone;
    }

    public CircularProgressBar getCpcPlayRingStone() {
        return cpcPlayRingStone;
    }

    public void setCpcPlayRingStone(CircularProgressBar cpcPlayRingStone) {
        this.cpcPlayRingStone = cpcPlayRingStone;
    }

    public FrameLayout getFlGetRingStone() {
        return flGetRingStone;
    }

    public void setFlGetRingStone(FrameLayout flGetRingStone) {
        this.flGetRingStone = flGetRingStone;
    }

    public int getMyImageSize() {
        return myImageSize;
    }

    public void setMyImageSize(int myImageSize) {
        this.myImageSize = myImageSize;
    }

    private DataCardItem.AppInfoUpdateListener mUpdateListener = new DataCardItem.AppInfoUpdateListener() {
        public void onContentUpdate(final DataCardItem appinfo) {
            ViewHolderRingStone.this.mHandler.post(new Runnable() {
                public void run() {
                    setValue(jsonObject, options, context, cardDataType);
                }
            });
        }

        public void onStatusUpdate(final DataCardItem appinfo) {
            ViewHolderRingStone.this.mHandler.post(new Runnable() {
                public void run() {
                    getBTActionRingTone().rebind(appinfo);
                }
            });
        }
    };
}
