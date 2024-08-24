package com.hm.viewdemo.nested_scroll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {

    public static final String TITLE = "title";
    private String mTitle = "Defaut Value";
    private RecyclerView mRecyclerView;
    // private TextView mTextView;
    private List<String> mDatas = new ArrayList<>();

    public static TabFragment newInstance(String title) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        mRecyclerView = view
                .findViewById(R.id.id_stickynavlayout_innerscrollview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // mTextView = (TextView) view.findViewById(R.id.id_info);
        // mTextView.setText(mTitle);
        for (int i = 0; i < 50; i++) {
            mDatas.add(mTitle + " -> " + i);
        }
//        mRecyclerView.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.nested_rv_item, mDatas) {
//            @Override
//            public void convert(ViewHolder holder, String o) {
//                holder.setText(R.id.id_info, o);
//            }
//        });

        mRecyclerView.setAdapter(new BaseAdapter() {
            @Override
            protected int getCount() {
                return 0;
            }

            @Override
            protected RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindView(RecyclerView.ViewHolder holder, int position) {

            }
        });
        return view;

    }


}
