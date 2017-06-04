package android.uikit;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class UIKitFrameLayout extends FrameLayout implements UIKitComponent {
    private static final String TAG = "UIKitFrameLayout";
    private ViewBlockManager viewBlockManager = null;
    private ViewBlock mViewBlock;
    private SparseArrayCompat<String> mViewBlockClassNamesArray = new SparseArrayCompat<>();
    private int mDepth = 0;
    private UIKitHelper mUiKitHelper = new UIKitHelper(this);
    private ViewGroup mParent;
    private boolean disallowIntercept;

    public UIKitFrameLayout(Context context) {
        super(context);
    }

    public UIKitFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIKitFrameLayout attachViewBlock(AttributeSet attrs) {
        mUiKitHelper.attachViewBlock(this, attrs);
        return this;
    }

    public UIKitFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIKitFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        mUiKitHelper.generateViewBlock(this, attrs);
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mUiKitHelper.onFinishInflateViewBlock(this);
    }

    @Override
    public ViewGroup getContainer() {
        return this;
    }

    @Override
    public ViewGroup getParentContainer() {
        return mParent;
    }

    @Override
    public boolean isUIKitComponent() {
        return getContext() instanceof UIKitActivity;
    }

    @Override
    public ViewBlockManager getViewBlockManager() {
        return viewBlockManager;
    }

    public UIKitFrameLayout setViewBlockManager(ViewBlockManager viewBlockManager) {
        this.viewBlockManager = viewBlockManager;
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public Activity getActivity() {
        return (Activity) getContext();
    }

    @Override
    public ViewBlock getViewBlock() {
        return mViewBlock;
    }

    @Override
    public ViewBlock setViewBlock(ViewBlock viewBlock) {
        this.mViewBlock = viewBlock;
        return this.mViewBlock;
    }

    @Override
    public SparseArrayCompat<String> getViewBlockClassNamesArray() {
        return mViewBlockClassNamesArray;
    }

    @Override
    public void setViewDepth(int mDepth) {
        this.mDepth = mDepth;
    }

    @Override
    public int getViewDepth() {
        return this.mDepth;
    }

    @Override
    public UIKitFrameLayout setParent(ViewGroup mParent) {
        this.mParent = mParent;
        return this;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (disallowIntercept) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void setDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.disallowIntercept = disallowIntercept;
    }
}
