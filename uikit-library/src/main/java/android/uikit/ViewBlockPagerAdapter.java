package android.uikit;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lazy on 2017/4/24.
 */

public abstract class ViewBlockPagerAdapter extends PagerAdapter {

    private final ViewBlockManager viewBlockManager;

    public ViewBlockPagerAdapter(@NonNull ViewBlockManager viewBlockManager) {
        this.viewBlockManager = viewBlockManager;
    }


    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    public abstract ViewBlock getItem(final int position);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((ViewBlock) object).getBlockingView() == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewBlock viewBlock = getItem(position);
        this.viewBlockManager.attach(viewBlock);
        container.addView(viewBlock.getBlockingView());
        return viewBlock;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewBlock viewBlock = (ViewBlock) object;
        this.viewBlockManager.detach(viewBlock);
        container.removeView(viewBlock.getBlockingView());
    }

    private static int makeId(int viewId, int id) {
        return viewId + id + 10000;
    }
}
