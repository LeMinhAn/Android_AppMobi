package vn.appsmobi.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.view.ViewHelper;

import vn.appsmobi.R;
import vn.appsmobi.fragment.DataCardListFragment;
import vn.appsmobi.ui.SlidingTabLayout;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.UIUtils;

public class ListCardActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    //init view
    int card_data_type;
    // init views
    Toolbar toolbar;
    ViewPager vpListCard;
    SlidingTabLayout stListCardActivity;
    View mHeaderView;
    //ViewPagerAdapter adapter;
    NavigationAdapter adapter;
    Context context = this;
    private TouchInterceptionFrameLayout mInterceptionLayout;
    private int mSlop;
    private boolean mScrolled;
    private ScrollState mLastScrollState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_card);
        getData();
        initViews();
        initValues();
        initActions();
    }

    //-------------GET DATA-----------//
    public void getData() {
        //Nhận giá trị từ HomeFragment
        card_data_type = getIntent().getIntExtra(Constants.Intent.CARD_DATA_TYPE, 0);
    }
    //-------------INIT VIEW-----------//
    public void initViews() {
        mHeaderView = (LinearLayout) findViewById(R.id.llHeaderListCardActivity);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.margin_small));
        // tool bar
        toolbar = (Toolbar) findViewById(R.id.tbListCardActivity);
        // view paper
        vpListCard = (ViewPager) findViewById(R.id.vpListCard);
        // vpListCard.setPagingEnabled(false);
        // tab bar
        stListCardActivity = (SlidingTabLayout) findViewById(R.id.stListCardActivity);
    }
    //-------------INIT VALUES-----------//
    public void initValues() {
        // tool bar
        setSupportActionBar(toolbar);
        // view paper
        //setupViewPager(vpListCard);
        adapter = new NavigationAdapter(getSupportFragmentManager());
        vpListCard.setAdapter(adapter);
        // set page limit cache to save page don't reload data
        vpListCard.setOffscreenPageLimit(3);
        final int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        findViewById(R.id.pager_wrapper).setPadding(0, UIUtils.calculateActionBarSize(context) + tabHeight, 0, 0);
        stListCardActivity.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        stListCardActivity.setSelectedIndicatorColors(getResources().getColor(R.color.main_color));
        stListCardActivity.setDistributeEvenly(true);
        stListCardActivity.setViewPager(vpListCard);
        ViewConfiguration vc = ViewConfiguration.get(this);
        mSlop = vc.getScaledTouchSlop();
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.containerListCard);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
    }
    //-------------INIT ACTIONS-----------//
    public void initActions() {

    }
    @Override
    public void onBackPressed() {
        // your code.
        this.finish();
    }
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (!mScrolled) {
            // This event can be used only when TouchInterceptionFrameLayout
            // doesn't handle the consecutive events.
            adjustToolbar(scrollState);
        }
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                // Horizontal scroll is maybe handled by ViewPager
                return false;
            }

            Scrollable scrollable = getCurrentScrollable();
            if (scrollable == null) {
                mScrolled = false;
                return false;
            }

            // If interceptionLayout can move, it should intercept.
            // And once it begins to move, horizontal scroll shouldn't work any longer.
            int toolbarHeight = toolbar.getHeight();
            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
            boolean scrollingUp = 0 < diffY;
            boolean scrollingDown = diffY < 0;
            if (scrollingUp) {
                if (translationY < 0) {
                    mScrolled = true;
                    mLastScrollState = ScrollState.UP;
                    return true;
                }
            } else if (scrollingDown) {
                if (-toolbarHeight < translationY) {
                    mScrolled = true;
                    mLastScrollState = ScrollState.DOWN;
                    return true;
                }
            }
            mScrolled = false;
            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -toolbar.getHeight(), 0);
            ViewHelper.setTranslationY(mInterceptionLayout, translationY);
            if (translationY < 0) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                lp.height = (int) (-translationY + UIUtils.getScreenHeight(context));
                mInterceptionLayout.requestLayout();
            }
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
            adjustToolbar(mLastScrollState);
        }
    };


    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.scroll);
    }

    private void adjustToolbar(ScrollState scrollState) {
        int toolbarHeight = toolbar.getHeight();
        final Scrollable scrollable = getCurrentScrollable();
        if (scrollable == null) {
            return;
        }
        int scrollY = scrollable.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else if (!toolbarIsShown() && !toolbarIsHidden()) {
            // Toolbar is moving but doesn't know which to move:
            // you can change this to hideToolbar()
            showToolbar();
        }
    }

    private Fragment getCurrentFragment() {
        return adapter.getItemAt(vpListCard.getCurrentItem());
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mInterceptionLayout) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mInterceptionLayout) == -toolbar.getHeight();
    }

    private void showToolbar() {
        animateToolbar(0);
    }

    private void hideToolbar() {
        animateToolbar(-toolbar.getHeight());
    }

    private void animateToolbar(final float toY) {
        float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
        if (layoutTranslationY != toY) {

            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translationY = (float) animation.getAnimatedValue();
                    ViewHelper.setTranslationY(mInterceptionLayout, translationY);
                    if (translationY < 0) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                        lp.height = (int) (-translationY + UIUtils.getScreenHeight(context));
                        mInterceptionLayout.requestLayout();
                    }
                }
            });
            animator.start();
        }
    }

    private class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private final String[] TITLES = new String[]{"DANH MỤC", "NỔI BẬT", "TOP CÀI ĐẶT", "MỚI NHẤT"};

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f;
            final int pattern = position % 5;
            switch (pattern) {
                case 0:
                    f = new DataCardListFragment(getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.CATEGORY, card_data_type);
                    break;
                case 1:
                    f = new DataCardListFragment(getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.HOT, card_data_type);
                    break;
                case 2:
                    f = new DataCardListFragment(getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.TOP, card_data_type);
                    break;
                case 3:
                    f = new DataCardListFragment(getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.NEW, card_data_type);
                    break;
                default:
                    f = new DataCardListFragment(getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.CATEGORY, card_data_type);
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

}
