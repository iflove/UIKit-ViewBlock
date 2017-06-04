package android.uikit.demo.viewblock;

import android.uikit.ViewBlock;
import android.uikit.demo.R;
import android.uikit.demo.thread.QueryTask;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StatusBarBlock extends ViewBlock {
    TextView statusBarTextView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    ScheduledFuture<?> scheduledFuture;

    public StatusBarBlock(View mBlockingView) {
        super(mBlockingView);
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        statusBarTextView = bindViewById(R.id.statusBarTextView);

        scheduledFuture = QueryTask.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                QueryTask.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String format = simpleDateFormat.format(new Date());
                        statusBarTextView.setText(format);
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduledFuture.cancel(true);
    }
}
