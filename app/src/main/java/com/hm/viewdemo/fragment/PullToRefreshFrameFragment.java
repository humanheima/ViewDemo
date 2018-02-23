package com.hm.viewdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.brotherd.pullrefresh.OnCheckCanDoRefreshListener;
import com.brotherd.pullrefresh.PullToRefreshBase;
import com.brotherd.pullrefresh.PullToRefreshFrameLayout;
import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PullToRefreshFrameFragment extends Fragment {

    private static final String TAG = "PullToRefreshFrameFragm";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pull_to_refresh_frame_layout)
    PullToRefreshFrameLayout prLayout;
    Unbinder unbinder;

    private RecycleViewAdapter adapter;
    private List<String> dataList;
    private int page = 1;
    private View loadAllView;
    private int offsetY;
    private LinearLayoutManager l;
    //是否可以向上拉加载更多
    private boolean canPullUp;

    public PullToRefreshFrameFragment() {
        // Required empty public constructor
    }

    public static PullToRefreshFrameFragment newInstance() {
        PullToRefreshFrameFragment fragment = new PullToRefreshFrameFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pull_to_refresh_frame, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadAllView = inflater.inflate(R.layout.item_load_all, null);
        dataList = new ArrayList<>();
        l = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(l);
        adapter = new RecycleViewAdapter(dataList, getActivity());
        recyclerView.setAdapter(adapter);
        prLayout.setOnCheckCanDoRefreshListener(
                new OnCheckCanDoRefreshListener() {
                    @Override
                    public boolean checkCanPullStart() {
                        return offsetY == 0;
                    }

                    @Override
                    public boolean checkCanPullEnd() {
                        return canPullUp;
                    }
                }
        );
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                offsetY += dy;
                Log.e(TAG, "dy==" + dy);
                if (dy >= 0 && l.findLastVisibleItemPosition() == dataList.size() - 1) {
                    canPullUp = true;
                } else {
                    canPullUp = false;
                }
            }
        });
        prLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<FrameLayout>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<FrameLayout> refreshView) {
                if (adapter != null)
                    adapter.removeFooterView();
                page = 1;
                dataList.clear();
                for (int i = 0; i < 20; i++) {
                    dataList.add("String" + i);
                }
                adapter.notifyDataSetChanged();
                if (prLayout != null) {
                    prLayout.onRefreshComplete();
                    prLayout.setMode(PullToRefreshBase.Mode.BOTH);
                }
                /*prLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (prLayout != null) {
                            prLayout.onRefreshComplete();
                            prLayout.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                        if (adapter != null)
                            adapter.removeFooterView();
                        page = 1;
                        dataList.clear();
                        for (int i = 0; i < 20; i++) {
                            dataList.add("String" + i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);*/
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<FrameLayout> refreshView) {
                canPullUp = false;
                if (prLayout != null) {
                    prLayout.onRefreshComplete();
                }
                if (page < 3) {
                    for (int i = 0; i < 12; i++) {
                        dataList.add("string" + i);
                    }
                    page++;
                    adapter.notifyDataSetChanged();
                } else {
                    if (prLayout != null) {
                        prLayout.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    adapter.addFooterView(loadAllView);
                }
               /* prLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canPullUp = false;
                        if (prLayout != null) {
                            prLayout.onRefreshComplete();
                        }
                        if (page < 3) {
                            for (int i = 0; i < 12; i++) {
                                dataList.add("string" + i);
                            }
                            page++;
                            adapter.notifyDataSetChanged();
                        } else {
                            if (prLayout != null) {
                                prLayout.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                            adapter.addFooterView(loadAllView);
                        }
                    }
                }, 1000);*/
            }
        });
        prLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                prLayout.setRefreshing();
            }
        }, 200);
//        prLayout.setRefreshing();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        prLayout.onRefreshComplete();
        unbinder.unbind();
    }

}
