package com.lwh.eventbusdemo.eventbus.subscribe;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lwh.eventbusdemo.eventbus.exception.EventBusException;
import com.lwh.eventbusdemo.util.CollectionsUtils;

/**
 * author: lanweihua
 * created on: 2/23/21 2:39 PM
 * description: 订阅者函数Finder
 */
public class SubscriberMethodFinder {

  // static和abstract修饰的方法不处理
  private static final int MODIFIERS_IGNORE = Modifier.STATIC | Modifier.ABSTRACT;

  // 订阅类和该类中订阅方法的映射
  private static final Map<Class<?>, List<SubscriberMethod>> sMethodCache =
      new ConcurrentHashMap<>();

  // 订阅者查询状态pool
  private static final int FIND_STATE_POLL_SIZE = 4;
  private static final SubscribeFindState[] sFindStatePool =
      new SubscribeFindState[FIND_STATE_POLL_SIZE];

  // 是否使用代码生成（目前不支持，仅支持反射）
  private final boolean mUseGenerateCode;

  public SubscriberMethodFinder(boolean useGenerateCode) {
    this.mUseGenerateCode = false;
  }

  // 查询某类的订阅方法(注解@Subscribe)
  @NonNull
  public List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {

    List<SubscriberMethod> subscriberMethods = sMethodCache.get(subscriberClass);
    if (!CollectionsUtils.isEmpty(subscriberMethods)) {
      return subscriberMethods;
    }

    // 使用代码生成
    if (mUseGenerateCode) {

      // 使用反射
    } else {
      subscriberMethods = findUsingReflection(subscriberClass);
    }
    if (CollectionsUtils.isEmpty(subscriberMethods)) {
      throw new EventBusException("Subscriber " + subscriberClass
          + " and its super classes have no public methods with the @Subscribe annotation");
    } else {
      sMethodCache.put(subscriberClass, subscriberMethods);
      return subscriberMethods;
    }
  }

  // 使用方式查找某类的所有订阅方法
  @Nullable
  private List<SubscriberMethod> findUsingReflection(Class<?> subscriberClass) {
    Method[] methods = subscriberClass.getDeclaredMethods();
    for (Method method : methods) {
      int modifiers = method.getModifiers();
      // 方法是public且非static非abstract
      if ((modifiers & Modifier.PUBLIC) == 0 || (modifiers & MODIFIERS_IGNORE) == 1) {
        continue;
      }
      // 方法要被@Subscribe注解
      if (!method.isAnnotationPresent(Subscribe.class)) {
        continue;
      }
      Class<?>[] prameterTypes = method.getParameterTypes();
      if (prameterTypes.length == 1) {

      } else {
        throw new EventBusException(
            "@Subscribe method Parameter must is one,but is " + prameterTypes.length);
      }

    }

  }

  // 生产FindState
  private SubscribeFindState prepareFindState() {
    synchronized (sFindStatePool) {
      for (int i = 0; i < FIND_STATE_POLL_SIZE; ++i) {
        SubscribeFindState findState = sFindStatePool[i];
        if (findState != null) {
          sFindStatePool[i] = null;
          return findState;
        }
      }
    }
    return new SubscribeFindState();
  }

  // 清除缓存
  public static void clearCache() {
    sMethodCache.clear();
  }

}
