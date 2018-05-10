package com.hm.viewdemo.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.NewsListView;
import com.hm.viewdemo.widget.NoConsumeScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestFuDanActivity extends BaseActivity {

    private static final String TAG = "TestFuDanActivity";
    @BindView(R.id.ll_news_list)
    LinearLayout llNewsList;
    @BindView(R.id.scroll_view_news)
    NoConsumeScrollView scrollViewNews;
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
        scrollViewNews.setFocusable(false);
        scrollViewNews.setEnabled(false);
        newsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                newsList.add("Normal");
            } else {
                newsList.add("Abnormal");
            }
        }
        initRightNews();
    }

    private void initRightNews() {
        for (String news : newsList) {
            NewsListView view = new NewsListView(this);
            view.setData(news, news);
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
                    newsListAnimator = ObjectAnimator.ofFloat(llNewsList, "translationY", 0, -diff);
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

    @OnClick(R.id.btn_reset)
    void back() {
        llNewsList.removeAllViews();
        llNewsList.invalidate();
        //initRightNews();
    }

}
