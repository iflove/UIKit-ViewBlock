package android.content;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.uikit.ViewBlock;

/**
 * Created by lazy on 2017/5/16.
 */

public class UIKitIntent extends Intent {
    public ViewBlock fromViewBlock;
    public ViewBlock toViewBlock;

    private UIKitIntent(Intent o) {
    }

    private UIKitIntent(String action) {
    }

    private UIKitIntent(String action, Uri uri) {
    }

    private UIKitIntent(Context packageContext, Class<?> cls) {
    }

    private UIKitIntent(String action, Uri uri, Context packageContext, Class<?> cls) {
    }

    public UIKitIntent(@NonNull ViewBlock fromViewBlock, @NonNull ViewBlock toViewBlock) {
        this.fromViewBlock = fromViewBlock;
        this.toViewBlock = toViewBlock;
    }

    public UIKitIntent(@NonNull ViewBlock fromViewBlock) {
        this.fromViewBlock = fromViewBlock;
    }

    public ViewBlock getFromViewBlock() {
        return fromViewBlock;
    }

    public void setFromViewBlock(ViewBlock fromViewBlock) {
        this.fromViewBlock = fromViewBlock;
    }

    public void setToViewBlock(ViewBlock toViewBlock) {
        this.toViewBlock = toViewBlock;
    }

    public ViewBlock getToViewBlock() {
        return toViewBlock;
    }
}
