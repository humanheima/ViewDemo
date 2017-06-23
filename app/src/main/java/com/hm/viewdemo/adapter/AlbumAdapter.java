package com.hm.viewdemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ImageUtil;
import com.hm.viewdemo.widget.TouchImageView;

import java.util.List;

public class AlbumAdapter extends PagerAdapter {

    private static final String TAG = "AlbumAdapter";

    private List<String> list;
    private Context context;
    private AlbumClickListener albumClickListener;

    public AlbumAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setAlbumClickListener(AlbumClickListener albumClickListener) {
        this.albumClickListener = albumClickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_img, container, false);
        TouchImageView imageView = (TouchImageView) view.findViewById(R.id.img_main);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumClickListener != null) {
                    albumClickListener.onAlbumClick();
                }
            }
        });
        ImageUtil.loadImage(context, list.get(position), imageView);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface AlbumClickListener {

        void onAlbumClick();
    }

}
