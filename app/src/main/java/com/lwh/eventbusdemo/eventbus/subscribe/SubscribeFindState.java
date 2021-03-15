package com.lwh.eventbusdemo.eventbus.subscribe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: lanweihua
 * created on: 2/23/21 4:17 PM
 * description: 订阅方法查找状态
 */
public class SubscribeFindState {

  // 订阅类所有的订阅方法（包含父类）
  public List<SubscriberMethod> mSubscriberMethods = new ArrayList<>();
  // 用于子类和父类相同的订阅方法(子类复写父类方法)，key: 方法签名    value：方法所在的类
  private Map<String, Class<?>> mSubscribeClassByMethodKey = new HashMap<>();
  // methodKey的构建器
  private final StringBuilder mMethodBuilder = new StringBuilder(128);
  // 订阅者class
  public Class<?> mSubscribeClass;
  // 用于迭代查找父类
  public Class<?> mClazz;

  // 是否跳过父类查找
  public boolean mSkipSuperClass;

  public void initFindState(Class<?> subscribeClass) {
    mSubscribeClass = subscribeClass;
    mClazz = subscribeClass;
    mSkipSuperClass = false;
  }

  // 定位到父类
  public void moveToSuperClass() {
    if (mSkipSuperClass) {
      mClazz = null;
    } else {
      mClazz = mClazz.getSuperclass();
      String clazzName = mClazz.getName();
      if (clazzName.startsWith("java.") || clazzName.startsWith("javax.") ||
          clazzName.startsWith("android.") || clazzName.startsWith("androidx.")) {
        mClazz = null;
      }
    }
  }

  /**
   * 某订阅方法是否valid，valid的情况
   * 1. 同一个class的不同方法如果订阅了同一种事件，是允许的
   * 2. 子类和父类有相同的订阅方法，即子类复写父类，只允许子类的有效
   *
   * @param eventType 事件类型
   * @param method    订阅方法
   * @return true为允许添加该订阅方法
   */
  public boolean checkAdd(Class<?> eventType, Method method) {
    mMethodBuilder.setLength(0);
    mMethodBuilder.append(method.getName())
        .append(">")
        .append(eventType.getName());
    String methodKey = mMethodBuilder.toString();
    Class<?> methodClass = method.getDeclaringClass();
    Class<?> methodClassOld = mSubscribeClassByMethodKey.put(methodKey, methodClass);
    // 如果子类没有该方法
    if (methodClassOld == null || methodClassOld.isAssignableFrom(methodClass)) {
      return true;
    } else {
      // 撤销上面的put操作
      mSubscribeClassByMethodKey.put(methodKey, methodClassOld);
      return false;
    }
  }

  // 资源释放
  public void recycle() {
    mSubscriberMethods.clear();
    mSubscribeClassByMethodKey.clear();
    mMethodBuilder.setLength(0);
    mSubscribeClass = null;
    mClazz = null;
    mSkipSuperClass = false;
  }

}
