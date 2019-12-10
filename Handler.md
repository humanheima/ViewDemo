


2019年7月27日。回顾以前写的博客，还是有收获的，温故而知新。

先来一张图，图片来自郭霖老师的博客。

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMTA0MjI1ODE5MjI4)

### Handler 实例化
在主线程中实例化

```
//在主线程创建Handler对象
private Handler handlerMain = new Handler() {
    @Override
    public void handleMessage(Message msg) {
       //在这里处理消息
    }
};

```

在子线程中实例化Handler

```
new Thread("子线程")  {
    @Override
    public void run() {
        //Looper准备
        Looper.prepare();
        handlerThread = new Handler() {
            @Override
            public void handleMessage(Message msg) {
               //在这里处理消息
            }
        };
        //由于loop方法是死循环，所以要写在最后
        Looper.loop();
    }
}.start();

```
在子线程中实例化Handler之前要注意先调用**Looper.prepare()**方法。不然会报下面的错误。

```
Can't create handler inside thread that has not called Looper.prepare()
```
意思就是在线程内部如果没有调用Looper.prepare()方法就不能创建一个Handler对象。但是为什么不调用Looper.prepare()方法就会报错呢？，看一看Handler的构造函数。

```
public Handler() {
    this(null, false);
}

```
在内部调用了两个参数的构造函数。

```
public Handler(Callback callback, boolean async) {
    if (FIND_POTENTIAL_LEAKS) {
        final Class<? extends Handler> klass = getClass();
        if (( klass.isAnonymousClass() 
                || klass.isMemberClass() 
                || klass.isLocalClass()) 
                && (klass.getModifiers() & Modifier.STATIC) == 0) {
            //使用handler可能会造成内存泄漏，这个日志应该都见过吧
            Log.w(TAG, "The following Handler class should be static
                 or leaks might occur: " + klass.getCanonicalName());
        }
    }

    mLooper = Looper.myLooper();
    if (mLooper == null) {
        //报错的地方。
        throw new RuntimeException( "Can't create handler inside thread 
            that has not called Looper.prepare()");
    }
    mQueue = mLooper.mQueue;
    mCallback = callback;
    mAsynchronous = async;
}

```

```
final Looper mLooper;
```

报错的原因是因为 mLooper 为null。那就是Looper.myLooper()返回值为null。接下来看一下Looper.myLooper()这个方法。

```
 /**
  * 返回和当前线程关联的Looper对象。如果调用线程没有和一个Looper对象关联则返回null。
  */
  public static Looper myLooper() {
      return sThreadLocal.get();
  }

```

返回和当前线程相关联的Looper,如果当前线程没有和一个Looper相关联，就返回null。sThreadLocal是一个ThreadLocal对象，它的声明如下：声明中的注释也说明了如果不调用Looper的prepare()方法，就会返回null。

```
//如果没有调用Looper的prepare()方法。sThreadLocal.get() 会返回null。
static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

```

接下来就看一看Looper的prepare方法

```
 /** 将当前线程初始化为一个looper。
  * 这给了你一个机会在真正启动looper之前创建一个Handler对象并引用此loopr对象。
  * 在调用looper方法之前必须调用这个方法，可以调用quit方法结束looper。
  */
  public static void prepare() {
	  //调用有参构造函数
      prepare(true);
   }
   
```

```
private static void prepare(boolean quitAllowed) {
    if (sThreadLocal.get() != null) {
        throw new RuntimeException("Only one Looper may be created per thread");
    }
    //初始化一个Looper对象。保存在sThreadLocal对象中。
    sThreadLocal.set(new Looper(quitAllowed));
}

```

所以调用Looper.prepare()方法后，sThreadLocal设置一个初始Looper对象，然后在Handler的构造函数中调用Looper.myLooper()方法才能得到一个非空的Looper对象。

**但是为什么在主线程中实例化一个Handler的时候为什么没有调用Looper.prepare()方法，却没有报错呢？这是由于在程序启动的时候，系统已经帮我们自动调用了Looper.prepareMainLooper()方法，初始化了一个Looper对象。**

ActivityThread中的main()方法
```
public static void main(String[] args) {
        
    ...
    Looper.prepareMainLooper();
    ...
    Looper.loop();
}

```

看一看 Looper.prepareMainLooper();方法。

```
/**
 * 将当前线程初始化为一个looper，将该looper作为应用的main looper。
 * 应用程序的main looper是系统创建的，你不应该调用此方法。
 */
public static void prepareMainLooper() {
   //内部调用了prepare方法，
   prepare(false);
    synchronized (Looper.class) {
        if (sMainLooper != null) {
            throw new IllegalStateException(
             "The main Looper has already been prepared.");
        }
        sMainLooper = myLooper();
    }
}

```
在这个方法中，系统会为我们自动创建一个应用的main Looper对象。所以在程序启动的时候主线程中已经存在一个Looper对象了，所以我们在主线程中初始化一个Handler的时候，就不用先调用Looper的prepare方法了。

