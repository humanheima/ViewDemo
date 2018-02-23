### AsyncTask

### 使用 
1. 实现AsyncTask
```
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

```

* 调用
```
  private DownloadTask downloadTask;
  downloadTask = new DownloadTask();
  downloadTask.execute("http://blog.csdn.net/u011240877/article/details/72905633");
  
  //如果要取消任务
   if (!downloadTask.isCancelled()) {
       downloadTask.cancel(true);
   }
```

####源码分析

* 先从 execute方法说起
```
    //execute方法要在主线程调用
    @MainThread
    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(sDefaultExecutor, params);
    }
    
    @MainThread
    public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec,
                Params... params) {
            if (mStatus != Status.PENDING) {
                switch (mStatus) {
                    case RUNNING:
                        throw new IllegalStateException("Cannot execute task:"
                                + " the task is already running.");
                    case FINISHED:
                        throw new IllegalStateException("Cannot execute task:"
                                + " the task has already been executed "
                                + "(a task can be executed only once)");
                }
            }
    
            mStatus = Status.RUNNING;
            
            //onPreExecute方法先执行
            onPreExecute();
    
            mWorker.mParams = params;
            //执行任务
            exec.execute(mFuture);
    
            return this;
        }    
    
```
在上面的代码中可以看到，最先执行的是 `onPreExecute` 方法，然后执行任务 `exec.execute(mFuture);` 根据传递过来
的参数可知 exec 就是 `sDefaultExecutor`,exec执行的任务是`mFuture`,接下来看一下这两个变量的声明和赋值。
```
private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
//按顺序线性执行任务
public static final Executor SERIAL_EXECUTOR = new SerialExecutor();
```
`sDefaultExecutor`就是一个线性执行器，按照任务提交的顺序执行任务。

```
private final FutureTask<Result> mFuture;
```
mFuture是一个FutureTask，在新建AsyncTask实例的时候被初始化
```
    //新建一个异步任务，这个构造方法必须在主线程调用
    public AsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
                mTaskInvoked.set(true);
                Result result = null;
                try {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    //noinspection unchecked
                    result = doInBackground(mParams);
                    Binder.flushPendingCommands();
                } catch (Throwable tr) {
                    mCancelled.set(true);
                    throw tr;
                } finally {
                    //通过Handler来发送消息
                    postResult(result);
                }
                return result;
            }
        };

        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                try {
                    postResultIfNotInvoked(get());
                } catch (InterruptedException e) {
                    android.util.Log.w(LOG_TAG, e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occurred while executing doInBackground()",
                            e.getCause());
                } catch (CancellationException e) {
                    postResultIfNotInvoked(null);
                }
            }
        };
    }
```
在AsyncTask构造方法中，初始化了mWorker和mFuture
```
private final WorkerRunnable<Params, Result> mWorker;
private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;
    }
```
WorkerRunnable实现了Callable接口，当Callable的call方法被执行时，会返回执行的结果

使用mWorker实例化mFuture
```
/**
     * 创建一个FutureTask，当FutureTask的run方法被调用的时候，会执行callable的call方法。
     */
    public FutureTask(Callable<V> callable) {
        if (callable == null)
            throw new NullPointerException();
        this.callable = callable;
        this.state = NEW;
    }

```
现在捋一下execute的执行过程
1. 执行onPreExecute();
2. sDefaultExecutor 执行mFuture，会调用 mFuture的run方法
3. mFuture的run方法中会调用传入的callable（就是mWorker）的call方法
4. 在mWorker的call方法中doInBackground方法会得到执行，doInBackground在工作线程上执行
5. doInBackground中可以调用publishProgress来公布任务执行的进度
6. doInBackground方法执行完毕后返回执行结果。

第5条：publishProgress方法通过Handler发送消息到主线程
```java
 @WorkerThread
    protected final void publishProgress(Progress... values) {
        if (!isCancelled()) {
            getHandler().obtainMessage(MESSAGE_POST_PROGRESS,
                    new AsyncTaskResult<Progress>(this, values)).sendToTarget();
        }
    }
```
第6条：doInBackground方法执行完毕后会通过Handler发送消息到主线程
```
postResult(result);

```

```
private Result postResult(Result result) {
        @SuppressWarnings("unchecked")
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT,
                new AsyncTaskResult<Result>(this, result));
        message.sendToTarget();
        return result;
    }
```
getHandler方法返回一个主线程的Handler

```java
private static Handler getHandler() {
        synchronized (AsyncTask.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler();
            }
            return sHandler;
        }
    }
```
```
private static InternalHandler sHandler;
```
```java
private static class InternalHandler extends Handler {
        
        //和主线程的消息队列关联
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    //如果消息是执行结果，则结束当前任务
                    result.mTask.finish(result.mData[0]);
                    break;
                case MESSAGE_POST_PROGRESS:
                    //如果消息是执行进度，则调用onProgressUpdate更新进度
                    result.mTask.onProgressUpdate(result.mData);
                    break;
            }
        }
    }
```
结束任务
```java

private void finish(Result result) {
        if (isCancelled()) {
            onCancelled(result);
        } else {
            onPostExecute(result);
        }
        mStatus = Status.FINISHED;
    }

```


 