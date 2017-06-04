package android.uikit;

import android.support.annotation.AnimRes;

/**
 * Created by lazy on 2017/5/26.
 */

interface ViewBlockAnimation {
    ViewBlock setCustomAnimations(@AnimRes int enter,
                                  @AnimRes int exit);

    ViewBlock setCustomAnimations(@AnimRes int enter,
                                  @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit);
}
