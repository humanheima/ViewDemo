package com.hm.viewdemo.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/4/20.
 */
public class ImageUtil {

    private static RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).dontAnimate();

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }
}
