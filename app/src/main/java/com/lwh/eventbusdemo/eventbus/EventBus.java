package com.lwh.eventbusdemo.eventbus;

import androidx.viewpager.widget.ViewPager;

/**
 * author: lanweihua
 * created on: 2/8/21 2:50 PM
 * description: 注册、解注册、发送事件、事件通知订阅者
 */
public class EventBus {

  static final String TAG = "EventBus";

  ViewPager mViewPager;
  private void aa(){
    mViewPager.dispatchSetSelected(true);
  }

}
