package android.uikit.demo.viewblock;

import android.uikit.ViewBlock;
import android.uikit.demo.R;
import android.view.View;
import android.widget.TextView;

public class ActionBarBlock extends ViewBlock {

    TextView appBarTextView;

    public ActionBarBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        appBarTextView = bindViewById(R.id.appBarTextView);
        appBarTextView.setText(R.string.app_name);
    }
}
