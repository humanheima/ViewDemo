package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;
import com.hm.viewdemo.fragment.FullSheetDialogFragment;
import com.hm.viewdemo.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private RecycleViewAdapter adapter;
    private List<String> stringList;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView recyclerView;

    public static void launch(Context context) {
        Intent starter = new Intent(context, BottomSheetActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        stringList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            stringList.add("string" + i);
        }
        recyclerView = new RecyclerView(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RecycleViewAdapter(stringList, this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void intro(View view) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.e(TAG, "onStateChanged" + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float onSlide) {
                Log.e(TAG, "onSlide" + onSlide);
            }
        });
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void select(View view) {
        new FullSheetDialogFragment().show(getSupportFragmentManager(), "dialog");
    }
}
