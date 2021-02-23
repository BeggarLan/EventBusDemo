package com.lwh.eventbusdemo.eventbus.thread;

import com.lwh.eventbusdemo.eventbus.EventBus;
import com.lwh.eventbusdemo.eventbus.subscribe.Subscription;

/**
 * author: lanweihua
 * created on: 2/20/21 5:14 PM
 * description: 后台执行的poster,耗时的任务应该由AsyncPoster去执行
 */
public class BackgroundPoster implements Runnable, Poster {

  // 取事件时，最长等待1秒钟
  private static final int sMaxMillisToWait = 1000;

  private PendingPostQueue mQueue;
  private EventBus mEventBus;
  // 后台线程是否正在处理事件消息
  private volatile boolean mExecutorRunning;

  public BackgroundPoster(EventBus eventBus) {
    mEventBus = eventBus;
    mQueue = new PendingPostQueue();
  }

  @Override
  public void enqueue(Subscription subscription, Object event) {
    PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
    synchronized (this) {
      mQueue.enqueue(pendingPost);
      if (!mExecutorRunning) {
        mExecutorRunning = true;
        // TODO: 2/22/21 后台线程执行
      }
    }
  }

  @Override
  public void run() {
    try {
      try {
        while (true) {
          PendingPost pendingPost = mQueue.poll(sMaxMillisToWait);
          if (pendingPost == null) {
            synchronized (this) {
              pendingPost = mQueue.poll();
              if (pendingPost == null) {
                mExecutorRunning = false;
                return;
              }
            }
          }
          // TODO: 2/22/21 订阅者处理消息
          // mEventBus.
        }
      } catch (InterruptedException e) {
        // TODO: 2/22/21 日志
      }
    } finally {
      mExecutorRunning = false;
    }
  }

}
