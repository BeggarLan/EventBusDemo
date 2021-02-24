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
  // 用于同一类中不同方法订阅了同一种类型的事件，key：事件类型     value：订阅方法
  private Map<Class<?>, Object> mAnyMethodByEventType = new HashMap<>();
  // 用于子类和父类相同的订阅方法(子类复写父类方法)，key: 方法签名    value：方法所在的类
  private Map<String, Class<?>> mSubscribeClassByMethodKey = new HashMap<>();
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
    Object existing = mAnyMethodByEventType.put(eventType, method);
    if (existing == null) {
      return true;

      // 该类型的事件存在多个函数订阅
    }else{
      if(existing instanceof Method){

      }else {
        return
      }
    }
  }

  private boolean check

}
