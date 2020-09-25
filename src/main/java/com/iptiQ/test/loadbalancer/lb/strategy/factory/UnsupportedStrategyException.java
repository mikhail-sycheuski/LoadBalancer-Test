package com.iptiQ.test.loadbalancer.lb.strategy.factory;

public class UnsupportedStrategyException extends RuntimeException {

  public UnsupportedStrategyException() {
  }

  public UnsupportedStrategyException(String message) {
    super(message);
  }

  public UnsupportedStrategyException(String message, Throwable cause) {
    super(message, cause);
  }
}
