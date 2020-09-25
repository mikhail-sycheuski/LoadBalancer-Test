package com.iptiQ.test.loadbalancer.lb.registry.healthcheck;

import static java.util.stream.Collectors.toMap;

import java.util.function.Function;

import com.iptiQ.test.loadbalancer.lb.registry.HeartbeatAwareProviderRegistry;
import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheck;
import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheckStatus;
import com.iptiQ.test.loadbalancer.provider.Provider;

public class HealthCheckTask implements Runnable {

  private HeartbeatAwareProviderRegistry provideRegistry;

  public HealthCheckTask(HeartbeatAwareProviderRegistry provideRegistry) {
    this.provideRegistry = provideRegistry;
  }

  @Override
  public void run() {
    System.out.println("Starting new health check round");

    provideRegistry.getRegisteredProviders().stream()
        .collect(toMap(Function.identity(), this::processHealthCheckForProvider))
        .forEach((key, value) -> provideRegistry.updateProviderStatus(key, value));
  }

  private HealthCheck processHealthCheckForProvider(Provider provider) {
    HealthCheckStatus healthCheckStatus = provider.healthCheck();

    System.out.printf("Provider[%s] status is - %s%n", provider.getInternalId(), healthCheckStatus);

    return new HealthCheck(provider.getInternalId(), healthCheckStatus);
  }
}
