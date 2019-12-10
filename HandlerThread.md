
但是我感觉我从来没有遇到过需要在子线程使用handler发送消息的情况呀。

当我们在子线程中实例化Handler的时候，需要先调用Looper.prepare()方法，然后实例化一个Handler对象，最后在开始loop循环。如下所示：

```
new Thread("子线程")  {
    @Override
    public void run() {
        //Looper准备
        Looper.prepare();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
               //在这里处理消息
            }
        };
        //开启loop循环，死循环，要写在最后面
        Looper.loop();
    }
}.start();

```
如果每次都得写这么一段代码那就没意思了。Android给我们提供了一个类似的类，可以直接使用。这个类就是`HandlerThread`。

```
public class HandlerThread extends Thread {

    int mPriority;
    //线程id
    int mTid = -1;
    Looper mLooper;
    //Handler实例，可能为null
    private @Nullable Handler mHandler;

    public HandlerThread(String name) {
        super(name);
        mPriority = Process.THREAD_PRIORITY_DEFAULT;
    }

    @Override
    public void run() {
        mTid = Process.myTid();
        //注释1处
        Looper.prepare();
        synchronized (this) {
            mLooper = Looper.myLooper();
            notifyAll();
        }
        Process.setThreadPriority(mPriority);
        onLooperPrepared();
        //注意，这里是死循环
        Looper.loop();
        //当退出循环的时候，将线程id重置为-1。
        mTid = -1;
    }
    
    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }
        
        //如果线程已经被启动了，等待looper对象创建完毕。
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }

}
```

在注释2处，为mHandler对象赋值。注意一下`getLooper()`方法。如果线程还没启动的化，返回null。否则需要等待looper对象创建完毕。


在注释1处，当线程启动的时候，调用`Looper.prepare()`来初始化当前线程的Looper对象。初始完毕后，在同步代码块中为mLooper对象赋值。

