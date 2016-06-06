package vn.appsmobi.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.HListView;
import vn.appsmobi.R;
import vn.appsmobi.activity.AppDetailActivity;
import vn.appsmobi.activity.ReadMoreActivity;
import vn.appsmobi.activity.SeeMoreActivity;
import vn.appsmobi.adapter.AdapterRowHorizontalCardSuggest;
import vn.appsmobi.adapter.CommentAdapter;
import vn.appsmobi.adapter.ImageAdapter;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.ui.CustomRatingBar;
import vn.appsmobi.ui.CustomRatingBarGuest;
import vn.appsmobi.ui.RoundedImageView;
import vn.appsmobi.ui.StatusDetailButton;

import static vn.appsmobi.utils.UIUtils.calculateActionBarSize;
import static vn.appsmobi.utils.UIUtils.getScreenHeight;
import static vn.appsmobi.utils.UIUtils.getScreenWidth;
import static vn.appsmobi.utils.UIUtils.setHListViewHeightBasedOnChildren;
import static vn.appsmobi.utils.UIUtils.setListViewHeightBasedOnChildren;
import static vn.appsmobi.utils.UIUtils.setRatingSize;

/**
 * Created by tobrother on 26/01/2016.
 */
//Giao diện chi tiết của app và game, film, book
public abstract class DetailActivityUI implements ObservableScrollViewCallbacks {
    LinearLayout llCountDown, llRate, llTopic, llCountDownParent, llRateParent, llTopicParent, llShare, llStick, llCommentParent;
    ImageView ivPromoImage, ivIcon, ivPromoVideoPlay;
    RoundedImageView ivAvatarUser;
    Gallery gSlideShow;
    CustomRatingBarGuest rbRate;
    CustomRatingBar rbDetail;
    TextView tvNameItem, tvAuthorItem, tvNameUser, tvCountDown, tvTopic, tvRate, tvShortDescription, tvReadMoreDescription;
    Button btSubmitComment, btReadMoreSuggest, btReadMoreComment;
    public StatusDetailButton btStatusDetailButton;
    ListView rvComments;
    HListView rvAppSuggest;
    View rootView;
    Activity activity;
    DataCardItem dataCardItem;
    // value
    DisplayImageOptions options;
    boolean Logged = false;
    ArrayList<String> slideShow;
    ArrayList<DataCardItem> appSuggest;
    int card_data_type;
    ImageAdapter imageAdapter;
    CommentAdapter commentAdapter;
    AdapterRowHorizontalCardSuggest suggestAppAdapter;
    StatusDetailButton.BidingData mBidingData;
    private Handler mHandler = new Handler();
    ObservableScrollView svParentAppDetailActivity;
//    ExpandableTextView expTv1;

    public DetailActivityUI(Activity activity, DisplayImageOptions options, DataCardItem dataCardItem) {
        this.activity = activity;
        this.dataCardItem = dataCardItem;
        this.options = options;
        initView();
        activity.setContentView(rootView);
        initValue();
        initAction();

    }

