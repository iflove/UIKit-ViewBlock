package android.utils;

import android.uikit.R;
import android.uikit.UIKitFrameLayout;
import android.uikit.UIKitLinearLayout;
import android.uikit.UIKitRelativeLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AndroidAttrs {
    public static final int[] ATTRS = new int[]
            { //
                    android.R.attr.name,
                    android.R.attr.id,
                    android.R.attr.tag,
                    R.attr.block_class,
            };

    public static final int NAME_INDEX = 0;
    public static final int ID_INDEX = 1;
    public static final int TAG_INDEX = 2;
    public static final int BLOCK_CLASS_INDEX = 3;

    public static final List<String> ANDROID_LAYOUT_TAG = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add(FrameLayout.class.getSimpleName());
            add(LinearLayout.class.getSimpleName());
            add(RelativeLayout.class.getSimpleName());
        }
    });

    public static final List<String> UIKIT_TAGS = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add(UIKitFrameLayout.class.getName());
            add(UIKitLinearLayout.class.getName());
            add(UIKitRelativeLayout.class.getName());

            addAll(ANDROID_LAYOUT_TAG);
            add("fragment");
        }
    });


}
