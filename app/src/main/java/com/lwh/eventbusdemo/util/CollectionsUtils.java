package com.lwh.eventbusdemo.util;

import java.util.Collection;

/**
 * author: lanweihua
 * created on: 2/23/21 3:24 PM
 * description: 集合工具类
 */
public class CollectionsUtils {

  public static boolean isEmpty(Collection<?> list) {
    return list == null || list.isEmpty();
  }

}
