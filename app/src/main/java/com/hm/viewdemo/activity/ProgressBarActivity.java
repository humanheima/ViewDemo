package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.RoundLayout;

import java.lang.ref.WeakReference;

import butterknife.BindView;

/**
 * Created by dumingwei on 2020/4/15
 * <p>
 * Desc: 测试loading dialog progress等
 */
public class ProgressBarActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_progress)
    TextView textProgress;

    private MyHandler handler;

    private RatingBar ratingBar;

    private Button btnChangeRating;

    private RoundLayout roundLayout;
    //宽度作为参考
    private ProgressBar horizontalProgressBar;

    private ProgressBar changeWidthProgressBar;
    private SeekBar sbController;


    public static void launch(Context context) {
        Intent starter = new Intent(context, ProgressBarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_progress_bar;
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


        roundLayout = findViewById(R.id.roundLayout);
        btnChangeRating = findViewById(R.id.btn_change_rate);

        sbController = findViewById(R.id.sbController);

        sbController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                changeWidth();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        horizontalProgressBar = findViewById(R.id.horizontalProgressBar);
        horizontalProgressBar.post(new Runnable() {
            @Override
            public void run() {
                changeWidth();
            }
        });


        btnChangeRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(3.5f);
            }
        });
    }

    private void changeWidth() {
        float progress = sbController.getProgress() * 1.0f;
        float max = sbController.getMax() * 1.0f;

        float percent = progress / max;

        Log.i(TAG, "run: percent = " + percent);

        int width = horizontalProgressBar.getMeasuredWidth();
        ViewGroup.LayoutParams layoutParams = roundLayout.getLayoutParams();

        int currentWidth =
        layoutParams.width = (int) (width * percent);
        roundLayout.setLayoutParams(layoutParams);
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
