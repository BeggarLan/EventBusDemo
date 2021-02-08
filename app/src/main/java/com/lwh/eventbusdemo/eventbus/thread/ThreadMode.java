package com.lwh.eventbusdemo.eventbus.thread;

/**
 * author: lanweihua
 * created on: 2/8/21 11:36 AM
 * description: 线程模式的声明
 */
public enum ThreadMode {

  /**
   * 默认的线程
   * 事件的订阅方直接使用事件的生产方所在的线程
   */
  POSTING,

  /**
   * 1. 如果事件的生产方在子线程，那么将事件post到主线程去执行
   * 2. 如果事件的生产方在主线程，那么事件的订阅方直接执行
   */
  MAIN,

  /**
   * 不管事件的生产方所在的线程是是不是主线程，都把事件post到主线程去执行
   */
  MAIN_ORDERED,

  /**
   * 1. 如果事件的生产者在子线程，那么那么订阅者直接执行
   * 2. 如果事件的生产者是主线程，那么将事件由线程池中的线程去执行
   */
  BACKGROUND,

  /**
   * 不管事件的生产所在的线程是什么，都把事件由线程池中的线程去执行
   */
  ASYNC;

}