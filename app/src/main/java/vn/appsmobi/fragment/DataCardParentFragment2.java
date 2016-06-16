package vn.appsmobi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import cn.yangbingqiang.android.parallaxviewpager.ParallaxViewPager;
import vn.appsmobi.R;
import vn.appsmobi.activity.HomeActivity;
import vn.appsmobi.ui.SlidingTabLayout;
import vn.appsmobi.utils.Constants;

public class DataCardParentFragment2 extends Fragment {

    private View view;
    private ParallaxViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private int card_data_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_datacard_parent, container, false);

        initValues();
        initView();

        return view;
    }

    private void initValues() {
        card_data_type = getArguments().getInt(Constants.Intent.CARD_DATA_TYPE);
    }

    private void initView() {
        mPagerAdapter = new NavigationAdapter(getChildFragmentManager());
        mPager = (ParallaxViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        //giữ lại data cho 3 fragment
        mPager.setOffscreenPageLimit(3);

        initViewSlidingTab();

        ViewCompat.setElevation(HomeActivity.llActionBar, getResources().getDimension(R.dimen.tool_bar_top_padding));
        HomeActivity.myToolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.main_color));
    }

    private void initViewSlidingTab() {
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        //Thay đổi màu khi tab được chọn
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.main_color));
        //Phân bố đều chiều rộng của các Textview trong viewpager
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);
    }

    // TODO: Rename and change types and number of parameters
    public static DataCardParentFragment2 newInstance(int fragment_type) {
        DataCardParentFragment2 fragment = new DataCardParentFragment2();
        Bundle args = new Bundle();
        args.putInt(Constants.Intent.CARD_DATA_TYPE, fragment_type);
        fragment.setArguments(args);
        return fragment;
    }
//    private Fragment getCurrentFragment() {
//        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
//    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private class NavigationAdapter extends CacheFragmentStatePagerAdapter {
        //Đặt tên cho các tab
        private final String[] TITLES = new String[]{getResources().getString(R.string.card_category), getResources().getString(R.string.card_hot), getResources().getString(R.string.card_top), getResources().getString(R.string.card_new)};

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment fragment;
            final int pattern = position % 5;
            switch (pattern) {
                case 0:
                    fragment = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.CATEGORY, card_data_type);
                    break;
                case 1:
                    fragment = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.HOT, card_data_type);
                    break;
                case 2:
                    fragment = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.TOP, card_data_type);
                    break;
                case 3:
                    fragment = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.NEW, card_data_type);
                    break;
                default:
                    fragment = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.CATEGORY, card_data_type);
                    break;
            }
            return fragment;
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
