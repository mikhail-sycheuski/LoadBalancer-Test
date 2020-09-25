package com.iptiQ.test.loadbalancer.lb.registry.healthcheck;

import static java.lang.String.format;

enum HealthCheckWorkerExceptionHandler implements Thread.UncaughtExceptionHandler {

  INSTANCE;

  @Override
  public void uncaughtException(Thread thread, Throwable exception) {
    System.out.println(format("Thread[%s] unexpectedly failed with exception: ", thread.getName()));
    exception.printStackTrace();
  }
}
