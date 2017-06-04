package android.uikit.demo.viewblock;

import android.uikit.ViewBlock;
import android.uikit.demo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Stack;

/**
 * Created by lazy on 2017/4/23.
 */

public class NavBarBlock extends ViewBlock implements View.OnClickListener {
    private static final String TAG = "NavBarBlock";

    public NavBarBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        ViewGroup blockingView = castBlockingView();
        int childCount = blockingView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = blockingView.getChildAt(i);
            childAt.setTag(i);
            childAt.setOnClickListener(this);
        }
    }

    private Stack<View> viewStack = new Stack<>();

    @Override
    public void onClick(View v) {
        Integer tag = (Integer) v.getTag();
        switch (tag) {
            case 0: {
                finishActivity();
            }
            break;
            case 1: {
                ViewGroup viewGroup = ((ViewGroup) getActivity().findViewById(R.id.main));
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.block_meizi, viewGroup, false);
                viewGroup.addView(inflate);
                inflate.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_up_in));
                viewStack.push(inflate);
            }
            break;
            case 2: {
                final ViewGroup viewGroup = ((ViewGroup) getActivity().findViewById(R.id.main));
                if (!viewStack.empty()) {
                    final View pop = viewStack.pop();
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_out);
                    pop.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            viewGroup.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewGroup.removeView(pop);
                                }
                            });

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
