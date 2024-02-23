package com.hm.viewdemo.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityTestFuDanBinding;
import com.hm.viewdemo.widget.NewsListView;
import java.util.ArrayList;
import java.util.List;

public class TestFuDanActivity extends BaseActivity<ActivityTestFuDanBinding> {

    private static final String TAG = "TestFuDanActivity";
    private List<String> newsList;
    private ObjectAnimator newsListAnimator;

    private TranslateAnimation translateAnimation;

    public static void launch(Context context) {
        Intent starter = new Intent(context, TestFuDanActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityTestFuDanBinding createViewBinding() {
        return ActivityTestFuDanBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        binding.scrollViewNews.setFocusable(false);
        binding.scrollViewNews.setEnabled(false);
        newsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                newsList.add("Normal");
            } else {
                newsList.add("Abnormal");
            }
        }
        binding.btnReset.setOnClickListener(v -> {
                    back();
                }
        );
        initRightNews();
    }

    private void initRightNews() {
        for (String news : newsList) {
            NewsListView view = new NewsListView(this);
            view.setData(news, news);
            binding.llNewsList.addView(view);
        }
        binding.llNewsList.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "llNewsList height=" + binding.llNewsList.getHeight() + ",scrollView height="
                        + binding.scrollViewNews.getHeight());
                int scrollViewHeight = binding.scrollViewNews.getHeight();
                int llNewsListHeight = binding.llNewsList.getHeight();
                if (llNewsListHeight > scrollViewHeight) {
                    int multiple = llNewsListHeight / scrollViewHeight;
                    int diff = llNewsListHeight - scrollViewHeight;
                    newsListAnimator = ObjectAnimator.ofFloat(binding.llNewsList, "translationY", 0, -diff);
                    //newsListAnimator.setDuration(multiple * CHANGE_RIGHT_NEWS_DELAY);
                    newsListAnimator.setDuration(3000);
                    //newsListAnimator.setRepeatCount(ValueAnimator.INFINITE);
                    newsListAnimator.setRepeatMode(ObjectAnimator.RESTART);
                    newsListAnimator.start();
                   /* translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0F, Animation.ABSOLUTE, 0F, Animation.ABSOLUTE, 0F, Animation.ABSOLUTE, -diff);
                    translateAnimation.setDuration(multiple * CHANGE_RIGHT_NEWS_DELAY);
                    translateAnimation.setRepeatMode(Animation.INFINITE);
                    translateAnimation.setRepeatCount(-1);
                    llNewsList.startAnimation(translateAnimation);*/

                }
            }
        });
    }

    void back() {
        binding.llNewsList.removeAllViews();
        binding.llNewsList.invalidate();
        //initRightNews();
    }

}
