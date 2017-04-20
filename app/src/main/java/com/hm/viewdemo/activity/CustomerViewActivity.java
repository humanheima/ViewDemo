package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.GridViewAdapter;
import com.hm.viewdemo.util.Images;

import java.util.Arrays;
import java.util.List;

public class CustomerViewActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private GridView gridView;
    private GridViewAdapter adapter;
    private List<String> stringList;

    public static void launch(Context context) {
        Intent starter = new Intent(context, CustomerViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_view);
        gridView = (GridView) findViewById(R.id.grid_view);
        stringList = Arrays.asList(Images.imageUrls);
        adapter = new GridViewAdapter(this, R.layout.item_gridview, stringList);
        gridView.setAdapter(adapter);
    }

}
