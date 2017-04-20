package com.hm.viewdemo.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ImageUtil;

import java.util.List;

/**
 * Created by dumingwei on 2017/4/20.
 */
public class GridViewAdapter extends ArrayAdapter<String> {

    private List<String> stringList;
    private Context context;
    private int resource;

    public GridViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> stringList) {
        super(context, resource, stringList);
        this.stringList = stringList;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(holder);
        } else {
            view=convertView;
            holder = (ViewHolder) view.getTag();
        }
        ImageUtil.loadImage(context, stringList.get(position), holder.imageView);
        return view;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
