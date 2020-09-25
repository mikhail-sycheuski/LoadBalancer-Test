package com.iptiQ.test.loadbalancer.client.simulator;

import static java.lang.String.format;

import java.util.concurrent.ThreadFactory;

public class ClientWorkerThreadFactory implements ThreadFactory {

  private static final String WORKER_THREAD_NAME = "ClientWorkerThread-%s";
  private int counter = 0;

  @Override
  public Thread newThread(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.setName(format(WORKER_THREAD_NAME, ++counter));
    thread.setDaemon(true);
    return thread;
  }
}
