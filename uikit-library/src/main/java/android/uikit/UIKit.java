package android.uikit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by lazy on 2017/5/7.
 */
// TODO: 2017/5/16 扩展
public class UIKit {
    private static boolean registerActivity = false;


    public static void inject(@NonNull final Activity activity) {
        if (activity instanceof UIKitActivity) {
            return;
        }

        View rootView = activity.findViewById(android.R.id.content);
        final ViewBlockManager mViewBlockManager = new ViewBlockManagerImpl();
        rootView.setTag(R.id.ViewBlockManager, mViewBlockManager);

        if (activity.getLayoutInflater().getFactory2() == null) {
            activity.getLayoutInflater().setFactory2(new LayoutInflater.Factory2() {
                @Override
                public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                    View view = UIKitActivity.onCreateUIKitView(parent, name, context, attrs, mViewBlockManager);
                    if (view != null) return view;
                    return activity.onCreateView(parent, name, activity, attrs);
                }

                @Override
                public View onCreateView(String name, Context context, AttributeSet attrs) {
                    return activity.onCreateView(name, activity, attrs);
                }
            });
        }

        registerActivityLifecycleCallbacks(activity);
    }

    private static void registerActivityLifecycleCallbacks(@NonNull Activity activity) {
        if (!registerActivity) {
            registerActivity = true;
            if (Build.VERSION.SDK_INT < 14) {
                return;
            }
            Application application = activity.getApplication();
            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_CREATE, savedInstanceState);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_START);
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_RESUME);
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_PAUSE);
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_STOP);
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_SAVE_INSTANCE_STATE, outState);
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    ViewBlockManager viewBlockManager = getViewBlockManager(activity);
                    if (viewBlockManager == null) return;
                    SparseArray<ViewBlock> viewBlocks = viewBlockManager.getViewBlocks();
                    UIKitActivity.dispatch(viewBlocks, UIKitActivity.ON_DESTROY);
                }
            });
        }
    }

    public static ViewBlockManager getViewBlockManager(@NonNull final Activity activity) {
        View rootView = activity.findViewById(android.R.id.content);
        return (ViewBlockManager) rootView.getTag(R.id.ViewBlockManager);
    }

}