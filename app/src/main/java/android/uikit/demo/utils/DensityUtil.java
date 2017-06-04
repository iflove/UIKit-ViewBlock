package android.uikit.demo.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 屏幕适配工具类
 */
public class DensityUtil {

    private static DensityUtil mDensityUtil = null;
    public DisplayMetrics metric;
    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;
    public static Application mApplication = null;


    public static void inject(@NonNull Application application) {
        mApplication = application;

    }

    public DensityUtil() {
        metric = mApplication.getResources().getDisplayMetrics();
        if (metric != null) {
            SCREEN_WIDTH = metric.widthPixels;
            SCREEN_HEIGHT = metric.heightPixels;
        }
    }


    public static DensityUtil getInstance() {
        if (mDensityUtil == null) {
            mDensityUtil = new DensityUtil();
        }
        return mDensityUtil;
    }

    public int PxConvertDp(int value) {
        int newvalue = 0;
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        if (densityDpi == 160) {
            newvalue = value;
        } else {
            newvalue = (int) (value * density);
        }
        return newvalue;
    }

    public int getDimension(int dimenId) {
        return (int) mApplication.getResources().getDimension(dimenId);
    }

    /*常用单位转换的辅助* /
    /**
     * dp转px
     *
     * @param context
     * @param val
     * @return
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, metric);
    }

    /**
     * sp转px
     *
     * @return
     */
    public int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, metric);
    }

    /**
     * px转dp
     *
     * @param pxVal
     * @return
     */
    public float px2dp(float pxVal) {
        final float scale = metric.density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public float px2sp(float pxVal) {
        return (pxVal / metric.scaledDensity);
    }

    private int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();
        int id = 0;
        try {
            id = resources.getIdentifier(
                    orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                    "dimen", "android");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }
}
