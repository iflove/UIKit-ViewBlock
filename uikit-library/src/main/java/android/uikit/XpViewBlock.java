package android.uikit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lazy on 2017/5/16.
 */

public abstract class XpViewBlock extends ViewBlock {

    public XpViewBlock(Context context) {
        super(context);
    }

    private XpViewBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
    }

    void createView(Context context) {
        mBlockingView = onCreateView(LayoutInflater.from(context), ((ViewGroup) getUIKitIntent().fromViewBlock.getBlockingView()));
        mBlockingViewId = mBlockingView.getId();
    }

    @NonNull
    protected abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

}
