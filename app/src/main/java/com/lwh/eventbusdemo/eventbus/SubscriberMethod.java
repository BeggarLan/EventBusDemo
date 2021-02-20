package com.lwh.eventbusdemo.eventbus;

import java.lang.reflect.Method;

import android.text.TextUtils;
import androidx.annotation.Nullable;

import com.lwh.eventbusdemo.eventbus.thread.ThreadMode;

/**
 * author: lanweihua
 * created on: 2/8/21 11:34 AM
 * description: 订阅对象：标记了Subscribe的方法
 */
public class SubscriberMethod {

  final Method mMethod; // 订阅方法
  final ThreadMode mThreadMode; // 订阅者要求事件执行的线程模型
  final Class<?> mEventType; // 事件的类型
  final int mPriority; // 订阅的优先级
  final boolean mSticky; // 是否粘性

  @Nullable
  String mMethodString;

  public SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType, int priority,
      boolean sticky) {
    mMethod = method;
    mThreadMode = threadMode;
    mEventType = eventType;
    mPriority = priority;
    mSticky = sticky;
  }

  @Override
  public boolean equals(Object otherMethod) {
    if (this == otherMethod) {
      return true;
    }
    if (otherMethod instanceof SubscriberMethod) {
      checkMethodString();
      SubscriberMethod otherSubscriberMethod = (SubscriberMethod) otherMethod;
      otherSubscriberMethod.checkMethodString();
      // 不使用Method.equals()，该方法效率很慢
      return TextUtils.equals(mMethodString, otherSubscriberMethod.mMethodString);
    } else {
      return false;
    }
  }

  // 针对该功能只需要拼凑出能区分不能对象的string就行
  private synchronized void checkMethodString() {
    if (TextUtils.isEmpty(mMethodString)) {
      StringBuilder stringBuilder = new StringBuilder(64);
      mMethodString = stringBuilder
          .append(mMethod.getDeclaringClass().getName())
          .append("#")
          .append(mMethod.getName())
          .append("(")
          .append(mEventType.getName())
          .toString();
    }
  }

  @Override
  public int hashCode() {
    return mMethod.hashCode();
  }

}
