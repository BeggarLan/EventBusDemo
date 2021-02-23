package com.lwh.eventbusdemo.eventbus.subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * author: lanweihua
 * created on: 2/23/21 4:17 PM
 * description: 订阅方法查找状态
 */
public class SubscribeFindState {

  public List<SubscriberMethod> mSubscriberMethods = new ArrayList<>();
  // 订阅者class
  public Class mSubscribeClass;
  // 用于迭代查找父类
  public Class mClazz;

}
