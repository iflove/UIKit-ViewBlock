package android.uikit.demo.viewblock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.uikit.ViewBlock;
import android.uikit.ViewBlockPagerAdapter;
import android.uikit.XpViewBlock;
import android.uikit.demo.R;
import android.uikit.demo.viewpager.CenterViewBlock;
import android.uikit.demo.viewpager.LeftViewBlock;
import android.uikit.demo.viewpager.RightViewBlock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazy on 2017/4/23.
 */

public class TabBlock extends XpViewBlock implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;


    public TabBlock(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.block_tab, container, false);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
    }

    @Override
    public void findViews() {
        super.findViews();
        viewPager = bindViewById(R.id.viewPager);
        tabLayout = bindViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
        final List<ViewBlock> viewBlockList = new ArrayList<>();
        viewBlockList.add(new LeftViewBlock(getContext()));
        viewBlockList.add(new CenterViewBlock(getContext()));
        viewBlockList.add(new RightViewBlock(getContext()));
        viewPager.setAdapter(new ViewBlockPagerAdapter(getViewBlockManager()) {
            @Override
            public ViewBlock getItem(int position) {
                return viewBlockList.get(position);
            }

            @Override
            public int getCount() {
                return viewBlockList.size();
            }
        });
    }

    @Override
    public void setViewListener() {
        super.setViewListener();
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabLayout.setScrollPosition(position, 0, true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(), false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
