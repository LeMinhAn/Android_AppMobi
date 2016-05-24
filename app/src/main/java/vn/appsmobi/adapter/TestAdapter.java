package vn.appsmobi.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.ui.ViewHolderBigCard;
import vn.appsmobi.ui.ViewHolderHorizontalCard;
import vn.appsmobi.ui.ViewHolderListVerticalCard;
import vn.appsmobi.ui.ViewHolderRingStone;
import vn.appsmobi.ui.ViewHolderTextInfo;
import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 06/01/2016.
 */
public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String PARALLAX_ADAPTER = "ParallaxRecyclerAdapter";
    private static final float SCROLL_MULTIPLIER = 0.5f;
    private ArrayList<CardItem> itemCards;
    private CustomRelativeWrapper mHeader;
    private OnClickEvent mOnClickEvent;
    private OnParallaxScroll mParallaxScroll;
    private RecyclerView mRecyclerView;
    private boolean mShouldClipView = true;
    private DisplayImageOptions options;
    private Context context;
    RecyclerAdapterMethods mRecyclerAdapterMethods;
    RecyclerView.OnScrollListener mOnScrollListener = null;

    public TestAdapter(Context context, DisplayImageOptions options) {
        this.context = context;
        this.options = options;
    }

    public static class VIEW_TYPES {
        public static final int HEADER = 222;
    }

    public interface RecyclerAdapterMethods {
        void configureViewHolderRingStone(final ViewHolderRingStone viewHolder, final int position);
    }

    public void implementRecyclerAdapterMethods(RecyclerAdapterMethods callbacks) {
        mRecyclerAdapterMethods = callbacks;
    }

    public void onBindViewHolderImpl(RecyclerView.ViewHolder holder, TestAdapter adapter, int position) throws JSONException {
        CardItem mItem = itemCards.get(position);
        switch (itemCards.get(position).getCard_type()) {
            case Constants.HOME_CARD_TYPE.BIG_CARD:
                ViewHolderBigCard viewHolderBigCard = (ViewHolderBigCard) holder;
                viewHolderBigCard.setValue(mItem, context, options);
                break;
            case Constants.HOME_CARD_TYPE.HORIZONTAL_CARD:
                ViewHolderHorizontalCard viewHolderHorizontalCard = (ViewHolderHorizontalCard) holder;
                viewHolderHorizontalCard.setValue(mItem, context, options);
                break;
            case Constants.HOME_CARD_TYPE.VERTICAL_CARD:
                ViewHolderListVerticalCard viewHolderVerticalCard = (ViewHolderListVerticalCard) holder;
                viewHolderVerticalCard.setValue(mItem, context, options);
                break;
            case Constants.HOME_CARD_TYPE.TEXT_CARD:
                ViewHolderTextInfo viewHolderTextInfo = (ViewHolderTextInfo) holder;
                viewHolderTextInfo.setValue(mItem, context);
                break;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup parent, TestAdapter adapter, int ViewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (ViewType) {
            case Constants.HOME_CARD_TYPE.BIG_CARD:
                viewHolder = new ViewHolderBigCard(inflater.inflate(R.layout.item_big_card, parent, false));
                break;
            case Constants.HOME_CARD_TYPE.HORIZONTAL_CARD:
                viewHolder = new ViewHolderHorizontalCard(inflater.inflate(R.layout.item_horizontal_card, parent, false));
                break;
            case Constants.HOME_CARD_TYPE.VERTICAL_CARD:
                viewHolder = new ViewHolderListVerticalCard(inflater.inflate(R.layout.item_list_vertical_card, parent, false));
                break;
            case Constants.HOME_CARD_TYPE.TEXT_CARD:
                viewHolder = new ViewHolderTextInfo(inflater.inflate(R.layout.item_text_info, parent, false), context, Constants.SCALE_IMAGE_CARD.VERTICAL_CARD);
                break;
            default:
                viewHolder = new ViewHolderBigCard(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
                break;
        }
        return viewHolder;
    }

    public int getItemCountImpl(TestAdapter adapter) {
        if (mHeader != null) {
            return itemCards.size() - 1;
        }
        return itemCards.size();
    }

    public interface OnClickEvent {
        /**
         * Event triggered when you click on a item of the adapter
         *
         * @param v        view
         * @param position position on the array
         */
        void onClick(View v, int position) throws JSONException;
    }

    public interface OnParallaxScroll {
        /**
         * Event triggered when the parallax is being scrolled.
         *
         * @param percentage
         * @param offset
         * @param parallax
         */
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    /**
     * Translates the adapter in Y
     *
     * @param of offset in px
     */
    public void translateHeader(float of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mHeader.setTranslationY(ofCalculated);
        } else {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mHeader.startAnimation(anim);
        }
        mHeader.setClipY(Math.round(ofCalculated));
        if (mParallaxScroll != null) {
            float left = Math.min(1, ((ofCalculated) / (mHeader.getHeight() * SCROLL_MULTIPLIER)));
            mParallaxScroll.onParallaxScroll(left, of, mHeader);
        }
    }

    public void setRecycleScrollListener(RecyclerView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    /**
     * Set the view as header.
     *
     * @param header The inflated header
     * @param view   The RecyclerView to set scroll listeners
     */
    public void setParallaxHeader(View header, final RecyclerView view) {
        mRecyclerView = view;
        mHeader = new CustomRelativeWrapper(header.getContext(), mShouldClipView);
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeader.addView(header, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    translateHeader(mRecyclerView.computeVerticalScrollOffset());
                }
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (position != 0 && mHeader != null) {
            try {
                onBindViewHolderImpl(viewHolder, this, position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (position != 0) {
            try {
                onBindViewHolderImpl(viewHolder, this, position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        if (i == VIEW_TYPES.HEADER && mHeader != null)
            return new ViewHolder(mHeader);
        if (mHeader != null && mRecyclerView != null) {
            final RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(0);
            if (holder != null) {
                translateHeader(-holder.itemView.getTop());
            }
        }
        final RecyclerView.ViewHolder holder = onCreateViewHolderImpl(viewGroup, this, i);
        if (mOnClickEvent != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mOnClickEvent.onClick(v, holder.getAdapterPosition());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return holder;
    }

    /**
     * @return true if there is a header on this adapter, false otherwise
     */
    public boolean hasHeader() {
        return mHeader != null;
    }

    public void setOnClickEvent(OnClickEvent onClickEvent) {
        mOnClickEvent = onClickEvent;
    }


    public boolean isShouldClipView() {
        return mShouldClipView;
    }

    /**
     * Defines if we will clip the layout or not. MUST BE CALLED BEFORE {@link #setParallaxHeader(android.view.View, android.support.v7.widget.RecyclerView)}
     *
     * @param shouldClickView
     */
    public void setShouldClipView(boolean shouldClickView) {
        mShouldClipView = shouldClickView;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, mHeader);
    }

    public TestAdapter(ArrayList<CardItem> data) {
        itemCards = data;
    }

    public ArrayList<CardItem> getData() {
        return itemCards;
    }


    public void addItem(CardItem item, int position) {
        itemCards.add(position, item);
        notifyItemInserted(position + (mHeader == null ? 0 : 1));
    }

    public void removeItem(CardItem item) {
        int position = itemCards.indexOf(item);
        if (position < 0)
            return;
        itemCards.remove(item);
        notifyItemRemoved(position + (mHeader == null ? 0 : 1));
    }

    public int getItemCount() {
        return getItemCountImpl(this) + (mHeader == null ? 0 : 1);
    }

    public void setData(ArrayList<CardItem> data) {
        CardItem header = new CardItem("", Constants.HOME_CARD_TYPE.HEADER);
        data.add(0, header);
        itemCards = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1)
            return itemCards.get(position).getCard_type();
        if (mHeader != null) {
            return position == 0 ? VIEW_TYPES.HEADER : itemCards.get(position).getCard_type();
        } else {
            return itemCards.get(position).getCard_type();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;
        private boolean mShouldClip;

        public CustomRelativeWrapper(Context context, boolean shouldClick) {
            super(context);
            mShouldClip = shouldClick;
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (mShouldClip) {
                canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            }
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }
}