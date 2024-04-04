package com.hm.viewdemo;

import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {

    // 在后台线程上执行耗时操作
    @Override
    protected String doInBackground(String... params) {
        // 这里的params是一个String数组，包含了从execute()方法传递过来的参数
        String param = params[0];

        // 执行一些耗时操作，例如网络请求
        // ...

        // 如果需要更新进度，可以调用publishProgress()方法
        publishProgress(50);

        // 返回结果
        return "result";
    }

    // 在UI线程上执行，通常用于初始化操作
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 例如：显示一个进度条
    }

    // 在doInBackground()方法执行后在UI线程上执行，用于处理后台任务的结果
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 例如：更新UI或通知用户任务已完成
    }

    // 在UI线程上执行，用于更新任务进度
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        // 例如：更新进度条
    }
}