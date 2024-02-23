package com.hm.viewdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.brotherd.pullrefresh.PullToRefreshBase;
import com.brotherd.pullrefresh.PullToRefreshRecyclerView;
import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;
import java.util.ArrayList;
import java.util.List;

public class PullToRefreshRecyclerViewFragment extends Fragment {

    private static final String TAG = "PullToRefreshRecyclerVi";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PullToRefreshRecyclerView prRecyclerView;

    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private List<String> dataList;
    private int page = 1;
    private View loadAllView;

    public PullToRefreshRecyclerViewFragment() {
    }

    public static PullToRefreshRecyclerViewFragment newInstance() {
        PullToRefreshRecyclerViewFragment fragment = new PullToRefreshRecyclerViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pull_to_refresh_recycler_view, container, false);
        prRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.pr_recycler_view);
        recyclerView = prRecyclerView.getRefreshableView();
        loadAllView = inflater.inflate(R.layout.item_load_all, null);
        dataList = new ArrayList<>();
        LinearLayoutManager l = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(l);
        adapter = new RecycleViewAdapter(dataList, getActivity());
        recyclerView.setAdapter(adapter);
        prRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                prRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prRecyclerView.onRefreshComplete();
                        if (adapter != null) {
                            adapter.removeFooterView();
                        }
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

        return view;
    }

}
