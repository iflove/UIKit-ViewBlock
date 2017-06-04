package android.uikit;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.Stack;

public abstract class ViewBlockManager {

    public abstract ViewBlockManager attach(@NonNull final ViewBlock viewBlock);

    public abstract ViewBlockManager detach(@NonNull final ViewBlock viewBlock);

    public abstract ViewBlockManager detachView(@NonNull final ViewBlock viewBlock);

    public abstract ViewBlock findViewBlockByKey(int key);

    public abstract boolean contains(@NonNull final ViewBlock viewBlock);

    abstract ViewBlockManager add(@NonNull final ViewBlock viewBlock);

    abstract ViewBlockManager remove(@NonNull final ViewBlock viewBlock);

    abstract ViewBlockManager addToParentView(@NonNull final ViewBlock viewBlock, @NonNull final ViewGroup container);

    abstract SparseArray<ViewBlock> getViewBlocks();

    abstract Stack<ViewBlock> getViewBlockStack();

    abstract void moveToState(int newState);
}

