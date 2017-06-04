package android.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.utils.AndroidAttrs;
import android.view.View;
import android.view.ViewGroup;

import com.lazy.library.logging.Logcat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static android.view.View.NO_ID;

final class UIKitHelper {
    static final String TAG_UIKIT = "UIKit";

    private final ViewGroup mHost;
    private final Context mContext;

    UIKitHelper(ViewGroup mHost) {
        this.mHost = mHost;
        this.mContext = mHost.getContext();

        TypedArray typedArray = this.mContext.getTheme().obtainStyledAttributes(R.style.ViewBlockTheme, new int[]{android.R.attr.background});
        int backgroundColor = typedArray.getColor(0, Color.WHITE);
        typedArray.recycle();
        if (this.mHost.getBackground() == null) {
            this.mHost.setBackgroundColor(backgroundColor);
        }
    }

    public Context getContext() {
        return mContext;
    }

    private Pair<Integer, String> getViewBlockAttrs(AttributeSet attrs) {
        int resourceId = NO_ID;
        String name = null;

        TypedArray array = getContext().obtainStyledAttributes(attrs, AndroidAttrs.ATTRS);
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case AndroidAttrs.ID_INDEX:
                    resourceId = array.getResourceId(index, NO_ID);
                    break;
                case AndroidAttrs.NAME_INDEX:
                    name = array.getString(index);
                    break;
                case AndroidAttrs.BLOCK_CLASS_INDEX:
                    name = array.getString(index);
                    break;
            }
        }
        array.recycle();
        return Pair.create(resourceId, name);
    }

    ViewBlock getParentBlock(UIKitComponent component) {
        ViewBlock parentBlock = null;
        ViewGroup parentContainer = component.getParentContainer();
        if (parentContainer instanceof UIKitComponent) {
            parentBlock = ((UIKitComponent) parentContainer).getViewBlock();
        }
        return parentBlock;
    }

    private ViewBlock getViewBlockByName(View view, String name) {
        ViewBlock viewBlock = null;
        try {
            Class<?> aClass = Class.forName(name);
            Constructor constructor = aClass.getConstructor(View.class);
            viewBlock = (ViewBlock) constructor.newInstance(view);
        } catch (ClassNotFoundException e) {
            Logcat.e().tag(TAG_UIKIT).msg(e.getMessage()).out();
        } catch (NoSuchMethodException e) {
            Logcat.e().tag(TAG_UIKIT).msg(e.getMessage()).out();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewBlock;
    }

    private ViewBlock createViewBlock(View view, int resourceId, String name) {

        if (name == null) {
            return null;
        }

        if (resourceId == NO_ID) {
            Logcat.w().tag(TAG_UIKIT).msg("ViewBlock name ").msg(name).msg(" view no id ").out();
        }

        return getViewBlockByName(view, name);
    }

    public void addViewBlockToViewBlockManager(ViewBlock viewBlock, ViewBlock parentBlock, ViewBlockManager viewBlockManager) {
        if (viewBlock != null) {
            viewBlock.setParent(parentBlock);

            ViewBlockParent parent = viewBlock.getParent();
            if (parent != null) {
                parent.addViewBlock(viewBlock);
            }
            viewBlockManager.add(viewBlock);
        }
    }

    ViewBlock attachViewBlock(UIKitComponent component, AttributeSet attrs) {
        Pair<Integer, String> blockAttrs = getViewBlockAttrs(attrs);

        int resourceId = blockAttrs.first;
        String name = blockAttrs.second;
        ViewBlock parentBlock = getParentBlock(component);

        ViewBlock viewBlock = createViewBlock(component.getContainer(), resourceId, name);
        if (viewBlock != null) {
            addViewBlockToViewBlockManager(viewBlock, parentBlock, component.getViewBlockManager());
        } else {
            viewBlock = parentBlock;
        }
        component.setViewBlock(viewBlock);
        return viewBlock;
    }

    void generateViewBlock(UIKitComponent component, AttributeSet attrs) {
        String tagName = ((XmlResourceParser) attrs).getName();
        if (!AndroidAttrs.UIKIT_TAGS.contains(tagName)) {
            Pair<Integer, String> viewBlockAttrs = getViewBlockAttrs(attrs);

            int resourceId = viewBlockAttrs.first;
            String name = viewBlockAttrs.second;

            if (name != null) {
                if (resourceId == NO_ID) {
                    Logcat.w().tag(TAG_UIKIT).msg("ViewBlock name ").msg(name).msg(" view no id ").out();
                    resourceId = component.getViewDepth();
                }
                component.getViewBlockClassNamesArray().put(resourceId, name);
            }
        }

        if (!"include".equals(tagName)) {
            component.setViewDepth(component.getViewDepth() + 1);
        }
    }

    void onFinishInflateViewBlock(@NonNull UIKitComponent component) {
        SparseArrayCompat<String> viewBlockClassNamesArray = component.getViewBlockClassNamesArray();
        int size = viewBlockClassNamesArray.size();

        boolean isEmpty = (size == 0);

        if (!isEmpty) {
            int mHostChildCount = mHost.getChildCount();
            for (int i = 0; i < mHostChildCount; i++) {
                View childAt = mHost.getChildAt(i);
                if (!(childAt instanceof ViewGroup)) {
                    attachChildViewBlock(component, viewBlockClassNamesArray, i, childAt);
                }
            }
        }

        if (!(component.getActivity() instanceof UIKitActivity)) {
            ViewGroup parent = component.getParentContainer();
            if (parent == null) return;
            if ((parent).getId() == android.R.id.content) {
                ViewBlockManager blockManager = UIKit.getViewBlockManager(component.getActivity());
                SparseArray<ViewBlock> viewBlocks = blockManager.getViewBlocks();
                UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_CREATE_VIEW);
            }
        }

    }

    private void attachChildViewBlock(@NonNull UIKitComponent component, SparseArrayCompat<String>
            childViewBlockClassNamesArray, int index, View childView) {
        int childViewChildAtId = childView.getId();
        int indexOfKey = childViewBlockClassNamesArray.indexOfKey(childViewChildAtId);
        if (indexOfKey < 0) {
            indexOfKey = childViewBlockClassNamesArray.indexOfKey(index);
        }
        if (indexOfKey < 0) {
            return;
        }

        String name = childViewBlockClassNamesArray.valueAt(indexOfKey);

        if (name == null) {
            return;
        }

        ViewBlock parentBlock = component.getViewBlock();
        ViewBlock viewBlock = createViewBlock(childView, childViewChildAtId, name);
        if (viewBlock != null) {
            addViewBlockToViewBlockManager(viewBlock, parentBlock, component.getViewBlockManager());
        }

        childViewBlockClassNamesArray.removeAt(indexOfKey);
    }

}