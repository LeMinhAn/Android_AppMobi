package vn.appsmobi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.ui.ViewHolderBigCard;
import vn.appsmobi.ui.ViewHolderCategory;
import vn.appsmobi.ui.ViewHolderHorizontalCard;
import vn.appsmobi.ui.ViewHolderImage;
import vn.appsmobi.ui.ViewHolderListVerticalCard;
import vn.appsmobi.ui.ViewHolderRingStone;
import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 26/01/2016.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<CardItem> cardItems;
    private DisplayImageOptions options;
    Context context;
    RecyclerAdapterMethods mRecyclerAdapterMethods;
    OnClickEvent mOnClickEvent;
    public String TAG = "DataBidingAdapter";

    // interface
    public interface OnClickEvent {
        void onClick(View v, int position) throws JSONException;
    }

    public void setOnClickEvent(OnClickEvent onClickEvent) {
        mOnClickEvent = onClickEvent;
    }

    public interface RecyclerAdapterMethods {
        void configurePlayRingTone(final ViewHolderRingStone viewHolder, final int position);

        void changeViewPlayRingTone(ViewHolderRingStone vh, int position);
    }

    public void implementRecyclerAdapterMethods(RecyclerAdapterMethods callbacks) {
        mRecyclerAdapterMethods = callbacks;
    }

    public HomeAdapter(Context context, ArrayList<CardItem> cardItems) {
        this.context = context;
        this.cardItems = cardItems;
        this.options = Constants.options;
    }

    //Truyền dữ liệu vào đúng các card tương ứng trong app, film, game, wallpaper, ringtone
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            //Dựa vào CardItem.card_type mà quy định cách hiển thị cho từng loại card
            //Giá trị trả về là số
            //if card_type = 0 thì đó là BIG_CARD
            case Constants.HOME_CARD_TYPE.BIG_CARD:
                View viewHolderBigCard = inflater.inflate(R.layout.item_big_card, parent, false);
                viewHolder = new ViewHolderBigCard(viewHolderBigCard);
                break;
            //if card_type = 4 thì đó là TEXT_CARD
            case Constants.HOME_CARD_TYPE.TEXT_CARD: // category
                View viewTextCard = inflater.inflate(R.layout.item_category, parent, false);
                viewHolder = new ViewHolderCategory(viewTextCard);
                break;
            //if card_type = 1 thì đó là HORIZONTAL_CARD
            case Constants.CARD_TYPE.HORIZONTAL_CARD: // top card of app , game , book , movie Fragment
                View viewHorizontalCard = inflater.inflate(R.layout.item_horizontal_card, parent, false);
                viewHolder = new ViewHolderHorizontalCard(viewHorizontalCard);
                break;
            //if card_type = 2 thì đó là VERTICAL_CARD
            case Constants.CARD_TYPE.VERTICAL_CARD: // app , game , book , movie card
                viewHolder = new ViewHolderListVerticalCard(inflater.inflate(R.layout.item_list_vertical_card, parent, false));
                break;
            //if card_type = 5 thì đó là HORIZONTAL_CARD_ONE
            case Constants.CARD_TYPE.HORIZONTAL_CARD_ONE: // ring tone card
                viewHolder = new ViewHolderRingStone(inflater.inflate(R.layout.item_ring_stone, parent, false));
                // cant play ringtone in home activity
                break;
            //if card_type = 6 thì đó là IMAGE_CARD
            case Constants.CARD_TYPE.IMAGE_CARD:  // wall paper card
                viewHolder = new ViewHolderImage(inflater.inflate(R.layout.item_card_image, parent, false), context, Constants.SCALE_IMAGE_CARD.IMAGE_CARD);
                break;
            //Mặc định là BIG_CARD
            default:
                viewHolder = new ViewHolderBigCard(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
                break;
        }
        //Bắt sự kiện click từng item view
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mOnClickEvent.onClick(v, viewHolder.getAdapterPosition());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return cardItems.get(position).getCard_type();
    }

    //Đưa dữ liệu lên view ở HomeFragment
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CardItem cardItem = cardItems.get(position);
        switch (cardItem.getCard_type()) {
            case Constants.HOME_CARD_TYPE.BIG_CARD:
                ViewHolderBigCard viewHolderBigCard = (ViewHolderBigCard) holder;
                //Set giá trị cho Big card
                viewHolderBigCard.setValue(cardItem, context, options);
                break;
            case Constants.HOME_CARD_TYPE.TEXT_CARD: // category
                ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
                //Set giá trị cho Text card
                viewHolderCategory.setValue(cardItem);
                break;
            case Constants.CARD_TYPE.HORIZONTAL_CARD: // top card of app , game , book , movie Fragment s
                try {
                    ViewHolderHorizontalCard vh = (ViewHolderHorizontalCard) holder;
                    //vh.setHideActionMore(true); // hide more action header
                    //Set giá trị cho horizontal card
                    vh.setValue(cardItem, context, options);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.CARD_TYPE.VERTICAL_CARD: // app , game , book , movie card

                ViewHolderListVerticalCard viewHolderVerticalCard = (ViewHolderListVerticalCard) holder;
                //set giá trị cho vertical card
                viewHolderVerticalCard.setValue(cardItem, context, options);
                break;
            case Constants.CARD_TYPE.HORIZONTAL_CARD_ONE: // ringtone card
                final ViewHolderRingStone vh = (ViewHolderRingStone) holder;

                mRecyclerAdapterMethods.changeViewPlayRingTone((ViewHolderRingStone) vh, position);
                vh.getSizeFull();
                vh.setSize();
                try {
                    //set giá trị cho horizontal card one
                    vh.setValue(cardItem.getCard_data().getJSONObject(0), options, context, cardItem.getCard_data_type());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerAdapterMethods.changeViewPlayRingTone(vh, position);
                vh.getIvPlayRingStone().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRecyclerAdapterMethods.configurePlayRingTone(vh, position);
                    }
                });

                break;
            case Constants.CARD_TYPE.IMAGE_CARD:  // wall paper card
                try {
                    ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                    //set giá trị cho image_card
                    viewHolderImage.setValue(cardItem.getCard_data().getJSONObject(0), options, context, cardItem.getCard_data_type());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }
    /*
    private void configureViewHolderComment(ViewHolderComment viewHolder, int position) throws JSONException {
        CommentItem item = new CommentItem();
        item.valueOf(cardItems.get(position).getCard_data().getJSONObject(0));
        viewHolder.getTvNameUserComment().setText(item.getName());
        viewHolder.getTvIntroComment().setText(item.getContent());
        viewHolder.getRbRatingComment().setRating(item.getRate());
        viewHolder.getTvTimeUserComment().setText(item.getPost_time());
        ImageLoader.getInstance().displayImage(item.getAvatar_image(), viewHolder.getIvAvatarComment(), options);
    }
    */
}
