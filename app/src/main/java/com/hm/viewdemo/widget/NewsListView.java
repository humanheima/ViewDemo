package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/7/17.
 */
public class NewsListView extends LinearLayout {

    private final String TAG = getClass().getSimpleName();

    private TextView tvItemName;
    private TextView tvItemStatus;

    public NewsListView(Context context) {
        this(context, null);
    }

    public NewsListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.parseColor("#a9222222"));
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_status_list_item, this);
        tvItemName = view.findViewById(R.id.tv_item_name);
        tvItemStatus = view.findViewById(R.id.tv_item_status);
    }

    public void setData(String name, String status) {
        if (null == tvItemName || null == tvItemStatus) {
            return;
        }
        if ("Normal".equals(status)) {
            tvItemStatus.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.sp_red_corners10));
            tvItemStatus.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            tvItemStatus.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.sp_white_corners10));
            tvItemStatus.setTextColor(getContext().getResources().getColor(R.color.black));
        }
        tvItemName.setText(name);
        tvItemStatus.setText(status);
    }

}
