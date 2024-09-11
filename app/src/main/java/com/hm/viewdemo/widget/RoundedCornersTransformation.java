package com.hm.viewdemo.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RoundedCornersTransformation extends BitmapTransformation {
    private final int radius;
    private final int margin;

    public RoundedCornersTransformation(int radius, int margin) {
        this.radius = radius;
        this.margin = margin;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        }

        Bitmap roundedBitmap = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        if (roundedBitmap == null) {
            roundedBitmap = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(roundedBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Path path = new Path();
        path.addRoundRect(new RectF(margin, margin, toTransform.getWidth() - margin, toTransform.getHeight() - margin), radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        return roundedBitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(("RoundedCornersTransformation(radius=" + radius + ", margin=" + margin + ")").getBytes(CHARSET));
    }
}