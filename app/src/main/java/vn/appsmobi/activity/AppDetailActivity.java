package vn.appsmobi.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.loader.BaseResult;
import vn.appsmobi.loader.CardDetailLoader;
import vn.appsmobi.model.ApplicationItem;
import vn.appsmobi.model.BookItem;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.model.MovieItem;
import vn.appsmobi.model.RingToneItem;
import vn.appsmobi.model.WallPaperItem;
import vn.appsmobi.ui.EmptyLoadingView;
import vn.appsmobi.ui.Refreshable;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.DetailActivityUI;
import vn.appsmobi.utils.DetailApplicationUI;
import vn.appsmobi.utils.DetailBookUI;
import vn.appsmobi.utils.DetailMediaActivityUI;
import vn.appsmobi.utils.DetailMovieUI;
import vn.appsmobi.utils.DetailRingToneUI;
import vn.appsmobi.utils.DetailWallPaperUI;
//Lớp cha của DetailApplicationUI, DetailMovieUI, DetailBookUI
public class AppDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<BaseResult>, Refreshable {

    private int card_data_type = 0;
    private Context context = this;
    private CardDetailLoader appDetailLoader;
    private Toolbar toolbar;
    private EmptyLoadingView appDetailLoading;
    private DetailActivityUI detailActivityUI;
    DetailMediaActivityUI detailMediaActivityUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init image loader
        //Nhận giá trị từ DetailActivityUI truyền sang
        card_data_type = getIntent().getIntExtra(Constants.Intent.CARD_DATA_TYPE, 0);
        //Nhận toàn bộ giá trị của DataCardItem từ DetailActivityUI truyền sang
        DataCardItem dataCardItem = getIntent().getParcelableExtra(Constants.Intent.CARD);
        switch (card_data_type) {
            //if card_data_type là APP hoặc Game thì sẽ lấy UI của DetailApplicationUI
            case Constants.CARD_DATA_TYPE.APP:
            case Constants.CARD_DATA_TYPE.GAME:
                //Dựa vào từng package name khác nhau mà hiển thị khác nhau
                DataCardItem.getAppByPackage(dataCardItem.getPackge_name()).addUpdateListener(mUpdateListener);
                ApplicationItem applicationItem = new ApplicationItem(dataCardItem);
                detailActivityUI = new DetailApplicationUI(this, Constants.options, applicationItem);
                break;
            //if card_data_type là film thì sẽ lấy UI của DetailMovieUI
            case Constants.CARD_DATA_TYPE.FILM:
                MovieItem movieItem = new MovieItem(dataCardItem);
                detailActivityUI = new DetailMovieUI(this, Constants.options, movieItem);
                break;
            //if card_data_type là film thì sẽ lấy UI của DetailBookUI
            case Constants.CARD_DATA_TYPE.BOOK:
                DataCardItem.getAppByPackage(dataCardItem.getPackge_name()).addUpdateListener(mUpdateListener);
                BookItem bookItem = new BookItem(dataCardItem);
                detailActivityUI = new DetailBookUI(this, Constants.options, bookItem);
                break;
            //if card_data_type là film thì sẽ lấy UI của DetailWallPaperUI
            case Constants.CARD_DATA_TYPE.WALLPAPER:
                WallPaperItem wallPaperItem = new WallPaperItem(dataCardItem);
                detailMediaActivityUI = new DetailWallPaperUI(this, Constants.options, wallPaperItem);
                break;
            //if card_data_type là film thì sẽ lấy UI của DetailRingToneUI
            case Constants.CARD_DATA_TYPE.RINGTONE:
                RingToneItem ringToneItem = new RingToneItem(dataCardItem);
                detailMediaActivityUI = new DetailRingToneUI(this, Constants.options, ringToneItem);
                break;
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appDetailLoading = (EmptyLoadingView) findViewById(R.id.appDetailLoading);
        appDetailLoading.setRefreshable(this);
        appDetailLoading.setNoNewDataCallback(new EmptyLoadingView.NoNewDataCallback() {
            @Override
            public boolean onNoNewData() {
                return true;
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }

    /*
      INIT LOADER
     */
    @Override
    public void refreshData() {
        if (appDetailLoader != null)
            appDetailLoader.reload();
    }

    @Override
    public Loader<BaseResult> onCreateLoader(int id, Bundle args) {
        appDetailLoader = new CardDetailLoader(context);
        appDetailLoader.setProgressNotifiable(appDetailLoading);
        return appDetailLoader;
    }

    @Override
    public void onLoadFinished(Loader<BaseResult> loader, final BaseResult data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CardItem> comments = ((CardDetailLoader.DetailCardResult) data).comments;
                ArrayList<CardItem> suggest_cards = ((CardDetailLoader.DetailCardResult) data).suggestApps;
                CardItem cardInfo = ((CardDetailLoader.DetailCardResult) data).infoCard;
                if (cardInfo != null) {
                    switch (card_data_type) {
                        case Constants.CARD_DATA_TYPE.APP:
                        case Constants.CARD_DATA_TYPE.GAME:
                            ApplicationItem item = new ApplicationItem();
                            try {
                                item.valueOf(cardInfo.getCard_data().getJSONObject(0));
                                detailActivityUI.update(item, suggest_cards, comments);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Constants.CARD_DATA_TYPE.FILM:
                            MovieItem movieItem = new MovieItem();
                            try {
                                movieItem.valueOf(cardInfo.getCard_data().getJSONObject(0));
                                detailActivityUI.update(movieItem, suggest_cards, comments);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Constants.CARD_DATA_TYPE.BOOK:
                            BookItem bookItem = new BookItem();
                            try {
                                bookItem.valueOf(cardInfo.getCard_data().getJSONObject(0));
                                detailActivityUI.update(bookItem, suggest_cards, comments);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Constants.CARD_DATA_TYPE.WALLPAPER:
                            break;
                        case Constants.CARD_DATA_TYPE.RINGTONE:
                            break;
                    }
                } else {
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<BaseResult> loader) {

    }

    private Handler mHandler = new Handler();
    //Update lại view: interface từ lớp DataCardItem, hàm AppInfoUpdateListener
    private DataCardItem.AppInfoUpdateListener mUpdateListener = new DataCardItem.AppInfoUpdateListener() {
        public void onContentUpdate(final DataCardItem appinfo) {
            mHandler.post(new Runnable() {
                public void run() {
                }
            });
        }

        public void onStatusUpdate(final DataCardItem appinfo) {
            mHandler.post(new Runnable() {
                public void run() {
                    detailActivityUI.btStatusDetailButton.rebind(appinfo);
                }
            });
        }
    };

    /**
     * Button back
     */
    @Override
    public void onBackPressed() {
        // your code.
        this.finish();
    }
}
