package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brotherd.pullrefresh.PullToRefreshBase;
import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.base.CommonViewHolder;
import com.hm.viewdemo.base.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PullToRefreshActivity extends BaseActivity {

    private static final String TAG = "PullToRefreshActivity";
    @BindView(R.id.pr_recycler_view)
    com.brotherd.pullrefresh.PullToRefreshRecyclerView prRecyclerView;
    private RecyclerView recyclerView;
    //private RecycleViewAdapter adapter;
    private List<String> dataList;
    private int page = 1;
    private View loadAllView;
    private CommonAdapter<String> adapter1;

    public static void launch(Context context) {
        Intent starter = new Intent(context, PullToRefreshActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_pull_to_refresh;
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
            }

            @Override
            public int getHolderType(int position, String s) {
                return R.layout.item_recycler_view;
            }
        };
        recyclerView.setAdapter(adapter1);
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
                        if (adapter1 != null)
                            adapter1.removeFooterView();
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
}
