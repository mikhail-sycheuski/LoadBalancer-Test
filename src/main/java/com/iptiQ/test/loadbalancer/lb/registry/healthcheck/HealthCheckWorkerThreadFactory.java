package com.iptiQ.test.loadbalancer.lb.registry.healthcheck;

import java.util.concurrent.ThreadFactory;

class HealthCheckWorkerThreadFactory implements ThreadFactory {

  private static final String WORKER_THREAD_NAME = "HealthCheckWorkerThread-1";

  @Override
  public Thread newThread(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.setName(WORKER_THREAD_NAME);
    thread.setDaemon(true);
    thread.setUncaughtExceptionHandler(HealthCheckWorkerExceptionHandler.INSTANCE);
    return thread;
  }
}
