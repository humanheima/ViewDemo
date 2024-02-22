package com.hm.viewdemo.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.AlbumAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityDragSlopLayoutBinding;
import com.hm.viewdemo.util.Images;
import com.hm.viewdemo.widget.DragSlopLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DragSlopLayoutActivity extends BaseActivity<ActivityDragSlopLayoutBinding> {

    private static final String TAG = "DragSlopLayoutActivity";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.drag_slop_layout)
    DragSlopLayout dragSlopLayout;
    @BindView(R.id.text_title)
    TextView textDragTitle;
    /*@BindView(R.id.text_content)
    TextView textDragContent;
    @BindView(R.id.text_press)
    TextView textDragPress;
    @BindView(R.id.text_date)
    TextView textDragDate;*/
    @BindView(R.id.nested_scroll_view)
    NestedScrollView scrollViewDragContent;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.text_hide_number_now)
    TextView textHideNumberNow;
    @BindView(R.id.text_hide_number_sum)
    TextView textHideNumberSum;
    @BindView(R.id.rl_hide)
    RelativeLayout rlHide;
    @BindView(R.id.text_number_now)
    TextView textNowNumber;
    @BindView(R.id.text_number_sum)
    TextView textTotalNumber;

    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private List<String> imageList;
    private List<String> contentList;
    private AlbumAdapter adapter;

    private ObjectAnimator rlTopOutAnimator;
    private ObjectAnimator rlTopInAnimator;
    private ObjectAnimator rlHideOutAnimator;
    private ObjectAnimator rlHideInAnimator;

    private float rlTopHeight;

    private int rlHideHeight;

    //点击隐藏顶部和文字介绍
    private boolean clickToHide = true;

    public static void launch(Context context) {
        Intent starter = new Intent(context, DragSlopLayoutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityDragSlopLayoutBinding createViewBinding() {
        return ActivityDragSlopLayoutBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        imageList = new ArrayList<>();
        contentList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_drag_slop, null);
            llContent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageList.add(Images.imageUrls[i]);
        }
        contentList.add(getString(R.string.news_content));
        contentList.add(getString(R.string.TomHardy));
        contentList.add(getString(R.string.ChristianBale));
        contentList.add(getString(R.string.MarkWahlberg));
        contentList.add(getString(R.string.WillSmith));
        contentList.add(getString(R.string.DenzelWashington));
        textTotalNumber.setText("/" + String.valueOf(contentList.size()));
        textHideNumberSum.setText("/" + String.valueOf(contentList.size()));
        textNowNumber.setText("1");
        textHideNumberNow.setText("1");
        rlTop.post(new Runnable() {
            @Override
            public void run() {
                rlTopHeight = rlTop.getHeight();
                rlTopOutAnimator = ObjectAnimator.ofFloat(rlTop, "translationY", 0, -rlTopHeight)
                        .setDuration(400);
                rlTopInAnimator = ObjectAnimator.ofFloat(rlTop, "translationY", -rlTopHeight, 0)
                        .setDuration(400);
            }
        });
        rlHide.post(new Runnable() {
            @Override
            public void run() {
                rlHideHeight = rlHide.getHeight();
                rlHide.setTranslationY(rlHideHeight);
                rlHideInAnimator = ObjectAnimator.ofFloat(rlHide, "translationY", rlHideHeight, 0)
                        .setDuration(400);
                rlHideOutAnimator = ObjectAnimator.ofFloat(rlHide, "translationY", 0, rlHideHeight)
                        .setDuration(400);
            }
        });

        adapter = new AlbumAdapter(this, imageList);
        adapter.setAlbumClickListener(new AlbumAdapter.AlbumClickListener() {
            @Override
            public void onAlbumClick() {
                if (clickToHide) {
                    clickToHide = false;
                    rlTopOutAnimator.start();
                    rlHideInAnimator.start();
                    dragSlopLayout.scrollOutScreen(400);
                    rlHide.setVisibility(View.VISIBLE);
                } else {
                    clickToHide = true;
                    rlTopInAnimator.start();
                    rlHideOutAnimator.start();
                    dragSlopLayout.scrollInScreen(400);
                    rlHide.setVisibility(View.INVISIBLE);
                }
            }
        });
        viewPager.setAdapter(adapter);
        //textDragContent.setText(contentList.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //textDragContent.setText(contentList.get(position));
                textNowNumber.setText(String.valueOf(position + 1));
                textHideNumberNow.setText(String.valueOf(position + 1));
            }
        });
        dragSlopLayout.setAttachScrollView(scrollViewDragContent);
        scrollViewDragContent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //向上滑动scrollY是正值
                Log.e(TAG, "scrollY=" + scrollY);
            }
        });
    }

    @Override
    protected void bindEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
