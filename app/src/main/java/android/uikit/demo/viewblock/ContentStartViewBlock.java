package android.uikit.demo.viewblock;

import android.content.Context;
import android.content.UIKitIntent;
import android.support.annotation.Nullable;
import android.uikit.ViewBlock;
import android.uikit.demo.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by lazy on 2017/5/7.
 */

public class ContentStartViewBlock extends ViewBlock implements View.OnClickListener {
    int index;

    public ContentStartViewBlock(Context context, @Nullable ViewGroup root) {
        super(context, R.layout.block_main, root, false);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        Button button = bindViewById(R.id.button);
        button.setOnClickListener(this);

        UIKitIntent kitIntent = getUIKitIntent();

        index = kitIntent.getIntExtra("index", 0);
        button.setText(String.format("%s %s", button.getText(), String.valueOf(index)));
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "onClick", Toast.LENGTH_SHORT).show();
        ContentStartViewBlock viewBlock = new ContentStartViewBlock(this.getContext(), ((ViewGroup) getBlockingView()));
        UIKitIntent kitIntent = new UIKitIntent(this, viewBlock);
        kitIntent.putExtra("index", index + 1);
        startViewBlock(kitIntent);

    }
}
