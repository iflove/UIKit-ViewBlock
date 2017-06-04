package android.uikit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.UIKitIntent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.utils.Preconditions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lazy on 2017/4/23.
 */

abstract class ContextViewBlock implements ActivityLifeCycle {
    protected final String TAG = this.getClass().getCanonicalName();
    protected final Activity mActivity;
    protected final Context mContext;

    View mBlockingView;
    @IdRes
    int mBlockingViewId = View.NO_ID;

    static final int INITIALIZING = 0;     // Not yet created.
    static final int CREATED = 1; // The activity has finished its creation.
    static final int CREATED_VIEW = 2;          // Created.
    static final int STARTED = 3;          // Created and started, not resumed.
    static final int RESUMED = 4;          // Created started and resumed.
    static final int STOPPED = 5;          // Fully created, not started.
    static final int PAUSE = 6;          // Created started and resumed.
    static final int STOP = 7;          // Created started and resumed.
    static final int DESTROY = 8;          // Created started and resumed.
    int mState = INITIALIZING;


    protected ContextViewBlock(Context context) {
        Preconditions.checkNotNull(context);
        this.mContext = context;
        this.mActivity = (Activity) this.mContext;
    }

    protected ContextViewBlock(View blockingView) {
        Preconditions.checkNotNull(blockingView);
        this.mBlockingView = blockingView;
        mBlockingViewId = blockingView.getId();
        this.mContext = blockingView.getContext();
        this.mActivity = (Activity) this.mContext;
        onAttachToActivity(mActivity);
    }

    protected ContextViewBlock(@NonNull Context context, @LayoutRes final int layoutResId) {
        this(context, layoutResId, null, false);
    }

    protected ContextViewBlock(@NonNull Context context, @LayoutRes final int layoutResId,
                               @Nullable ViewGroup root, boolean attachToRoot) {
        this(LayoutInflater.from(context).inflate(layoutResId, root, attachToRoot));
    }

    protected ContextViewBlock(@NonNull Activity activity, @IdRes final int resId) {
        this(activity.findViewById(resId));
    }

    protected abstract void onAttachToActivity(@NonNull Activity activity);

    protected abstract void onDetachToActivity(@NonNull Activity activity);

    protected abstract void onFinishInflateView();

    public int getId() {
        return mBlockingViewId;
    }

    public Resources getResources() {
        return mActivity.getResources();
    }

    public Application getApplication() {
        return mActivity.getApplication();
    }

    public Intent getIntent() {
        return mActivity.getIntent();
    }

    public void setIntent(Intent newIntent) {
        mActivity.setIntent(newIntent);
    }

    public Activity getActivity() {
        return mActivity;
    }

    public Context getContext() {
        return mContext;
    }

    public AssetManager getAssets() {
        return mContext.getAssets();
    }

    public Looper getMainLooper() {
        return mContext.getMainLooper();
    }

    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    protected void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(getContext(), clazz);
        mActivity.startActivity(intent);
    }

    public void finishActivity() {
        mActivity.finish();
    }


    public View getBlockingView() {
        return mBlockingView;
    }

    protected <T> T castBlockingView() {
        //noinspection unchecked
        return (T) mBlockingView;
    }

    public abstract UIKitIntent getUIKitIntent();

    public abstract void setUIKitIntent(UIKitIntent mUiKitIntent);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        moveToState(CREATED);
    }

    @Override
    public void onCreateView() {
        moveToState(CREATED_VIEW);
        onFinishInflateView();
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        moveToState(STARTED);
    }

    @Override
    public void onResume() {
        moveToState(RESUMED);
    }

    @Override
    public void onPause() {
        moveToState(PAUSE);
    }

    @Override
    public void onStop() {
        moveToState(STOP);
    }

    @Override
    public void onRestart() {
        moveToState(STARTED);
    }

    @Override
    public void onDestroy() {
        moveToState(DESTROY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onBackPressed() {

    }

    public int getState() {
        return mState;
    }

    void moveToState(int newState) {
        this.mState = newState;
    }
}
