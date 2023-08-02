package com.hm.viewdemo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;
import com.hm.viewdemo.inter.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by p_dmweidu on 2023/7/27
 * Desc: 全屏BottomSheetDialogFragment
 */
public class FullSheetDialogFragment extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    private RecyclerView recyclerView;
    private ArrayList<String> stringList;
    private RecycleViewAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.bottom_sheet_recycler_view, null);
        stringList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            stringList.add("string" + i);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecycleViewAdapter(stringList, getContext());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                doClick(view);
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //默认全屏展开
        //mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void doClick(View v) {
        //点击任意布局关闭
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}