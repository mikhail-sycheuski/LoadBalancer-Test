package com.iptiQ.test.loadbalancer.lb.strategy;

public class NoAvailableProviderException extends RuntimeException {

  public NoAvailableProviderException() {}

  public NoAvailableProviderException(String message) {
    super(message);
  }

  public NoAvailableProviderException(String message, Throwable cause) {
    super(message, cause);
  }
}
