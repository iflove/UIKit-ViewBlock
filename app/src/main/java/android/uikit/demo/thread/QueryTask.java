package android.uikit.demo.thread;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by lazy on 2017/4/23.
 */

public class QueryTask {
    public final static ExecutorService executorService = Executors.newCachedThreadPool();
    public final static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    public final static Handler H = new Handler();


    public static void runOnUiThread(Runnable runnable) {
        H.post(runnable);
    }

}
