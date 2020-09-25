package com.iptiQ.test.loadbalancer.lb.registry.healthcheck;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.iptiQ.test.loadbalancer.lb.registry.HeartbeatAwareProviderRegistry;

public class HealthCheckManagerImplRepeatable implements HealthCheckManager {

  private static final int SINGLE_WORKER = 1;

  private final ScheduledExecutorService executorService;
  private final HeartbeatAwareProviderRegistry heartbeatAwareProviderRegistry;
  private final int healthCheckIntervalMS;

  public HealthCheckManagerImplRepeatable(
      HeartbeatAwareProviderRegistry heartbeatAwareProviderRegistry, int healthCheckIntervalMS) {
    this.heartbeatAwareProviderRegistry = heartbeatAwareProviderRegistry;
    this.healthCheckIntervalMS = healthCheckIntervalMS;
    this.executorService =
        new ScheduledThreadPoolExecutor(SINGLE_WORKER, new HealthCheckWorkerThreadFactory());
  }

  @Override
  public void initialize() {
    System.out.println("Started HealthCheckManager initialization");

    executorService.scheduleWithFixedDelay(
        new HealthCheckTask(heartbeatAwareProviderRegistry),
        healthCheckIntervalMS,
        healthCheckIntervalMS,
        TimeUnit.MILLISECONDS);

    System.out.println("Finalized HealthCheckManager initialization");
  }

  @Override
  public void shutdown() {
    System.out.println("Started  HealthCheckManager shutdown");

    executorService.shutdown();

    try {
      if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException exception) {
      executorService.shutdownNow();
    }

    System.out.println("Finalized HealthCheckManager shutdown");
  }
}
