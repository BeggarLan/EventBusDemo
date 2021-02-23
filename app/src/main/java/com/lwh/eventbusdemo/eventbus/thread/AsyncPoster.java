package com.lwh.eventbusdemo.eventbus.thread;

import com.lwh.eventbusdemo.eventbus.EventBus;
import com.lwh.eventbusdemo.eventbus.subscribe.Subscription;

/**
 * author: lanweihua
 * created on: 2/23/21 2:19 PM
 * description: 异步Poster
 */
public class AsyncPoster implements Runnable, Poster {

  private PendingPostQueue mQueue;
  private EventBus mEventBus;

  public AsyncPoster(EventBus eventBus) {
    mEventBus = eventBus;
    mQueue = new PendingPostQueue();
  }

  @Override
  public void enqueue(Subscription subscription, Object event) {
    PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
    mQueue.enqueue(pendingPost);
    // TODO: 2/23/21 提交到线程池去执行
  }

  @Override
  public void run() {
    PendingPost pendingPost = mQueue.poll();
    if (pendingPost == null) {
      throw new IllegalStateException("No pending post available");
    }
    // TODO: 2/23/21  通知订阅者
    // mEventBus.
  }

}
