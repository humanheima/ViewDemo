package com.hm.viewdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.bean.MyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2020/4/1
 * <p>
 * Desc:
 */
public class ListViewAdapter extends ArrayAdapter<MyBean> {

    private int resource;
    private Context context;

    public ListViewAdapter(Context context, int resource, ArrayList<MyBean> lists) {
        super(context, resource, lists);
        this.context = context;
        this.resource = resource;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        MyBean bean = getItem(position);
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.textViewTitle = view.findViewById(R.id.tvTitle);
            holder.textViewDetail = view.findViewById(R.id.tvDetail);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.textViewTitle.setText(bean.getTitle());
        holder.textViewDetail.setText(bean.getDetail());
        return view;

    }

    //内部类，用来提高listView的性能
    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewDetail;
    }

}


