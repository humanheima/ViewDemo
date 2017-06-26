package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.AlbumAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.util.Images;
import com.hm.viewdemo.widget.demo.DragSlopLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DragSlopLayoutActivity extends BaseActivity {

    private static final String TAG = "DragSlopLayoutActivity";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.drag_slop_layout)
    DragSlopLayout dragSlopLayout;
    @BindView(R.id.text_drag_title)
    TextView textDragTitle;
    @BindView(R.id.text_drag_number)
    TextView textDragNumber;
    @BindView(R.id.text_drag_content)
    TextView textDragContent;
    @BindView(R.id.text_drag_press)
    TextView textDragPress;
    @BindView(R.id.text_drag_date)
    TextView textDragDate;
    @BindView(R.id.rl_drag_view)
    RelativeLayout rlDragView;
    @BindView(R.id.scroll_view_drag_content)
    NestedScrollView scrollViewDragContent;

    private List<String> imageList;
    private List<String> contentList;
    private AlbumAdapter adapter;

    public static void launch(Context context) {
        Intent starter = new Intent(context, DragSlopLayoutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_drag_slop_layout;
    }

    @Override
    protected void initData() {
        imageList = new ArrayList<>();
        contentList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            imageList.add(Images.imageUrls[i]);
        }
        contentList.add(getString(R.string.news_content));
        contentList.add(getString(R.string.TomHardy));
        contentList.add(getString(R.string.ChristianBale));
        contentList.add(getString(R.string.MarkWahlberg));
        contentList.add(getString(R.string.WillSmith));
        contentList.add(getString(R.string.DenzelWashington));
        adapter = new AlbumAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        textDragContent.setText(contentList.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                textDragContent.setText(contentList.get(position));
            }
        });
        dragSlopLayout.setAttachScrollView(scrollViewDragContent);
    }

}
