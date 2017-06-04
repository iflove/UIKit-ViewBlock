package android.uikit;

import android.app.Activity;
import android.content.Context;
import android.content.UIKitIntent;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.Stack;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class ViewBlock extends ContextViewBlock implements ViewBlockParent,
        ViewBlockAnimation, BaseViewInterface {
    private static final String TAG = "ViewBlock";
    private ViewBlockParent mParent;
    UIKitIntent mUiKitIntent = new UIKitIntent(this);

    private SparseArrayCompat<ViewBlock> mChildren = new SparseArrayCompat<>(12);
    private int mChildrenCount;

    @IntDef({VISIBLE, View.INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    int mEnterAnim = R.anim.push_up_in;
    int mExitAnim = R.anim.push_up_out;
    int mPopEnterAnim = R.anim.push_down_in;
    int mPopExitAnim = R.anim.push_down_out;

    ViewBlock(Context mContext) {
        super(mContext);
    }

    public ViewBlock(View mBlockingView) {
        super(mBlockingView);
    }

    public ViewBlock(@NonNull Activity activity, @IdRes int resId) {
        super(activity, resId);
    }

    public ViewBlock(@NonNull Context context, @LayoutRes int layoutResId) {
        super(context, layoutResId);
    }

    public ViewBlock(@NonNull Context context, @LayoutRes int layoutResId,
                     @Nullable ViewGroup root, boolean attachToRoot) {
        super(context, layoutResId, root, attachToRoot);
    }

    public void setParent(ViewBlockParent mParent) {
        this.mParent = mParent;
    }

    @Override
    public ViewBlockParent getParent() {
        return mParent;
    }

    @Override
    public void addViewBlock(ViewBlock child) {
        addViewBlock(child, -1);
    }

    @Override
    public void addViewBlock(ViewBlock child, int index) {
        if (index < 0) {
            index = mChildrenCount;
        }
        mChildren.put(index, child);
        mChildrenCount++;
    }

    void removeChildViewBlock(int index) {
        mChildren.remove(index);
    }

    SparseArrayCompat<ViewBlock> getChildren() {
        return mChildren;
    }

    @Override
    public ViewBlock getChildAt(int index) {
        if (index < 0 || index >= mChildrenCount) {
            return null;
        }
        return mChildren.get(index);
    }

    @Override
    public int getChildrenCount() {
        return mChildrenCount;
    }

    @Nullable
    public final View findViewById(@IdRes int id) {
        if (id < 0 || mBlockingView == null) {
            return null;
        }
        return mBlockingView.findViewById(id);
    }

    protected <T> T bindViewById(@IdRes int id) {
        //noinspection unchecked
        if (id < 0 || mBlockingView == null) {
            return null;
        }
        //noinspection unchecked
        return (T) mBlockingView.findViewById(id);
    }

    protected <T> T bindViewById(@NonNull View view, @IdRes int id) {
        //noinspection unchecked
        if (id < 0 || view == null) {
            return null;
        }
        //noinspection unchecked
        return (T) view.findViewById(id);
    }

    public void setVisibility(@Visibility int visibility) {
        mBlockingView.setVisibility(visibility);
    }

    public ViewBlockManager getViewBlockManager() {
        if (getActivity() instanceof UIKitActivity) {
            UIKitActivity uiKitActivity = (UIKitActivity) getContext();
            return uiKitActivity.getViewBlockManager();
        }
        if (mBlockingView instanceof UIKitComponent) {
            return ((UIKitComponent) mBlockingView).getViewBlockManager();
        }
        return null;
    }

    @Override
    protected void onAttachToActivity(@NonNull Activity activity) {

    }

    @Override
    protected void onDetachToActivity(@NonNull Activity activity) {

    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        findViews();
        setViewListener();
        processExtraData();
    }

    @Override
    protected void onFinishInflateView() {

    }

    protected void startViewBlock(UIKitIntent uiKitIntent) {
        if (mPopExitAnim != 0) {
            uiKitIntent.fromViewBlock.getBlockingView().startAnimation(AnimationUtils.loadAnimation(getContext(), mPopExitAnim));
        }
        uiKitIntent.toViewBlock.setUIKitIntent(uiKitIntent);
        startViewBlock(uiKitIntent.toViewBlock);
    }


    private void startViewBlock(ViewBlock viewBlock) {
        if (mBlockingView instanceof ViewGroup) {
            getViewBlockManager().attach(viewBlock);

            ViewGroup viewGroup = (ViewGroup) this.mBlockingView;
            if (viewGroup instanceof UIKitComponent) {
                ((UIKitComponent) viewGroup).setDisallowInterceptTouchEvent(true);
            }

            View mBlockingView1 = viewBlock.mBlockingView;
            if (mBlockingView1.getParent() == null) {
                ((ViewGroup) viewGroup.getParent()).addView(mBlockingView1);
            }

            if (mEnterAnim != 0) {
                mBlockingView1.startAnimation(AnimationUtils.loadAnimation(getContext(), mEnterAnim));
            }
            getViewStack().push(viewBlock);

        }
    }

    private void startViewBlock(Class<? extends ViewBlock> clazz) {
        try {
            Constructor<? extends ViewBlock> constructor = clazz.getConstructor(Context.class);
            ViewBlock viewBlock = constructor.newInstance(getContext());
            startViewBlock(viewBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Stack<ViewBlock> getViewStack() {
        return getViewBlockManager().getViewBlockStack();
    }

    void popViewBlock(final ViewBlock viewBlock) {
        if (mExitAnim != 0) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), mExitAnim);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    removeToViewBlock(viewBlock);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            viewBlock.mBlockingView.startAnimation(animation);
        } else {
            removeToViewBlock(viewBlock);
        }
        if (mPopEnterAnim != 0) {
            viewBlock.getUIKitIntent().getFromViewBlock().mBlockingView.startAnimation(AnimationUtils.loadAnimation(getContext(), mPopEnterAnim));
        }
    }

    private void removeToViewBlock(final ViewBlock viewBlock) {
        viewBlock.mBlockingView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup viewGroup = (ViewGroup) ViewBlock.this.mBlockingView;

                View fromView = ViewBlock.this.getUIKitIntent().getFromViewBlock().mBlockingView;
                if (fromView instanceof UIKitComponent) {
                    ((UIKitComponent) fromView).setDisallowInterceptTouchEvent(false);
                }
                ((ViewGroup) viewGroup.getParent()).removeView(viewBlock.mBlockingView);
                getViewBlockManager().detach(viewBlock);
            }
        });
    }

    @Override
    public ViewBlock setCustomAnimations(@AnimRes int enter, @AnimRes int exit) {
        return setCustomAnimations(enter, exit, 0, 0);
    }

    @Override
    public ViewBlock setCustomAnimations(@AnimRes int enter, @AnimRes int exit,
                                         @AnimRes int popEnter, @AnimRes int popExit) {
        mEnterAnim = enter;
        mExitAnim = exit;
        mPopEnterAnim = popEnter;
        mPopExitAnim = popExit;
        return this;
    }

    @Override
    public UIKitIntent getUIKitIntent() {
        return this.mUiKitIntent;
    }

    @Override
    public void setUIKitIntent(UIKitIntent mUiKitIntent) {
        this.mUiKitIntent = mUiKitIntent;
    }

    @Override
    public final ViewBlock getViewBlock() {
        return this;
    }

    @Override
    public void findViews() {

    }

    @Override
    public void setViewListener() {

    }

    @Override
    public void processExtraData() {

    }
}
