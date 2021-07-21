package com.hm.viewdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hm.viewdemo.R;
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
        final TouchImageView imageView = view.findViewById(R.id.img_main);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumClickListener != null) {
                    albumClickListener.onAlbumClick();
                }
            }
        });
        Glide.with(context).asBitmap().load(list.get(position)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
                imageView.setZoom(1);
            }
        });
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
