package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.brotherd.pullrefresh.PullToRefreshBase;
import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.base.CommonAdapter;
import com.hm.viewdemo.base.CommonViewHolder;
import com.hm.viewdemo.databinding.ActivityPullToRefreshBinding;
import com.hm.viewdemo.util.ScreenUtil;
import com.hm.viewdemo.widget.SimpleFlowLayout;
import com.hm.viewdemo.widget.xfermode.SpecialMaskViewGroup;
import java.util.ArrayList;
import java.util.List;

public class PullToRefreshActivity extends BaseActivity<ActivityPullToRefreshBinding> implements
        ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "PullToRefreshActivity";
    @BindView(R.id.pr_recycler_view)
    com.brotherd.pullrefresh.PullToRefreshRecyclerView prRecyclerView;
    private RecyclerView recyclerView;
    //private RecycleViewAdapter adapter;
    private List<String> dataList;
    private int page = 1;
    private View loadAllView;
    private CommonAdapter<String> adapter1;

    private View activityRootView;

    private SpecialMaskViewGroup specialMaskViewGroup;

    public static void launch(Context context) {
        Intent starter = new Intent(context, PullToRefreshActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityPullToRefreshBinding createViewBinding() {
        return ActivityPullToRefreshBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        recyclerView = prRecyclerView.getRefreshableView();
        loadAllView = getLayoutInflater().inflate(R.layout.item_load_all, null);
        dataList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new CommonAdapter<String>(this, dataList) {
            @Override
            public void bindViewHolder(CommonViewHolder holder, String s, int position) {
                holder.setTextViewText(R.id.text1, s);
                SimpleFlowLayout simpleFlowLayout = holder.getView(R.id.simple_flow_layout);
                // 关键字集合
                List<String> list = new ArrayList<>();
               /* list.add("dmw");
                list.add("dmw");
                list.add("dmw");
                list.add("dmw");
                list.add("dmw");
                list.add("dmw");*/
                list.add("杜明伟");
                list.add("杜明伟");
                list.add("杜明伟");
                list.add("杜明伟");
                list.add("杜明伟");
                list.add("杜明伟");
                list.add("杜明伟");
                simpleFlowLayout.setViews(list, new SimpleFlowLayout.OnItemClickListener() {
                    @Override
                    public void onItemClick(String content) {
                        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public int getHolderType(int position, String s) {
                return R.layout.item_recycler_view;
            }
        };
        recyclerView.setAdapter(adapter1);
        activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        specialMaskViewGroup = findViewById(R.id.special_mask_view_group);

    }

    @Override
    protected void bindEvent() {
        prRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                prRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prRecyclerView.onRefreshComplete();
                        if (adapter1 != null) {
                            adapter1.removeFooterView();
                        }
                        prRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        page = 1;
                        dataList.clear();
                        for (int i = 0; i < 20; i++) {
                            dataList.add("String" + i);
                        }
                        adapter1.notifyDataSetChanged();
                    }
                }, 2000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                prRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prRecyclerView.onRefreshComplete();
                        if (page < 3) {
                            for (int i = 0; i < 12; i++) {
                                dataList.add("string" + i);
                            }
                            page++;
                            adapter1.notifyDataSetChanged();
                        } else {
                            prRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            adapter1.addFooterView(loadAllView);
                        }
                    }
                }, 2000);
            }
        });
        prRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                prRecyclerView.setRefreshing();
            }
        }, 300);
    }

    @Override
    public void onGlobalLayout() {
        if (isKeyboardShown(activityRootView)) {
            Log.e(TAG, "软键盘弹起");
            specialMaskViewGroup.setGradientEnabled(false);

        } else {
            Log.e(TAG, "软键盘关闭");
            specialMaskViewGroup.setGradientEnabled(true);
        }
    }

    private boolean isKeyboardShown(View rootView) {
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        Log.i(TAG, "isKeyboardShown: " + dm.toString());
        //设定一个认为是软键盘弹起的阈值,只要超过这个阈值，就认为软键盘弹起了,正常来说，软键盘不止50dp高
        final int softKeyboardHeight = (int) (50 * dm.density);
        //得到屏幕可见区域的大小
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        //
        /**
         * dm.heightPixels是整个屏幕高度可以认为是屏幕高度，不包含状态栏的高度；
         * 比如说屏幕高度是2340。状态栏高度是85，那么dm.heightPixels就是 2255。（测试了一下，即使是全屏主题，也不包含状态栏高度）
         * r.bottom 是 R.id.content 在屏幕上的底部坐标，也就是说 R.id.content 的底部坐标是。如果我们的View是全屏的，
         * 那么如果键盘没有弹起，r.bottom = 2340
         * 如果软键盘弹起，那么 R.id.content 的底部坐标就会变小。变成 r.bottom - 软键盘的高度。
         *
         * 正常来说 r.bottom 应该是>= dm.heightPixels的。从我们的测试来看，r.bottom 比 dm.heightPixels 高了状态栏的高度。
         * 软键盘弹起的时候， r.bottom 就会变小。我们认为r.bottom 只要比 dm.heightPixels 小了softKeyboardHeight高度，就是软键盘弹起了。
         *
         */
        int heightDiff = dm.heightPixels - r.bottom;

        //r.bottom是屏幕底部坐标，dm.heightPixels是整个屏幕高度
        Log.i(TAG, "isKeyboardShown: r.bottom = " + r.bottom);
        Log.i(TAG, "isKeyboardShown: statusBarHeight = " + ScreenUtil.getStatusBarHeight(this));

        return heightDiff > softKeyboardHeight;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

    }
}
