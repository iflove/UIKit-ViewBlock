package android.uikit.demo.viewblock;

import android.graphics.Color;
import android.uikit.ViewBlock;
import android.uikit.demo.R;
import android.view.View;
import android.widget.TextView;

public class ScreenViewBlock extends ViewBlock {

    public ScreenViewBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        TextView textView = bindViewById(R.id.screenViewBlockTextView);
        textView.setText(ScreenViewBlock.class.getSimpleName());
        textView.setTextColor(Color.RED);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = bindViewById(R.id.screenViewBlockTextView);
        textView.setTextColor(Color.BLUE);

    }
}
