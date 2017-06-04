package android.uikit.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.uikit.UIKit;
import android.uikit.demo.R;

public class GeneralActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIKit.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
