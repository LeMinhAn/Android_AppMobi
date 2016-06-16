package vn.appsmobi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.activity.HomeActivity;
import vn.appsmobi.activity.ImageDetailActivity;
import vn.appsmobi.adapter.DataBidingAdapter;
import vn.appsmobi.loader.BaseResult;
import vn.appsmobi.loader.CardLoader;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.model.RingToneItem;
import vn.appsmobi.ui.EmptyLoadingView;
import vn.appsmobi.ui.HidingScrollListener;
import vn.appsmobi.ui.Refreshable;
import vn.appsmobi.ui.ViewHolderRingStone;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.ToastUtil;
import vn.appsmobi.utils.Utils;

import static vn.appsmobi.utils.UIUtils.calculateActionBarSize;

public class DataCardListFragment extends Fragment implements LoaderManager.LoaderCallbacks<BaseResult>, Refreshable, DataBidingAdapter.OnClickEvent {

    private ArrayList<CardItem> cardItems;
    private DataBidingAdapter dataBidingAdapter;
    private int resType, card_data_type;
    private int currentPlayingPosition = STATE_NOT_PLAY;
    private Drawable IMAGE_PLAY, IMAGE_PAUSE;
    private View view;

    private ObservableRecyclerView rvDataCardList;
    private CardLoader cardLoader;
    private EmptyLoadingView cardLoadingFragment;
    private Handler mHandler = new Handler();
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
    private StaggeredGridLayoutManager gridLayout;

    public static MediaPlayer mpPlayStream;
    public static int STATE_NOT_PLAY = -1;

    boolean isEndBottom = false;
    int color;

    @SuppressLint("ValidFragment")
    public DataCardListFragment(int color) {
        this.color = color;
    }


    public DataCardListFragment() {
    }

    public static DataCardListFragment newInstance(int tab_type, int fragment_type) {
        DataCardListFragment fragment = new DataCardListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.Intent.EXTRA_TAB_TYPE, tab_type);
        args.putInt(Constants.Intent.CARD_DATA_TYPE, fragment_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resType = getArguments().getInt(Constants.Intent.EXTRA_TAB_TYPE);
            card_data_type = getArguments().getInt(Constants.Intent.CARD_DATA_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_data_card_list, container, false);

        getData();
        initView();
        initActions();
        /*
        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            rvDataCardList.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                rvDataCardList.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        */
        return view;
    }

    public void getData() {

        IMAGE_PLAY = getActivity().getResources().getDrawable(R.drawable.ic_play_circle);
        IMAGE_PAUSE = getActivity().getResources().getDrawable(R.drawable.ic_pause_ring_stone);

    }

    private void refreshView() {
        cardLoadingFragment.setRefreshable(this);
        cardLoadingFragment.setNoNewDataCallback(new EmptyLoadingView.NoNewDataCallback() {
            @Override
            public boolean onNoNewData() {
                isEndBottom = false;
                return false;
            }
        });
    }

    private void setLayoutManager() {
        if (resType == Constants.TAB_TYPE.CATEGORY) {
            gridLayout = new StaggeredGridLayoutManager(1, 1);
        } else {
            if (card_data_type == Constants.CARD_DATA_TYPE.WALLPAPER) {
                gridLayout = new StaggeredGridLayoutManager(2, 1);
            } else if (card_data_type == Constants.CARD_DATA_TYPE.RINGTONE) {
                gridLayout = new StaggeredGridLayoutManager(3, 1);
            } else {
                gridLayout = new StaggeredGridLayoutManager(1, 1);
            }
        }
        gridLayout.setReverseLayout(false);
    }

    public void initView() {

        cardItems = new ArrayList<>();
        dataBidingAdapter = new DataBidingAdapter(getActivity(), cardItems);
        dataBidingAdapter.implementRecyclerAdapterMethods(myRecyclerAdapterMethods);

        cardLoadingFragment = (EmptyLoadingView) view.findViewById(R.id.cardLoadingFragment);
        refreshView();

        rvDataCardList = (ObservableRecyclerView) view.findViewById(R.id.rvDataCardList);
        setLayoutManager();
        rvDataCardList.setLayoutManager(gridLayout);
        rvDataCardList.setHasFixedSize(true);
    }

