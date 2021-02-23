package com.lwh.eventbusdemo.eventbus.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import androidx.annotation.NonNull;

import com.lwh.eventbusdemo.eventbus.EventBus;
import com.lwh.eventbusdemo.eventbus.Subscription;
import com.lwh.eventbusdemo.eventbus.exception.EventBusException;

/**
 * author: lanweihua
 * created on: 2/20/21 4:25 PM
 * description: 事件处理(用于主线程)
 */
public class HandlerPoster extends Handler implements Poster {

  private PendingPostQueue mQueue; // 消息队列
  private EventBus mEventBus; // 事件总线
  private volatile boolean mHandleActive; // 是否通知handler处理消息
  private int mMaxInsideHandleTimeMs; // handler每次运行的最长事件，防止事件过长堵塞主线程

  public HandlerPoster(EventBus eventBus, Looper looper, int maxInsideHandleTimeMs) {
    super(looper);
    mEventBus = eventBus;
    mMaxInsideHandleTimeMs = maxInsideHandleTimeMs;
    mQueue = new PendingPostQueue();
  }

  @Override
  public void enqueue(Subscription subscription, Object event) {
    PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
    synchronized (this) {
      mQueue.enqueue(pendingPost);
      // 将mHandleActive的判断不放在handleMessage,是为了减少sendMessage的次数，算个小优化
      if (!mHandleActive) {
        mHandleActive = true;
        if (!sendMessage(obtainMessage())) {
          throw new EventBusException("Could not send handler message");
        }
      }
    }
  }

  @Override
  public void handleMessage(@NonNull Message msg) {
    // 是否重新安排消息循环
    boolean reScheduled = false;
    try {
      long startTime = SystemClock.uptimeMillis();
      while (true) {
        PendingPost pendingPost = mQueue.poll();
        if (pendingPost == null) {
          // enqueue方法是多个线程都可以调用的
          synchronized (this) {
            pendingPost = mQueue.poll();
            if (pendingPost == null) {
              mHandleActive = false;
              return;
            }
          }
        }
        // TODO: 2/20/21 订阅者处理消息
        //mEventBus.
        long intervalTimeMs = SystemClock.uptimeMillis() - startTime;
        // while循环中处理的时间已经超出设置的时间，那么要退出，以免占用主线程
        if (intervalTimeMs >= mMaxInsideHandleTimeMs) {
          // 向主线程的韩消息队列发送处理事件的消息，防止mQueue中剩下的事件得不到处理
          if (!sendMessage(obtainMessage())) {
            throw new EventBusException("Could not send handler message");
          }
          reScheduled = true;
          return;
        }
      }
    } finally {
      mHandleActive = reScheduled;
    }
  }

}
