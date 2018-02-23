package com.hm.viewdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/7/17.
 * 实现和MarqueeView是一样的
 */
public class NewsListView extends LinearLayout {

    private final String TAG = getClass().getSimpleName();

    private TextView textNews;

    public NewsListView(Context context) {
        this(context, null);
    }

    public NewsListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_list, this);
        textNews = (TextView) view.findViewById(R.id.text_news);
    }

    public void setText(String text) {
        if (textNews == null || TextUtils.isEmpty(text)) {
            return;
        }
        textNews.setText(text);
    }

}
