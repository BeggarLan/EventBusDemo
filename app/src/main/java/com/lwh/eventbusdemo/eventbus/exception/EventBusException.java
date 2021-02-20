package com.lwh.eventbusdemo.eventbus.exception;

/**
 * author: lanweihua
 * created on: 2/20/21 4:34 PM
 * description:
 */
public class EventBusException extends RuntimeException{

  public EventBusException() {
  }

  public EventBusException(String message) {
    super(message);
  }

  public EventBusException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventBusException(Throwable cause) {
    super(cause);
  }

}
