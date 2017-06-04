package android.uikit.demo;

import android.app.Application;
import android.uikit.demo.utils.DensityUtil;
import android.uikit.demo.utils.ResUtil;

import com.lazy.library.logging.Builder;
import com.lazy.library.logging.Logcat;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ResUtil.inject(this);
        DensityUtil.inject(this);
        Builder builder = Logcat.newBuilder();
        builder.logCatLogLevel(Logcat.SHOW_DEBUG_LOG | Logcat.SHOW_WARN_LOG | Logcat.SHOW_ERROR_LOG);//设置日志等级
        Logcat.initialize(this, builder.build());
    }
}
