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
import vn.appsmobi.ui.SlidingTabLayout;
import vn.appsmobi.utils.Constants;
//Fragment của 4 cái: danh mục, nổi bật, top, new
public class DataCardParentFragment2 extends Fragment {

    private View mHeaderView;
    private View mToolbarView;
    private ParallaxViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private int card_data_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Đọc file fragment_datacard_parent.xml để vẽ ra giao diện người dùng.
        View view = inflater.inflate(R.layout.fragment_datacard_parent, container, false);
        //Lấy giá trị
        card_data_type = getArguments().getInt(Constants.Intent.CARD_DATA_TYPE);
        //Khởi tạo tab
        mPagerAdapter = new NavigationAdapter(getChildFragmentManager());
        //Khởi tạo thanh kéo cho viewpager
        mPager = (ParallaxViewPager) view.findViewById(R.id.pager);
        //set data cho viewpager
        mPager.setAdapter(mPagerAdapter);

        //giữ lại data cho 3 fragment
        mPager.setOffscreenPageLimit(3);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        //Thay đổi màu khi tab được chọn
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.main_color));
        //Phân bố đều chiều rộng của các Textview trong viewpager
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

        mHeaderView = getActivity().findViewById(R.id.llActionBar);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.tool_bar_top_padding));
        mToolbarView = getActivity().findViewById(R.id.myToolbar);
        //set background cho thanh toolbar
        mToolbarView.setBackgroundColor(getActivity().getResources().getColor(R.color.main_color));

        return view;
    }

    // TODO: Rename and change types and number of parameters
    public static DataCardParentFragment2 newInstance(int fragment_type) {
        DataCardParentFragment2 fragment = new DataCardParentFragment2();
        Bundle args = new Bundle();
        args.putInt(Constants.Intent.CARD_DATA_TYPE, fragment_type);
        fragment.setArguments(args);
        return fragment;
    }


    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private class NavigationAdapter extends CacheFragmentStatePagerAdapter {
        //Đặt tên cho các tab
        private final String[] TITLES = new String[]{"DANH MỤC", "NỔI BẬT", "TOP CÀI ĐẶT", "MỚI NHẤT"};

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f;
            //Chia 5
            final int pattern = position % 5;
            switch (pattern) {
                case 0:
                    //Tab 1 hiện fragment danh mục
                    f = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.CATEGORY, card_data_type);
                    break;
                case 1:
                    //Tab 2 hiện fragment nổi bật
                    f = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.HOT, card_data_type);
                    break;
                case 2:
                    //Tab 3 hiện fragment top
                    f = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.TOP, card_data_type);
                    break;
                case 3:
                    //Tab 4 hiện fragment mới nhất
                    f = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.NEW, card_data_type);
                    break;
                default:
                    //Mặc định hiện fragment danh mục
                    f = new DataCardListFragment(getActivity().getResources().getColor(R.color.md_white_1000)).newInstance(Constants.TAB_TYPE.CATEGORY, card_data_type);
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
