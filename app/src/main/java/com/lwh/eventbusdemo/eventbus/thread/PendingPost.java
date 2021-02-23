package com.lwh.eventbusdemo.eventbus.thread;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lwh.eventbusdemo.eventbus.subscribe.Subscription;

/**
 * author: lanweihua
 * created on: 2/20/21 10:46 AM
 * description: 对订阅者和事件对象的封装
 */
public class PendingPost {

  // 池大小
  private static int PENDING_POST_POOL_SIZE = 1000;
  private static List<PendingPost> sPendingPostPool = new ArrayList<>();

  // 事件对象
  @NonNull Object mEvent;
  // 订阅者
  @NonNull Subscription mSubscription;
  @Nullable PendingPost mNext;

  public PendingPost(@NonNull Object event, @NonNull Subscription subscription) {
    mEvent = event;
    mSubscription = subscription;
  }

  // 获得一个PendingPost
  public static PendingPost obtainPendingPost(
      @NonNull Subscription subscription,
      @NonNull Object event) {
    synchronized (sPendingPostPool) {
      int size = sPendingPostPool.size();
      if (size > 0) {
        PendingPost pendingPost = sPendingPostPool.remove(size - 1);
        pendingPost.mEvent = event;
        pendingPost.mSubscription = subscription;
        pendingPost.mNext = null;
        return pendingPost;
      }
      return new PendingPost(event, subscription);
    }
  }

  // 重制一个pendingPost
  public static void releasePendingPost(@NonNull PendingPost pendingPost) {
    pendingPost.mEvent = null;
    pendingPost.mNext = null;
    pendingPost.mSubscription = null;
    if (sPendingPostPool.size() < PENDING_POST_POOL_SIZE) {
      sPendingPostPool.add(pendingPost);
    }
  }

}
