package com.lwh.eventbusdemo.eventbus.thread;

import com.lwh.eventbusdemo.eventbus.Subscription;

/**
 * author: lanweihua
 * created on: 2/20/21 10:32 AM
 * description: 事件的发送
 */
public interface Poster {

  /**
   * @param subscription 订阅对象
   * @param event        事件对象
   */
  void enqueue(Subscription subscription, Object event);
}
