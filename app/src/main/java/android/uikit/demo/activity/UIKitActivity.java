package android.uikit.demo.activity;

import android.os.Bundle;
import android.uikit.demo.R;

public class UIKitActivity extends android.uikit.UIKitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