    public void initActions() {
        rvDataCardList.setOnScrollListener(new HidingScrollListener(getActivity()) {
            @Override
            public void onMoved(int distance) {
                HomeActivity.llActionBar.setTranslationY(-distance);
            }

            @Override
            public void onShow() {
                HomeActivity.llActionBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                HomeActivity.llActionBar.animate().translationY(-calculateActionBarSize(getActivity())).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });
        rvDataCardList.setAdapter(dataBidingAdapter);
        dataBidingAdapter.setOnClickEvent(this);
        rvDataCardList.setOnScrollListener(mOnScrollListener);
        //rvDataCardList.setItemViewCacheSize(4);
        // rvDataCardList.setScrollViewCallbacks((ObservableScrollViewCallbacks) getActivity());

    }

    DataBidingAdapter.RecyclerAdapterMethods myRecyclerAdapterMethods = new DataBidingAdapter.RecyclerAdapterMethods() {
        @Override
        public void changeViewPlayRingTone(ViewHolderRingStone vh, int position) {
            CircularProgressBar pb = vh.getCpcPlayRingStone();
            ImageView iv = vh.getIvPlayRingStone();
            TextView tv = vh.getTvNameRingStone();
            if (position == currentPlayingPosition) {
                //pb.setVisibility(View.VISIBLE);
                mProgressUpdater.mBarToUpdate = pb;
                mProgressUpdater.mIVPlay = iv;
                mProgressUpdater.mTVName = tv;
                mHandler.postDelayed(mProgressUpdater, 100);
            } else {
                //pb.setVisibility(View.GONE);
                pb.setProgress(0);
                if (mProgressUpdater.mBarToUpdate == pb) {
                    //this progress would be updated, but this is the wrong position
                    mProgressUpdater.mBarToUpdate = null;
                    mProgressUpdater.mIVPlay = null;
                    mProgressUpdater.mTVName = null;
                }
            }
        }

        //Cấu hình chức năng chơi nhạc
        @Override
        public void configurePlayRingTone(ViewHolderRingStone viewHolder, int position) {
            //start playing item at position
            if (currentPlayingPosition != position) {
                try {
                    RingToneItem item = new RingToneItem();
                    item.valueOf(cardItems.get(position).getCard_data().getJSONObject(0));
                    // play ringtone
                    playPlayback(item.getResource_url());
                    if (currentPlayingPosition != STATE_NOT_PLAY) {
                        dataBidingAdapter.notifyItemChanged(currentPlayingPosition);
                    }
                    currentPlayingPosition = position;
                    mHandler.postDelayed(mProgressUpdater, 100);
                    //trigger list refresh, this will make progressbar start updating if visible
                    dataBidingAdapter.notifyItemChanged(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mpPlayStream.stop();
                mpPlayStream.reset();
                if (currentPlayingPosition != STATE_NOT_PLAY) {
                    dataBidingAdapter.notifyItemChanged(currentPlayingPosition);
                }
                currentPlayingPosition = STATE_NOT_PLAY;
                //currentPlayingPosition=STATE_NOT_PLAY;
                mHandler.postDelayed(mProgressUpdater, 100);
                //trigger list refresh, this will make progressbar start updating if visible
                dataBidingAdapter.notifyItemChanged(position);
            }
            //adapterCardSuggest.notifyItemChanged(currentPlayingPosition);
        }

    };

    private class PlaybackUpdater implements Runnable {
        public CircularProgressBar mBarToUpdate = null;
        public ImageView mIVPlay;
        public TextView mTVName;

        @Override
        public void run() {
            if ((currentPlayingPosition != STATE_NOT_PLAY) && (null != mBarToUpdate)) {
                if (mpPlayStream.isPlaying()) {
                    mBarToUpdate.setProgress((100 * mpPlayStream.getCurrentPosition() / mpPlayStream.getDuration()));    //Cast
                    mIVPlay.setImageDrawable(IMAGE_PAUSE);
                    // UIUtils.loadImageLoader(Constants.options,IMAGE_PAUSE,mIVPlay);
                    mTVName.setSelected(true);
                    mTVName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    mTVName.setSingleLine(true);
                } else {
                    //UIUtils.loadImageLoader(Constants.options, IMAGE_PLAY, mIVPlay);
                    mIVPlay.setImageDrawable(IMAGE_PLAY);
                    mBarToUpdate.setProgress(0);
                }
                mHandler.postDelayed(this, 100);
            } else {

            }
        }
    }

    private void stopPlayback() {
        currentPlayingPosition = STATE_NOT_PLAY;
        mProgressUpdater.mBarToUpdate = null;
        mProgressUpdater.mIVPlay = null;
        mpPlayStream.stop();
    }

    private void playPlayback(String source) {
        if (mpPlayStream == null) {
            mpPlayStream = new MediaPlayer();
            mpPlayStream.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else {
            mpPlayStream.stop();
            mpPlayStream.reset();
        }
        try {
            mpPlayStream.setDataSource(source);
            mpPlayStream.prepareAsync();
            mpPlayStream.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mpPlayStream.start();
                }
            });
            mpPlayStream.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //  UIUtils.loadImageLoader(Constants.options, IMAGE_PLAY, mProgressUpdater.mIVPlay);
                    mProgressUpdater.mIVPlay.setImageDrawable(IMAGE_PLAY);
                    mProgressUpdater.mBarToUpdate.setProgress(0);
                    currentPlayingPosition = STATE_NOT_PLAY;
                }
            });
            mpPlayStream.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    switch (extra) {
                        case MediaPlayer.MEDIA_ERROR_IO:
                            ToastUtil.show(getActivity(), "MEDIA_ERROR_IO");
                            break;
                        case MediaPlayer.MEDIA_ERROR_MALFORMED:
                            ToastUtil.show(getActivity(), "MEDIA_ERROR_MALFORMED");
                            break;
                        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                            ToastUtil.show(getActivity(), "MEDIA_ERROR_UNSUPPORTED");
                            break;
                        case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                            ToastUtil.show(getActivity(), "MEDIA_ERROR_TIMED_OUT");
                            break;
                    }
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            stopPlayback();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            // if(sortMenu.isExpanded())
            //     sortMenu.toggle();
            float y = recyclerView.getY();
            int visibleItemCount = gridLayout.getChildCount();
            int totalItemCount = gridLayout.getItemCount();
            int[] visibleItems = gridLayout.findFirstVisibleItemPositions(null);
            int pastVisiblesItems = visibleItems[0];
            if ((totalItemCount > visibleItemCount) && (totalItemCount - visibleItemCount <= pastVisiblesItems)) {
                if ((cardLoader != null && !cardLoader.isLoading()) && isEndBottom) {
                    ((CardLoader) cardLoader).nextPage();
                    ((CardLoader) cardLoader).forceLoad();
                }
            }
            /*
            if (y < dy ) {
                toolbarContainer.animate()
                        .translationY(-toolbarContainer.getBottom())
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
                getActivity().findViewById(R.id.flMainContainer).setPadding(0,0,0,0);
            } else {
                toolbarContainer.animate()
                        .translationY(0)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(100)
                        .start();
                getActivity().findViewById(R.id.flMainContainer).setPadding(0,150, 0, 0);
            }
            */
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

    }


    @Override
    public void onClick(View v, int position) throws JSONException {
        if (cardItems.get(position).getCard_type() != Constants.CARD_TYPE.HORIZONTAL_CARD && cardItems.get(position).getCard_type() != Constants.CARD_TYPE.TEXT_CARD) {
            if (card_data_type == Constants.CARD_TYPE.IMAGE_CARD) {
                Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                // Setup the transition to the detail activity
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.ivImageWallPaper), "cover");
                startActivity(intent, options.toBundle());
            } else {
                DataCardItem card = new DataCardItem();
                card.valueOf(cardItems.get(position).getCard_data().getJSONObject(0));
                Utils.EventClick.CardClick(getActivity(), card);
            }
        }
        /*
        Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
        // Setup the transition to the detail activity
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.ivImageWallPaper), "cover");
        startActivity(intent, options.toBundle());
        */
    }

    @Override
    public Loader<BaseResult> onCreateLoader(int id, Bundle args) {
        cardLoader = new CardLoader(getActivity());
        cardLoader.setProgressNotifiable(cardLoadingFragment);
        cardLoader.setRequestType(Constants.RequestType.LIST_APP);
        cardLoader.setCardDataType(card_data_type);
        cardLoader.setResType(resType);
        return cardLoader;
    }

    @Override
    public void onLoadFinished(Loader<BaseResult> loader, final BaseResult data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CardItem> newData = ((CardLoader.CardResult) data).cards;
                if (newData != null && newData.size() > 0) {
                    if (dataBidingAdapter == null) {
                    } else {
                        if (!cardItems.containsAll(newData)) {
                            cardItems.addAll(newData);
                            dataBidingAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    isEndBottom = true;
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<BaseResult> loader) {

    }

    @Override
    public void refreshData() {
        if (cardLoader != null)
            cardLoader.reload();
    }


}
