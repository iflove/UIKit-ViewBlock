package android.uikit.demo.viewblock;

import android.support.v4.view.ViewPager;
import android.uikit.ViewBlock;
import android.uikit.ViewBlockPagerAdapter;
import android.uikit.demo.R;
import android.uikit.demo.banner.MeiZiViewBlock;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainBlock extends ViewBlock {
    private TextView mainBlockTextView;
    private ViewPager bannerViewPager;

    public MainBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        bannerViewPager = bindViewById(R.id.bannerViewPager);
    }

    @Override
    protected void onFinishInflateView() {
        super.onFinishInflateView();
        mainBlockTextView = bindViewById(R.id.mainBlockTextView);
        mainBlockTextView.setText(mainBlockTextView.getText() + ": onFinishInflateView Call");
    }

    @Override
    public void onStart() {
        super.onStart();

        final List<ViewBlock> viewBlockList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MeiZiViewBlock meiZiViewBlock = new MeiZiViewBlock(getContext());
            viewBlockList.add(meiZiViewBlock);
        }


        bannerViewPager.setAdapter(new ViewBlockPagerAdapter(getViewBlockManager()) {
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
}
