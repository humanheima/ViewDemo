package com.hm.viewdemo.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import java.util.List;

/**
 * Created by dumingwei on 2018/2/22 0022.
 */

public class DownloadThread extends HandlerThread implements Handler.Callback {

    private final String TAG = getClass().getName();
    private final String KEY_URL = "url";
    public static final int TYPE_START = 2;
    public static final int TYPE_FINISHED = 3;

    private Handler mWorkHandler;
    private Handler mUIHandler;
    private List<String> urlList;

    public DownloadThread(String name) {
        super(name);
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public void setmUIHandler(Handler mUIHandler) {
        this.mUIHandler = mUIHandler;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mWorkHandler = new Handler(getLooper(), this);
        if (mUIHandler == null) {
            throw new IllegalArgumentException("not set uiHandler");
        }
        for (String url : urlList) {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_URL, url);
            msg.setData(bundle);
            mWorkHandler.sendMessage(msg);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg == null || msg.getData() == null) {
            return false;
        }
        String url = (String) msg.getData().get(KEY_URL);
        //下载开始，通知主线程
        Message startMsg = mUIHandler.obtainMessage(TYPE_START, "\n 开始下载 @" + DateUtil.currentTime() + "\n" + url);
        mUIHandler.sendMessage(startMsg);

        SystemClock.sleep(2000);    //模拟下载

        //下载完成，通知主线程
        Message finishMsg = mUIHandler.obtainMessage(TYPE_FINISHED, "\n 下载完成 @" + DateUtil.currentTime() + "\n" + url);
        mUIHandler.sendMessage(finishMsg);

        return true;
    }

    @Override
    public boolean quitSafely() {
        mUIHandler = null;
        return super.quitSafely();
    }
}
