package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.hm.viewdemo.adapter.CustomExpandableListAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityExpandableListViewBinding;

public class ExpandableListViewActivity extends BaseActivity<ActivityExpandableListViewBinding> {

    private String[] groupStrings = {"西游记", "水浒传", "三国演义", "红楼梦"};
    private String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };

    private CustomExpandableListAdapter adapter;

    public static void launch(Context context) {
        Intent starter = new Intent(context, ExpandableListViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityExpandableListViewBinding createViewBinding() {
        return ActivityExpandableListViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        adapter = new CustomExpandableListAdapter(this, groupStrings, childStrings);
        binding.expandableListview.setAdapter(adapter);
       /* expandableListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Toast.makeText(ExpandableListViewActivity.this, groupStrings[groupPosition], Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
        binding.expandableListview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(ExpandableListViewActivity.this, groupStrings[groupPosition] + "合并",
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.expandableListview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(ExpandableListViewActivity.this, groupStrings[groupPosition] + "展开",
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.expandableListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
                    long id) {
                Toast.makeText(ExpandableListViewActivity.this, childStrings[groupPosition][childPosition],
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}
