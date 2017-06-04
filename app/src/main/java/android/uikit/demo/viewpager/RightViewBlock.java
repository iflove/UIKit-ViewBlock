package android.uikit.demo.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.uikit.XpViewBlock;
import android.uikit.demo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lazy on 2017/6/3.
 */

public class RightViewBlock extends XpViewBlock {

    public RightViewBlock(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.block_right, container);
    }
}
