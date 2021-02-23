package com.lwh.eventbusdemo.eventbus.subscribe;

/**
 * dinfgyuehz
 * author: lanweihua
 * created on: 2/9/21 3:59 PM
 * description: 事件对象和订阅者的映射
 */
public class Subscription {

  // 订阅函数所在的类对象
  final Object mSubscriber;
  // 事件订阅者
  final SubscriberMethod mSubscriberMethod;

  // 是否有效
  volatile boolean mActive;

  public Subscription(Object subscriber,
      SubscriberMethod subscriberMethod) {
    mSubscriber = subscriber;
    mSubscriberMethod = subscriberMethod;
    mActive = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Subscription) {
      Subscription other = (Subscription) o;
      return mSubscriber == other.mSubscriber
          && mSubscriberMethod.equals(other.mSubscriberMethod);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return mSubscriber.hashCode() + mSubscriberMethod.mMethod.hashCode();
  }

}