    public void initView() {
        rootView = getLayoutView();

//        expTv1 = (ExpandableTextView) rootView.findViewById(R.id.expand_text_view);
//        expTv1.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
//            @Override
//            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
//            }
//        });
//        expTv1.setText(dataCardItem.getShort_description());

        // LinearLayout
        llCountDown = (LinearLayout) rootView.findViewById(R.id.llCountDown);
        llCountDownParent = (LinearLayout) rootView.findViewById(R.id.llCountDownParent);
        llRate = (LinearLayout) rootView.findViewById(R.id.llRate);
        llRateParent = (LinearLayout) rootView.findViewById(R.id.llRateParent);
        llTopic = (LinearLayout) rootView.findViewById(R.id.llTopic);
        llTopicParent = (LinearLayout) rootView.findViewById(R.id.llTopicParent);
        llShare = (LinearLayout) rootView.findViewById(R.id.llShare);
        llStick = (LinearLayout) rootView.findViewById(R.id.llStick);
        llCommentParent = (LinearLayout) rootView.findViewById(R.id.llCommentParent);
        // ImageView
        ivPromoImage = (ImageView) rootView.findViewById(R.id.ivPromoImage);
        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        ivPromoVideoPlay = (ImageView) rootView.findViewById(R.id.ivPromoVideoPlay);
        ivAvatarUser = (RoundedImageView) rootView.findViewById(R.id.ivAvatarUser);
        // Gallery
        gSlideShow = (Gallery) rootView.findViewById(R.id.gSlideShow);
        //Rating Bar
        rbRate = (CustomRatingBarGuest) rootView.findViewById(R.id.rbRate);
        setRatingSize(rbRate, activity, (float) 1.5);
        // waiting rating bar count rate
        // TextView
        tvNameItem = (TextView) rootView.findViewById(R.id.tvNameItem);
        tvAuthorItem = (TextView) rootView.findViewById(R.id.tvAuthorItem);
        tvShortDescription = (TextView) rootView.findViewById(R.id.tvShortDescription);
        tvReadMoreDescription = (TextView) rootView.findViewById(R.id.tvReadMoreDescription);
        tvNameUser = (TextView) rootView.findViewById(R.id.tvNameUser);
        tvCountDown = (TextView) rootView.findViewById(R.id.tvCountDown);
        tvTopic = (TextView) rootView.findViewById(R.id.tvTopic);
        tvRate = (TextView) rootView.findViewById(R.id.tvRate);
        // Button
        btStatusDetailButton = (StatusDetailButton) rootView.findViewById(R.id.btStatusDetailButton);
        btSubmitComment = (Button) rootView.findViewById(R.id.btSubmitComment);
        btReadMoreSuggest = (Button) rootView.findViewById(R.id.btReadMoreSuggest);
        btReadMoreComment = (Button) rootView.findViewById(R.id.btReadMoreComment);
        // ListView
        rvComments = (ListView) rootView.findViewById(R.id.lvComment);
        rvAppSuggest = (HListView) rootView.findViewById(R.id.hlvAppSuggest);
        // rating bar
        rbDetail = (CustomRatingBar) rootView.findViewById(R.id.rbDetail);
        svParentAppDetailActivity = (ObservableScrollView) rootView.findViewById(R.id.svParentAppDetailActivity);
        svParentAppDetailActivity.setScrollViewCallbacks(this);
        // custom view
        customView();
        setSizeView();
    }

