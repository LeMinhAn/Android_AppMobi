package vn.appsmobi.adapter;

/**
 * Created by tobrother on 23/01/2016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import org.json.JSONException;

import java.util.List;

public class CardAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final String TAG = "ParallaxRecyclerAdapter";
    private final float SCROLL_MULTIPLIER = 0.5f;

    public class VIEW_TYPES {
        public static final int NORMAL = 111;
        public static final int HEADER = 222;
        public static final int FIRST_VIEW = 333;
    }

    public interface RecyclerAdapterMethods {
        void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) throws JSONException;

        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i);

        int getItemCount();
    }


    public interface OnClickEvent {
        /**
         * Event triggered when you click on a item of the adapter
         *
         * @param v        view
         * @param position position on the array
         */
        void onClick(View v, int position);
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


    private List<T> mData;
    private View mHeader;
    private RecyclerAdapterMethods mRecyclerAdapterMethods;
    private OnClickEvent mOnClickEvent;
    private OnParallaxScroll mParallaxScroll;
    private RecyclerView mRecyclerView;
    private int mTotalYScrolled;
    private int headearHeight = 0;
    RecyclerView.OnScrollListener mOnScrollListener = null;

    public void translateHeader(float of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;

        Log.d(TAG, "ofCalculated:" + ofCalculated);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(TAG, "setTranslationY:" + ofCalculated);
            mHeader.setTranslationY(ofCalculated);
        } else {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mHeader.startAnimation(anim);
        }
        if (mParallaxScroll != null) {
            Log.d(TAG, "mHeader:" + headearHeight + "::::" + mHeader.getHeight());
            float left = Math.min(1, ((ofCalculated) / (headearHeight * SCROLL_MULTIPLIER)));
            Log.d(TAG, "left:" + left);
            mParallaxScroll.onParallaxScroll(left, of, mHeader);
        }
    }


    public void setRecycleScrollListener(RecyclerView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }


    public void setParallaxHeader(View header, final RecyclerView view) {
        mHeader = header;
        final ViewTreeObserver observer = mHeader.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headearHeight = mHeader.getHeight();
                mHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mRecyclerView = view;
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    mTotalYScrolled += dy;
                    Log.d(TAG, "mTotalYScrolled:" + mTotalYScrolled);
                    if (mTotalYScrolled < 0)
                        mTotalYScrolled = 0;
                    translateHeader(mTotalYScrolled);
                }
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });

    }


    public View getParallaxHeader() {
        return mHeader;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (mRecyclerAdapterMethods == null)
            throw new NullPointerException("You must call implementRecyclerAdapterMethods");
        if (i == 0 && mHeader == null) {
            try {
                mRecyclerAdapterMethods.onBindViewHolder(viewHolder, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (i != 0 && mHeader != null)
            try {
                mRecyclerAdapterMethods.onBindViewHolder(viewHolder, i - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else if (i != 0)
            try {
                mRecyclerAdapterMethods.onBindViewHolder(viewHolder, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else if (i == 0 && mHeader != null) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);

        }
        if (mOnClickEvent != null)
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickEvent.onClick(v, i - (mHeader == null ? 0 : 1));
                }
            });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context ctx = viewGroup.getContext();
        if (mRecyclerAdapterMethods == null)
            throw new NullPointerException("You must call implementRecyclerAdapterMethods");
        if (i == VIEW_TYPES.HEADER && mHeader != null) {
            mHeader.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ViewHolder header = new ViewHolder(mHeader);
            return header;
        }

        if (i == VIEW_TYPES.FIRST_VIEW && mHeader != null && mRecyclerView != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForPosition(0);
            if (holder != null) {
                translateHeader(-holder.itemView.getTop());
                mTotalYScrolled = -holder.itemView.getTop();
            }
        }
        return mRecyclerAdapterMethods.onCreateViewHolder(viewGroup, i);
    }

    public void setOnClickEvent(OnClickEvent onClickEvent) {
        mOnClickEvent = onClickEvent;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, mHeader);
    }

    public CardAdapter(List<T> data) {
        mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addItem(T item, int position) {
        mData.add(position, item);
        notifyItemInserted(position + (mHeader == null ? 0 : 1));
    }

    public void removeItem(T item) {
        int position = mData.indexOf(item);
        if (position < 0)
            return;
        mData.remove(item);
        notifyItemRemoved(position + (mHeader == null ? 0 : 1));
    }


    public int getItemCount() {
        if (mRecyclerAdapterMethods == null)
            throw new NullPointerException("You must call implementRecyclerAdapterMethods");
        return mRecyclerAdapterMethods.getItemCount() + (mHeader == null ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecyclerAdapterMethods == null)
            throw new NullPointerException("You must call implementRecyclerAdapterMethods");
        if (position == 1)
            return VIEW_TYPES.FIRST_VIEW;
        return position == 0 ? VIEW_TYPES.HEADER : VIEW_TYPES.NORMAL;
    }

    public void implementRecyclerAdapterMethods(RecyclerAdapterMethods callbacks) {
        mRecyclerAdapterMethods = callbacks;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }
}