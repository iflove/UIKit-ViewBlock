package android.uikit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.utils.Preconditions;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Stack;

import static android.utils.AndroidAttrs.ANDROID_LAYOUT_TAG;


public class UIKitActivity extends AppCompatActivity {
    private static final String TAG = "UIKitActivity";
    private static final String LAYOUT_LINEAR_LAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAME_LAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVE_LAYOUT = "RelativeLayout";


    static final String ON_CREATE_VIEW = "onCreateView";
    static final String ON_CREATE = "onCreate";
    static final String ON_START = "onStart";
    static final String ON_NEW_INTENT = "onNewIntent";
    static final String ON_RESTART = "onRestart";
    static final String ON_RESUME = "onResume";
    static final String ON_STOP = "onStop";
    static final String ON_PAUSE = "onPause";
    static final String ON_DESTROY = "onDestroy";
    static final String ON_RESTORE_INSTANCE_STATE = "onRestoreInstanceState";
    static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";
    static final String ON_ACTIVITY_RESULT = "onActivityResult";
    static final String ON_BACK_PRESSED = "onBackPressed";

    final ViewBlockManager mViewBlockManager = new ViewBlockManagerImpl();
    private boolean enterContentView;

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();
    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    static final Object[] sConstructorArgs = new Object[2];


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (!enterContentView && Preconditions.nonNull(parent) && parent.getId() == android.R.id.content) {
            enterContentView = true;
        }
        if (enterContentView) {
            View view = onCreateUIKitView(parent, name, context, attrs, mViewBlockManager);

            if (view != null) return view;
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    static View onCreateUIKitView(View parent, String name, Context context, AttributeSet attrs,
                                  final ViewBlockManager mViewBlockManager) {
        View view = null;

        if (ANDROID_LAYOUT_TAG.contains(name)) {
            if (name.equals(LAYOUT_FRAME_LAYOUT)) {
                view = new UIKitFrameLayout(context, attrs).setParent(((ViewGroup) parent))
                        .setViewBlockManager(mViewBlockManager).attachViewBlock(attrs);
            }

            if (name.equals(LAYOUT_LINEAR_LAYOUT)) {
                view = new UIKitLinearLayout(context, attrs).setParent(((ViewGroup) parent))
                        .setViewBlockManager(mViewBlockManager).attachViewBlock(attrs);
            }

            if (name.equals(LAYOUT_RELATIVE_LAYOUT)) {
                view = new UIKitRelativeLayout(context, attrs).setParent(((ViewGroup) parent))
                        .setViewBlockManager(mViewBlockManager).attachViewBlock(attrs);
            }
        } else {
            try {
                if (-1 != name.indexOf('.')) {
                    sConstructorArgs[0] = context;
                    view = createView(context, name, null, attrs);
                    if (view instanceof UIKitComponent) {
                        ((UIKitComponent) view).setParent(((ViewGroup) parent))
                                .setViewBlockManager(mViewBlockManager)
                                .attachViewBlock(attrs);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private static View createView(Context context, String name, String prefix, AttributeSet attrs)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Class<? extends View> clazz = null;
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor == null) {
            clazz = context.getClassLoader().loadClass(
                    prefix != null ? (prefix + name) : name).asSubclass(View.class);
            constructor = clazz.getConstructor(mConstructorSignature);
            constructor.setAccessible(true);
            sConstructorMap.put(name, constructor);
        }
        Object[] args = sConstructorArgs;
        args[1] = attrs;

        return constructor.newInstance(args);
    }

    public ViewBlockManager getViewBlockManager() {
        return mViewBlockManager;
    }

    private SparseArray<ViewBlock> getViewBlocks() {
        return mViewBlockManager.getViewBlocks();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mViewBlockManager.moveToState(ContextViewBlock.CREATED_VIEW);
        dispatch(ON_CREATE_VIEW);
    }

    public void setContentView(@NonNull ViewBlock viewBlock) {
        super.setContentView(viewBlock.mBlockingView);
        mViewBlockManager.moveToState(ContextViewBlock.CREATED_VIEW);
        getViewBlockManager().add(viewBlock);
        dispatch(ON_CREATE_VIEW);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mViewBlockManager.moveToState(ContextViewBlock.CREATED_VIEW);
        dispatch(ON_CREATE_VIEW);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mViewBlockManager.moveToState(ContextViewBlock.CREATED_VIEW);
        dispatch(ON_CREATE_VIEW);
    }

    private void setEnterContentView(boolean enterContentView) {
        this.enterContentView = enterContentView;
    }

    public void setIsUsedAndroidLayout(boolean isUsedAndroidLayout) {
        this.enterContentView = isUsedAndroidLayout;
    }

    static boolean dispatch(@NonNull final SparseArray<ViewBlock> viewBlocks, @NonNull final String method, final Object... args) {
        for (int i = 0, size = viewBlocks.size(); i < size; i++) {
            ViewBlock viewBlock = viewBlocks.valueAt(i);
            switch (method) {
                case ON_CREATE_VIEW: {
                    viewBlock.onCreateView();
                }
                break;
                case ON_CREATE: {
                    viewBlock.onCreate((Bundle) args[0]);
                }
                break;
                case ON_NEW_INTENT: {
                    viewBlock.onNewIntent((Intent) args[0]);
                }
                break;
                case ON_START: {
                    viewBlock.onStart();
                }
                break;
                case ON_RESTART: {
                    viewBlock.onRestart();
                }
                break;
                case ON_PAUSE: {
                    viewBlock.onPause();
                }
                break;
                case ON_RESUME: {
                    viewBlock.onResume();
                }
                break;
                case ON_STOP: {
                    viewBlock.onStop();
                }
                break;
                case ON_DESTROY: {
                    viewBlock.onDestroy();
                }
                break;
                case ON_ACTIVITY_RESULT: {
                    viewBlock.onActivityResult((Integer) args[0], (Integer) args[1], (Intent) args[2]);
                }
                case ON_BACK_PRESSED: {
                    viewBlock.onBackPressed();
                }
                break;
                case ON_RESTORE_INSTANCE_STATE: {
                    viewBlock.onRestoreInstanceState(((Bundle) args[0]));
                }
                break;
                case ON_SAVE_INSTANCE_STATE: {
                    viewBlock.onSaveInstanceState(((Bundle) args[0]));
                }
                break;
                default:
                    break;
            }

        }
        return false;
    }

    private boolean dispatch(final String method, final Object... args) {
        SparseArray<ViewBlock> viewBlocks = getViewBlocks();
        return dispatch(viewBlocks, method, args);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBlockManager.moveToState(ContextViewBlock.CREATED);
        dispatch(ON_CREATE, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dispatch(ON_SAVE_INSTANCE_STATE, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dispatch(ON_RESTORE_INSTANCE_STATE, savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dispatch(ON_NEW_INTENT, intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mViewBlockManager.moveToState(ContextViewBlock.STARTED);
        dispatch(ON_RESTART);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewBlockManager.moveToState(ContextViewBlock.STARTED);
        dispatch(ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewBlockManager.moveToState(ContextViewBlock.RESUMED);
        dispatch(ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewBlockManager.moveToState(ContextViewBlock.PAUSE);
        dispatch(ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewBlockManager.moveToState(ContextViewBlock.STOPPED);
        dispatch(ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewBlockManager.moveToState(ContextViewBlock.DESTROY);
        dispatch(ON_DESTROY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dispatch(ON_ACTIVITY_RESULT, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dispatch(ON_BACK_PRESSED);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Stack<ViewBlock> viewBlockStack = getViewBlockManager().getViewBlockStack();
            if (!viewBlockStack.isEmpty()) {
                ViewBlock block = viewBlockStack.pop();
                if (block != null) {
                    block.popViewBlock(block);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
