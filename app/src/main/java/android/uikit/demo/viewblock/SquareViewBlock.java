package android.uikit.demo.viewblock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.uikit.ViewBlock;
import android.view.View;

import com.lazy.library.logging.Logcat;

/**
 * Created by lazy on 2017/6/3.
 */

public class SquareViewBlock extends ViewBlock {
    private static final String TAG = "SquareViewBlock";

    public SquareViewBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onCreate").out();
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onCreateView").out();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onStart").out();

    }

    @Override
    public void onResume() {
        super.onResume();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onResume").out();
    }

    @Override
    public void onPause() {
        super.onPause();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onPause").out();
    }

    @Override
    public void onStop() {
        super.onStop();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onStop").out();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onRestart").out();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logcat.d().tag(TAG).msg("SquareViewBlock:").msg(this.hashCode()).msg("onDestroy").out();

    }
}
