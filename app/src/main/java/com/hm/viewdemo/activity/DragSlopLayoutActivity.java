package com.hm.viewdemo.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;
import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.AlbumAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityDragSlopLayoutBinding;
import com.hm.viewdemo.util.Images;
import java.util.ArrayList;
import java.util.List;

public class DragSlopLayoutActivity extends BaseActivity<ActivityDragSlopLayoutBinding> {

    private static final String TAG = "DragSlopLayoutActivity";

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
            binding.llContent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            imageList.add(Images.imageUrls[i]);
        }
        contentList.add(getString(R.string.news_content));
        contentList.add(getString(R.string.TomHardy));
        contentList.add(getString(R.string.ChristianBale));
        contentList.add(getString(R.string.MarkWahlberg));
        contentList.add(getString(R.string.WillSmith));
        contentList.add(getString(R.string.DenzelWashington));
        binding.textNumberSum.setText("/" + String.valueOf(contentList.size()));
        binding.textNumberSum.setText("/" + String.valueOf(contentList.size()));
        binding.textHideNumberNow.setText("1");
        binding.textHideNumberNow.setText("1");
        binding.rlTop.post(new Runnable() {
            @Override
            public void run() {
                rlTopHeight = binding.rlTop.getHeight();
                rlTopOutAnimator = ObjectAnimator.ofFloat(binding.rlTop, "translationY", 0, -rlTopHeight)
                        .setDuration(400);
                rlTopInAnimator = ObjectAnimator.ofFloat(binding.rlTop, "translationY", -rlTopHeight, 0)
                        .setDuration(400);
            }
        });
        binding.rlHide.post(new Runnable() {
            @Override
            public void run() {
                rlHideHeight = binding.rlHide.getHeight();
                binding.rlHide.setTranslationY(rlHideHeight);
                rlHideInAnimator = ObjectAnimator.ofFloat(binding.rlHide, "translationY", rlHideHeight, 0)
                        .setDuration(400);
                rlHideOutAnimator = ObjectAnimator.ofFloat(binding.rlHide, "translationY", 0, rlHideHeight)
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
                    binding.dragSlopLayout.scrollOutScreen(400);
                    binding.rlHide.setVisibility(View.VISIBLE);
                } else {
                    clickToHide = true;
                    rlTopInAnimator.start();
                    rlHideOutAnimator.start();
                    binding.dragSlopLayout.scrollInScreen(400);
                    binding.rlHide.setVisibility(View.INVISIBLE);
                }
            }
        });
        binding.viewPager.setAdapter(adapter);
        //textDragContent.setText(contentList.get(0));
        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //textDragContent.setText(contentList.get(position));
                binding.textNumberNow.setText(String.valueOf(position + 1));
                binding.textHideNumberNow.setText(String.valueOf(position + 1));
            }
        });
        binding.dragSlopLayout.setAttachScrollView(binding.nestedScrollView);
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //向上滑动scrollY是正值
                Log.e(TAG, "scrollY=" + scrollY);
            }
        });
    }

    @Override
    protected void bindEvent() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
