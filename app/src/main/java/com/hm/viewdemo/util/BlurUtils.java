package com.hm.viewdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by p_dmweidu on 2024/1/22
 * Desc: RenderScript 高斯模糊工具类
 */
public class BlurUtils {


    public static Bitmap fastBlur(Context context, Bitmap bitmap,float radius) {
        if (context == null || bitmap == null) {
            return null;
        }
        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation out = Allocation.createFromBitmap(renderScript, outBitmap);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.setInput(in);
        scriptIntrinsicBlur.forEach(out);
        out.copyTo(outBitmap);
        scriptIntrinsicBlur.destroy();
        bitmap.recycle();
        return outBitmap;
    }
}
