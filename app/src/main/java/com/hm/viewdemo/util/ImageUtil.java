package com.hm.viewdemo.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/4/20.
 */
public class ImageUtil {

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(imageView);
    }
}
