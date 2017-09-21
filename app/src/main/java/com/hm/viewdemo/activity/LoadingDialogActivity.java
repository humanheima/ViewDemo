package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class LoadingDialogActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_progress)
    TextView textProgress;

    private MyHandler handler;
    private int progress;

    public static void launch(Context context) {
        Intent starter = new Intent(context, LoadingDialogActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_loading_dialog;
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

    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseActivity> weakActivity;

        public MyHandler(BaseActivity baseActivity) {
            this.weakActivity = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadingDialogActivity activity = ((LoadingDialogActivity) weakActivity.get());
            if (activity != null) {
                activity.progressBar.setProgress(msg.arg1);
                activity.textProgress.setText(msg.arg1 + "%");
            }
        }
    }

}
