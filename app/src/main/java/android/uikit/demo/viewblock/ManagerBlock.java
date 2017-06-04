package android.uikit.demo.viewblock;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.uikit.ViewBlock;
import android.uikit.XpViewBlock;
import android.uikit.demo.R;
import android.uikit.demo.utils.DensityUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

/**
 * Created by lazy on 2017/6/3.
 */

public class ManagerBlock extends XpViewBlock implements View.OnClickListener {
    FloatingActionButton floatingActionButton;
    ViewBlock lastViewBlock;
    int[] colors = new int[]{
            Color.rgb(80, 172, 25),
            Color.rgb(254, 79, 0),
            Color.rgb(234, 231, 14),
            Color.rgb(89, 89, 89),
            Color.rgb(228, 154, 65),
            getResources().getColor(R.color.colorPrimary),
            getResources().getColor(R.color.colorAccent),
    };

    public ManagerBlock(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.block_manager, container, false);
    }

    @Override
    public void findViews() {
        super.findViews();
        floatingActionButton = bindViewById(R.id.fab);
    }

    @Override
    public void setViewListener() {
        super.setViewListener();
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void processExtraData() {
        super.processExtraData();
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();

        int screenWidth = DensityUtil.getInstance().getScreenWidth();
        int screenHeight = DensityUtil.getInstance().getScreenHeight();

        int w = random.nextInt(screenWidth / 2);


        View blockView = new View(getContext());
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(Math.max(100, w), Math.max(100, w));
        marginLayoutParams.leftMargin = random.nextInt(screenWidth / 2);
        marginLayoutParams.topMargin = random.nextInt(screenHeight / 2);
        blockView.setLayoutParams(marginLayoutParams);
        blockView.setBackgroundColor(colors[random.nextInt(colors.length)]);
        final SquareViewBlock squareViewBlock = new SquareViewBlock(blockView);
        squareViewBlock.setParent(this);
        squareViewBlock.getBlockingView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewBlockManager().detachView(squareViewBlock);
                getViewBlockManager().detach(squareViewBlock);
            }
        });
        getViewBlockManager().attach(squareViewBlock);
        lastViewBlock = squareViewBlock;


    }
}
