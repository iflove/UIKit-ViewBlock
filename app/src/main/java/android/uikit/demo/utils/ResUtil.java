package android.uikit.demo.utils;

import android.app.Application;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.uikit.demo.R;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lazy on 2016/12/13.
 * <p>
 * Describe:
 */

public final class ResUtil {
    private static final String TAG = "ResUtil";
    public static Application mApplication = null;

    public static void inject(@NonNull Application application) {
        mApplication = application;
    }

    public static int getViewId(String packageName, String idName) {
        int id = 0;
        try {
            Class<?> cls = Class.forName(packageName + ".R$" + "id");
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getViewId(String idName) {
        int id = 0;
        try {
            Class<?> cls = R.id.class;
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getDrawableId(String packageName, String idName) {
        int id = 0;
        try {
            Class<?> cls = Class.forName(packageName + ".R$" + "drawable");
            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
//			e.printStackTrace();
        }
        return id;
    }

    public static int getDrawableId(String idName) {
        int id = 0;
        try {
            Class<?> cls = R.drawable.class;
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
//			e.printStackTrace();
        }
        return id;
    }

    public static int getStringId(String packageName, String idName) {
        int id = 0;
        try {
            Class<?> cls = Class.forName(packageName + ".R$" + "string");
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getStringId(String idName) {
        int id = 0;
        try {
            Class<?> cls = R.string.class;
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getXmlId(String packageName, String idName) {
        int id = 0;
        try {
            Class<?> cls = Class.forName(packageName + ".R$" + "xml");
            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getXmlId(String idName) {
        int id = 0;
        try {
//            Class<?> cls = R.xml.class;
//            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * @param id
     * @return
     */
    public static int[] getArrayRes(@ArrayRes int id) {
        return mApplication.getResources().getIntArray(id);
    }

    /**
     * @param id
     * @return
     */
    public static String[] getStringArray(@ArrayRes int id) {
        return mApplication.getResources().getStringArray(id);
    }

    public static int[] getResourcesIdArray(@ArrayRes int id) {
        TypedArray typedArray = mApplication.getResources().obtainTypedArray(id);
        int length = typedArray.length();
        int[] resArray = new int[length];
        for (int i = 0; i < length; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            resArray[i] = resourceId;
        }
        typedArray.recycle();
        return resArray;
    }

    public static Integer[] getResourcesIdIntegerArray(@ArrayRes int id) {
        TypedArray typedArray = mApplication.getResources().obtainTypedArray(id);
        int length = typedArray.length();
        Integer[] resArray = new Integer[length];
        for (int i = 0; i < length; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            resArray[i] = resourceId;
        }
        typedArray.recycle();
        return resArray;
    }

    public static List<Integer> getResourcesIdList(@ArrayRes int id) {
        return Arrays.asList(getResourcesIdIntegerArray(id));
    }

    public static List<String> getListString(@ArrayRes int id) {
        return Arrays.asList(mApplication.getResources().getStringArray(id));
    }

    public static String getString(@StringRes int id) {
        return mApplication.getResources().getString(id);
    }

    public static int getColor(@ColorRes int id) {
        return mApplication.getResources().getColor(id);
    }

}
