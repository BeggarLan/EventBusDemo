package com.lwh.eventbusdemo.eventbus.thread;

/**
 * author: lanweihua
 * created on: 2/20/21 10:59 AM
 * description: PendingPost的队列
 */
public final class PendingPostQueue {
  private PendingPost head;
  private PendingPost tail;

  // 入队
  synchronized void enqueue(PendingPost pendingPost) {
    if (pendingPost == null) {
      throw new NullPointerException("pendingPost is null ,can not enqueue");
    }
    if (tail != null) {
      tail.mNext = pendingPost;
    } else if (head == null) {
      head = tail = pendingPost;
    } else {
      // head不为空但是tail为空，理论上不会发生
      throw new IllegalStateException("Head present, but no tail");
    }
    // 唤醒那些想取元素但是队列为空而等待的线程
    notifyAll();
  }

  // 提取元素
  synchronized PendingPost poll() {
    PendingPost pendingPost = head;
    if (head != null) {
      head = head.mNext;
      if (head == null) {
        tail = null;
      }
    }
    return pendingPost;
  }

  // 提取元素(如果队列为空的话，等待一段时间)
  synchronized PendingPost poll(int maxMillisToWait) throws InterruptedException {
    if (head == null) {
      wait(maxMillisToWait);
    }
    return poll();
  }

}