### Handler 发送消息
```
public final boolean sendMessage(Message msg){
    return sendMessageDelayed(msg, 0);
}

```
内部最终会调用sendMessageAtTime方法
```
public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
     MessageQueue queue = mQueue;
     if (queue == null) {
        RuntimeException e = new RuntimeException(
                this + " sendMessageAtTime() called with no mQueue");
        return false;
    }
    //注释1处，
    return enqueueMessage(queue, msg, uptimeMillis);
}

```
在注释1处，调用enqueueMessage方法
```
private boolean enqueueMessage(MessageQueue queue, Message msg, 
    long uptimeMillis) {
    //注释1处，
    msg.target = this;
    if (mAsynchronous) {
        msg.setAsynchronous(true);
    }
    //注释2处
    return queue.enqueueMessage(msg, uptimeMillis);
}

```
在上面方法的注释1处，将Message的target设置为当前的Handler对象。

在注释2处，调用了MessageQueue的enqueueMessage方法。

MessageQueue的类定义是这样说的。

```
/**
 * MessageQueue是一个底层的类来维持一个消息列表，这个消息列表由Looper负责分发，
 * 消息不是直接添加到消息队列里面的，而是通过和Looper相关联的Handler对象添加的消息队列里面的。
 * 
 * 你可以调用Looper.myQueue()方法获取当前线程的MessageQueue。
 */
 public final class MessageQueue {
     //...
 }
```

MessageQueue的enqueueMessage方法
```
boolean enqueueMessage(Message msg, long when) {
       
    synchronized (this) {
           
        msg.markInUse();
        msg.when = when;
        Message p = mMessages;
        boolean needWake;
            
        if (p == null || when == 0 || when < p.when) {
            // 将消息插入到队列的头部，如果队列阻塞了，唤醒队列。
            msg.next = p;
            mMessages = msg;
            needWake = mBlocked;
        } else {
            //通过比较when，将message插入到队列到中间。通常我们不必唤醒队列，
            //除非队列的头部有同步屏障并且该message是队列中最早的异步消息。  
            needWake = mBlocked && p.target == null && msg.isAsynchronous();
            Message prev;
            for (;;) {
                prev = p;
                p = p.next;
                if (p == null || when < p.when) {
                    break;
                }
                if (needWake && p.isAsynchronous()) {
                    needWake = false;
                }
            }
            //将msg插入到合适的位置
            msg.next = p;
            prev.next = msg;
        }

        // 如果需要唤醒队列
        if (needWake) {
            nativeWake(mPtr);
        }
    }
    return true;
}

```
到这里发送消息完毕，Handler发送消息就是将消息按照时间排序插入到MessageQueue中。

### Handler处理消息
Handler想要处理消息，就必须先收到消息，那么handler是怎样收到消息的呢?
答案是通过Looper的loop()方法进行分发的。

Looper的loop()方法

```
public static void loop() {
      final Looper me = myLooper();
      final MessageQueue queue = me.mQueue;

     
    //循环分发消息。
    for (;;) {
        //注释1处，
        Message msg = queue.next(); // 可能阻塞
        if (msg == null) {
            // 没有消息表明消息队列正在退出。那就return。
            return;
        }
        try {
            //注释2处，在这里消息的target会调用dispatchMessage方法，来处理消息。
            msg.target.dispatchMessage(msg);
        } 
        msg.recycleUnchecked();
    }
}

```
在loop()方法中，注释1处，调用了MessageQueue的next方法。
```
Message next() {
    //...
    int nextPollTimeoutMillis = 0;
    for (;;) {
        if (nextPollTimeoutMillis != 0) {
            Binder.flushPendingCommands();
        }
        //感觉就是在这里阻塞的
        nativePollOnce(ptr, nextPollTimeoutMillis);

        synchronized (this) {
            //尝试获取下一个message。如果找到了，则返回。
            final long now = SystemClock.uptimeMillis();
            Message prevMsg = null;
            Message msg = mMessages;
            if (msg != null && msg.target == null) {
                // 存在同步屏障，查找队列中的下一个异步消息。
                do {
                    prevMsg = msg;
                    msg = msg.next;
                } while (msg != null && !msg.isAsynchronous());
            }
            if (msg != null) {
                if (now < msg.when) {
                    // 下一个消息还没准备好，设置超时在消息准备好时唤醒。
                    nextPollTimeoutMillis = (int) 
                        Math.min(msg.when - now, Integer.MAX_VALUE);
                } else {
                    // 获取了一个消息
                    mBlocked = false;
                    if (prevMsg != null) {
                        prevMsg.next = msg.next;
                    } else {
                        mMessages = msg.next;
                    }
                    msg.next = null;
                    if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                    msg.markInUse();
                    //返回获取的消息
                    return msg;
                }
            } else {
                // No more messages.
                nextPollTimeoutMillis = -1;
            }

            // 现在处理退出消息已处理所有待处理消息。
            if (mQuitting) {
                dispose();
                return null;
            }

        }
           
        // 所以回去重新查找待处理的消息而无需等待。
        nextPollTimeoutMillis = 0;
    }
}

```
方法内部会循环从消息队列中取出message,如果没有消息就阻塞，如果有消息，就返回消息交给message的处理者来进行处理。

