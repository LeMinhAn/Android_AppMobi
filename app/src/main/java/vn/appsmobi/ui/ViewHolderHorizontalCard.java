package vn.appsmobi.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;

import vn.appsmobi.R;
import vn.appsmobi.activity.ReadMoreActivity;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 28/12/2015.
 */
//Giao diện cho HomeFragment
public class ViewHolderHorizontalCard extends RecyclerView.ViewHolder {
    private Button btReadMoreCardSuggest2;
    public LinearLayout hlvRowItemCardSuggest2;
    private TextView tvTitleHorizontalCard;
    private boolean hideActionMore = false;

    //private LinearLayout hsvRowItemCardSuggest2;
    public ViewHolderHorizontalCard(View v) {
        super(v);
        btReadMoreCardSuggest2 = (Button) v.findViewById(R.id.btReadMoreCardSuggest2);
        hlvRowItemCardSuggest2 = (LinearLayout) v.findViewById(R.id.hsvRowItemCardSuggest2);
        tvTitleHorizontalCard = (TextView) v.findViewById(R.id.tvTitleHorizontalCard);
    }

    public void setValue(CardItem cardItem, final Context context, DisplayImageOptions options) throws JSONException {
        final int cardDataType = cardItem.getCard_data_type();



        // gán gái trị cho linerlayout
        switch (cardDataType) {
            //Lấy 3 trường: icon_image, rate, name từ Data_Card_Items trong lớp HorizontalRowView
            case Constants.CARD_DATA_TYPE.APP:
            case Constants.CARD_DATA_TYPE.FILM:
            case Constants.CARD_DATA_TYPE.GAME:
            case Constants.CARD_DATA_TYPE.BOOK:
                HorizontalRowView horizontalRowView;
                getHlvRowItemCardSuggest2().removeAllViews();
                for (int i = 0; i < cardItem.getCard_data().length(); i++) {
                    horizontalRowView = new HorizontalRowView(context);
                    horizontalRowView.setValue(context, cardItem.getCard_data().getJSONObject(i), cardItem.getCard_data_type());
                    /**
                     *
                     */
                    getHlvRowItemCardSuggest2().addView(horizontalRowView.getRootView());
                }
                break;
            //Chỉ lấy icon_image từ Data_Card_Items trong lớp SlideImageView
            case Constants.CARD_DATA_TYPE.WALLPAPER:
                SlideImageView slideImageView;
                getHlvRowItemCardSuggest2().removeAllViews();
                for (int i = 0; i < cardItem.getCard_data().length(); i++) {
                    slideImageView = new SlideImageView(context);
                    slideImageView.setValue(cardItem.getCard_data().getJSONObject(i), context, cardItem.getCard_data_type());
                    getHlvRowItemCardSuggest2().addView(slideImageView.getRootView());
                }
                break;
            //Lấy 3 trường: icon_image, rate, name từ Data_Card_Items trong lớp RingToneView
            case Constants.CARD_DATA_TYPE.RINGTONE:
                getHlvRowItemCardSuggest2().removeAllViews();
                RingToneView ringToneView;
                for (int i = 0; i < cardItem.getCard_data().length(); i++) {
                    ringToneView = new RingToneView(context);
                    ringToneView.getSizeHalf();
                    ringToneView.setSize();
                    ringToneView.setValue(cardItem.getCard_data().getJSONObject(i), context, cardItem.getCard_data_type());
                    getHlvRowItemCardSuggest2().addView(ringToneView.getRootView());
                }
                break;
        }
        // end


        getTvTitleHorizontalCard().setText(cardItem.getCard_title());

        //Sự kiện nút xem thêm
        getBtReadMoreCardSuggest2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadMoreActivity.class);
                intent.putExtra(Constants.Intent.READ_MORE_TYPE, 0);
                intent.putExtra(Constants.Intent.CARD_DATA_TYPE, cardDataType);
                context.startActivity(intent);
            }
        });
        if (hideActionMore) {
            getTvTitleHorizontalCard().setVisibility(View.GONE);
            getBtReadMoreCardSuggest2().setVisibility(View.GONE);
        }
    }

    public Button getBtReadMoreCardSuggest2() {
        return btReadMoreCardSuggest2;
    }

    public void setBtReadMoreCardSuggest2(Button btReadMoreCardSuggest2) {
        this.btReadMoreCardSuggest2 = btReadMoreCardSuggest2;
    }

    public LinearLayout getHlvRowItemCardSuggest2() {
        return hlvRowItemCardSuggest2;
    }

    public void setHlvRowItemCardSuggest2(LinearLayout hlvRowItemCardSuggest2) {
        this.hlvRowItemCardSuggest2 = hlvRowItemCardSuggest2;
    }


    public TextView getTvTitleHorizontalCard() {
        return tvTitleHorizontalCard;
    }

    public void setTvTitleHorizontalCard(TextView tvTitleHorizontalCard) {
        this.tvTitleHorizontalCard = tvTitleHorizontalCard;
    }

    public boolean isHideActionMore() {
        return hideActionMore;
    }

    public void setHideActionMore(boolean hideActionMore) {
        this.hideActionMore = hideActionMore;
    }
}
