package vn.appsmobi.activity;

import android.app.SearchManager;
import android.content.Context;
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

public class HomeActivity extends AppCompatActivity {

    private AccountHeader headerResult;
    private SearchView searchView = null;
    public static TextView tvToolBarTitle;
    boolean doubleBackToExitPressedOnce;
    public static LinearLayout llActionBar;
    public static Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initHeaderAccount();
        initMenuDrawer(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new HomeFragment();
        transaction.replace(R.id.flMainContainer, fragment);
        transaction.commit();
    }

    public void initViews() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        llActionBar = (LinearLayout) findViewById(R.id.llActionBar);
        tvToolBarTitle = (TextView) findViewById(R.id.tvToolBarTitle);
    }

    public void initHeaderAccount() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(getResources().getString(R.string.account_name)).withEmail(getResources().getString(R.string.account_mail)).withIcon(getResources().getDrawable(R.drawable.icon_wallpaper))
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

    public void initMenuDrawer(Bundle savedInstanceState) {
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(myToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.menu_home).withIcon(FontAwesome.Icon.faw_home),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.menu_application).withIcon(FontAwesome.Icon.faw_cubes),
                        new SecondaryDrawerItem().withName(R.string.menu_game).withIcon(FontAwesome.Icon.faw_gamepad),
                        new SecondaryDrawerItem().withName(R.string.menu_ebook).withIcon(FontAwesome.Icon.faw_book),
                        new SecondaryDrawerItem().withName(R.string.menu_movie).withIcon(FontAwesome.Icon.faw_video_camera),
                        new SecondaryDrawerItem().withName(R.string.menu_walpaper).withIcon(FontAwesome.Icon.faw_picture_o),
                        new SecondaryDrawerItem().withName(R.string.menu_ringstone).withIcon(FontAwesome.Icon.faw_music),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.menu_check).withIcon(FontAwesome.Icon.faw_star),
                        new SecondaryDrawerItem().withName(R.string.menu_history).withIcon(FontAwesome.Icon.faw_history),
                        new SecondaryDrawerItem().withName(R.string.menu_contact).withIcon(FontAwesome.Icon.faw_phone)
                )
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        return true;
                    }
                })
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
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.menu_setting).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(10)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Fragment fragment = null;
                        switch (position) {
                            case 1:
                                fragment = new HomeFragment();
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar));
                                break;
                            case 3:
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.APP);
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar_change));
                                break;
                            case 4:
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.GAME);
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar_change));
                                break;
                            case 5:
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.BOOK);
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar_change));
                                break;
                            case 6:
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.FILM);
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar_change));
                                break;
                            case 7:
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.WALLPAPER);
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar_change));
                                break;
                            case 8:
                                fragment = new DataCardParentFragment2().newInstance(Constants.CARD_DATA_TYPE.RINGTONE);
                                tvToolBarTitle.setTextColor(getResources().getColor(R.color.text_toolbar_change));
                                break;
                            case 10:
                                fragment = new SettingFragment();
                                break;
                            case 11:
                                fragment = new HistorySettingFragment();
                                break;
                            case 12:
                                fragment = new SettingFragment();
                                break;
                            case -1:
                                fragment = new SettingFragment();
                                break;
                        }
                        transaction.replace(R.id.flMainContainer, fragment);
                        transaction.commit();
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_state, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager;
        searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.back_again, Toast.LENGTH_SHORT).show();
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