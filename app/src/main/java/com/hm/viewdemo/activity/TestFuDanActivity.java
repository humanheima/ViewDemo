package com.hm.viewdemo.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.NewsListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestFuDanActivity extends BaseActivity {


    private static final String TAG = "TestFuDanActivity";
    public static final int CHANGE_RIGHT_NEWS_DELAY = 10000;

    @BindView(R.id.ll_news_list)
    LinearLayout llNewsList;
    @BindView(R.id.scroll_view_news)
    ScrollView scrollViewNews;
    private List<String> newsList;
    private ObjectAnimator newsListAnimator;

    private TranslateAnimation translateAnimation;

    public static void launch(Context context) {
        Intent starter = new Intent(context, TestFuDanActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_test_fu_dan;
    }

    @Override
    protected void initData() {
        newsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            newsList.add(i + "After more than a year of development and months of testing by developers and early adopters (thank you!), we're now ready to officially launch Android 8.0 Oreo to the world. Android 8.0 brings a ton of great ");
        }
        initRightNews();
    }

    private void initRightNews() {
        for (String news : newsList) {
            NewsListView view = new NewsListView(this);
            view.setText(news);
            llNewsList.addView(view);
        }
        llNewsList.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "llNewsList height=" + llNewsList.getHeight() + ",scrollView height=" + scrollViewNews.getHeight());
                int scrollViewHeight = scrollViewNews.getHeight();
                int llNewsListHeight = llNewsList.getHeight();
                if (llNewsListHeight > scrollViewHeight) {
                    int multiple = llNewsListHeight / scrollViewHeight;
                    int diff = llNewsListHeight - scrollViewHeight;
                   /* newsListAnimator = ObjectAnimator.ofFloat(llNewsList, "translationY", 0, -diff);
                    newsListAnimator.setDuration(multiple * CHANGE_RIGHT_NEWS_DELAY);
                    newsListAnimator.setRepeatCount(ValueAnimator.INFINITE);
                    newsListAnimator.setRepeatMode(ObjectAnimator.RESTART);
                    newsListAnimator.start();*/
                    translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0F, Animation.ABSOLUTE, 0F, Animation.ABSOLUTE, 0F, Animation.ABSOLUTE, -diff);
                    translateAnimation.setDuration(multiple * CHANGE_RIGHT_NEWS_DELAY);
                    translateAnimation.setRepeatMode(Animation.INFINITE);
                    translateAnimation.setRepeatCount(-1);
                    llNewsList.startAnimation(translateAnimation);

                }
            }
        });
    }

    @OnClick(R.id.button)
    void back() {
        finish();
    }

}
