package android.uikit;

/**
 * Created by lazy on 2017/4/21.
 */

public interface ViewBlockParent {
    ViewBlockParent getParent();

    int getChildrenCount();

    void addViewBlock(ViewBlock child);

    void addViewBlock(ViewBlock child, int index);

    ViewBlock getChildAt(int index);

    ViewBlock getViewBlock();
}