Looper的loop()方法注释2处
```
msg.target.dispatchMessage(msg);

```
msg.target是什么东西的，其实就是Handler对象，在Handler的sendMessageAtTime方法中会调用Handler的enqueueMessage方法，而在这个方法中

```
private boolean enqueueMessage(MessageQueue queue, Message msg,
     long uptimeMillis) {
     //msg.target就是当前Handler对象。
     msg.target = this;
     if (mAsynchronous) {
        msg.setAsynchronous(true);
     }
    return queue.enqueueMessage(msg, uptimeMillis);
}

```
Handler的dispatchMessage(msg);

```
/**
 * Handle system messages here.
 */
public void dispatchMessage(Message msg) {
    if (msg.callback != null) {
        handleCallback(msg);
    } else {
         if (mCallback != null) {
           if (mCallback.handleMessage(msg)) {
                 return;
             }
         }
         handleMessage(msg);
    }
}

```

在这个方法中，处理消息的地方有三个。

```
if (msg.callback != null) {
    handleCallback(msg);
} 

```
这个msg.callback是从哪里冒出来的呢？

当我们使用Handler的post方法的时候，为什么直接在Runnable的run方法中就可以直接进行ui操作呢？

```
handlerMain.post(new Runnable() {
     @Override
     public void run() {
         // 在这里就可以直接进行UI操作  
     }
});

```
post方法

```
public final boolean post(Runnable r){
   return  sendMessageDelayed(getPostMessage(r), 0);
}

```

Handler的getPostMessage方法
```
private static Message getPostMessage(Runnable r) {
    Message m = Message.obtain();
    //这个Runnable 对象r 就是消息的callback
    m.callback = r;
    return m;
}

```
所以当我们使用Handler的post方法的时候，会把Runnable 对象转化为一个Message 对象添加到消息队列，而且这个Message 的callback就是这个Runnable 对象对象。在Handler dispatchMessage(Message msg) 方法的时候，发现消息对象的callback不为空，就会调用 handleCallback(msg)方法。

```
if (msg.callback != null) {
    handleCallback(msg);
} 

```

看一下Handler的handleCallback(msg)方法

```
private static void handleCallback(Message message) {
    message.callback.run();
}

```
直接调用callback的run方法。因为我们的Handler是在主线程中实例化的，handler的handleCallback方法也是在主线程中运行，所以callback的run方法中的逻辑（更新ui）也是在主线程中运行。可以安全的更新ui。

在Handler dispatchMessage(Message msg) 方法的时候，如果消息对象的callback为空，下面的代码就会执行。
```
else {
   if (mCallback != null) {
        if (mCallback.handleMessage(msg)) {
            return;
        }
    }
    handleMessage(msg);
}
```
首先会判断mCallback 是不是为空，如果不为空，就由mCallback来处理消息并返回。这个mCallback是从哪里来的呢。

```
private Handler handlerMain = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
        return false;
    } }) {
      //...
    };
```
如果我们构造Handler对象的时候，传入一个Callback对象，那么mCallback 就会被赋值。

```
public Handler(Callback callback, boolean async) {
   ...
   mCallback = callback;
   ...
}

```

如果我们在实例化Handler对象的时候没有传入一个Callback对象，那么我们就需要复写Handler的handlerMessage(Message msg)方法来处理消息。

```
private Handler handlerMain = new Handler() {
   @Override
   public void handleMessage(Message msg) {
      //处理消息。
   }
};

```

参考链接

* [Android异步消息处理机制完全解析，带你从源码的角度彻底理解](https://blog.csdn.net/guolin_blog/article/details/9991569)
* [Android消息机制](http://blog.csdn.net/roly_yu/article/details/52327598)
* [Android Handler机制完全解析](https://www.jianshu.com/p/fc858849a497)