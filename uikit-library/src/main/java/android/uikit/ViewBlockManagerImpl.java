package android.uikit;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.utils.Preconditions;
import android.view.View;
import android.view.ViewGroup;

import java.util.Stack;

import static android.view.View.NO_ID;

final class ViewBlockManagerImpl extends ViewBlockManager {
    private final SparseArray<ViewBlock> viewBlockSparseArray = new SparseArray<>();
    private final Stack<ViewBlock> mViewBlockStack = new Stack<>();

    int mCurState = ContextViewBlock.INITIALIZING;

    @Override
    public ViewBlockManager attach(@NonNull ViewBlock viewBlock) {
        if (!contains(viewBlock)) {
            performXpViewBlock(viewBlock);
            performAddToParentView(viewBlock);
            add(viewBlock);
        }
        moveToState(viewBlock);
        return this;
    }

    private void performAddToParentView(@NonNull ViewBlock viewBlock) {
        if (viewBlock.getBlockingView().getParent() == null) {
            ViewBlockParent parent = viewBlock.getParent();
            if (parent != null) {
                parent.addViewBlock(viewBlock);
                View blockingView = parent.getViewBlock().getBlockingView();
                if (blockingView instanceof ViewGroup) {
                    addToParentView(viewBlock, ((ViewGroup) blockingView));
                }
            }
        }
    }

    @Override
    public ViewBlockManager detach(@NonNull ViewBlock viewBlock) {
        if (contains(viewBlock)) {
            int childrenCount = viewBlock.getChildrenCount();
            for (int index = childrenCount - 1; index >= 0; index--) {
                ViewBlock viewBlockChildAt = viewBlock.getChildAt(index);
                detach(viewBlockChildAt);
            }
            remove(viewBlock);
        }
        viewBlock.onDestroy();
        viewBlock.moveToState(ContextViewBlock.INITIALIZING);
        return this;
    }

    @Override
    public ViewBlockManager detachView(@NonNull ViewBlock viewBlock) {
        if (contains(viewBlock) && viewBlock.getBlockingView() != null) {
            ViewBlockParent parent = viewBlock.getParent();
            if (parent != null) {
                ViewBlock block = parent.getViewBlock();
                View blockingView = block.getBlockingView();
                if (blockingView instanceof ViewGroup) {
                    ((ViewGroup) blockingView).removeView(viewBlock.getBlockingView());
                }
            }
        }
        return this;
    }

    @Override
    public ViewBlock findViewBlockByKey(int key) {
        return viewBlockSparseArray.get(key);
    }

    @Override
    public boolean contains(@NonNull ViewBlock viewBlock) {
        return (viewBlockSparseArray.indexOfKey(viewBlock.getId()) > 0
                || viewBlockSparseArray.indexOfValue(viewBlock) != -1);
    }

    @Override
    public ViewBlockManager add(@NonNull ViewBlock viewBlock) {
        Preconditions.checkNotNull(viewBlock);
        int id = viewBlock.getId();
        if (id != NO_ID) {
            viewBlockSparseArray.put(id, viewBlock);
        } else {
            viewBlockSparseArray.put(viewBlock.hashCode(), viewBlock);
        }
        return this;
    }

    @Override
    ViewBlockManager addToParentView(@NonNull ViewBlock viewBlock, @NonNull final ViewGroup container) {
        if (viewBlock.mBlockingView == null) {
            return this;
        }
        container.addView(viewBlock.mBlockingView);
        return this;
    }

    private void performXpViewBlock(@NonNull ViewBlock viewBlock) {
        if (viewBlock.mBlockingView == null) {
            if (viewBlock instanceof XpViewBlock) {
                ((XpViewBlock) viewBlock).createView(viewBlock.getContext());
            }
        }
    }

    @Override
    public ViewBlockManager remove(@NonNull ViewBlock viewBlock) {
        Preconditions.checkNotNull(viewBlock);
        viewBlockSparseArray.remove(viewBlock.getId() == NO_ID ? viewBlock.hashCode() : viewBlock.getId());
        return this;
    }

    public SparseArray<ViewBlock> getViewBlocks() {
        return viewBlockSparseArray;
    }

    public Stack<ViewBlock> getViewBlockStack() {
        return mViewBlockStack;
    }

    @Override
    void moveToState(int newState) {
        this.mCurState = newState;
    }

    void moveToState(@NonNull ViewBlock viewBlock) {
        int viewBlockState = viewBlock.getState();
        if (viewBlockState == mCurState) {
            return;
        }
        if (viewBlockState < mCurState) {
            if (mCurState >= ContextViewBlock.CREATED && viewBlockState < ContextViewBlock.CREATED) {
                viewBlock.onCreate(null);
            } else if (mCurState >= ContextViewBlock.CREATED_VIEW && viewBlockState < ContextViewBlock.CREATED_VIEW) {
                viewBlock.onCreateView();
            } else if (mCurState >= ContextViewBlock.STARTED && viewBlockState < ContextViewBlock.STARTED) {
                viewBlock.onStart();
            } else if (mCurState >= ContextViewBlock.RESUMED && viewBlockState < ContextViewBlock.RESUMED) {
                viewBlock.onResume();
            } else if (mCurState >= ContextViewBlock.PAUSE && viewBlockState < ContextViewBlock.PAUSE) {
                viewBlock.onPause();
            } else if (mCurState >= ContextViewBlock.STOP && viewBlockState < ContextViewBlock.STOP) {
                viewBlock.onStop();
            } else if (mCurState >= ContextViewBlock.DESTROY && viewBlockState < ContextViewBlock.DESTROY) {
                viewBlock.onDestroy();
            }
        }
        moveToState(viewBlock);
    }
}
