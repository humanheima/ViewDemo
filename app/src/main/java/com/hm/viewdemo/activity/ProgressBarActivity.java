package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityProgressBarBinding;
import java.lang.ref.WeakReference;

/**
 * Created by dumingwei on 2020/4/15
 * <p>
 * Desc: 测试loading dialog progress等
 * 参考链接：https://blog.csdn.net/mad1989/article/details/38042875
 */
public class ProgressBarActivity extends BaseActivity<ActivityProgressBarBinding> {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_progress)
    TextView textProgress;

    private MyHandler handler;

    private RatingBar ratingBar;

    private Button btnChangeRating;


    public static void launch(Context context) {
        Intent starter = new Intent(context, ProgressBarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityProgressBarBinding createViewBinding() {
        return ActivityProgressBarBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        handler = new MyHandler(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i <= 100; i++) {
                        try {
                            Thread.sleep(50);
                            Message msg = handler.obtainMessage();
                            msg.arg1 = i;
                            handler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        ratingBar = findViewById(R.id.rating_bar);
        btnChangeRating = findViewById(R.id.btn_change_rate);

        btnChangeRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(3.5f);
            }
        });
    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseActivity> weakActivity;

        public MyHandler(BaseActivity baseActivity) {
            this.weakActivity = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ProgressBarActivity activity = ((ProgressBarActivity) weakActivity.get());
            if (activity != null) {
                activity.progressBar.setProgress(msg.arg1);
                activity.textProgress.setText(msg.arg1 + "%");
            }
        }
    }

}
