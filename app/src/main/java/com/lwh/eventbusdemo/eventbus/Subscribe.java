package com.lwh.eventbusdemo.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lwh.eventbusdemo.eventbus.thread.ThreadMode;

/**
 * author: lanweihua
 * created on: 2/8/21 2:33 PM
 * description: 事件订阅者需要注解@Subscribe
 */

@Retention(RetentionPolicy.SOURCE.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

  /**
   * 订阅者处理事件要求的线程模型
   */
  ThreadMode threadMode() default ThreadMode.POSTING;

  /**
   * 订阅者接收消息是否允许接收粘性消息
   */
  boolean sticky() default false;

  /**
   * 优先级，当有多个订阅者订阅同一个事件的时候，消息到来的时候，优先级越大，越先收到消息
   */
  int priority() default 0;

}
