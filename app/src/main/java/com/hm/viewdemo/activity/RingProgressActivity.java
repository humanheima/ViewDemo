package com.hm.viewdemo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.widget.RingProgressView;

/**
 * Author        Hule  hu.le@cesgroup.com.cn
 * Date          2017/6/12 15:38
 * Description:  TODO: 自定义环形进度条
 */

public class RingProgressActivity extends Activity {

    private RingProgressView ringProgressView;
    private ValueAnimator valueAnimator;


    private static final String TAG = "RingProgressActivity";

    public static void launch(Context context) {
        Intent starter = new Intent(context, RingProgressActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_progress);
        ringProgressView = findViewById(R.id.ringProgress);
        //excuteProgress();

        int maxProgress = ringProgressView.getMaxProgress();
        Log.d(TAG, "onCreate: maxProgress = " + maxProgress);

        /**
         * 这里注意，动画的最大进度是大于RingProgressView的。
         * 比如RingProgressView的最大进度是100 ，这里我们动画的结束值是101。
         * 解决的问题： 比如RingProgressView的最大进度是100 ，这里我们动画的结束值如果也写100的话，在动画repeat的时候，可能
         * RingProgressView没有到达最大值，可能只到了99
         */
        valueAnimator = ValueAnimator.ofInt(1, maxProgress + 1);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentProgress = (int) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: currentProgress = " + currentProgress);
                ringProgressView.setCurrentProgress(currentProgress);
                ringProgressView.invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //super.onAnimationEnd(animation);

            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);//重新播放
        valueAnimator.setStartDelay(1000);//动画延迟多长时间开始
        valueAnimator.setDuration(3000);//设置动画时长
    }

    private void excuteProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        int currentProgress = ringProgressView.getCurrentProgress();
                        if (currentProgress >= 100) {
                            currentProgress = 0;
                        }
                        Thread.sleep(200);
                        currentProgress += 1;
                        ringProgressView.setCurrentProgress(currentProgress);
                        ringProgressView.postInvalidate();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_start_anim) {
            valueAnimator.start();
        } else if (id == R.id.btn_stop_anim) {
            valueAnimator.cancel();
        }
    }
}
