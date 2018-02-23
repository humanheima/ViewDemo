package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ActivityTestHandlerBinding;
import com.hm.viewdemo.util.DownloadThread;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * 测试 Handler,HandleThread,AsyncTask
 */

public class TestHandlerAsyncTaskActivity extends AppCompatActivity {

    public static final int TYPE_INNER_THREAD = 1;
    private MyHandler myHandler;
    private ActivityTestHandlerBinding binding;
    private DownloadThread downloadThread;
    private DownloadTask downloadTask;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TestHandlerAsyncTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_handler);
        myHandler = new MyHandler(this);
        downloadThread = new DownloadThread("下载线程");
        downloadThread.setmUIHandler(myHandler);
        downloadThread.setUrlList(Arrays.asList("http://blog.csdn.net/u011240877/article/details/72905631",
                "http://blog.csdn.net/u011240877/article/details/72905632",
                "http://blog.csdn.net/u011240877/article/details/72905633"));

    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_handler_inner_thread_communicate:
                sendEmptyMessage();
                break;
            case R.id.btn_test_handler_thread:
                downloadThread.start();
                binding.btnTestHandlerThread.setText("正在下载");
                binding.btnTestHandlerThread.setEnabled(false);
                break;
            case R.id.btn_asynctask_start:
                downloadTask = new DownloadTask();
                downloadTask.execute("http://blog.csdn.net/u011240877/article/details/72905633");
                break;
            case R.id.btn_asynctask_cancel:
                if (!downloadTask.isCancelled()) {
                    downloadTask.cancel(true);
                }
                break;
            default:
                break;
        }
    }

    private void sendEmptyMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    static class MyHandler extends Handler {

        private WeakReference<TestHandlerAsyncTaskActivity> weakReference;

        public MyHandler(TestHandlerAsyncTaskActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TestHandlerAsyncTaskActivity testHandlerActivity = weakReference.get();
            switch (msg.what) {
                case TYPE_INNER_THREAD:
                    Toast.makeText(testHandlerActivity, "收到别的线程发送的消息", Toast.LENGTH_SHORT).show();
                    testHandlerActivity.binding.btnHandlerInnerThreadCommunicate.setText("收到别的线程发送的消息");
                    testHandlerActivity.binding.btnHandlerInnerThreadCommunicate.setClickable(false);
                    break;
                case DownloadThread.TYPE_START:
                    testHandlerActivity.binding.tvStartInfo.setText(testHandlerActivity.binding.tvStartInfo.getText() + "\n" + msg.obj);
                    break;
                case DownloadThread.TYPE_FINISHED:
                    testHandlerActivity.binding.tvFinishInfo.setText(testHandlerActivity.binding.tvFinishInfo.getText() + "\n" + msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 以下载一个文件为例
     */
    static class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        private final String TAG = getClass().getName();

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: showDialog");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String url = strings[0];
            try {
                while (true) {
                    int percent = doDownload(url);
                    //发布进度
                    publishProgress(percent);
                    if (percent >= 100) {
                        //下载完成
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            //设置进度
            Log.d(TAG, "onProgressUpdate: progress=" + progress);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, "onPostExecute: dismissDialog");
            if (success) {
                Log.e(TAG, "success");
            } else {
                Log.e(TAG, "failed");
            }
        }

        //模拟下载
        private int doDownload(String url) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        }
    }

}


