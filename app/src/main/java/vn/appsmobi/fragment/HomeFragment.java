package vn.appsmobi.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.activity.HomeActivity;
import vn.appsmobi.adapter.AdapterHomeMenu;
import vn.appsmobi.adapter.HomeAdapter;
import vn.appsmobi.loader.BaseResult;
import vn.appsmobi.loader.CardLoader;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.ItemMenu;
import vn.appsmobi.ui.EmptyLoadingView;
import vn.appsmobi.ui.Refreshable;
import vn.appsmobi.utils.Constants;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;
import static vn.appsmobi.utils.UIUtils.setGridViewHeightBasedOnChildren;

/**
 * Created by tobrother on 04/04/2016.
 */
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<BaseResult>, Refreshable, ObservableScrollViewCallbacks {
    // View
    View view;
    GridView gvFragmentHome;
    RecyclerView rvFragmentHome;
    EmptyLoadingView clFragmentHome;
    StaggeredGridLayoutManager gridLayout;
    ObservableScrollView svParentHomeFragment;
    // Loader for this activity
    CardLoader cardLoader;
    boolean isEndBottom = false;
    // ----- init array list------
    ArrayList<CardItem> cardItems;
    ArrayList<ItemMenu> item_menus;
    String[] menu_titles;
    TypedArray menu_icons;
    // ------ init adapter---------
    AdapterHomeMenu menuAdapter;
    HomeAdapter adapterCardSuggest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Đọc file fragment_home.xml để vẽ ra giao diện người dùng.
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getData();
        initView();
        initValues();
        initActions();
        return view;
    }

    // ___________GET DATA_____________ //
    public void getData() {
        //Get data cho header fragment home
        menu_titles = getResources().getStringArray(R.array.menu_titles);
        menu_icons = getResources().obtainTypedArray(R.array.menu_icons);
        item_menus = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            item_menus.add(new ItemMenu(i, menu_titles[i], menu_icons.getResourceId(i, -1)));
        }

        cardItems = new ArrayList<>();
    }

    // ___________INIT VIEW_____________ //
    public void initView() {
        //set background cho linner bao thanh tollbar
        getActivity().findViewById(R.id.llActionBar).setBackgroundResource(android.R.color.transparent);
        //set background cho thanh toolbar, ở đây set background là hình chữ nhật màu trắng, bo góc
        getActivity().findViewById(R.id.myToolbar).setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_toolbar));
        // view for this fragment
        gvFragmentHome = (GridView) view.findViewById(R.id.gvFragmentHome);
        rvFragmentHome = (RecyclerView) view.findViewById(R.id.rvFragmentHome);
        clFragmentHome = (EmptyLoadingView) view.findViewById(R.id.clFragmentHome);
        svParentHomeFragment = (ObservableScrollView) view.findViewById(R.id.svParentHomeFragment);
        // recycle
        gridLayout = new StaggeredGridLayoutManager(1, 1);
        gridLayout.setReverseLayout(false);
        rvFragmentHome.setLayoutManager(gridLayout);
        rvFragmentHome.setBackgroundColor(Color.TRANSPARENT);

    }


    // ___________INIT VALUES_____________ //
    public void initValues() {
        //set kích thước của từng icon hiển thi trong gridview
        int sizeIconMenu = (int) ((getScreenWidth(getActivity()) / 3) - (2 * getResources().getDimension(R.dimen.margin_xlarge)));
        menuAdapter = new AdapterHomeMenu(getActivity(), item_menus, sizeIconMenu);
        //set data cho gridview
        gvFragmentHome.setAdapter(menuAdapter);
        //Phương pháp tính chiều cao GridView dựa trên số lượng các mục nó chứa và thiết lập chiều cao cho GridView tại thời gian chạy.
        setGridViewHeightBasedOnChildren(gvFragmentHome, getActivity());
        //recycle view
        adapterCardSuggest = new HomeAdapter(getActivity(), cardItems);
        rvFragmentHome.setAdapter(adapterCardSuggest);

        //__________Load lại view____________//
        clFragmentHome.setRefreshable(this);
        clFragmentHome.setNoNewDataCallback(new EmptyLoadingView.NoNewDataCallback() {
            @Override
            public boolean onNoNewData() {
                isEndBottom = false;
                return false;
            }
        });

    }

    // ___________INIT ACTIONS_____________ //
    public void initActions() {
        //Sự kiện onClick items gridview
        gvFragmentHome.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.APP);
                        break;
                    case 1:
                        fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.GAME);
                        break;
                    case 2:
                        fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.BOOK);
                        break;
                    case 3:
                        fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.FILM);
                        break;
                    case 4:
                        fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.WALLPAPER);
                        break;
                    case 5:
                        fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.RINGTONE);
                        break;
                }
                ((TextView) ((HomeActivity) getActivity()).myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                //Cập nhật lại fragment
                transaction.replace(R.id.flMainContainer, fragment);
                transaction.commit();
            }
        });
        //Gọi hàm setScrollViewCallbacks cho custom ScrollView
        svParentHomeFragment.setScrollViewCallbacks(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //Bộ loader dùng để load data
    @Override
    public Loader<BaseResult> onCreateLoader(int id, Bundle args) {
        cardLoader = new CardLoader(getActivity());
        cardLoader.setProgressNotifiable(clFragmentHome);
        // cardLoader.setRequestType(0);
        return cardLoader;
    }

    @Override
    public void onLoadFinished(Loader<BaseResult> loader, final BaseResult data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CardItem> newData = ((CardLoader.CardResult) data).cards;
                if (newData != null && newData.size() > 0) {
                    if (adapterCardSuggest == null) {
                    } else {
                        if (!cardItems.containsAll(newData)) {
                            cardItems.addAll(newData);
                            adapterCardSuggest.notifyDataSetChanged();
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


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(gvFragmentHome, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (((AppCompatActivity) getActivity()).getSupportActionBar().isShowing()) {
                getActivity().findViewById(R.id.myToolbar).animate()
                        .translationY(-getActivity().findViewById(R.id.myToolbar).getBottom())
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!((AppCompatActivity) getActivity()).getSupportActionBar().isShowing()) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                getActivity().findViewById(R.id.myToolbar).animate()
                        .translationY(0)
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
            }
        }
    }
}

