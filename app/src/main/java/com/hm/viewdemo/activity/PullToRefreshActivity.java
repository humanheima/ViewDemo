package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brotherd.pullrefresh.PullToRefreshBase;
import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;
import com.hm.viewdemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PullToRefreshActivity extends BaseActivity {

    private static final String TAG = "PullToRefreshActivity";
    @BindView(R.id.pr_recycler_view)
    com.brotherd.pullrefresh.PullToRefreshRecyclerView prRecyclerView;
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private List<String> dataList;
    private int page = 1;
    private View loadAllView;

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
        LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);
        adapter = new RecycleViewAdapter(dataList, this);
        recyclerView.setAdapter(adapter);
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
                        if (adapter != null)
                            adapter.removeFooterView();
                        prRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
                        page = 1;
                        dataList.clear();
                        for (int i = 0; i < 20; i++) {
                            dataList.add("String" + i);
                        }
                        adapter.notifyDataSetChanged();
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
                            adapter.notifyDataSetChanged();
                        } else {
                            prRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            adapter.addFooterView(loadAllView);
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
