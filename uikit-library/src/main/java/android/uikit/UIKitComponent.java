package android.uikit;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

public interface UIKitComponent {

    Context getContext();

    Activity getActivity();

    ViewGroup getContainer();

    UIKitComponent setParent(ViewGroup mParent);

    ViewGroup getParentContainer();

    boolean isUIKitComponent();

    UIKitComponent setViewBlockManager(ViewBlockManager viewBlockManager);

    ViewBlockManager getViewBlockManager();

    ViewBlock getViewBlock();

    ViewBlock setViewBlock(ViewBlock viewBlock);

    SparseArrayCompat<String> getViewBlockClassNamesArray();

    void setViewDepth(int mDepth);

    int getViewDepth();

    UIKitComponent attachViewBlock(AttributeSet attrs);

    void setDisallowInterceptTouchEvent(boolean disallowIntercept);
}
