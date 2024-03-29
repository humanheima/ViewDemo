package com.hm.viewdemo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.brotherd.pullrefresh.OnCheckCanDoRefreshListener;
import com.brotherd.pullrefresh.PullToRefreshBase;
import com.brotherd.pullrefresh.PullToRefreshFrameLayout;
import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;
import java.util.ArrayList;
import java.util.List;


public class PullToRefreshFrameFragment extends Fragment {

    private static final String TAG = "PullToRefreshFrameFragm";

    RecyclerView recyclerView;
    PullToRefreshFrameLayout prLayout;

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
        prLayout = view.findViewById(R.id.pull_to_refresh_frame_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
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
                if (adapter != null) {
                    adapter.removeFooterView();
                }
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

}
