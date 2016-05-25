package vn.appsmobi.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import vn.appsmobi.R;
import vn.appsmobi.fragment.DataCardParentFragment2;
import vn.appsmobi.fragment.HistorySettingFragment;
import vn.appsmobi.fragment.HomeFragment;
import vn.appsmobi.fragment.SettingFragment;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.LogUtil;

public class HomeActivity extends AppCompatActivity {

    // View
    public Toolbar myToolbar;
    private AccountHeader headerResult;
    private SearchView searchView;
    FrameLayout flMainContainer;
    LinearLayout llActionBar;
    Drawer result;
    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đọc file activity_main.xml để vẽ ra giao diện người dùng.
        setContentView(R.layout.activity_main);
        getValues();
        initViews();
        initValues();
        initActions();
        initHeaderAccount();
        initMenuDrawer(savedInstanceState);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new HomeFragment();
        //Thay đổi fragment
        transaction.replace(R.id.flMainContainer, fragment);
        transaction.commit();
    }

    //__________GET VALUES___________//
    public void getValues() {

    }

    //__________INIT VIEWS___________//
    public void initViews() {
        //Loading bắt đầu
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        //Tạo thanh Toolbar
        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        //Đặt thanh công cụ cho ứng dụng
        setSupportActionBar(myToolbar);
        //Tạo FrameLayout
        flMainContainer = (FrameLayout) findViewById(R.id.flMainContainer);
        //Tạo LinnearLayout chứa thanh Toolbar
        llActionBar = (LinearLayout) findViewById(R.id.llActionBar);
        //_______________________LOADING VIEW___________________________//
    }

    //__________INIT VALUES___________//
    public void initValues() {

    }

    //__________INIT ACTIONS___________//
    public void initActions() {

    }

    //__________INIT HEADER ACCOUNT___________//
    //Tạo account trong thanh menu
    public void initHeaderAccount() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                //set background cho khung account
                .withHeaderBackground(R.drawable.header)
                //thêm một số trường cho khung account
                .addProfiles(
                        new ProfileDrawerItem().withName("Lê Minh Ân").withEmail("leminhan.cst@gmail.com").withIcon(getResources().getDrawable(R.drawable.icon_wallpaper))
                )
                //thay đổi account
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
    }

    //__________INIT MENU DRAWER___________//
    //Tạo một số items cho thanh menu
    public void initMenuDrawer(Bundle savedInstanceState) {
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(myToolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        //Nhóm 1
                        new PrimaryDrawerItem().withName("Trang Chủ").withIcon(FontAwesome.Icon.faw_home),
                        //Thanh ngang
                        new DividerDrawerItem(),
                        //here we use a customPrimaryDrawerItem we defined in our sample app
                        //this custom DrawerItem extends the PrimaryDrawerItem so it just overwrites some methods
                        // new CustomUrlPrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"),
                        //Nhóm 2
                        new SecondaryDrawerItem().withName("Ứng Dụng").withIcon(FontAwesome.Icon.faw_cubes),
                        new SecondaryDrawerItem().withName("Trò Chơi").withIcon(FontAwesome.Icon.faw_gamepad),
                        new SecondaryDrawerItem().withName("E-Book").withIcon(FontAwesome.Icon.faw_book),
                        new SecondaryDrawerItem().withName("Phim").withIcon(FontAwesome.Icon.faw_video_camera),
                        new SecondaryDrawerItem().withName("Hình Nền").withIcon(FontAwesome.Icon.faw_picture_o),
                        new SecondaryDrawerItem().withName("Nhạc Chuông").withIcon(FontAwesome.Icon.faw_music),
                        //Thanh ngang
                        new DividerDrawerItem(),
                        //Nhóm 3
                        new SecondaryDrawerItem().withName("Đánh Dấu").withIcon(FontAwesome.Icon.faw_star),
                        new SecondaryDrawerItem().withName("Lịch Sử Cài Đặt").withIcon(FontAwesome.Icon.faw_history),
                        new SecondaryDrawerItem().withName("Liên hệ").withIcon(FontAwesome.Icon.faw_phone)
                ) // add the items we want to use with our Drawer
                //Sự kiện nút home
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        //return true if we have consumed the event
                        return true;
                    }
                })
                //Sự kiện đóng mở thanh menu
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        getSupportActionBar().show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                //Thêm trường cho thanh menu
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Cài Đặt").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(10)
                )
                //Sự kiện click từng items cho trong thanh menu
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //Quản lý fragment
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Fragment fragment = null;
                        LogUtil.e("positon ", String.valueOf(position));
                        // start from 3
                        switch (position) {
                            case 1:
                                //vị trí đầu tiên sẽ trỏ đến HomeFragment
                                fragment = new HomeFragment();
                                //Set màu cho text view trong thanh toolbar
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#6f6f6f"));
                                break;
                            //Vị trí thứ 2 là thanh ngang nên bỏ qua case 2
                            case 3:
                                //vị trí thứ 3 sẽ trỏ đến fragment App
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.APP);
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                                break;
                            case 4:
                                //vị trí thứ 4 sẽ trỏ đến fragment Game
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.GAME);
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                                break;
                            case 5:
                                //vị trí thứ 5 sẽ trỏ đến fragment Bokk
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.BOOK);
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                                break;
                            case 6:
                                //vị trí thứ 6 sẽ trỏ đến fragment App
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.FILM);
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                                break;
                            case 7:
                                //vị trí thứ 7 sẽ trỏ đến fragment Wallpaper
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.WALLPAPER);
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                                break;
                            case 8:
                                //vị trí thứ 8 sẽ trỏ đến fragment Ringtone
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.RINGTONE);
                                ((TextView) myToolbar.findViewById(R.id.tvToolBarTitle)).setTextColor(Color.parseColor("#FFFFFF"));
                                break;
                            //Vị trí thứ 9 là thanh ngang nên bỏ case 9
                            case 10:
                                //vị trí thứ 10 sẽ trỏ đến fragment Đánh dấu
                                fragment = new SettingFragment();
                                break;
                            case 11:
                                //vị trí thứ 11 sẽ trỏ đến fragment Lịch sử cài đặt
                                fragment = new HistorySettingFragment();
                                break;
                            case 12:
                                //vị trí thứ 8 sẽ trỏ đến fragment Liên hệ
                                fragment = new SettingFragment();
                                break;
                            //Vị trí cuối cùng luôn là -1 sẽ trỏ đến fragment setting
                            case -1:
                                fragment = new SettingFragment();
                                break;
                        }
                        //Cập nhật lại fragment
                        transaction.replace(R.id.flMainContainer, fragment);
                        transaction.commit();
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    //Xử lý nút search trong thanh toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_state, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager;
        searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Tìm kiếm ....");
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
            searchView.setQueryHint("Tìm kiếm ....");
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    // actionSearch(query);
                    searchView.setFocusable(false);
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    /*
                    Fragment fragment = new CategoryFragment();
                    Bundle bundle = new Bundle();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.containerView,fragment)
                            .commit();
                    */
                    return false;
                }
            });

        }
        return super.onCreateOptionsMenu(menu);
    }

    //Xử ký nút back hệ thống
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            //super.onBackPressed();
            finish();
            return;
        }
    }
}