    public void initAction() {
        btSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btReadMoreSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadMoreActivity.class);
                intent.putExtra(Constants.Intent.READ_MORE_TYPE, 1);
                intent.putExtra(Constants.Intent.CARD, dataCardItem.getId());
                activity.startActivity(intent);
            }
        });
        btReadMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadMoreActivity.class);
                intent.putExtra(Constants.Intent.READ_MORE_TYPE, 1);
                intent.putExtra(Constants.Intent.CARD, dataCardItem.getId());
                activity.startActivity(intent);
            }
        });
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvReadMoreDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SeeMoreActivity.class);
                //LogUtil.e("content",dataCardItem.getShort_description());
                intent.putExtra(Constants.Intent.CARD_CONTENT, dataCardItem.getShort_description());
                intent.putExtra(Constants.Intent.CARD_NAME, dataCardItem.getName());
                activity.startActivity(intent);
            }
        });
        rbRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rvAppSuggest.setOnItemClickListener(new HListView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int mposition, long id) {
                Intent intent = new Intent(activity, AppDetailActivity.class);
                Bundle bundle = new Bundle();
                //Truyền giá trị sang AppDetailActivity
                bundle.putParcelable(Constants.Intent.CARD, appSuggest.get(mposition));
                //Truyền giá trị sang AppDetailActivity
                bundle.putInt(Constants.Intent.CARD_DATA_TYPE, card_data_type);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        /**
         * play video by call youtube application
         */
        ivPromoVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + dataCardItem.getPromo_video().split("/?v=")[1]));
                activity.startActivity(intent);
            }
        });
    }

    public void setBidingData(StatusDetailButton.BidingData _mBidingData) {
        this.mBidingData = _mBidingData;
    }

    // abstract function
    abstract View getLayoutView();

    abstract void setSizeCustomView(int sizeCustomView);

    abstract void customView();

    abstract void initCustomValue();

    abstract void updateCustomView(DataCardItem dataCardItem);

    abstract ArrayList<String> getSlideImage(DataCardItem dataCardItem);

    abstract LinearLayout.LayoutParams getLayoutParamsIcon();

    abstract public void updateContent();

    abstract public void updateStatus();

    // cal and set size for all view
    public void setSizeView() {
        // height for image app , comment box , gallery
        int heightPromoImage = (getScreenHeight(activity) - calculateActionBarSize(activity)) / 3;
        ivPromoImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPromoImage));
        // size for icon analyze of this item
        int sizeAnalytic = getScreenWidth(activity) / 6;
        llCountDown.setLayoutParams(new LinearLayout.LayoutParams(sizeAnalytic, sizeAnalytic));
        llRate.setLayoutParams(new LinearLayout.LayoutParams(sizeAnalytic, sizeAnalytic));
        llTopic.setLayoutParams(new LinearLayout.LayoutParams(sizeAnalytic, sizeAnalytic));
        setSizeCustomView(sizeAnalytic);
        // icon app
        ivIcon.setLayoutParams(getLayoutParamsIcon());
        // avatar comment item
        int sizeAvatarUser = sizeAnalytic / 2;
        ivAvatarUser.setLayoutParams(new LinearLayout.LayoutParams(sizeAvatarUser, sizeAvatarUser));
        // image play video game or app trailer
        RelativeLayout.LayoutParams layoutParamsIVPlayVideo = new RelativeLayout.LayoutParams(sizeAnalytic, sizeAnalytic);
        layoutParamsIVPlayVideo.addRule(RelativeLayout.CENTER_IN_PARENT);
        ivPromoVideoPlay.setLayoutParams(layoutParamsIVPlayVideo);
    }

    public void initValue() {
        // imageView
        //ImageLoader.getInstance().displayImage(dataCardItem.getIcon_image(), ivIcon, options);
        UIUtils.loadImageLoader(Constants.options, dataCardItem.getIcon_image(), ivIcon);
        //setImageViewWithScale(ivPromoImage, UIUtils.getScreenWidth(activity), UIUtils.getScreenHeight(activity) / 3, dataCardItem.getPromo_image());
        UIUtils.loadImageLoader(Constants.options, dataCardItem.getPromo_image(), ivPromoImage);


        // if user logged show avatar of user
        if (Logged) {
            //ivAvatarUser
            //tvNameUser.setText();
        } else {
            String url = "drawable://" + R.drawable.ic_person_black_48dp;
            UIUtils.loadImageLoader(Constants.options, url, ivAvatarUser);
            //ivAvatarUser.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_person_black_48dp));
            tvNameUser.setText("Khách");
        }
        // basic information
        tvNameItem.setText(dataCardItem.getName());
        tvAuthorItem.setText(dataCardItem.getAuthor());
        tvShortDescription.setText(dataCardItem.getShort_description());

        // analytic information
        tvCountDown.setText(Integer.toString(dataCardItem.getDowns_count()));
        tvRate.setText(Float.toString(dataCardItem.getRate()));
        rbDetail.setScore(dataCardItem.getRate());
        tvTopic.setText("");
        if (dataCardItem.getPromo_video() != null) {
            ivPromoVideoPlay.setVisibility(View.VISIBLE);
        }


        // custom value
        initCustomValue();

        btStatusDetailButton.setmBidingData(mBidingData);

        this.dataCardItem.addUpdateListener(this.mUpdateListener);

        btStatusDetailButton.rebind(dataCardItem);
        //}

    }

    public void update(DataCardItem dataCardItem, final ArrayList<CardItem> suggestApps, ArrayList<CardItem> commentItems) throws JSONException {
        // update data for comments
        commentAdapter = new CommentAdapter(activity, commentItems, options);
        rvComments.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(rvComments);
        // update data for app suggest
        card_data_type = suggestApps.get(0).getCard_data_type();
        suggestAppAdapter = new AdapterRowHorizontalCardSuggest(activity, suggestApps.get(0).getCard_data(), dataCardItem.getData_type());
        rvAppSuggest.setAdapter(suggestAppAdapter);
        setHListViewHeightBasedOnChildren(rvAppSuggest);
        // update view for custom
        updateCustomView(dataCardItem);
        svParentAppDetailActivity.fullScroll(View.FOCUS_UP);
        // load suggest items
    }

    private DataCardItem.AppInfoUpdateListener mUpdateListener = new DataCardItem.AppInfoUpdateListener() {

        public void onContentUpdate(final DataCardItem appinfo) {
            mHandler.post(new Runnable() {
                public void run() {
                    initValue();
                }
            });
        }

        public void onStatusUpdate(final DataCardItem appinfo) {
            mHandler.post(new Runnable() {
                public void run() {
                    btStatusDetailButton.rebind(appinfo);
                }
            });
        }
    };

}
